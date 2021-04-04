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

package com.travelbackintime.buybitcoin.homecoming.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import bitcoin.backintime.com.backintimebuybitcoin.R
import bitcoin.backintime.com.backintimebuybitcoin.databinding.FragmentHomeComingBinding
import bitcoin.backintime.com.backintimebuybitcoin.databinding.FragmentHomeComingEventBinding
import com.travelbackintime.buybitcoin.impl.TimeTravelEvenWrapper
import dagger.android.support.DaggerFragment
import javax.inject.Inject

const val EXTRA_RESULT = "extra_result"

class HomeComingFragment : DaggerFragment() {

    companion object {
        fun create(event: TimeTravelEvenWrapper): Fragment {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_RESULT, event)
            val homeComingFragment = HomeComingFragment()
            homeComingFragment.arguments = bundle
            return homeComingFragment
        }
    }

    @Inject
    lateinit var viewModel: HomeComingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val event: TimeTravelEvenWrapper = arguments?.getParcelable(EXTRA_RESULT)
            ?: TimeTravelEvenWrapper.RealWorldEvent("", "", false)
        viewModel.event = event
        return provideView(
            event = event,
            inflater = inflater,
            container = container
        )
    }

    private fun provideView(
        event: TimeTravelEvenWrapper,
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View {
        val viewBinding: ViewBinding = when (event) {
            is TimeTravelEvenWrapper.TimeTravelEvent -> {
                DataBindingUtil.inflate<FragmentHomeComingBinding>(
                    inflater, R.layout.fragment_home_coming, container, false
                ).apply {
                    viewModel = this@HomeComingFragment.viewModel
                }
            }
            is TimeTravelEvenWrapper.RealWorldEvent -> {
                DataBindingUtil.inflate<FragmentHomeComingEventBinding>(
                    inflater, R.layout.fragment_home_coming_event, container, false
                ).apply {
                    viewModel = this@HomeComingFragment.viewModel
                }
            }
        }
        return viewBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewLifecycleOwnerLiveData.observe(this, {
            it.lifecycle.addObserver(viewModel)
        })
    }
}
