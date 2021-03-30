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

package com.travelbackintime.buybitcoin.time_travel.router

import androidx.appcompat.app.AppCompatActivity
import com.github.vase4kin.timetravelmachine.TimeTravelMachine
import com.github.vase4kin.timetravelmachine.TimeTravelMachine.Companion.maxDate
import com.github.vase4kin.timetravelmachine.TimeTravelMachine.Companion.minDate
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog
import com.travelbackintime.buybitcoin.router.InternalRouter
import com.travelbackintime.buybitcoin.time_travel.view.InvestMoneyBottomSheetDialog
import com.travelbackintime.buybitcoin.time_travel.view.TimeTravelFragment
import java.util.Calendar
import javax.inject.Inject

interface TimeTravelRouter {
    fun openLoadingFragment(event: TimeTravelMachine.Event)
    fun showAmountDialog()
    fun showSetDateDialog()
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

    override fun showSetDateDialog() {
        val calendar = Calendar.getInstance()
        val dateDialog = DatePickerDialog.Builder(
                fragment,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_WEEK))
                .setThemeDark(true)
                .setMaxDate(maxDate)
                .setMinDate(minDate)
                .build()
        dateDialog.show(activity.supportFragmentManager, DatePickerDialog::class.java.name)
    }

    override fun openLoadingFragment(event: TimeTravelMachine.Event) {
        internalRouter.openLoading(event)
    }

    override fun openErrorFragment() {
        internalRouter.openError()
    }
}
