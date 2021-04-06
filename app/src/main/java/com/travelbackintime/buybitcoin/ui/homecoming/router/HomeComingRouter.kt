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

package com.travelbackintime.buybitcoin.ui.homecoming.router

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.travelbackintime.buybitcoin.ui.homecoming.view.HomeComingFragment
import com.travelbackintime.buybitcoin.ui.router.InternalRouter
import javax.inject.Inject

interface HomeComingRouter {
    fun openTimeTravelFragment()
    fun openPoweredByCoinDeskUrl()
}

class HomeComingRouterImpl @Inject constructor(
    private val fragment: HomeComingFragment,
    private val internalRouter: InternalRouter
) : HomeComingRouter {

    private val activity = fragment.activity as AppCompatActivity

    override fun openTimeTravelFragment() {
        internalRouter.openTimeTravelFromHomeComing()
    }

    override fun openPoweredByCoinDeskUrl() {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(activity.resources.getString(R.string.powered_by_coindesk_url))
        )
        activity.startActivity(browserIntent)
    }
}
