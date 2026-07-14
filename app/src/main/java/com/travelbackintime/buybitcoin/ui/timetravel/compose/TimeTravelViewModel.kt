package com.travelbackintime.buybitcoin.ui.timetravel.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.vase4kin.bitcoin.remoteconfig.RemoteConfigService
import com.github.vase4kin.crashlytics.Crashlytics
import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachine
import com.github.vase4kin.shared.tracker.Tracker
import com.travelbackintime.buybitcoin.coroutines.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TimeTravelViewModel @Inject constructor(
    private val timeTravelMachine: TimeTravelMachine,
    private val tracker: Tracker,
    private val crashlytics: Crashlytics,
    remoteConfigService: RemoteConfigService,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val mutableUiState = MutableStateFlow(
        TimeTravelUiState(isAdsEnabled = remoteConfigService.isAdsEnabled),
    )
    val uiState: StateFlow<TimeTravelUiState> = mutableUiState.asStateFlow()

    private val navigationChannel = Channel<TimeTravelNavigation>(Channel.BUFFERED)
    val navigation = navigationChannel.receiveAsFlow()

    fun selectDate(dateMillis: Long) {
        mutableUiState.update { it.copy(selectedDateMillis = dateMillis) }
        tracker.trackUserSetsTime(dateMillis.toString())
    }

    fun selectInvestment(amount: Double) {
        if (!amount.isFinite() || amount < MINIMUM_INVESTMENT) return
        mutableUiState.update { it.copy(investedMoney = amount) }
        tracker.trackUserSetsMoney(amount.toString())
    }

    @Suppress("TooGenericExceptionCaught")
    fun invest() {
        val state = mutableUiState.value
        val date = state.selectedDateMillis
        val amount = state.investedMoney
        if (date == null || amount == null || state.isLoading) return

        tracker.trackUserTravelsBackAndBuys()
        mutableUiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val destination = try {
                val event = withContext(ioDispatcher) {
                    timeTravelMachine.travelInTime(date, amount)
                }
                TimeTravelNavigation.Result(
                    event as TimeTravelMachine.Event.TimeTravelEvent,
                ).also {
                    tracker.trackUserGetsToTimeTravelEvent(
                        profitMoney = it.event.profitMoney,
                        investedMoney = it.event.investedMoney,
                        time = it.event.timeToTravel,
                    )
                }
            } catch (exception: Exception) {
                crashlytics.recordException(exception)
                TimeTravelNavigation.Error
            }
            mutableUiState.update { it.copy(isLoading = false) }
            navigationChannel.send(destination)
        }
    }

    fun reset() {
        mutableUiState.update {
            TimeTravelUiState(isAdsEnabled = it.isAdsEnabled)
        }
        tracker.trackUserStartsOver()
    }

    fun trackShare() = tracker.trackUserSharesWithFriends()

    fun trackPriceProvider() = tracker.trackUserClicksOnPriceProvider()

    private companion object {
        const val MINIMUM_INVESTMENT = 1.0
    }
}
