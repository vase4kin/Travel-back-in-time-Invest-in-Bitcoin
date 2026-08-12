package com.github.vase4kin.shared.timetravelmachine

import com.github.vase4kin.shared.repository.BitcoinPriceProvider
import com.github.vase4kin.shared.repository.BitcoinPrices
import com.github.vase4kin.shared.repository.Repository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TimeTravelMachineImplTest {
    @Test
    fun calculatesPresentValueFromHistoricalInvestment() = runTest {
        val machine = TimeTravelMachineImpl(
            repository = FakeRepository(
                historicalPrice = 10_000.0,
                currentPrice = 60_000.0,
            ),
        )

        val event = machine.travelInTime(
            time = 1_600_000_000_000,
            investedMoney = 500.0,
        )

        assertEquals(
            TimeTravelMachine.Event.TimeTravelEvent(
                profitMoney = 3_000.0,
                investedMoney = 500.0,
                timeToTravel = 1_600_000_000_000,
                priceProvider = TimeTravelMachine.PriceProvider(
                    displayName = "Coin Metrics",
                    websiteUrl = "https://coinmetrics.io/",
                ),
            ),
            event,
        )
    }
}

private class FakeRepository(private val historicalPrice: Double, private val currentPrice: Double) : Repository {
    override suspend fun getBitcoinPrices(date: String) = BitcoinPrices(
        historicalPrice = historicalPrice,
        currentPrice = currentPrice,
        provider = BitcoinPriceProvider.COIN_METRICS,
    )
}
