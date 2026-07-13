package com.travelbackintime.buybitcoin.ui.timetravel.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.shared.timetravelmachine.TimeTravelConstraints
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

@Serializable
private data object SplashRoute : NavKey

@Serializable
private data object TimeTravelRoute : NavKey

@Serializable
private data object ErrorRoute : NavKey

@Serializable
private data class ResultRoute(val profitMoney: Double, val investedMoney: Double, val timeToTravel: Long) : NavKey

@Composable
fun TimeTravelApp(viewModel: TimeTravelViewModel) {
    val backStack = rememberNavBackStack(SplashRoute)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.navigation.collect { destination ->
            when (destination) {
                TimeTravelNavigation.Error -> backStack.add(ErrorRoute)
                is TimeTravelNavigation.Result -> backStack.add(
                    ResultRoute(
                        profitMoney = destination.event.profitMoney,
                        investedMoney = destination.event.investedMoney,
                        timeToTravel = destination.event.timeToTravel,
                    ),
                )
            }
        }
    }

    BitcoinTheme {
        NavDisplay(
            backStack = backStack,
            onBack = { if (backStack.size > 1) backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<SplashRoute> {
                    LaunchedEffect(Unit) {
                        delay(SPLASH_DURATION_MILLIS)
                        backStack.clear()
                        backStack.add(TimeTravelRoute)
                    }
                    SplashScreen()
                }
                entry<TimeTravelRoute> {
                    TimeTravelScreen(
                        uiState = uiState,
                        onDateSelected = viewModel::selectDate,
                        onInvestmentSelected = viewModel::selectInvestment,
                        onInvest = viewModel::invest,
                    )
                }
                entry<ResultRoute> { result ->
                    ResultScreen(
                        result = result,
                        showAds = uiState.isAdsEnabled,
                        onShare = viewModel::trackShare,
                        onPriceProvider = viewModel::trackPriceProvider,
                        onStartOver = {
                            viewModel.reset()
                            backStack.clear()
                            backStack.add(TimeTravelRoute)
                        },
                    )
                }
                entry<ErrorRoute> {
                    ErrorScreen(
                        onRetry = {
                            backStack.removeLastOrNull()
                            viewModel.invest()
                        },
                    )
                }
            },
        )
    }
}

@Composable
internal fun BitcoinTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFFFB300),
            secondary = Color(0xFF00C853),
            background = Color(0xFF090D12),
            surface = Color(0xFF111820),
        ),
        content = content,
    )
}

@Composable
private fun ScreenBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Image(
            painter = painterResource(R.drawable.ic_splash),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.16f,
            modifier = Modifier.fillMaxSize(),
        )
        content()
    }
}

@Composable
private fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.ic_splash),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
    val dateFormat = remember { DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US) }

    ScreenBackground {
        Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.press_start_2p)),
                )
                Text(
                    text = stringResource(R.string.app_description),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { showDatePicker = true },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                ) {
                    Text(
                        uiState.selectedDateMillis?.let { dateFormat.format(Date(it)) }
                            ?: stringResource(R.string.button_set_date_title),
                    )
                }
                Button(
                    onClick = { showAmountSheet = true },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                ) {
                    Text(
                        uiState.investedMoney?.let(currency::format)
                            ?: stringResource(R.string.button_set_amount_title),
                    )
                }
                Button(
                    onClick = onInvest,
                    enabled = uiState.canInvest,
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Text(stringResource(R.string.button_buy_bitcoin_title))
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val selectableDates = remember {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis in
                    TimeTravelConstraints.minDateTimeInMillis..TimeTravelConstraints.maxDateTimeInMillis
            }
        }
        val state = rememberDatePickerState(
            initialSelectedDateMillis = uiState.selectedDateMillis
                ?: TimeTravelConstraints.maxDateTimeInMillis,
            selectableDates = selectableDates,
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let(onDateSelected)
                    showDatePicker = false
                }) { Text(stringResource(R.string.button_confirm_amount_title)) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.button_cancel))
                }
            },
        ) {
            DatePicker(state = state)
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InvestmentSheet(onDismiss: () -> Unit, onConfirm: (Double) -> Unit) {
    var amount by remember { mutableStateOf("") }
    val parsedAmount = amount.toDoubleOrNull()
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.button_set_amount_title),
                style = MaterialTheme.typography.headlineSmall,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf("1", "10", "100").forEach { suggestion ->
                    OutlinedButton(
                        onClick = { amount = suggestion },
                        modifier = Modifier.weight(1f),
                    ) { Text("$$suggestion") }
                }
            }
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter { char -> char.isDigit() || char == '.' } },
                label = { Text(stringResource(R.string.hint_set_amount, "USD")) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = parsedAmount != null && parsedAmount < 1.0,
                supportingText = {
                    if (parsedAmount != null && parsedAmount < 1.0) {
                        Text(stringResource(R.string.error_set_amount_zero, "dollar"))
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Button(
                onClick = { parsedAmount?.let(onConfirm) },
                enabled = parsedAmount != null && parsedAmount.isFinite() && parsedAmount >= 1.0,
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) { Text(stringResource(R.string.button_confirm_amount_title)) }
        }
    }
}

