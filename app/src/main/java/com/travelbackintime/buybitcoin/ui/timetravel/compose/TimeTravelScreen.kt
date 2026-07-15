package com.travelbackintime.buybitcoin.ui.timetravel.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bitcoin.backintime.com.backintimebuybitcoin.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun TimeTravelScreen(
    uiState: TimeTravelUiState,
    onDateSelected: (Long) -> Unit,
    onInvestmentSelected: (Double) -> Unit,
    onInvest: () -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showAmountSheet by remember { mutableStateOf(false) }
    val currency = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.US) }
    val selection = InputSelection(
        date = uiState.selectedDateMillis?.let { dateFormat.format(Date(it)) },
        money = uiState.investedMoney?.let(currency::format),
    )
    val callbacks = InputCallbacks(
        showDatePicker = { showDatePicker = true },
        showAmountSheet = { showAmountSheet = true },
        invest = onInvest,
    )

    RetroScreenBackground {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        ) {
            if (maxHeight < COMPACT_SCREEN_HEIGHT) {
                CompactInputContent(
                    uiState = uiState,
                    selection = selection,
                    callbacks = callbacks,
                )
            } else {
                RegularInputContent(
                    uiState = uiState,
                    selection = selection,
                    callbacks = callbacks,
                )
            }
        }
    }

    if (showDatePicker) {
        RetroDatePickerDialog(
            selectedDateMillis = uiState.selectedDateMillis,
            onDismiss = { showDatePicker = false },
            onConfirm = {
                onDateSelected(it)
                showDatePicker = false
            },
        )
    }

    if (showAmountSheet) {
        InvestmentSheet(
            onDismiss = { showAmountSheet = false },
            onConfirm = {
                onInvestmentSelected(it)
                showAmountSheet = false
            },
        )
    }
}

@Composable
private fun RegularInputContent(uiState: TimeTravelUiState, selection: InputSelection, callbacks: InputCallbacks) {
    Box(modifier = Modifier.fillMaxSize()) {
        ScreenTitle(modifier = Modifier.align(Alignment.TopCenter))
        ScreenDescription(modifier = Modifier.align(Alignment.Center))
        InputActions(
            uiState = uiState,
            selection = selection,
            callbacks = callbacks,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun CompactInputContent(uiState: TimeTravelUiState, selection: InputSelection, callbacks: InputCallbacks) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ScreenTitle()
        ScreenDescription(modifier = Modifier.padding(vertical = 16.dp))
        InputActions(
            uiState = uiState,
            selection = selection,
            callbacks = callbacks,
        )
    }
}

@Composable
private fun ScreenTitle(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.app_name),
        modifier = modifier.fillMaxWidth().padding(16.dp),
        style = RetroTitleStyle,
        color = RetroWhite,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun ScreenDescription(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.app_description),
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        style = RetroDescriptionStyle,
        color = RetroWhite,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun InputActions(
    uiState: TimeTravelUiState,
    selection: InputSelection,
    callbacks: InputCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().widthIn(max = 640.dp).padding(horizontal = ScreenPadding),
    ) {
        RetroButton(
            text = selection.date ?: stringResource(R.string.button_set_date_title),
            onClick = callbacks.showDatePicker,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        RetroButton(
            text = selection.money ?: stringResource(R.string.button_set_amount_title),
            onClick = callbacks.showAmountSheet,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        RetroButton(
            text = stringResource(R.string.button_buy_bitcoin_title),
            onClick = callbacks.invest,
            enabled = uiState.canInvest,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))
    }
}

private val COMPACT_SCREEN_HEIGHT = 520.dp

private data class InputSelection(val date: String?, val money: String?)

private data class InputCallbacks(
    val showDatePicker: () -> Unit,
    val showAmountSheet: () -> Unit,
    val invest: () -> Unit,
)
