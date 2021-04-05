package com.github.vase4kin.shared.coindesk.service

/**
 * https://www.coindesk.com/coindesk-api
 */
interface CoinDeskService {

    /**
     * Get bitcoin price by date
     *
     * @param date - the date, format is yyyy-MM-dd
     */
    suspend fun getBitcoinHistoricalPrice(date: String): BitcoinHistoricalPrice

    /**
     * Get current bitcoin price by today
     */
    suspend fun getBitcoinCurrentPrice(): BitcoinCurrentPrice

    companion object {
        internal const val BASE_HOST = "api.coindesk.com"
        internal const val PATH_GET_CURRENT_BITCOIN_PRICE = "v1/bpi/currentprice.json"
        internal const val PATH_GET_BITCOIN_PRICE_BY_DATE = "v1/bpi/historical/close.json"
    }
}
