package com.github.vase4kin.shared.repository

import com.github.vase4kin.shared.bitcoinprice.service.BitcoinPriceService
import com.github.vase4kin.shared.bitcoinprice.service.models.BitcoinCurrentPrice
import com.github.vase4kin.shared.bitcoinprice.service.models.BitcoinHistoricalPrice
import com.github.vase4kin.shared.bitcoinprice.service.models.BitcoinHistoricalPricePoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RepositoryImplTest {
    @Test
    fun primaryProviderReturnsCompletePricePair() = runTest {
        val primary = FakeBitcoinPriceService(historicalPrice = 7_200.5, currentPrice = 60_000.0)
        val fallback = FakeBitcoinPriceService(historicalPrice = 7_100.0, currentPrice = 59_000.0)

        val prices = repository(primary, fallback).getBitcoinPrices("2020-01-01")

        assertEquals(
            BitcoinPrices(
                historicalPrice = 7_200.5,
                currentPrice = 60_000.0,
                provider = BitcoinPriceProvider.BLOCKCHAIN_COM,
            ),
            prices,
        )
        assertEquals(0, fallback.requestCount)
    }

    @Test
    fun primaryFailureUsesCompleteFallbackPricePair() = runTest {
        val primary = FakeBitcoinPriceService(historicalFailure = TestProviderException("offline"))
        val fallback = FakeBitcoinPriceService(historicalPrice = 7_100.0, currentPrice = 59_000.0)

        val prices = repository(primary, fallback).getBitcoinPrices("2020-01-01")

        assertEquals(
            BitcoinPrices(
                historicalPrice = 7_100.0,
                currentPrice = 59_000.0,
                provider = BitcoinPriceProvider.COIN_METRICS,
            ),
            prices,
        )
        assertEquals(2, fallback.requestCount)
    }

    @Test
    fun unusablePrimaryHistoricalPriceUsesFallbackWithoutRequestingPrimaryCurrentPrice() = runTest {
        val primary = FakeBitcoinPriceService(historicalPrice = Double.NaN, currentPrice = 60_000.0)
        val fallback = FakeBitcoinPriceService(historicalPrice = 7_100.0, currentPrice = 59_000.0)

        val prices = repository(primary, fallback).getBitcoinPrices("2020-01-01")

        assertEquals(BitcoinPriceProvider.COIN_METRICS, prices.provider)
        assertEquals(1, primary.requestCount)
    }

    @Test
    fun unusablePrimaryCurrentPriceUsesFallbackPair() = runTest {
        INVALID_PRICES.forEach { invalidPrice ->
            val primary = FakeBitcoinPriceService(historicalPrice = 7_200.5, currentPrice = invalidPrice)
            val fallback = FakeBitcoinPriceService(historicalPrice = 7_100.0, currentPrice = 59_000.0)

            val prices = repository(primary, fallback).getBitcoinPrices("2020-01-01")

            assertEquals(BitcoinPriceProvider.COIN_METRICS, prices.provider)
            assertEquals(7_100.0, prices.historicalPrice)
            assertEquals(59_000.0, prices.currentPrice)
        }
    }

    @Test
    fun primaryCancellationIsPropagatedWithoutCallingFallback() = runTest {
        val primary = FakeBitcoinPriceService(historicalFailure = CancellationException("cancelled"))
        val fallback = FakeBitcoinPriceService(historicalPrice = 7_100.0, currentPrice = 59_000.0)

        assertFailsWith<CancellationException> {
            repository(primary, fallback).getBitcoinPrices("2020-01-01")
        }

        assertEquals(0, fallback.requestCount)
    }

    @Test
    fun fallbackCancellationIsPropagated() = runTest {
        val primary = FakeBitcoinPriceService(historicalFailure = TestProviderException("offline"))
        val fallback = FakeBitcoinPriceService(historicalFailure = CancellationException("cancelled"))

        assertFailsWith<CancellationException> {
            repository(primary, fallback).getBitcoinPrices("2020-01-01")
        }
    }

    @Test
    fun dualProviderFailureBecomesOneAvailabilityFailure() = runTest {
        val primary = FakeBitcoinPriceService(historicalFailure = TestProviderException("primary offline"))
        val fallback = FakeBitcoinPriceService(historicalFailure = TestProviderException("fallback offline"))

        val failure = assertFailsWith<BitcoinPriceUnavailableException> {
            repository(primary, fallback).getBitcoinPrices("2020-01-01")
        }

        assertEquals(true, failure.message?.contains("primary offline"))
        assertEquals(true, failure.message?.contains("fallback offline"))
    }

    private fun repository(primary: BitcoinPriceService, fallback: BitcoinPriceService) = RepositoryImpl(
        primaryBitcoinPriceService = primary,
        fallbackBitcoinPriceService = fallback,
    )

    private companion object {
        val INVALID_PRICES = listOf(0.0, -1.0, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)
    }
}

private class FakeBitcoinPriceService(
    private val historicalPrice: Double? = null,
    private val currentPrice: Double? = null,
    private val historicalFailure: Exception? = null,
    private val currentFailure: Exception? = null,
) : BitcoinPriceService {
    var requestCount = 0
        private set

    override suspend fun getBitcoinHistoricalPrice(date: String): BitcoinHistoricalPrice {
        requestCount++
        historicalFailure?.let { throw it }
        return BitcoinHistoricalPrice(
            historicalPrice?.let { price ->
                listOf(BitcoinHistoricalPricePoint(x = 1_577_836_800, y = price))
            }.orEmpty(),
        )
    }

    override suspend fun getBitcoinCurrentPrice(): Map<String, BitcoinCurrentPrice> {
        requestCount++
        currentFailure?.let { throw it }
        return currentPrice?.let { price -> mapOf("USD" to BitcoinCurrentPrice(last = price)) }.orEmpty()
    }
}

private class TestProviderException(message: String) : IllegalStateException(message)
