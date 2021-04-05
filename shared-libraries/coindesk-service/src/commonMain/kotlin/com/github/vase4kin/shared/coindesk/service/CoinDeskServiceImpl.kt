package com.github.vase4kin.shared.coindesk.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.utils.io.core.use
import kotlinx.serialization.json.Json

class CoinDeskServiceImpl : CoinDeskService {

    override suspend fun getBitcoinHistoricalPrice(date: String): BitcoinHistoricalPrice {
        return createClient().use {
            it.get(CoinDeskService.PATH_GET_BITCOIN_PRICE_BY_DATE) {
                parameter("start", date)
                parameter("end", date)
            }
        }
    }

    override suspend fun getBitcoinCurrentPrice(): BitcoinCurrentPrice {
        return createClient().use {
            it.get(CoinDeskService.PATH_GET_CURRENT_BITCOIN_PRICE)
        }
    }

    private fun createClient(): HttpClient {
        return HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json {
                    ignoreUnknownKeys = true
                })
                acceptContentTypes = acceptContentTypes + listOf(ContentType.Application.JavaScript)
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = CoinDeskService.BASE_HOST
                }
            }
        }
    }
}
