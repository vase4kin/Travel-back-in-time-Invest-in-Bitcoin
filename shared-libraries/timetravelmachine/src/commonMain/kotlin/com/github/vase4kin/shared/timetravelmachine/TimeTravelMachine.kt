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

package com.github.vase4kin.shared.timetravelmachine

interface TimeTravelMachine {

    /**
     * @param time          - the time as UTC milliseconds from the epoch
     * @param investedMoney - the amount of invested money in double
     */
    suspend fun travelInTime(time: Long, investedMoney: Double): Event

    /**
     * Possible events that time travel machine can return
     */
    sealed class Event {
        /**
         * Event that is a specific date event, e.g. the date bitcoin was created (2009/01/03)
         *
         * @param title       - the title of the real world event
         * @param description - the description of the real world event
         * @param isDonate    - the flag that indicates if the donate button should be shown
         */
        data class RealWorldEvent(
            val title: String,
            val description: String,
            val isDonate: Boolean
        ) : Event()

        /**
         * Event that represents the profit calculation of the time travel
         *
         * @param profitMoney   - the profit money
         * @param investedMoney - the amount of the money that have been invested
         * @param timeToTravel  - the time of the time travel represented as UTC milliseconds from the epoch
         */
        data class TimeTravelEvent(
            val profitMoney: Double,
            val investedMoney: Double,
            val timeToTravel: Long
        ) : Event()
    }
}
