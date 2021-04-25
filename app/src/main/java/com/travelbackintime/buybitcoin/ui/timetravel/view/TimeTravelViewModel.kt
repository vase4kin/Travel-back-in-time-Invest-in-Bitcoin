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

package com.travelbackintime.buybitcoin.ui.timetravel.view

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleObserver
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.crashlytics.Crashlytics
import com.github.vase4kin.shared.timetravelmachine.TimeTravelConstraints
import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachine
import com.github.vase4kin.shared.tracker.Tracker
import com.travelbackintime.buybitcoin.ui.timetravel.router.TimeTravelRouter
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import com.travelbackintime.buybitcoin.utils.ResourcesProvider
import com.travelbackintime.buybitcoin.utils.onChanged
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

private const val DEFAULT_INVESTED_MONEY: Double = 0.0

@Suppress("LongParameterList")
class TimeTravelViewModel @Inject constructor(
    private val tracker: Tracker,
    private val formatterUtils: FormatterUtils,
    private val timeTravelMachine: TimeTravelMachine,
    private val router: TimeTravelRouter,
    private val coroutineScope: Lazy<LifecycleCoroutineScope>,
    private val crashlytics: Crashlytics,
    resourcesProvider: ResourcesProvider
) : LifecycleObserver {

    val isBuyBitcoinButtonEnabled = ObservableBoolean(false)
    val isWarpingThroughSpaceAndTime = ObservableBoolean(false)
    val timeToTravelText =
        ObservableField(resourcesProvider.getString(R.string.button_set_date_title)).onChanged {
            enableBuyBitcoinButton()
        }
    val investedMoneyText = ObservableField(
        resourcesProvider.getString(R.string.button_set_amount_title)
    ).onChanged {
        enableBuyBitcoinButton()
    }

    private var investedMoney: Double = DEFAULT_INVESTED_MONEY
    private var timeToTravel: Long = TimeTravelConstraints.maxDateTimeInMillis

    fun onBuyBitcoinButtonClick() {
        tracker.trackUserTravelsBackAndBuys()
        travelInTime()
    }

    fun onSetInvestedMoneyButtonClick() = router.showAmountDialog()

    fun onSetTimeToTravelButtonClick() {
        router.showSetDateDialog(onDateSelected = {
            this.timeToTravel = it
            val formattedTimeToTravel = formatterUtils.formatDate(Date(timeToTravel))
            tracker.trackUserSetsTime(formattedTimeToTravel)
            timeToTravelText.set(formattedTimeToTravel)
        })
    }

    fun setInvestedMoney(investedMoney: Double) {
        this.investedMoney = investedMoney
        val formattedInvestedMoney = formatterUtils.formatPrice(investedMoney)
        tracker.trackUserSetsMoney(formattedInvestedMoney)
        investedMoneyText.set(formattedInvestedMoney)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun travelInTime() {
        coroutineScope.get().launch {
            withContext(Dispatchers.IO) {
                isWarpingThroughSpaceAndTime.set(true)
                val event = try {
                    timeTravelMachine.travelInTime(
                        time = timeToTravel,
                        investedMoney = investedMoney
                    )
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                }
                isWarpingThroughSpaceAndTime.set(false)
                withContext(Dispatchers.Main) {
                    when (event) {
                        is TimeTravelMachine.Event -> router.openLoadingFragment(event)
                        else -> router.openErrorFragment()
                    }
                }
            }
        }
    }

    private fun isBuyBitcoinButtonEnabled(): Boolean {
        return timeToTravel != TimeTravelConstraints.maxDateTimeInMillis && investedMoney != DEFAULT_INVESTED_MONEY
    }

    private fun enableBuyBitcoinButton() {
        if (isBuyBitcoinButtonEnabled()) {
            isBuyBitcoinButtonEnabled.set(true)
        }
    }
}
