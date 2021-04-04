package com.github.vase4kin.shared.repository

interface Repository {
    /**
     * Get bitcoin price by date in double
     *
     * startDate and endDate format is yyyy-MM-dd
     */
    suspend fun getBitcoinPriceByDate(date: String): Double

    /**
     * Get current bitcoin price by today in double
     */
    suspend fun getCurrentBitcoinPrice(): Double
}
