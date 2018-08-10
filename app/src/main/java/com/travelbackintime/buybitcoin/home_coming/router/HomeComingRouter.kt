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

package com.travelbackintime.buybitcoin.home_coming.router

import android.support.v7.app.AppCompatActivity
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.travelbackintime.buybitcoin.home_coming.view.HomeComingFragment
import com.travelbackintime.buybitcoin.time_travel.view.TimeTravelFragment
import javax.inject.Inject

interface HomeComingRouter {

    fun openTimeTravelFragment()
}

class HomeComingRouterImpl @Inject constructor(fragment: HomeComingFragment) : HomeComingRouter {

    private val activity = fragment.activity as AppCompatActivity

    override fun openTimeTravelFragment() {
        val fragmentManager = activity.supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStackImmediate()
        }
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, TimeTravelFragment.create())
                .commit()
    }
}
