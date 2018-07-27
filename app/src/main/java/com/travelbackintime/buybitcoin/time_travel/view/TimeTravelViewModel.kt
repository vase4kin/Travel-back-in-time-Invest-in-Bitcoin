package com.travelbackintime.buybitcoin.time_travel.view

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult
import com.travelbackintime.buybitcoin.time_travel.router.TimeTravelRouter
import com.travelbackintime.buybitcoin.time_travel_machine.TimeTravelMachine
import com.travelbackintime.buybitcoin.tracker.Tracker
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import com.travelbackintime.buybitcoin.utils.ResourcesProviderUtils
import java.util.*
import javax.inject.Inject

class TimeTravelViewModel @Inject constructor(
        private val tracker: Tracker,
        private val formatterUtils: FormatterUtils,
        private val timeTravelMachine: TimeTravelMachine,
        private val router: TimeTravelRouter,
        resourcesProviderUtils: ResourcesProviderUtils) {

    val isBuyBitcoinButtonEnabled = ObservableBoolean(false)
    val timeToTravelText = ObservableField<String>(resourcesProviderUtils.getString(R.string.button_set_date_title))
    val investedMoneyText = ObservableField<String>(resourcesProviderUtils.getString(R.string.button_set_amount_title))

    private var investedMoney: Double = Double.NaN
    private var timeToTravel: Date? = null

    fun onBuyBitcoinButtonClick() {
        tracker.trackUserTravelsBackAndBuys()
        processData()
    }

    fun onSetInvestedMoneyButtonClick() = router.showAmountDialog()

    fun onSetTimeToTravelButtonClick() = router.showSetDateDialog()

    fun setTimeToTravel(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val timeToTravel = getTimeToTravel(year, monthOfYear, dayOfMonth)
        this.timeToTravel = timeToTravel
        val formattedTimeToTravel = formatterUtils.formatDate(timeToTravel)
        tracker.trackUserSetsTime(formattedTimeToTravel)
        timeToTravelText.set(formattedTimeToTravel)
        enableBuyBitcoinButton()
    }

    fun setInvestedMoney(investedMoney: Double) {
        this.investedMoney = investedMoney
        val formattedInvestedMoney = formatterUtils.formatPrice(investedMoney)
        tracker.trackUserSetsMoney(formattedInvestedMoney)
        investedMoneyText.set(formattedInvestedMoney)
        enableBuyBitcoinButton()
    }

    private fun getTimeToTravel(year: Int, monthOfYear: Int, dayOfMonth: Int): Date {
        val timeToTravelCalendar = Calendar.getInstance(Locale.US)
        timeToTravelCalendar.clear()
        timeToTravelCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0)
        return timeToTravelCalendar.time
    }

    private fun enableBuyBitcoinButton() {
        if (timeToTravel != null && investedMoney != Double.NaN) {
            isBuyBitcoinButtonEnabled.set(true)
        }
    }

    private fun processData() {
        val event = timeTravelMachine.getTimeEvent(timeToTravel)
        when (event.type) {
            TimeTravelMachine.EventType.NO_EVENT -> {
                val status = timeTravelMachine.getBitcoinStatus(timeToTravel)
                when (status) {
                    TimeTravelMachine.BitcoinStatus.EXIST -> {
                        val profit = calculateProfit()
                        val timeTravelResult = TimeTravelResult(
                                status = status,
                                profit = profit,
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
                                eventType = event.type))
            }
        }
    }

    private fun calculateProfit(): Double {
        val pricePerBitcoinInThePast = timeTravelMachine.getBitcoinPrice(timeToTravel)
        val pricePerBitcoinNow = timeTravelMachine.bitcoinCurrentPrice
        val bitcoinInvestment = investedMoney / pricePerBitcoinInThePast
        return pricePerBitcoinNow * bitcoinInvestment
    }
}