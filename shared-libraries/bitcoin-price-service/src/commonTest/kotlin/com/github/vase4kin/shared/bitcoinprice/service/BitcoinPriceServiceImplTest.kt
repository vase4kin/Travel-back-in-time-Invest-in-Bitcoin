package com.github.vase4kin.shared.bitcoinprice.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class BitcoinPriceServiceImplTest {
    @Test
    fun historicalRequestUsesDateAndMapsDailyUsdPrice() = runTest {
        val service = serviceReturning(
            body = HISTORICAL_RESPONSE,
            verifyRequest = { url ->
                assertEquals("/charts/market-price", url.encodedPath)
                assertEquals("2020-01-01", url.parameters["start"])
                assertEquals("1days", url.parameters["timespan"])
                assertEquals("false", url.parameters["sampled"])
            },
        )

        val result = service.getBitcoinHistoricalPrice("2020-01-01")

        assertEquals(7_168.31, result.values.first().y)
    }

    @Test
    fun currentRequestMapsUsdLastPrice() = runTest {
        val service = serviceReturning(
            body = CURRENT_RESPONSE,
            verifyRequest = { url -> assertEquals("/ticker", url.encodedPath) },
        )

        val result = service.getBitcoinCurrentPrice()

        assertEquals(62_621.99, result.getValue("USD").last)
    }

    private fun serviceReturning(body: String, verifyRequest: (io.ktor.http.Url) -> Unit): BitcoinPriceService {
        val engine = MockEngine { request ->
            verifyRequest(request.url)
            respond(
                content = body,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        return BitcoinPriceServiceImpl(client)
    }

    private companion object {
        val HISTORICAL_RESPONSE = """
            {
              "status": "ok",
              "name": "Market Price (USD)",
              "unit": "USD",
              "period": "day",
              "values": [{"x": 1577836800, "y": 7168.31}]
            }
        """.trimIndent()

        val CURRENT_RESPONSE = """
            {
              "USD": {
                "15m": 62621.99,
                "last": 62621.99,
                "buy": 62621.99,
                "sell": 62621.99,
                "symbol": "USD"
              }
            }
        """.trimIndent()
    }
}
