package com.travelbackintime.buybitcoin.ui.timetravel.compose

import com.github.vase4kin.bitcoin.remoteconfig.RemoteConfigService
import com.github.vase4kin.crashlytics.Crashlytics
import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachine
import com.github.vase4kin.shared.tracker.Tracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TimeTravelViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun selectingInputsEnablesInvestment() {
        val viewModel = createViewModel()

        viewModel.selectDate(DATE)
        viewModel.selectInvestment(AMOUNT)

        assertTrue(viewModel.uiState.value.canInvest)
        assertEquals(DATE, viewModel.uiState.value.selectedDateMillis)
        assertEquals(AMOUNT, viewModel.uiState.value.investedMoney)
    }

    @Test
    fun invalidInvestmentIsIgnored() {
        val viewModel = createViewModel()

        viewModel.selectInvestment(0.0)

        assertFalse(viewModel.uiState.value.canInvest)
        assertEquals(null, viewModel.uiState.value.investedMoney)
    }

    @Test
    fun successfulInvestmentNavigatesToResult() = runTest(dispatcher) {
        val event = TimeTravelMachine.Event.TimeTravelEvent(3_000.0, AMOUNT, DATE)
        val viewModel = createViewModel(FakeTimeTravelMachine { event })
        viewModel.selectDate(DATE)
        viewModel.selectInvestment(AMOUNT)
        val navigation = async { viewModel.navigation.first() }

        viewModel.invest()
        advanceUntilIdle()

        assertEquals(TimeTravelNavigation.Result(event), navigation.await())
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun failedInvestmentIsReportedAndNavigatesToError() = runTest(dispatcher) {
        val failure = IllegalStateException("network")
        val crashlytics = RecordingCrashlytics()
        val viewModel = createViewModel(
            machine = FakeTimeTravelMachine { throw failure },
            crashlytics = crashlytics,
        )
        viewModel.selectDate(DATE)
        viewModel.selectInvestment(AMOUNT)
        val navigation = async { viewModel.navigation.first() }

        viewModel.invest()
        advanceUntilIdle()

        assertEquals(TimeTravelNavigation.Error, navigation.await())
        assertEquals(failure::class, crashlytics.recorded?.let { it::class })
        assertEquals(failure.message, crashlytics.recorded?.message)
    }

    private fun createViewModel(
        machine: TimeTravelMachine = FakeTimeTravelMachine {
            TimeTravelMachine.Event.TimeTravelEvent(3_000.0, AMOUNT, DATE)
        },
        crashlytics: Crashlytics = RecordingCrashlytics(),
    ) = TimeTravelViewModel(
        timeTravelMachine = machine,
        tracker = NoOpTracker,
        crashlytics = crashlytics,
        remoteConfigService = FakeRemoteConfigService,
        ioDispatcher = dispatcher,
    )

    private companion object {
        const val DATE = 1_600_000_000_000L
        const val AMOUNT = 500.0
    }
}

private class FakeTimeTravelMachine(private val result: suspend () -> TimeTravelMachine.Event) : TimeTravelMachine {
    override suspend fun travelInTime(time: Long, investedMoney: Double) = result()
}

private class RecordingCrashlytics : Crashlytics {
    var recorded: Throwable? = null

    override fun recordException(throwable: Throwable) {
        recorded = throwable
    }
}

private object FakeRemoteConfigService : RemoteConfigService {
    override val isAdsEnabled = false
}

private object NoOpTracker : Tracker {
    override fun trackUserSetsTime(time: String) = Unit
    override fun trackUserSetsMoney(money: String) = Unit
    override fun trackUserTravelsBackAndBuys() = Unit
    override fun trackUserSeesEmptyAmountError() = Unit
    override fun trackUserSeesAtLeastDollarError() = Unit
    override fun trackUserSeesYouReachError() = Unit
    override fun trackUserStartsOver() = Unit
    override fun trackUserSharesWithFriends() = Unit
    override fun trackUserCopiesBtcWalletAddress() = Unit
    override fun trackUserGetsToRealWorldEvent(eventName: String) = Unit
    override fun trackUserGetsToNoPriceAvailableEvent() = Unit
    override fun trackUserGetsToTimeTravelEvent(profitMoney: Double, investedMoney: Double, time: Long) = Unit
    override fun trackUserRetries() = Unit
    override fun trackUserChooseMoneySuggestion(amount: String) = Unit
    override fun trackUserClicksOnPriceProvider() = Unit
}
