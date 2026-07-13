/*
 * Copyright 2021  Andrey Tolpeev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.vase4kin.shared.bitcoinprice.service

import com.github.vase4kin.shared.bitcoinprice.service.models.BitcoinCurrentPrice
import com.github.vase4kin.shared.bitcoinprice.service.models.BitcoinHistoricalPrice

/** Network boundary for the Bitcoin/USD price provider. */
interface BitcoinPriceService {

    /**
     * Get bitcoin price by date
     *
     * @param date - the date, format is yyyy-MM-dd
     */
    suspend fun getBitcoinHistoricalPrice(date: String): BitcoinHistoricalPrice

    /**
     * Get current bitcoin price by today
     */
    suspend fun getBitcoinCurrentPrice(): Map<String, BitcoinCurrentPrice>

    companion object {
        internal const val BASE_HOST = "api.blockchain.info"
        internal const val PATH_GET_CURRENT_BITCOIN_PRICE = "ticker"
        internal const val PATH_GET_BITCOIN_PRICE_BY_DATE = "charts/market-price"
    }
}
