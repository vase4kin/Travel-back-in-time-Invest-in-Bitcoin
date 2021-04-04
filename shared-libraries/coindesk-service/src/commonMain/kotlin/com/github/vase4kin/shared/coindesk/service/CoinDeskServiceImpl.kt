package com.github.vase4kin.shared.coindesk.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
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
    }

    override suspend fun getBitcoinHistoricalPrice(date: String): BitcoinHistoricalPrice {
        val url = "${CoinDeskService.BASE_URL}${CoinDeskService.URL_GET_BITCOIN_PRICE_BY_DATE}"
        return client.use {
            client.get(url) {
                parameter("start", date)
                parameter("end", date)
            }
        }
    }

    override suspend fun getBitcoinCurrentPrice(): BitcoinCurrentPrice {
        val url = "${CoinDeskService.BASE_URL}${CoinDeskService.URL_GET_CURRENT_BITCOIN_PRICE}"
        return client.use {
            client.get(url)
        }
    }
}
