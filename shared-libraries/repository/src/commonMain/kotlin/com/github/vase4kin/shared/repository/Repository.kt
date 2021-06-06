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

package com.github.vase4kin.shared.repository

interface Repository {
    /**
     * Get bitcoin price by date in double
     *
     * @param date - the date, format is yyyy-MM-dd
     */
    suspend fun getBitcoinPriceByDate(date: String): Double

    /**
     * Get current bitcoin price by today in double
     */
    suspend fun getCurrentBitcoinPrice(): Double
}
