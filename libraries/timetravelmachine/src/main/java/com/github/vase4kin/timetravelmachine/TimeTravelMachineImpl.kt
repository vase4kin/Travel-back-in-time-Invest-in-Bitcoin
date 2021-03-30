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

import android.content.res.Resources
import com.github.vase4kin.repository.Repository
import com.github.vase4kin.timetravelmachine.model.TimeTravelEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val REF_EVENTS = "events"
private const val PATTERN_TIME_DATE = "yyyy-MM-dd"
private const val PATTERN_TIME_EVENT_DATE = "yyyy-MM-dd"
private const val DATE_PRICE_AVAILABLE = "2010-7-18"

@Suppress("TooManyFunctions")
class TimeTravelMachineImpl(
    private val database: FirebaseDatabase,
    private val repository: Repository,
    private val resources: Resources
) : TimeTravelMachine {

    private var timeTravelEvents: Map<String, TimeTravelEvent> = HashMap()

    init {
        fetchData()
    }

    override fun travelInTime(
        timeToTravel: Date,
        investedMoney: Double
    ): Single<TimeTravelMachine.Event> {
        val event = getTimeEvent(timeToTravel)
        if (event != null) {
            return Single.just(
                TimeTravelMachine.Event.RealWorldEvent(
                    title = event.title,
                    description = event.description,
                    isDonate = event.isDonate
                )
            )
        } else {
            return when {
                isDateBeforePriceIsAvailable(timeToTravel) -> Single.just(
                    TimeTravelMachine.Event.RealWorldEvent(
                        title = resources.getString(R.string.text_basically_nothing),
                        description = "",
                        isDonate = false
                    )
                )
                else -> calculateProfit(timeToTravel, investedMoney)
            }
        }
    }

    private fun calculateProfit(
        timeToTravel: Date,
        investedMoney: Double
    ): Single<TimeTravelMachine.Event> {
        return Single.zip(
            getBitcoinPriceByDate(timeToTravel),
            getBitcoinCurrentPrice(),
            { t1, t2 -> calculateProfit(t1, t2, investedMoney) }
        ).map { profit ->
            TimeTravelMachine.Event.TimeTravelEvent(
                profitMoney = profit,
                investedMoney = investedMoney,
                timeToTravel = timeToTravel
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

    private fun getBitcoinPriceByDate(timeToTravel: Date): Single<Double> {
        val serverDate = convertDateToServerDateFormat(timeToTravel)
        return repository.getBitcoinPriceByDate(serverDate)
    }

    private fun getBitcoinCurrentPrice(): Single<Double> {
        return repository.getCurrentBitcoinPrice()
    }

    private fun getTimeEvent(timeToTravel: Date): TimeTravelEvent? {
        val eventServerDate = convertDateToEventServerDateFormat(timeToTravel)
        return timeTravelEvents[eventServerDate]
    }

    private fun isDateBeforePriceIsAvailable(date: Date): Boolean {
        val dateFirst = parseServerDateToDate(DATE_PRICE_AVAILABLE)
        return dateFirst.after(date)
    }

    private fun fetchData() {
        database.reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val timeEventsGenericTypeIndicator = object :
                    GenericTypeIndicator<Map<@JvmSuppressWildcards String, @JvmSuppressWildcards TimeTravelEvent>>() {}
                timeTravelEvents =
                    dataSnapshot.child(REF_EVENTS).getValue(timeEventsGenericTypeIndicator)
                        ?: HashMap()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                FirebaseCrashlytics.getInstance().recordException(databaseError.toException())
            }
        })
    }

    private fun convertDateToServerDateFormat(date: Date): String {
        val dateFormat = SimpleDateFormat(PATTERN_TIME_DATE, Locale.US)
        return dateFormat.format(date)
    }

    private fun convertDateToEventServerDateFormat(date: Date): String {
        val dateFormat = SimpleDateFormat(PATTERN_TIME_EVENT_DATE, Locale.US)
        return dateFormat.format(date)
    }

    private fun parseServerDateToDate(date: String): Date {
        val dateFormat = SimpleDateFormat(PATTERN_TIME_DATE, Locale.US)
        return try {
            dateFormat.parse(date)
        } catch (e: ParseException) {
            Date()
        }
    }
}
