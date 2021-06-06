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

package com.travelbackintime.buybitcoin.ui.homecoming.view

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.github.vase4kin.coindesk.remoteconfig.RemoteConfigService
import com.github.vase4kin.shared.tracker.Tracker
import com.travelbackintime.buybitcoin.ui.homecoming.router.HomeComingRouter
import com.travelbackintime.buybitcoin.ui.homecoming.share.ShareHelper
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import com.travelbackintime.buybitcoin.utils.TimeTravelEvenWrapper
import java.util.Date
import javax.inject.Inject

@Suppress("TooManyFunctions", "LongParameterList")
class HomeComingViewModel @Inject constructor(
    private val router: HomeComingRouter,
    private val shareHelper: ShareHelper,
    private val tracker: Tracker,
    private val configService: RemoteConfigService,
    private val formatterUtils: FormatterUtils
) : LifecycleObserver {

    val isShareViewVisible = ObservableBoolean(false)
    val isParamViewVisible = ObservableBoolean(false)
    val isProfitViewVisible = ObservableBoolean(false)
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
            is TimeTravelEvenWrapper.TimeTravelEvent -> processTimeTravelEvent(event)
        }
    }

    private fun shouldShowAds(): Boolean {
        return configService.isAdsEnabled
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

    fun openPoweredByCoinDeskUrl() {
        router.openPoweredByCoinDeskUrl()
        tracker.trackUserClicksOnPoweredByCoinDesk()
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
