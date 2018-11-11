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

package com.travelbackintime.buybitcoin.home_coming.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import bitcoin.backintime.com.backintimebuybitcoin.R
import bitcoin.backintime.com.backintimebuybitcoin.databinding.FragmentHomeComingBinding
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult
import dagger.android.support.DaggerFragment
import javax.inject.Inject

const val EXTRA_RESULT = "extra_result"

class HomeComingFragment : DaggerFragment() {

    companion object {
        fun create(result: TimeTravelResult): Fragment {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_RESULT, result)
            val homeComingFragment = HomeComingFragment()
            homeComingFragment.arguments = bundle
            return homeComingFragment
        }
    }

    @Inject
    lateinit var viewModel: HomeComingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentHomeComingBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home_coming, container, false)
        viewModel.result = arguments?.getParcelable(EXTRA_RESULT)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.handleOnCreate()
    }

}
