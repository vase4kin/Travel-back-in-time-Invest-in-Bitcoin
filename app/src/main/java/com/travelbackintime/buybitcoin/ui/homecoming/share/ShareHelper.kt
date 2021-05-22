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

package com.travelbackintime.buybitcoin.ui.homecoming.share

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.travelbackintime.buybitcoin.utils.TimeTravelEvenWrapper
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import java.util.Date
import javax.inject.Inject

interface ShareHelper {
    fun shareWithFriends(event: TimeTravelEvenWrapper.TimeTravelEvent)
}

class ShareHelperImpl @Inject constructor(
    fragment: Fragment,
    private val formatterUtils: FormatterUtils
) : ShareHelper {

    private val activity = fragment.activity as AppCompatActivity

    override fun shareWithFriends(event: TimeTravelEvenWrapper.TimeTravelEvent) {
        val textToShare = createShareText(event)
        ShareCompat.IntentBuilder(activity)
            .setHtmlText(textToShare)
            .setType("text/plain")
            .startChooser()
    }

    private fun createGooglePlayLink(): String {
        return activity.getString(R.string.url_google_play, activity.packageName)
    }

    private fun createShareText(event: TimeTravelEvenWrapper.TimeTravelEvent): String {
        val googlePlayLink = createGooglePlayLink()
        val date = event.timeToTravel
        val profitValue = event.profitMoney
        val investedValue = formatterUtils.formatPrice(event.investedMoney)
        val profit = formatterUtils.formatPrice(profitValue)
        return activity.getString(
            R.string.text_share,
            formatterUtils.formatDateToShareText(Date(date)),
            investedValue, profit, googlePlayLink
        )
    }
}
