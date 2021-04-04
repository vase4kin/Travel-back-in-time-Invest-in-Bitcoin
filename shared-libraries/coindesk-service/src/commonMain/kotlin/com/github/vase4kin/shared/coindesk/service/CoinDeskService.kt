package com.github.vase4kin.shared.coindesk.service

/**
 * https://www.coindesk.com/coindesk-api
 */
interface CoinDeskService {

    /**
     * Get bitcoin price by date
     *
     * startDate and endDate format is yyyy-MM-dd
     */
    suspend fun getBitcoinHistoricalPrice(date: String): BitcoinHistoricalPrice

    /**
     * Get current bitcoin price by today
     */
    suspend fun getBitcoinCurrentPrice(): BitcoinCurrentPrice

    companion object {
        internal const val BASE_URL = "https://api.coindesk.com/"
        internal const val URL_GET_CURRENT_BITCOIN_PRICE = "v1/bpi/currentprice.json"
        internal const val URL_GET_BITCOIN_PRICE_BY_DATE = "v1/bpi/historical/close.json"
    }
}
