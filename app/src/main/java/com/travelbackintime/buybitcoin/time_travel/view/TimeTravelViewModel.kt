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

package com.travelbackintime.buybitcoin.time_travel.view

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.timetravelmachine.TimeTravelMachine
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult
import com.travelbackintime.buybitcoin.time_travel.router.TimeTravelRouter
import com.travelbackintime.buybitcoin.tracker.Tracker
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import com.travelbackintime.buybitcoin.utils.ResourcesProviderUtils
import com.travelbackintime.buybitcoin.utils.onChanged
import java.util.*
import javax.inject.Inject

private const val DEFAULT_INVESTED_MONEY: Double = 0.0

class TimeTravelViewModel @Inject constructor(
        private val tracker: Tracker,
        private val formatterUtils: FormatterUtils,
        private val timeTravelMachine: TimeTravelMachine,
        private val router: TimeTravelRouter,
        resourcesProviderUtils: ResourcesProviderUtils) {

    val isBuyBitcoinButtonEnabled = ObservableBoolean(false)
    val timeToTravelText = ObservableField<String>(resourcesProviderUtils.getString(R.string.button_set_date_title)).onChanged {
        enableBuyBitcoinButton()
    }
    val investedMoneyText = ObservableField<String>(resourcesProviderUtils.getString(R.string.button_set_amount_title)).onChanged {
        enableBuyBitcoinButton()
    }

    private var investedMoney: Double = DEFAULT_INVESTED_MONEY
    private var timeToTravel: Date? = null

    fun onBuyBitcoinButtonClick() {
        tracker.trackUserTravelsBackAndBuys()
        travelInTime()
    }

    fun onSetInvestedMoneyButtonClick() = router.showAmountDialog()

    fun onSetTimeToTravelButtonClick() = router.showSetDateDialog()

    fun setTimeToTravel(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val timeToTravel = getTimeToTravel(year, monthOfYear, dayOfMonth)
        this.timeToTravel = timeToTravel
        val formattedTimeToTravel = formatterUtils.formatDate(timeToTravel)
        tracker.trackUserSetsTime(formattedTimeToTravel)
        timeToTravelText.set(formattedTimeToTravel)
    }

    fun setInvestedMoney(investedMoney: Double) {
        this.investedMoney = investedMoney
        val formattedInvestedMoney = formatterUtils.formatPrice(investedMoney)
        tracker.trackUserSetsMoney(formattedInvestedMoney)
        investedMoneyText.set(formattedInvestedMoney)
    }

    private fun getTimeToTravel(year: Int, monthOfYear: Int, dayOfMonth: Int): Date {
        val timeToTravelCalendar = Calendar.getInstance(Locale.US)
        timeToTravelCalendar.clear()
        timeToTravelCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
        return timeToTravelCalendar.time
    }

    private fun travelInTime() {
        val event = timeTravelMachine.getTimeEvent(timeToTravel)
        when (event.eventType) {
            TimeTravelMachine.EventType.NO_EVENT -> {
                val status = timeTravelMachine.getBitcoinStatus(timeToTravel)
                when (status) {
                    TimeTravelMachine.BitcoinStatus.EXIST -> {
                        val profit = calculateProfit()
                        val timeTravelResult = TimeTravelResult(
                                status = status,
                                profitMoney = profit,
                                investedMoney = investedMoney,
                                timeToTravel = timeToTravel)
                        router.openLoadingActivity(timeTravelResult)
                    }
                    else -> router.openLoadingActivity(TimeTravelResult(status = status))
                }
            }
            else -> {
                router.openLoadingActivity(
                        TimeTravelResult(
                                status = TimeTravelMachine.BitcoinStatus.EXIST,
                                eventType = event.eventType))
            }
        }
    }

    private fun calculateProfit(): Double {
        val pricePerBitcoinInThePast = timeTravelMachine.getBitcoinPrice(timeToTravel)
        val pricePerBitcoinNow = timeTravelMachine.getBitcoinCurrentPrice()
        val bitcoinInvestment = investedMoney / pricePerBitcoinInThePast
        return pricePerBitcoinNow * bitcoinInvestment
    }

    private fun isBuyBitcoinButtonEnabled(): Boolean {
        return timeToTravel != null && investedMoney != DEFAULT_INVESTED_MONEY
    }

    private fun enableBuyBitcoinButton() {
        if (isBuyBitcoinButtonEnabled()) {
            isBuyBitcoinButtonEnabled.set(true)
        }
    }
}