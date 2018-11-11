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

package com.travelbackintime.buybitcoin.time_travel.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import bitcoin.backintime.com.backintimebuybitcoin.R
import bitcoin.backintime.com.backintimebuybitcoin.databinding.FragmentTimeTravelBinding
import com.philliphsu.bottomsheetpickers.date.DatePickerDialog
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class TimeTravelFragment : DaggerFragment(), DatePickerDialog.OnDateSetListener, InvestMoneyBottomSheetDialog.InvestMoneyListener {

    companion object {
        fun create(): Fragment {
            return TimeTravelFragment()
        }
    }

    @Inject
    lateinit var viewModel: TimeTravelViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentTimeTravelBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_time_travel, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDateSet(dialog: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        viewModel.setTimeToTravel(year, monthOfYear, dayOfMonth)
    }

    override fun onInvestedMoneySet(investedMoney: Double) {
        viewModel.setInvestedMoney(investedMoney)
    }
}
