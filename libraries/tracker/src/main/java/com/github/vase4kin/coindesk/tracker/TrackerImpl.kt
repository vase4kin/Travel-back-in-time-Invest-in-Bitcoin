package com.github.vase4kin.coindesk.tracker

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.Date

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

    override fun trackUserGetsToRealWorldEvent(eventName: String) {
        val bundle = Bundle()
        bundle.putString(PARAMETER_EVENT_NAME, eventName)
        analytics.logEvent(EVENT_USER_GETS_TO_SATOSHI, bundle)
    }

    override fun trackUserGetsToTimeTravelEvent(
        profitMoney: Double,
        investedMoney: Double,
        timeToTravel: Date
    ) {
        val bundle = Bundle()
        bundle.putDouble(PARAMETER_PROFIT, profitMoney)
        bundle.putDouble(PARAMETER_INVESTED, investedMoney)
        bundle.putString(PARAMETER_TIME, timeToTravel.toString())
        analytics.logEvent(EVENT_USER_GETS_TO_EXIST, bundle)
    }

    override fun trackUserSharesOnFb() {
        analytics.logEvent(EVENT_USER_SHARES_ON_FB, null)
    }

    override fun trackUserSharesOnTwitter() {
        analytics.logEvent(EVENT_USER_SHARES_ON_TWITTER, null)
    }

    override fun trackUserChooseMoneySuggestion(amount: String) {
        val bundle = Bundle()
        bundle.putString(PARAMETER_MONEY_SUGGESTION, amount)
        analytics.logEvent(EVENT_USER_CHOOSE_SUGGESTION, bundle)
    }
}
