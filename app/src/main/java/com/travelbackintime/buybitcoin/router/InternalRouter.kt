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

package com.travelbackintime.buybitcoin.router

import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachine
import com.travelbackintime.buybitcoin.error.view.ErrorFragment
import com.travelbackintime.buybitcoin.homecoming.view.HomeComingFragment
import com.travelbackintime.buybitcoin.impl.TimeTravelEvenWrapper
import com.travelbackintime.buybitcoin.loading.LoadingFragment
import com.travelbackintime.buybitcoin.timetravel.view.TimeTravelActivity
import com.travelbackintime.buybitcoin.timetravel.view.TimeTravelFragment
import com.travelbackintime.buybitcoin.utils.addFragmentFadeTransitions
import com.travelbackintime.buybitcoin.utils.addFragmentSlideTransitions

interface InternalRouter {
    fun openTimeTravelFromSplash()
    fun openError()
    fun closeError()
    fun openLoading(event: TimeTravelMachine.Event)
    fun openHomeComingFromLoading(event: TimeTravelEvenWrapper)
    fun openTimeTravelFromHomeComing()
}

class InternalRouterImpl(
    private val activity: TimeTravelActivity
) : InternalRouter {
    override fun openTimeTravelFromSplash() {
        val fragment = TimeTravelFragment.create()
        addFragmentFadeTransitions(fragment, activity.applicationContext)
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commitAllowingStateLoss()
    }

    override fun openError() {
        val fragment = ErrorFragment.create()
        addFragmentSlideTransitions(fragment, activity.applicationContext)
        activity.supportFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    override fun closeError() {
        activity.supportFragmentManager.popBackStack()
    }

    override fun openLoading(event: TimeTravelMachine.Event) {
        val fragment = LoadingFragment.create(event)
        addFragmentSlideTransitions(fragment, activity.applicationContext)
        activity.supportFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    override fun openHomeComingFromLoading(event: TimeTravelEvenWrapper) {
        val homeComingFragment = HomeComingFragment.create(event)
        addFragmentSlideTransitions(homeComingFragment, activity.applicationContext)
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.popBackStack()
        fragmentManager
                .beginTransaction()
                .add(R.id.container, homeComingFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    override fun openTimeTravelFromHomeComing() {
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.popBackStack()
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, TimeTravelFragment.create())
                .commitAllowingStateLoss()
    }
}
