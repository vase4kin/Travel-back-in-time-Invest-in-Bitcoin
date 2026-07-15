package com.travelbackintime.buybitcoin.ui.timetravel.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.vase4kin.shared.timetravelmachine.TimeTravelConstraints

@Preview(name = "Input - completed", showBackground = true, widthDp = 360, heightDp = 720)
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

@Preview(name = "Input - initial", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
internal fun InitialTimeTravelScreenPreview() {
    BitcoinTheme {
        TimeTravelScreen(
            uiState = TimeTravelUiState(),
            onDateSelected = {},
            onInvestmentSelected = {},
            onInvest = {},
        )
    }
}

@Preview(name = "Input - loading", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
internal fun LoadingTimeTravelScreenPreview() {
    BitcoinTheme {
        TimeTravelScreen(
            uiState = TimeTravelUiState(
                selectedDateMillis = TimeTravelConstraints.minDateTimeInMillis,
                investedMoney = 100.0,
                isLoading = true,
            ),
            onDateSelected = {},
            onInvestmentSelected = {},
            onInvest = {},
        )
    }
}

@Preview(name = "Result", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
internal fun ResultScreenPreview() {
    BitcoinTheme {
        ResultScreen(
            result = ResultRoute(
                profitMoney = 71_262.57,
                investedMoney = 100.0,
                timeToTravel = TimeTravelConstraints.minDateTimeInMillis,
            ),
            showAds = false,
            onShare = {},
            onPriceProvider = {},
            onStartOver = {},
        )
    }
}

@Preview(name = "Error", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
internal fun ErrorScreenPreview() {
    BitcoinTheme {
        ErrorScreen(onRetry = {})
    }
}

@Preview(name = "Loading animation", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
internal fun LoadingScreenPreview() {
    BitcoinTheme {
        LoadingScreen(onFinished = {}, animationEnabled = false)
    }
}

@Preview(name = "Amount sheet content", showBackground = true, widthDp = 360)
@Composable
internal fun InvestmentSheetContentPreview() {
    BitcoinTheme {
        InvestmentSheetContent(
            amount = "",
            errorMessage = null,
            callbacks = InvestmentSheetCallbacks(
                amountChanged = {},
                suggestionSelected = {},
                confirmed = {},
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Date picker", showBackground = true, widthDp = 360)
@Composable
internal fun DatePickerPreview() {
    BitcoinTheme {
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme,
            typography = Typography(),
        ) {
            RetroDatePickerContent(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = TimeTravelConstraints.minDateTimeInMillis,
                ),
            )
        }
    }
}
