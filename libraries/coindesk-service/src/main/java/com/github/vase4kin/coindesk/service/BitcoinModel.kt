package com.github.vase4kin.coindesk.service

data class BitcoinHistoricalPrice(
        val bpi: Map<String, Float>
)

data class BitcoinCurrentPrice(
        val bpi: Map<String, BitcoinPrice>
)

data class BitcoinPrice(
        val rate_float: Float
)