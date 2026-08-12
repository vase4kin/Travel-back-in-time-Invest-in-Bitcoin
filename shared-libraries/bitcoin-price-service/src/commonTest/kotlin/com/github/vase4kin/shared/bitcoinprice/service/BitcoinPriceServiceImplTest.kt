package com.github.vase4kin.shared.bitcoinprice.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.headersOf
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith

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

    @Test
    fun requestTimeoutIsRetriedOnceBeforeBeingPropagated() = runTest {
        var attempts = 0
        val service = serviceWithEngine(requestTimeoutMillis = 10) {
            attempts++
            delay(50)
            jsonResponse(CURRENT_RESPONSE)
        }

        assertFails { service.getBitcoinCurrentPrice() }

        assertEquals(2, attempts)
    }

    @Test
    fun retryableHttpStatusesAreRetriedOnce() = runTest {
        listOf(
            HttpStatusCode.RequestTimeout,
            HttpStatusCode.TooManyRequests,
            HttpStatusCode.InternalServerError,
        ).forEach { retryableStatus ->
            var attempts = 0
            val service = serviceWithEngine {
                attempts++
                if (attempts == 1) {
                    jsonResponse("{}", retryableStatus)
                } else {
                    jsonResponse(CURRENT_RESPONSE)
                }
            }

            val result = service.getBitcoinCurrentPrice()

            assertEquals(2, attempts, "Expected a retry for $retryableStatus")
            assertEquals(62_621.99, result.getValue("USD").last)
        }
    }

    @Test
    fun retryAfterHeaderOverridesFixedDelay() = runTest {
        var attempts = 0
        val delays = mutableListOf<Long>()
        val service = serviceWithEngine(retryDelay = delays::add) {
            attempts++
            if (attempts == 1) {
                respond(
                    content = "{}",
                    status = HttpStatusCode.TooManyRequests,
                    headers = headersOf(
                        HttpHeaders.ContentType to listOf("application/json"),
                        HttpHeaders.RetryAfter to listOf("2"),
                    ),
                )
            } else {
                jsonResponse(CURRENT_RESPONSE)
            }
        }

        service.getBitcoinCurrentPrice()

        assertEquals(2, attempts)
        assertEquals(listOf(2_000L), delays)
    }

    @Test
    fun retryStopsAfterTwoAttempts() = runTest {
        var attempts = 0
        val service = serviceWithEngine {
            attempts++
            jsonResponse("{}", HttpStatusCode.ServiceUnavailable)
        }

        assertEquals(emptyMap(), service.getBitcoinCurrentPrice())

        assertEquals(2, attempts)
    }

    @Test
    fun ordinaryClientErrorIsNotRetried() = runTest {
        var attempts = 0
        val service = serviceWithEngine {
            attempts++
            jsonResponse("{}", HttpStatusCode.NotFound)
        }

        assertEquals(emptyMap(), service.getBitcoinCurrentPrice())

        assertEquals(1, attempts)
    }

    @Test
    fun networkFailureIsRetriedOnce() = runTest {
        var attempts = 0
        val service = serviceWithEngine {
            attempts++
            if (attempts == 1) {
                throw IOException("network unavailable")
            }
            jsonResponse(CURRENT_RESPONSE)
        }

        val result = service.getBitcoinCurrentPrice()

        assertEquals(2, attempts)
        assertEquals(62_621.99, result.getValue("USD").last)
    }

    @Test
    fun decodingFailureIsNotRetried() = runTest {
        var attempts = 0
        val service = serviceWithEngine {
            attempts++
            jsonResponse("not-json")
        }

        assertFails { service.getBitcoinCurrentPrice() }

        assertEquals(1, attempts)
    }

    @Test
    fun cancellationIsNotRetried() = runTest {
        var attempts = 0
        val service = serviceWithEngine {
            attempts++
            throw CancellationException("cancelled")
        }

        assertFailsWith<CancellationException> { service.getBitcoinCurrentPrice() }

        assertEquals(1, attempts)
    }

    private fun serviceReturning(body: String, verifyRequest: (Url) -> Unit): BitcoinPriceService =
        serviceWithEngine { request ->
            verifyRequest(request.url)
            jsonResponse(body)
        }

    private fun serviceWithEngine(
        requestTimeoutMillis: Long = 10_000,
        retryDelay: suspend (Long) -> Unit = {},
        handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData,
    ): BitcoinPriceService {
        val client = HttpClient(MockEngine(handler)) {
            configureBitcoinPriceHttpClient(
                requestTimeoutMillis = requestTimeoutMillis,
                retryDelay = retryDelay,
            )
        }
        return BitcoinPriceServiceImpl(client)
    }

    private fun MockRequestHandleScope.jsonResponse(
        content: String,
        status: HttpStatusCode = HttpStatusCode.OK,
    ): HttpResponseData = respond(
        content = content,
        status = status,
        headers = headersOf(HttpHeaders.ContentType, "application/json"),
    )

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
