/*
 * Copyright 2018 Andrey Tolpeev
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

package com.github.vase4kin.shared.timetravelmachine

import com.github.vase4kin.shared.database.LocalDatabase
import com.github.vase4kin.shared.repository.Repository
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TimeTravelMachineImpl(
    private val repository: Repository,
    private val eventWithNoPrice: TimeTravelMachine.Event.RealWorldEvent
) : TimeTravelMachine {

    override suspend fun travelInTime(
        time: Long,
        investedMoney: Double
    ): TimeTravelMachine.Event {
        val serverDate = convertDateToServerDateFormat(time)
        return when (val event = repository.getTimeEvent(serverDate)) {
            is LocalDatabase.TimeTravelEvent.Event -> {
                TimeTravelMachine.Event.RealWorldEvent(
                    title = event.title,
                    description = event.description,
                    isDonate = event.isDonate
                )
            }
            is LocalDatabase.TimeTravelEvent.NoEvent -> {
                when {
                    isDateBeforePriceIsAvailable(time) -> eventWithNoPrice
                    else -> calculateProfit(time, investedMoney)
                }
            }
        }
    }

    private suspend fun calculateProfit(
        time: Long,
        investedMoney: Double
    ): TimeTravelMachine.Event {
        val bitcoinPriceByDate = getBitcoinPriceByDate(time)
        val bitcoinCurrentPrice = getBitcoinCurrentPrice()
        val profit = calculateProfit(bitcoinPriceByDate, bitcoinCurrentPrice, investedMoney)
        return TimeTravelMachine.Event.TimeTravelEvent(
            profitMoney = profit,
            investedMoney = investedMoney,
            timeToTravel = time
        )
    }

    private fun calculateProfit(
        bitcoinHistoricalPrice: Double,
        bitcoinCurrentPrice: Double,
        investedMoney: Double
    ): Double {
        val bitcoinInvestment = investedMoney / bitcoinHistoricalPrice
        return bitcoinCurrentPrice * bitcoinInvestment
    }

    private suspend fun getBitcoinPriceByDate(time: Long): Double {
        val serverDate = convertDateToServerDateFormat(time)
        return repository.getBitcoinPriceByDate(serverDate)
    }

    private suspend fun getBitcoinCurrentPrice(): Double {
        return repository.getCurrentBitcoinPrice()
    }

    private fun isDateBeforePriceIsAvailable(date: Long): Boolean {
        val dateFirst = TimeTravelConstraints.minCoinDeskDateTimeInMillis
        return dateFirst > date
    }

    private fun convertDateToServerDateFormat(date: Long): String {
        return Instant.fromEpochMilliseconds(date)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date.toString()
    }
}
