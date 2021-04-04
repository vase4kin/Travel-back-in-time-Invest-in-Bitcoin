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

import io.reactivex.Single
import java.util.Date

interface TimeTravelMachine {

    /**
     * @param time          - the time as UTC milliseconds from the epoch
     * @param investedMoney - the amount of invested money in double
     */
    fun travelInTime(time: Long, investedMoney: Double): Single<Event>

    sealed class Event {
        data class RealWorldEvent(
            val title: String,
            val description: String,
            val isDonate: Boolean
        ) : Event()

        data class TimeTravelEvent(
            val profitMoney: Double,
            val investedMoney: Double,
            val timeToTravel: Date
        ) : Event()
    }
}
