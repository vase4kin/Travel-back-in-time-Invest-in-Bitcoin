package com.github.vase4kin.shared.repository

import com.github.vase4kin.shared.bitcoinprice.service.BitcoinPriceService
import com.github.vase4kin.shared.bitcoinprice.service.models.BitcoinCurrentPrice
import com.github.vase4kin.shared.bitcoinprice.service.models.BitcoinHistoricalPrice
import com.github.vase4kin.shared.bitcoinprice.service.models.BitcoinHistoricalPricePoint
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RepositoryImplTest {
    @Test
    fun mapsHistoricalUsdPrice() = runTest {
        val repository = RepositoryImpl(
            FakeBitcoinPriceService(
                historical = BitcoinHistoricalPrice(
                    listOf(BitcoinHistoricalPricePoint(x = 1_577_836_800, y = 7_200.5)),
                ),
            ),
        )

        assertEquals(7_200.5, repository.getBitcoinPriceByDate("2020-01-01"))
    }

    @Test
    fun mapsCurrentUsdPrice() = runTest {
        val repository = RepositoryImpl(
            FakeBitcoinPriceService(
                current = mapOf("USD" to BitcoinCurrentPrice(last = 60_000.0)),
            ),
        )

        assertEquals(60_000.0, repository.getCurrentBitcoinPrice())
    }

    @Test
    fun missingPricesPreserveLegacyNaNBehavior() = runTest {
        val repository = RepositoryImpl(FakeBitcoinPriceService())

        assertTrue(repository.getBitcoinPriceByDate("2020-01-01").isNaN())
        assertTrue(repository.getCurrentBitcoinPrice().isNaN())
    }
}

private class FakeBitcoinPriceService(
    private val historical: BitcoinHistoricalPrice = BitcoinHistoricalPrice(emptyList()),
    private val current: Map<String, BitcoinCurrentPrice> = emptyMap(),
) : BitcoinPriceService {
    override suspend fun getBitcoinHistoricalPrice(date: String) = historical

    override suspend fun getBitcoinCurrentPrice() = current
}
