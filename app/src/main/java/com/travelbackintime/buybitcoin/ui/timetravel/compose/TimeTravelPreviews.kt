package com.travelbackintime.buybitcoin.ui.timetravel.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.vase4kin.shared.timetravelmachine.TimeTravelConstraints

@Preview(showBackground = true)
@Composable
internal fun TimeTravelScreenPreview() {
    BitcoinTheme {
        TimeTravelScreen(
            uiState = TimeTravelUiState(
                selectedDateMillis = TimeTravelConstraints.minDateTimeInMillis,
                investedMoney = 100.0,
            ),
            onDateSelected = {},
            onInvestmentSelected = {},
            onInvest = {},
        )
    }
}
