package com.github.vase4kin.shared.bitcoinprice.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CoinMetricsBitcoinPriceServiceTest {
    @Test
    fun historicalRequestUsesDailyPriceForSelectedDate() = runTest {
        val service = serviceReturning(HISTORICAL_RESPONSE) { request ->
            assertEquals("/v4/timeseries/asset-metrics", request.url.encodedPath)
            assertEquals("community-api.coinmetrics.io", request.url.host)
            assertEquals("btc", request.url.parameters["assets"])
            assertEquals("PriceUSD", request.url.parameters["metrics"])
            assertEquals("1d", request.url.parameters["frequency"])
            assertEquals("2020-01-01", request.url.parameters["start_time"])
            assertEquals("2020-01-01", request.url.parameters["end_time"])
            assertEquals("1", request.url.parameters["limit_per_asset"])
        }

        val price = service.getBitcoinHistoricalPrice("2020-01-01")

        assertEquals(7_168.31, price.values.single().y)
    }

    @Test
    fun currentRequestUsesLatestOneSecondReferenceRate() = runTest {
        val service = serviceReturning(CURRENT_RESPONSE) { request ->
            assertEquals("ReferenceRateUSD", request.url.parameters["metrics"])
            assertEquals("1s", request.url.parameters["frequency"])
            assertEquals("1", request.url.parameters["limit_per_asset"])
            assertEquals(null, request.url.parameters["start_time"])
        }

        val price = service.getBitcoinCurrentPrice()

        assertEquals(62_621.99, price.getValue("USD").last)
    }

    @Test
    fun missingOrMalformedValuesProduceNoPrice() = runTest {
        val service = serviceReturning("""{"data":[{"PriceUSD":"not-a-number"}]}""") {}

        assertEquals(emptyList(), service.getBitcoinHistoricalPrice("2020-01-01").values)
        assertEquals(emptyMap(), service.getBitcoinCurrentPrice())
    }

    private fun serviceReturning(
        response: String,
        verifyRequest: (io.ktor.client.request.HttpRequestData) -> Unit,
    ): BitcoinPriceService {
        val engine = MockEngine { request ->
            verifyRequest(request)
            respond(response, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }
        val client = HttpClient(engine) {
            configureBitcoinPriceHttpClient(host = "community-api.coinmetrics.io", retryDelay = {})
        }
        return CoinMetricsBitcoinPriceService(client)
    }

    private companion object {
        val HISTORICAL_RESPONSE = """
            {
              "data": [{
                "asset": "btc",
                "time": "2020-01-01T00:00:00.000000000Z",
                "PriceUSD": "7168.31"
              }]
            }
        """.trimIndent()

        val CURRENT_RESPONSE = """
            {
              "data": [{
                "asset": "btc",
                "time": "2026-08-15T00:00:00.000000000Z",
                "ReferenceRateUSD": "62621.99"
              }]
            }
        """.trimIndent()
    }
}
