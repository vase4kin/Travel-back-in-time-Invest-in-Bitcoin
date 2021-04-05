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

package com.travelbackintime.buybitcoin.homecoming.view

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.coindesk.remoteconfig.RemoteConfigService
import com.github.vase4kin.shared.tracker.Tracker
import com.travelbackintime.buybitcoin.homecoming.router.HomeComingRouter
import com.travelbackintime.buybitcoin.homecoming.share.ShareHelper
import com.travelbackintime.buybitcoin.impl.TimeTravelEvenWrapper
import com.travelbackintime.buybitcoin.utils.ClipboardUtils
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import com.travelbackintime.buybitcoin.utils.ResourcesProviderUtils
import com.travelbackintime.buybitcoin.utils.ToastUtils
import java.util.Date
import javax.inject.Inject

@Suppress("TooManyFunctions", "LongParameterList")
class HomeComingViewModel @Inject constructor(
    private val router: HomeComingRouter,
    private val shareHelper: ShareHelper,
    private val tracker: Tracker,
    private val configService: RemoteConfigService,
    private val formatterUtils: FormatterUtils,
    private val resourcesProviderUtils: ResourcesProviderUtils,
    private val toastUtils: ToastUtils,
    private val clipboardUtils: ClipboardUtils
) : LifecycleObserver {

    val isShareViewVisible = ObservableBoolean(false)
    val isParamViewVisible = ObservableBoolean(false)
    val isProfitViewVisible = ObservableBoolean(false)
    val isDonateViewVisible = ObservableBoolean(false)
    val isDescriptionViewVisible = ObservableBoolean(false)
    val title = ObservableField<String>()
    val description = ObservableField<String>()
    val profitMoneyText = ObservableField<String>()
    val investedMoneyText = ObservableField<String>()
    val monthText = ObservableField<String>()
    val yearText = ObservableField<String>()

    val isAdsEnabled: Boolean
        get() = shouldShowAds()

    var event: TimeTravelEvenWrapper? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun handleOnCreate() {
        val event = this.event ?: return
        when (event) {
            is TimeTravelEvenWrapper.RealWorldEvent -> processRealWorldEvent(event)
            is TimeTravelEvenWrapper.TimeTravelEvent -> processTimeTravelEvent(event)
        }
    }

    private fun shouldShowAds(): Boolean {
        val event = event
        return if (event is TimeTravelEvenWrapper.RealWorldEvent) {
            !event.isDonate
        } else {
            true
        } && configService.isAdsEnabled
    }

    private fun processRealWorldEvent(event: TimeTravelEvenWrapper.RealWorldEvent) {
        title.set(event.title)
        description.set(event.description)
        isDescriptionViewVisible.set(event.description.isNotEmpty())
        isDonateViewVisible.set(event.isDonate)
        tracker.trackUserGetsToRealWorldEvent(event.title)
    }

    private fun processTimeTravelEvent(event: TimeTravelEvenWrapper.TimeTravelEvent) {
        setTimeMachineDisplay(event)
        isShareViewVisible.set(true)
        isParamViewVisible.set(true)
        isProfitViewVisible.set(true)
        tracker.trackUserGetsToTimeTravelEvent(
            investedMoney = event.investedMoney,
            profitMoney = event.profitMoney,
            time = event.timeToTravel
        )
    }

    fun onStartOver() {
        router.openTimeTravelFragment()
        tracker.trackUserStartsOver()
    }

    fun onShareWithFriends() {
        val event = event
        if (event is TimeTravelEvenWrapper.TimeTravelEvent) {
            shareHelper.shareWithFriends(event)
            tracker.trackUserSharesWithFriends()
        }
    }

    fun onShareOnFacebook() {
        shareHelper.shareToFaceBook()
        tracker.trackUserSharesOnFb()
    }

    fun onShareOnTwitter() {
        val event = event
        if (event is TimeTravelEvenWrapper.TimeTravelEvent) {
            shareHelper.shareToTwitter(event)
            tracker.trackUserSharesOnTwitter()
        }
    }

    fun onCopyWalletAddress() {
        clipboardUtils.copyToClipBoard(
            resourcesProviderUtils.getString(R.string.donate_copy_label),
            resourcesProviderUtils.getString(R.string.donate_btc_wallet_address)
        )
        toastUtils.showToast(R.string.donate_toast)
        tracker.trackUserCopiesBtcWalletAddress()
    }

    fun openPoweredByCoinDeskUrl() {
        router.openPoweredByCoinDeskUrl()
    }

    private fun setTimeMachineDisplay(event: TimeTravelEvenWrapper.TimeTravelEvent) {
        val profitMoney = formatterUtils.formatPriceAsOnlyDigits(event.profitMoney)
        val investedMoney = formatterUtils.formatPriceAsOnlyDigits(event.investedMoney)
        val date = event.timeToTravel
        val month = formatterUtils.formatMonth(Date(date))
        val year = formatterUtils.formatYear(Date(date))
        profitMoneyText.set(profitMoney)
        investedMoneyText.set(investedMoney)
        monthText.set(month)
        yearText.set(year)
    }
}
