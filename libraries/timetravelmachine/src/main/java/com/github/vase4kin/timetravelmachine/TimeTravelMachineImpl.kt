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

import android.content.SharedPreferences
import com.github.vase4kin.repository.Repository
import com.github.vase4kin.timetravelmachine.model.TimeTravelEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.*
import io.reactivex.Single
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

private const val REF_CONNECTION = ".info/connected"
private const val REF_EVENTS = "events"
private const val KEY_DATA_DOWNLOADED = "key_data_downloaded"
private const val PATTERN_TIME_DATE = "yyyy-MM-dd"
private const val PATTERN_TIME_EVENT_DATE = "yyyy-MM-dd"
private const val DATE_PRICE_AVAILABLE = "2010-7-18"

class TimeTravelMachineImpl(
        private val database: FirebaseDatabase,
        private val sharedPreferences: SharedPreferences,
        private val repository: Repository
) : TimeTravelMachine {

    private var timeTravelEvents: Map<String, TimeTravelEvent> = HashMap()

    private var isDataDownloaded: Boolean
        get() = sharedPreferences.getBoolean(KEY_DATA_DOWNLOADED, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_DATA_DOWNLOADED, value).apply()

    override fun initFlowCapacitor(listener: TimeTravelMachine.FlowCapacitorInitListener) {
        database.getReference(REF_CONNECTION).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val connected = dataSnapshot.getValue(Boolean::class.java)
                if (connected == true) {
                    fetchData(listener)
                    isDataDownloaded = true
                } else {
                    if (isDataDownloaded) {
                        fetchData(listener)
                    } else {
                        listener.onDataNotDownloaded()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                FirebaseCrashlytics.getInstance().recordException(databaseError.toException())
                listener.onError()
            }
        })
    }

    override fun getBitcoinPriceByDate(timeToTravel: Date?): Single<Double> {
        timeToTravel ?: return Single.just(Double.NaN)
        val serverDate = convertDateToServerDateFormat(timeToTravel)
        return repository.getBitcoinPriceByDate(serverDate)
    }

    override fun getBitcoinCurrentPrice(): Single<Double> {
        return repository.getCurrentBitcoinPrice()
    }

    override fun getBitcoinStatus(timeToTravel: Date?): TimeTravelMachine.BitcoinStatus {
        timeToTravel ?: return TimeTravelMachine.BitcoinStatus.BASICALLY_NOTHING
        return when {
            isDateBeforePriceIsAvailable(timeToTravel) -> TimeTravelMachine.BitcoinStatus.BASICALLY_NOTHING
            else -> TimeTravelMachine.BitcoinStatus.EXIST
        }
    }

    override fun getTimeEvent(timeToTravel: Date?): TimeTravelEvent {
        timeToTravel ?: return TimeTravelEvent(TimeTravelMachine.EventType.NO_EVENT.name)
        val eventServerDate = convertDateToEventServerDateFormat(timeToTravel)
        val timeTravelEvent = timeTravelEvents[eventServerDate]
        return timeTravelEvent ?: TimeTravelEvent(TimeTravelMachine.EventType.NO_EVENT.name)
    }

    private fun isDateBeforePriceIsAvailable(date: Date): Boolean {
        val dateFirst = parseServerDateToDate(DATE_PRICE_AVAILABLE)
        return dateFirst.after(date)
    }

    private fun fetchData(listener: TimeTravelMachine.FlowCapacitorInitListener) {
        database.reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val timeEventsGenericTypeIndicator = object : GenericTypeIndicator<Map<@JvmSuppressWildcards String, @JvmSuppressWildcards TimeTravelEvent>>() {}
                timeTravelEvents = dataSnapshot.child(REF_EVENTS).getValue(timeEventsGenericTypeIndicator)
                        ?: HashMap()
                listener.onSuccess()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                FirebaseCrashlytics.getInstance().recordException(databaseError.toException())
                listener.onError()
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
