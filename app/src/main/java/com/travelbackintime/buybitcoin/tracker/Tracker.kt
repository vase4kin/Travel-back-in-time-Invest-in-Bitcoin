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

package com.travelbackintime.buybitcoin.tracker

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

private const val EVENT_USER_SETS_TIME = "event_user_sets_time"
private const val EVENT_USER_SETS_MONEY = "event_user_sets_money"
private const val EVENT_USER_TRAVELS_BACK = "event_user_travels_back"
private const val EVENT_USER_STARTS_OVER = "event_user_starts_over"
private const val EVENT_USER_SHARES_WITH_FRIENDS = "event_user_shares_with_friends"
private const val EVENT_USER_SHARES_ON_FB = "event_user_shares_on_fb"
private const val EVENT_USER_SHARES_ON_TWITTER = "event_user_shares_on_twitter"
private const val EVENT_USER_SEES_EMPTY_AMOUNT_ERROR = "event_user_sees_empty_mon_error"
private const val EVENT_USER_SEES_AT_LEAST_DOLLAR_ERROR = "event_user_sees_one_dollar_error"
private const val EVENT_USER_SEES_YOU_RICH_ERROR = "event_user_sees_you_rich_error"
private const val EVENT_USER_COPIES_BTC_WALLET = "event_user_copies_btc_wallet"
private const val EVENT_USER_GETS_TO_PIZZA_LOVER = "event_user_gets_to_pizza_lover"
private const val EVENT_USER_GETS_TO_SATOSHI = "event_user_gets_to_satoshi"
private const val EVENT_USER_GETS_TO_EXIST = "event_user_gets_to_exist"
private const val EVENT_USER_GETS_TO_NOT_BORN = "event_user_gets_to_not_exist"
private const val EVENT_USER_GETS_TO_BASICALLY_NOTHING = "event_user_gets_to_2009"
private const val EVENT_USER_GETS_TO_MAGICIAN = "event_user_gets_to_future"
private const val EVENT_USER_FETCHES_DATA = "event_user_fetch_data"
private const val EVENT_USER_RETRIES = "event_user_retries"
private const val PARAMETER_TIME = "parameter_time"
private const val PARAMETER_MONEY = "parameter_money"
private const val PARAMETER_RESULT = "parameter_result"

interface Tracker {

    fun trackUserSetsTime(time: String)

    fun trackUserSetsMoney(money: String)

    fun trackUserTravelsBackAndBuys()

    fun trackUserSeesEmptyAmountError()

    fun trackUserSeesAtLeastDollarError()

    fun trackUserSeesYouReachError()

    fun trackUserStartsOver()

    fun trackUserSharesWithFriends()

    fun trackUserCopiesBtcWalletAddress()

    fun trackUserGetsToPizzaLover()

    fun trackUserGetsToSatoshi()

    fun trackUserGetsToExist()

    fun trackUserGetsToNotBorn()

    fun trackUserGetsToBasicallyNothing()

    fun trackUserGetsToMagician()

    fun trackUserSharesOnFb()

    fun trackUserSharesOnTwitter()

    fun trackDataDownloadedSuccessfully()

    fun trackDataDownloadedError()

    fun trackDataNotDownloaded()

    fun trackUserRetries()
}

private const val STATUS_SUCCESS = "success"
private const val STATUS_ERROR = "error"
private const val STATUS_NOT_DOWNLOADED = "not_downloaded"

class TrackerImpl(private val analytics: FirebaseAnalytics) : Tracker {

    override fun trackUserRetries() {
        analytics.logEvent(EVENT_USER_RETRIES, Bundle())
    }

    override fun trackUserSetsTime(time: String) {
        val bundle = Bundle()
        bundle.putString(PARAMETER_TIME, time)
        analytics.logEvent(EVENT_USER_SETS_TIME, bundle)
    }

    override fun trackUserSetsMoney(money: String) {
        val bundle = Bundle()
        bundle.putString(PARAMETER_MONEY, money)
        analytics.logEvent(EVENT_USER_SETS_MONEY, bundle)
    }

    override fun trackUserTravelsBackAndBuys() {
        analytics.logEvent(EVENT_USER_TRAVELS_BACK, null)
    }

    override fun trackUserSeesEmptyAmountError() {
        analytics.logEvent(EVENT_USER_SEES_EMPTY_AMOUNT_ERROR, null)
    }

    override fun trackUserSeesAtLeastDollarError() {
        analytics.logEvent(EVENT_USER_SEES_AT_LEAST_DOLLAR_ERROR, null)
    }

    override fun trackUserSeesYouReachError() {
        analytics.logEvent(EVENT_USER_SEES_YOU_RICH_ERROR, null)
    }

    override fun trackUserStartsOver() {
        analytics.logEvent(EVENT_USER_STARTS_OVER, null)
    }

    override fun trackUserSharesWithFriends() {
        analytics.logEvent(EVENT_USER_SHARES_WITH_FRIENDS, null)
    }

    override fun trackUserCopiesBtcWalletAddress() {
        analytics.logEvent(EVENT_USER_COPIES_BTC_WALLET, null)
    }

    override fun trackUserGetsToPizzaLover() {
        analytics.logEvent(EVENT_USER_GETS_TO_PIZZA_LOVER, null)
    }

    override fun trackUserGetsToSatoshi() {
        analytics.logEvent(EVENT_USER_GETS_TO_SATOSHI, null)
    }

    override fun trackUserGetsToExist() {
        analytics.logEvent(EVENT_USER_GETS_TO_EXIST, null)
    }

    override fun trackUserGetsToNotBorn() {
        analytics.logEvent(EVENT_USER_GETS_TO_NOT_BORN, null)
    }

    override fun trackUserGetsToBasicallyNothing() {
        analytics.logEvent(EVENT_USER_GETS_TO_BASICALLY_NOTHING, null)
    }

    override fun trackUserGetsToMagician() {
        analytics.logEvent(EVENT_USER_GETS_TO_MAGICIAN, null)
    }

    override fun trackUserSharesOnFb() {
        analytics.logEvent(EVENT_USER_SHARES_ON_FB, null)
    }

    override fun trackUserSharesOnTwitter() {
        analytics.logEvent(EVENT_USER_SHARES_ON_TWITTER, null)
    }

    override fun trackDataDownloadedSuccessfully() {
        trackDataDownloaded(STATUS_SUCCESS)
    }

    override fun trackDataDownloadedError() {
        trackDataDownloaded(STATUS_ERROR)
    }

    override fun trackDataNotDownloaded() {
        trackDataDownloaded(STATUS_NOT_DOWNLOADED)
    }

    private fun trackDataDownloaded(status: String) {
        val bundle = Bundle()
        bundle.putString(PARAMETER_RESULT, status)
        analytics.logEvent(EVENT_USER_FETCHES_DATA, bundle)
    }
}
