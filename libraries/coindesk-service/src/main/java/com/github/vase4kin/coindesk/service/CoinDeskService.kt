package com.github.vase4kin.coindesk.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * https://www.coindesk.com/coindesk-api
 */
interface CoinDeskService {

    /**
     * startDate and endDate format is yyyy-MM-dd
     */
    @GET("v1/bpi/historical/close.json")
    fun getBitcoinHistoricalPrice(
        @Query("start") startDate: String,
        @Query("end") endDate: String,
    ): Single<BitcoinHistoricalPrice>

    @GET("v1/bpi/currentprice.json")
    fun getBitcoinCurrentPrice(): Single<BitcoinCurrentPrice>

    companion object {
        const val BASE_URL = "https://api.coindesk.com/"
    }
}
