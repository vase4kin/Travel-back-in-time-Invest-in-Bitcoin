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

package com.travelbackintime.buybitcoin.home_coming.interactor

import android.support.v7.app.AppCompatActivity
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.travelbackintime.buybitcoin.home_coming.view.EXTRA_RESULT
import com.travelbackintime.buybitcoin.home_coming.view.HomeComingFragment
import com.travelbackintime.buybitcoin.remote_config.RemoteConfigService
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import javax.inject.Inject

class HomeComingInteractorImpl @Inject constructor(
        private val fragment: HomeComingFragment,
        private val configService: RemoteConfigService,
        private val formatterUtils: FormatterUtils) : HomeComingInteractor {

    override val result: TimeTravelResult?
        get() {
            val args = fragment.arguments
            return args?.getParcelable(EXTRA_RESULT)
        }

    override val isAdsEnabled: Boolean
        get() = configService.isAdsEnabled

    override fun createGooglePlayLink(): String {
        val activity = fragment.activity as AppCompatActivity
        return activity.getString(R.string.url_google_play, activity.packageName)
    }

    override fun createShareText(result: TimeTravelResult): String {
        val googlePlayLink = createGooglePlayLink()
        val date = result.timeToTravel
        val profitValue = result.profitMoney
        val profit = formatterUtils.formatPrice(profitValue)
        return fragment.getString(R.string.text_share, formatterUtils.formatDateToShareText(date!!), profit, googlePlayLink)
    }
}
