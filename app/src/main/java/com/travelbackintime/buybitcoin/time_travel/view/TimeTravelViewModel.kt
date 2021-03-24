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

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.timetravelmachine.TimeTravelMachine
import com.travelbackintime.buybitcoin.time_travel.router.TimeTravelRouter
import com.travelbackintime.buybitcoin.tracker.Tracker
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import com.travelbackintime.buybitcoin.utils.ResourcesProviderUtils
import com.travelbackintime.buybitcoin.utils.onChanged
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

private const val DEFAULT_INVESTED_MONEY: Double = 0.0

class TimeTravelViewModel @Inject constructor(
        private val tracker: Tracker,
        private val formatterUtils: FormatterUtils,
        private val timeTravelMachine: TimeTravelMachine,
        private val router: TimeTravelRouter,
        resourcesProviderUtils: ResourcesProviderUtils
) : LifecycleObserver {

    val isBuyBitcoinButtonEnabled = ObservableBoolean(false)
    val timeToTravelText = ObservableField(resourcesProviderUtils.getString(R.string.button_set_date_title)).onChanged {
        enableBuyBitcoinButton()
    }
    val investedMoneyText = ObservableField(resourcesProviderUtils.getString(R.string.button_set_amount_title)).onChanged {
        enableBuyBitcoinButton()
    }

    private var investedMoney: Double = DEFAULT_INVESTED_MONEY
    private var timeToTravel: Date = TimeTravelMachine.Companion.maxDate.time

    private val compositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onUnBind() {
        compositeDisposable.clear()
    }

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
        timeTravelMachine.travelInTime(
                timeToTravel = timeToTravel,
                investedMoney = investedMoney
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            router.openLoadingFragment(it)
                        },
                        onError = {
                            router.openErrorFragment()
                        }
                )
                .addTo(compositeDisposable)
    }

    private fun isBuyBitcoinButtonEnabled(): Boolean {
        return timeToTravel != TimeTravelMachine.Companion.maxDate.time && investedMoney != DEFAULT_INVESTED_MONEY
    }

    private fun enableBuyBitcoinButton() {
        if (isBuyBitcoinButtonEnabled()) {
            isBuyBitcoinButtonEnabled.set(true)
        }
    }
}