package com.travelbackintime.buybitcoin.ui.timetravel.compose

import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachine

data class TimeTravelUiState(
    val selectedDateMillis: Long? = null,
    val investedMoney: Double? = null,
    val isLoading: Boolean = false,
    val isAdsEnabled: Boolean = false,
) {
    val canInvest: Boolean
        get() = selectedDateMillis != null && investedMoney != null && !isLoading
}

sealed interface TimeTravelNavigation {
    data class Result(val event: TimeTravelMachine.Event.TimeTravelEvent) : TimeTravelNavigation

    data object Error : TimeTravelNavigation
}
