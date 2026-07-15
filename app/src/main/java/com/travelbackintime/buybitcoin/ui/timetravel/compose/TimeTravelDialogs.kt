package com.travelbackintime.buybitcoin.ui.timetravel.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.shared.timetravelmachine.TimeTravelConstraints
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RetroDatePickerDialog(selectedDateMillis: Long?, onDismiss: () -> Unit, onConfirm: (Long) -> Unit) {
    val selectableDates = remember {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis in
                TimeTravelConstraints.minDateTimeInMillis..TimeTravelConstraints.maxDateTimeInMillis
        }
    }
    val state = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis ?: TimeTravelConstraints.maxDateTimeInMillis,
        initialDisplayedMonthMillis = selectedDateMillis ?: TimeTravelConstraints.maxDateTimeInMillis,
        selectableDates = selectableDates,
    )
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = Typography(),
    ) {
        val colors = retroDatePickerColors()
        DatePickerDialog(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(16.dp),
            colors = colors,
            confirmButton = {
                TextButton(
                    onClick = { state.selectedDateMillis?.let(onConfirm) },
                    enabled = state.selectedDateMillis != null,
                ) {
                    Text(
                        stringResource(R.string.button_confirm_amount_title).uppercase(Locale.US),
                        color = RetroWhite,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        stringResource(R.string.button_cancel).uppercase(Locale.US),
                        color = RetroWhite,
                    )
                }
            },
        ) {
            RetroDatePickerContent(state = state, colors = colors)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RetroDatePickerContent(state: DatePickerState, colors: DatePickerColors = retroDatePickerColors()) {
    DatePicker(
        state = state,
        title = null,
        colors = colors,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun retroDatePickerColors() = DatePickerDefaults.colors(
    containerColor = RetroSurface,
    titleContentColor = RetroWhite,
    headlineContentColor = RetroWhite,
    weekdayContentColor = RetroWhite,
    subheadContentColor = RetroWhite,
    navigationContentColor = RetroWhite,
    yearContentColor = RetroWhite,
    disabledYearContentColor = RetroGray,
    currentYearContentColor = RetroWhite,
    selectedYearContentColor = RetroWhite,
    selectedYearContainerColor = RetroDateHeader,
    dayContentColor = RetroWhite,
    disabledDayContentColor = RetroGray,
    selectedDayContentColor = RetroWhite,
    selectedDayContainerColor = RetroDateHeader,
    todayContentColor = RetroWhite,
    todayDateBorderColor = RetroWhite,
    dividerColor = RetroGray,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InvestmentSheet(onDismiss: () -> Unit, onConfirm: (Double) -> Unit) {
    var amount by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<Int?>(null) }
    var hasShownRichWarning by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    fun confirmAmount() {
        val parsedAmount = amount.toDoubleOrNull()
        errorMessage = when {
            parsedAmount == null -> R.string.error_set_amount_empty
            parsedAmount < MINIMUM_INVESTMENT -> R.string.error_set_amount_zero
            parsedAmount >= RICH_INVESTMENT_THRESHOLD && !hasShownRichWarning -> {
                hasShownRichWarning = true
                R.string.error_set_amount_rich
            }
            else -> {
                onConfirm(parsedAmount)
                null
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = RetroSurface,
        contentColor = RetroWhite,
        dragHandle = null,
    ) {
        InvestmentSheetContent(
            amount = amount,
            errorMessage = errorMessage,
            callbacks = InvestmentSheetCallbacks(
                amountChanged = {
                    if (it.length <= MAXIMUM_AMOUNT_LENGTH) {
                        amount = it.filter { character -> character.isDigit() || character == '.' }
                        errorMessage = null
                    }
                },
                suggestionSelected = onConfirm,
                confirmed = {
                    confirmAmount()
                    keyboardController?.hide()
                },
            ),
            modifier = Modifier.navigationBarsPadding(),
        )
    }
}

@Composable
internal fun InvestmentSheetContent(
    amount: String,
    errorMessage: Int?,
    callbacks: InvestmentSheetCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth().background(RetroSurface).padding(ScreenPadding),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            listOf(
                R.string.button_confirm_amount_title_1 to 1.0,
                R.string.button_confirm_amount_title_10 to 10.0,
                R.string.button_confirm_amount_title_100 to 100.0,
            ).forEach { (label, value) ->
                RetroButton(
                    text = stringResource(label),
                    onClick = { callbacks.suggestionSelected(value) },
                    modifier = Modifier.weight(1f),
                    compactText = true,
                )
            }
        }
        TextField(
            value = amount,
            onValueChange = callbacks.amountChanged,
            modifier = Modifier.fillMaxWidth(),
            textStyle = RetroDescriptionStyle.copy(fontSize = 36.sp, lineHeight = 42.sp),
            label = {
                Text(
                    stringResource(R.string.hint_set_amount, "US Dollar"),
                    style = RetroDescriptionStyle,
                )
            },
            supportingText = errorMessage?.let { message ->
                {
                    Text(
                        text = if (message == R.string.error_set_amount_zero) {
                            stringResource(message, "dollar")
                        } else {
                            stringResource(message)
                        },
                        style = RetroDescriptionStyle.copy(fontSize = 18.sp, lineHeight = 22.sp),
                        color = RetroWhite,
                    )
                }
            },
            isError = errorMessage != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { callbacks.confirmed() }),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = RetroSurface,
                unfocusedContainerColor = RetroSurface,
                disabledContainerColor = RetroSurface,
                errorContainerColor = RetroSurface,
                focusedTextColor = RetroWhite,
                unfocusedTextColor = RetroWhite,
                focusedLabelColor = RetroWhite,
                unfocusedLabelColor = RetroWhite,
                errorLabelColor = RetroWhite,
                cursorColor = RetroWhite,
                errorCursorColor = RetroWhite,
                focusedIndicatorColor = RetroWhite,
                unfocusedIndicatorColor = RetroWhite,
                errorIndicatorColor = RetroWhite,
            ),
        )
        Spacer(Modifier.height(16.dp))
        RetroButton(
            text = stringResource(R.string.button_confirm_amount_title),
            onClick = callbacks.confirmed,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

internal data class InvestmentSheetCallbacks(
    val amountChanged: (String) -> Unit,
    val suggestionSelected: (Double) -> Unit,
    val confirmed: () -> Unit,
)

private const val MINIMUM_INVESTMENT = 1.0
private const val RICH_INVESTMENT_THRESHOLD = 1_000_000.0
private const val MAXIMUM_AMOUNT_LENGTH = 10
