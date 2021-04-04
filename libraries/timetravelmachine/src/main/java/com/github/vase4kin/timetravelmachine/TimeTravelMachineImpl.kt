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

package com.github.vase4kin.timetravelmachine

import com.github.vase4kin.database.LocalDatabase
import com.github.vase4kin.repository.Repository
import io.reactivex.Single
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val PATTERN_TIME_DATE = "yyyy-MM-dd"

class TimeTravelMachineImpl(
    private val repository: Repository,
    private val defaultEvent: TimeTravelMachine.Event.RealWorldEvent
) : TimeTravelMachine {

    override fun travelInTime(
        time: Long,
        investedMoney: Double
    ): Single<TimeTravelMachine.Event> {
        val eventServerDate = convertDateToTimeMachineDate(time.toDate())
        return repository.getTimeEvent(eventServerDate)
            .flatMap { event ->
                when (event) {
                    is LocalDatabase.TimeTravelEvent.Event -> Single.just(
                        TimeTravelMachine.Event.RealWorldEvent(
                            title = event.title,
                            description = event.description,
                            isDonate = event.isDonate
                        )
                    )
                    LocalDatabase.TimeTravelEvent.NoEvent -> {
                        when {
                            isDateBeforePriceIsAvailable(time.toDate()) -> Single.just(defaultEvent)
                            else -> calculateProfit(time.toDate(), investedMoney)
                        }
                    }
                }
            }
    }

    private fun calculateProfit(
        time: Date,
        investedMoney: Double
    ): Single<TimeTravelMachine.Event> {
        return Single.zip(
            getBitcoinPriceByDate(time),
            getBitcoinCurrentPrice(),
            { t1, t2 -> calculateProfit(t1, t2, investedMoney) }
        ).map { profit ->
            TimeTravelMachine.Event.TimeTravelEvent(
                profitMoney = profit,
                investedMoney = investedMoney,
                timeToTravel = time
            )
        }
    }

    private fun calculateProfit(
        bitcoinHistoricalPrice: Double,
        bitcoinCurrentPrice: Double,
        investedMoney: Double
    ): Double {
        val bitcoinInvestment = investedMoney / bitcoinHistoricalPrice
        return bitcoinCurrentPrice * bitcoinInvestment
    }

    private fun getBitcoinPriceByDate(time: Date): Single<Double> {
        val serverDate = convertDateToTimeMachineDate(time)
        return repository.getBitcoinPriceByDate(serverDate)
    }

    private fun getBitcoinCurrentPrice(): Single<Double> {
        return repository.getCurrentBitcoinPrice()
    }

    private fun isDateBeforePriceIsAvailable(date: Date): Boolean {
        val dateFirst = TimeTravelConstraints.minCoinDeskDateTimeInMillis.toDate()
        return dateFirst.after(date)
    }

    private fun convertDateToTimeMachineDate(date: Date): String {
        val dateFormat = SimpleDateFormat(PATTERN_TIME_DATE, Locale.US)
        return dateFormat.format(date)
    }

    private fun Long.toDate() = Date(this)
}