@Composable
private fun ResultScreen(
    result: ResultRoute,
    showAds: Boolean,
    onShare: () -> Unit,
    onPriceProvider: () -> Unit,
    onStartOver: () -> Unit,
) {
    val context = LocalContext.current
    val currency = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    val dateFormat = remember { DateFormat.getDateInstance(DateFormat.LONG, Locale.US) }
    val shareText = stringResource(
        R.string.text_share,
        dateFormat.format(Date(result.timeToTravel)),
        currency.format(result.investedMoney),
        currency.format(result.profitMoney),
        stringResource(R.string.url_google_play, context.packageName),
    )
    val shareTitle = stringResource(R.string.button_share_title)
    val priceProviderUrl = stringResource(R.string.price_provider_url)
    ScreenBackground {
        Box(
            modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().widthIn(max = 720.dp).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Font(R.font.press_start_2p)),
                )
                ResultDisplay(
                    label = stringResource(R.string.result_destination_time),
                    value = dateFormat.format(Date(result.timeToTravel)),
                )
                ResultDisplay(
                    label = stringResource(R.string.result_invested_money_title),
                    value = currency.format(result.investedMoney),
                )
                ResultDisplay(
                    label = stringResource(R.string.result_profit_money_title),
                    value = currency.format(result.profitMoney),
                )
                Button(
                    onClick = {
                        onShare()
                        context.startActivity(
                            Intent.createChooser(
                                Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                },
                                shareTitle,
                            ),
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                ) { Text(stringResource(R.string.button_share_title)) }
                OutlinedButton(
                    onClick = onStartOver,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                ) { Text(stringResource(R.string.button_start_over_title)) }
                if (showAds) {
                    AdBanner()
                }
                TextButton(
                    onClick = {
                        onPriceProvider()
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(priceProviderUrl),
                            ),
                        )
                    },
                ) { Text(stringResource(R.string.powered_by_price_provider)) }
            }
        }
    }
}

@Composable
private fun AdBanner() {
    val adUnitId = stringResource(R.string.ad_mob_id)
    var adView by remember { mutableStateOf<AdView?>(null) }
    AndroidView(
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                this.adUnitId = adUnitId
                loadAd(AdRequest.Builder().build())
                adView = this
            }
        },
    )
    DisposableEffect(Unit) {
        onDispose { adView?.destroy() }
    }
}

@Composable
private fun ResultDisplay(label: String, value: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF06160A),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge, color = Color(0xFF75FF87))
            Spacer(Modifier.height(8.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF75FF87),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun ErrorScreen(onRetry: () -> Unit) {
    ScreenBackground {
        Column(
            modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(stringResource(R.string.retry_title), style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.retry_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onRetry,
                modifier = Modifier.fillMaxWidth().widthIn(max = 480.dp).height(64.dp),
            ) { Text(stringResource(R.string.retry_button_text)) }
        }
    }
}

private const val SPLASH_DURATION_MILLIS = 1_500L
