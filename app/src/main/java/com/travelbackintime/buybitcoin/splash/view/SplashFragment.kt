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

package com.travelbackintime.buybitcoin.splash.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import bitcoin.backintime.com.backintimebuybitcoin.R
import bitcoin.backintime.com.backintimebuybitcoin.databinding.FragmentSplashBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SplashFragment : DaggerFragment() {

    companion object {
        fun create(): Fragment {
            return SplashFragment()
        }
    }

    @Inject
    lateinit var viewModel: SplashViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentSplashBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_splash, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.handleOnCreate()
    }
}
