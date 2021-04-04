package com.github.vase4kin.shared.coindesk.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.utils.io.core.use

class CoinDeskServiceImpl : CoinDeskService {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        defaultRequest {
            url {
                host = CoinDeskService.BASE_URL
            }
        }
    }

    override suspend fun getBitcoinHistoricalPrice(date: String): BitcoinHistoricalPrice {
        return client.use {
            client.get(CoinDeskService.URL_GET_BITCOIN_PRICE_BY_DATE) {
                parameter("start", date)
                parameter("end", date)
            }
        }
    }

    override suspend fun getBitcoinCurrentPrice(): BitcoinCurrentPrice {
        return client.use {
            client.get(CoinDeskService.URL_GET_CURRENT_BITCOIN_PRICE)
        }
    }
}
