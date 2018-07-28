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

package com.travelbackintime.buybitcoin.time_travel_machine

import com.travelbackintime.buybitcoin.time_travel_machine.model.TimeTravelEvent
import java.util.*

interface TimeTravelMachine {

    fun initFlowCapacitor(listener: FlowCapacitorInitListener)

    fun getBitcoinPrice(timeToTravel: Date?): Double

    fun getBitcoinCurrentPrice(): Double

    fun getBitcoinStatus(timeToTravel: Date?): BitcoinStatus

    fun getTimeEvent(timeToTravel: Date?): TimeTravelEvent

    interface FlowCapacitorInitListener {
        fun onSuccess()

        fun onError()

        fun onDataNotDownloaded()
    }

    enum class BitcoinStatus {
        EXIST, NOT_BORN, AM_I_A_MAGICIAN_TO_KNOW
    }

    enum class EventType {
        BASICALLY_NOTHING, HELLO_SATOSHI, PIZZA_LOVER, NO_EVENT
    }
}