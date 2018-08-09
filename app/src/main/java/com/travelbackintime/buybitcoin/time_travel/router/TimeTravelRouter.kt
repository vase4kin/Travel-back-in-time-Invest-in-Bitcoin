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

import android.support.v7.app.AppCompatActivity
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog
import com.travelbackintime.buybitcoin.loading.LoadingFragment
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult
import com.travelbackintime.buybitcoin.time_travel.view.InvestMoneyBottomSheetDialog
import com.travelbackintime.buybitcoin.time_travel.view.TimeTravelFragment
import com.travelbackintime.buybitcoin.utils.addTransitions
import java.util.*
import javax.inject.Inject

interface TimeTravelRouter {

    fun openLoadingActivity(result: TimeTravelResult)

    fun showAmountDialog()

    fun showSetDateDialog()
}

class TimeTravelRouterImpl @Inject constructor(private val fragment: TimeTravelFragment) : TimeTravelRouter {

    val activity: AppCompatActivity = fragment.activity as AppCompatActivity

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
                calendar.get(Calendar.DAY_OF_MONTH))
                .setThemeDark(true)
                .build()
        dateDialog.show(activity.supportFragmentManager, DatePickerDialog::class.java.name)
    }

    override fun openLoadingActivity(result: TimeTravelResult) {
        val fragment = LoadingFragment.create(result)
        addTransitions(fragment, activity.applicationContext)
        activity.supportFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit()
    }
}
