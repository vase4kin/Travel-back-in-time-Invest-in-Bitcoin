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

package com.travelbackintime.buybitcoin.home_coming.view

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.timetravelmachine.TimeTravelMachine
import com.travelbackintime.buybitcoin.home_coming.router.HomeComingRouter
import com.travelbackintime.buybitcoin.remote_config.RemoteConfigService
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult
import com.travelbackintime.buybitcoin.tracker.Tracker
import com.travelbackintime.buybitcoin.utils.*
import javax.inject.Inject

class HomeComingViewModel @Inject constructor(
        private val router: HomeComingRouter,
        private val tracker: Tracker,
        private val configService: RemoteConfigService,
        private val formatterUtils: FormatterUtils,
        private val resourcesProviderUtils: ResourcesProviderUtils,
        private val toastUtils: ToastUtils,
        private val clipboardUtils: ClipboardUtils) {

    val isShareViewVisible = ObservableBoolean(false)
    val isParamViewVisible = ObservableBoolean(false)
    val isProfitViewVisible = ObservableBoolean(false)
    val isInfoTextViewVisible = ObservableBoolean(false)
    val isDonateViewVisible = ObservableBoolean(false)
    val infoText = ObservableField<String>().onChanged {
        isInfoTextViewVisible.set(true)
    }
    val profitMoneyText = ObservableField<String>()
    val investedMoneyText = ObservableField<String>()
    val monthText = ObservableField<String>()
    val yearText = ObservableField<String>()

    val isAdsEnabled: Boolean
        get() = configService.isAdsEnabled

    var result: TimeTravelResult? = null

    fun handleOnCreate() {
        val result = this.result ?: return
        when {
            result.eventType == TimeTravelMachine.EventType.NO_EVENT -> {
                when (result.status) {
                    TimeTravelMachine.BitcoinStatus.AM_I_A_MAGICIAN_TO_KNOW -> {
                        showInfoText(R.string.result_title_1)
                        tracker.trackUserGetsToMagician()
                    }
                    TimeTravelMachine.BitcoinStatus.NOT_BORN -> {
                        showInfoText(R.string.result_title_2)
                        tracker.trackUserGetsToNotBorn()
                    }
                    TimeTravelMachine.BitcoinStatus.EXIST -> {
                        setTimeMachineDisplay(result)
                        isShareViewVisible.set(true)
                        isParamViewVisible.set(true)
                        isProfitViewVisible.set(true)
                        tracker.trackUserGetsToExist()
                    }
                }
            }
            else -> {
                when (result.eventType) {
                    TimeTravelMachine.EventType.HELLO_SATOSHI -> {
                        showInfoText(R.string.result_title_4)
                        isDonateViewVisible.set(true)
                        tracker.trackUserGetsToSatoshi()
                    }
                    TimeTravelMachine.EventType.PIZZA_LOVER -> {
                        showInfoText(R.string.result_title_5)
                        tracker.trackUserGetsToPizzaLover()
                    }
                    TimeTravelMachine.EventType.BASICALLY_NOTHING, TimeTravelMachine.EventType.NO_EVENT -> {
                        showInfoText(R.string.result_title_3)
                        tracker.trackUserGetsToBasicallyNothing()
                    }
                }
            }
        }
    }

    fun onStartOver() {
        router.openTimeTravelFragment()
        tracker.trackUserStartsOver()
    }

    fun onShareWithFriends() {
        val result = this.result ?: return
        router.shareWithFriends(result)
        tracker.trackUserSharesWithFriends()
    }

    fun onShareOnFacebook() {
        router.shareToFaceBook()
        tracker.trackUserSharesOnFb()
    }

    fun onShareOnTwitter() {
        val result = this.result ?: return
        router.shareToTwitter(result)
        tracker.trackUserSharesOnTwitter()
    }

    fun onCopyWalletAddress() {
        clipboardUtils.copyToClipBoard(
                resourcesProviderUtils.getString(R.string.donate_copy_label),
                resourcesProviderUtils.getString(R.string.donate_btc_wallet_address))
        toastUtils.showToast(R.string.donate_toast)
        tracker.trackUserCopiesBtcWalletAddress()
    }

    private fun setTimeMachineDisplay(result: TimeTravelResult) {
        val profitMoney = formatterUtils.formatPriceAsOnlyDigits(result.profitMoney)
        val investedMoney = formatterUtils.formatPriceAsOnlyDigits(result.investedMoney)
        val date = result.timeToTravel
        val month = formatterUtils.formatMonth(date!!)
        val year = formatterUtils.formatYear(date)
        profitMoneyText.set(profitMoney)
        investedMoneyText.set(investedMoney)
        monthText.set(month)
        yearText.set(year)
    }

    private fun showInfoText(resourceId: Int) {
        infoText.set(resourcesProviderUtils.getString(resourceId))
    }
}