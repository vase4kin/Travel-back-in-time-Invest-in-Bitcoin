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

package com.travelbackintime.buybitcoin.timetravel.router

import androidx.appcompat.app.AppCompatActivity
import com.github.vase4kin.timetravelmachine.TimeTravelConstraints
import com.github.vase4kin.timetravelmachine.TimeTravelMachine
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.travelbackintime.buybitcoin.router.InternalRouter
import com.travelbackintime.buybitcoin.timetravel.view.InvestMoneyBottomSheetDialog
import com.travelbackintime.buybitcoin.timetravel.view.TimeTravelFragment
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

interface TimeTravelRouter {
    fun openLoadingFragment(event: TimeTravelMachine.Event)
    fun showAmountDialog()
    fun showSetDateDialog(onDateSelected: (date: Long) -> Unit)
    fun openErrorFragment()
}

class TimeTravelRouterImpl @Inject constructor(
    private val fragment: TimeTravelFragment,
    private val internalRouter: InternalRouter
) : TimeTravelRouter {

    private val activity: AppCompatActivity = fragment.activity as AppCompatActivity

    override fun showAmountDialog() {
        val dialog = InvestMoneyBottomSheetDialog()
        dialog.setListener(fragment)
        dialog.show(activity.supportFragmentManager, InvestMoneyBottomSheetDialog::class.java.name)
    }

    override fun showSetDateDialog(onDateSelected: (date: Long) -> Unit) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setStart(TimeTravelConstraints.minDateTimeInMillis)
                    .setEnd(TimeTravelConstraints.maxDateTimeInMillis)
                    .setValidator(DateVal())
                    .setOpenAt(TimeTravelConstraints.maxDateTimeInMillis)
                    .build()
            )
            .setSelection(TimeTravelConstraints.maxDateTimeInMillis)
            .setTitleText("")
            .build()
        picker.addOnPositiveButtonClickListener {
            onDateSelected(it)
        }
        picker.show(activity.supportFragmentManager, MaterialDatePicker::class.java.name)
    }

    override fun openLoadingFragment(event: TimeTravelMachine.Event) {
        internalRouter.openLoading(event)
    }

    override fun openErrorFragment() {
        internalRouter.openError()
    }

    @Parcelize
    private class DateVal : CalendarConstraints.DateValidator {
        override fun isValid(date: Long): Boolean {
            val timeRange =
                TimeTravelConstraints.minDateTimeInMillis..TimeTravelConstraints.maxDateTimeInMillis
            return date in timeRange
        }
    }
}
