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

import com.github.vase4kin.timetravelmachine.model.TimeTravelEvent
import io.reactivex.Single
import java.util.*

interface TimeTravelMachine {

    fun initFlowCapacitor(listener: FlowCapacitorInitListener)

    fun getBitcoinPriceByDate(timeToTravel: Date?): Single<Double>

    fun getBitcoinCurrentPrice(): Single<Double>

    fun getBitcoinStatus(timeToTravel: Date?): BitcoinStatus

    fun getTimeEvent(timeToTravel: Date?): TimeTravelEvent

    interface FlowCapacitorInitListener {
        fun onSuccess()
        fun onError()
        fun onDataNotDownloaded()
    }

    enum class BitcoinStatus {
        EXIST, BASICALLY_NOTHING
    }

    enum class EventType {
        HELLO_SATOSHI, PIZZA_LOVER, NO_EVENT
    }

    companion object {
        // Set a max date to minus one as coin desk does not support current date
        val maxDate: Calendar = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_WEEK, -1)
        }

        // Set a minimum date to 2009/1/03
        val minDate = GregorianCalendar(2009, 0, 3)
    }
}
