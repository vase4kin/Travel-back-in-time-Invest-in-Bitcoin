package com.travelbackintime.buybitcoin.impl

import android.os.Parcelable
import com.github.vase4kin.timetravelmachine.TimeTravelMachine
import kotlinx.parcelize.Parcelize
import java.util.Date

sealed class TimeTravelEvenWrapper : Parcelable {
    @Parcelize
    data class RealWorldEvent(
        val title: String,
        val description: String,
        val isDonate: Boolean
    ) : TimeTravelEvenWrapper()

    @Parcelize
    data class TimeTravelEvent(
        val profitMoney: Double,
        val investedMoney: Double,
        val timeToTravel: Date
    ) : TimeTravelEvenWrapper()
}

fun TimeTravelMachine.Event.wrap(): TimeTravelEvenWrapper {
    return when (this) {
        is TimeTravelMachine.Event.RealWorldEvent -> TimeTravelEvenWrapper.RealWorldEvent(
            title = this.title,
            description = this.description,
            isDonate = this.isDonate
        )
        is TimeTravelMachine.Event.TimeTravelEvent -> TimeTravelEvenWrapper.TimeTravelEvent(
            profitMoney = this.profitMoney,
            investedMoney = this.investedMoney,
            timeToTravel = this.timeToTravel
        )
    }
}
