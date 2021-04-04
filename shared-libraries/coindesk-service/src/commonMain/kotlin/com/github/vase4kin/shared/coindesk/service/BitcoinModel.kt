package com.github.vase4kin.shared.coindesk.service

import kotlinx.serialization.Serializable

@Serializable
data class BitcoinHistoricalPrice(
    val bpi: Map<String, Float>
)

@Serializable
data class BitcoinCurrentPrice(
    val bpi: Map<String, BitcoinPrice>
)

@Suppress("ConstructorParameterNaming")
@Serializable
data class BitcoinPrice(
    val rate_float: Float
)
