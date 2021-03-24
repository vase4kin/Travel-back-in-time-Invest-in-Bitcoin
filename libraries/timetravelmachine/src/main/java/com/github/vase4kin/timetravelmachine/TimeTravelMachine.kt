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

import android.os.Parcelable
import io.reactivex.Single
import kotlinx.android.parcel.Parcelize
import java.util.*

interface TimeTravelMachine {

    fun travelInTime(timeToTravel: Date, investedMoney: Double): Single<Event>

    sealed class Event : Parcelable {
        @Parcelize
        data class RealWorldEvent(
                val title: String,
                val description: String,
                val isDonate: Boolean
        ) : Event()

        @Parcelize
        data class TimeTravelEvent(
                val profitMoney: Double,
                val investedMoney: Double,
                val timeToTravel: Date
        ) : Event()
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
