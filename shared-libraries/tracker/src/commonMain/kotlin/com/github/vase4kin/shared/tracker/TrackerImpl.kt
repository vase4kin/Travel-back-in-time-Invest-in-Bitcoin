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

package com.github.vase4kin.shared.tracker

import kotlinx.datetime.Instant

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
private const val EVENT_USER_GETS_TO_SATOSHI = "event_user_gets_to_satoshi"
private const val EVENT_USER_GETS_TO_EXIST = "event_user_gets_to_exist"
private const val EVENT_USER_RETRIES = "event_user_retries"
private const val EVENT_USER_CHOOSE_SUGGESTION = "event_user_choose_money_suggestion"
private const val PARAMETER_TIME = "parameter_time"
private const val PARAMETER_MONEY = "parameter_money"
private const val PARAMETER_PROFIT = "parameter_profit"
private const val PARAMETER_INVESTED = "parameter_invested"
private const val PARAMETER_EVENT_NAME = "parameter_name"
private const val PARAMETER_MONEY_SUGGESTION = "parameter_money_suggestion"

@Suppress("TooManyFunctions")
class TrackerImpl(
    private val analytics: NativeAnalytics
) : Tracker {

    override fun trackUserRetries() {
        analytics.logEvent(EVENT_USER_RETRIES)
    }

    override fun trackUserSetsTime(time: String) {
        analytics.logEvent(
            EVENT_USER_SETS_TIME, mapOf(
                PARAMETER_TIME to time
            )
        )
    }

    override fun trackUserSetsMoney(money: String) {
        analytics.logEvent(
            EVENT_USER_SETS_MONEY, mapOf(
                PARAMETER_MONEY to money
            )
        )
    }

    override fun trackUserTravelsBackAndBuys() {
        analytics.logEvent(EVENT_USER_TRAVELS_BACK)
    }

    override fun trackUserSeesEmptyAmountError() {
        analytics.logEvent(EVENT_USER_SEES_EMPTY_AMOUNT_ERROR)
    }

    override fun trackUserSeesAtLeastDollarError() {
        analytics.logEvent(EVENT_USER_SEES_AT_LEAST_DOLLAR_ERROR)
    }

    override fun trackUserSeesYouReachError() {
        analytics.logEvent(EVENT_USER_SEES_YOU_RICH_ERROR)
    }

    override fun trackUserStartsOver() {
        analytics.logEvent(EVENT_USER_STARTS_OVER)
    }

    override fun trackUserSharesWithFriends() {
        analytics.logEvent(EVENT_USER_SHARES_WITH_FRIENDS)
    }

    override fun trackUserCopiesBtcWalletAddress() {
        analytics.logEvent(EVENT_USER_COPIES_BTC_WALLET)
    }

    override fun trackUserGetsToRealWorldEvent(eventName: String) {
        analytics.logEvent(
            EVENT_USER_GETS_TO_SATOSHI, mapOf(
                PARAMETER_EVENT_NAME to eventName
            )
        )
    }

    override fun trackUserGetsToTimeTravelEvent(
        profitMoney: Double,
        investedMoney: Double,
        time: Long
    ) {
        analytics.logEvent(
            EVENT_USER_GETS_TO_EXIST, mapOf(
                PARAMETER_PROFIT to profitMoney.toString(),
                PARAMETER_INVESTED to investedMoney.toString(),
                PARAMETER_TIME to Instant.fromEpochMilliseconds(time).toString()
            )
        )
    }

    override fun trackUserSharesOnFb() {
        analytics.logEvent(EVENT_USER_SHARES_ON_FB)
    }

    override fun trackUserSharesOnTwitter() {
        analytics.logEvent(EVENT_USER_SHARES_ON_TWITTER)
    }

    override fun trackUserChooseMoneySuggestion(amount: String) {
        analytics.logEvent(
            EVENT_USER_CHOOSE_SUGGESTION, mapOf(
                PARAMETER_MONEY_SUGGESTION to amount
            )
        )
    }
}
