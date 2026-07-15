package com.travelbackintime.buybitcoin.ui.timetravel.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun ResultScreen(
    result: ResultRoute,
    showAds: Boolean,
    onShare: () -> Unit,
    onPriceProvider: () -> Unit,
    onStartOver: () -> Unit,
) {
    val context = LocalContext.current
    val moneyFormat = remember { NumberFormat.getNumberInstance(Locale.US) }
    val monthFormat = remember { SimpleDateFormat("MMM", Locale.US) }
    val yearFormat = remember { SimpleDateFormat("yyyy", Locale.US) }
    val shareDateFormat = remember { SimpleDateFormat("MMMM yyyy", Locale.US) }
    val shareCurrencyFormat = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    val travelDate = remember(result.timeToTravel) { Date(result.timeToTravel) }
    val investedMoney = remember(result.investedMoney) {
        moneyFormat.minimumFractionDigits = 2
        moneyFormat.maximumFractionDigits = 2
        moneyFormat.format(result.investedMoney)
    }
    val profitMoney = remember(result.profitMoney) {
        moneyFormat.minimumFractionDigits = 2
        moneyFormat.maximumFractionDigits = 2
        moneyFormat.format(result.profitMoney)
    }
    val shareText = stringResource(
        R.string.text_share,
        shareDateFormat.format(travelDate),
        shareCurrencyFormat.format(result.investedMoney),
        shareCurrencyFormat.format(result.profitMoney),
        stringResource(R.string.url_google_play, context.packageName),
    )
    val shareTitle = stringResource(R.string.button_share_title)
    val priceProviderUrl = stringResource(R.string.price_provider_url)

    RetroScreenBackground {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().safeDrawingPadding(),
            contentAlignment = Alignment.TopCenter,
        ) {
            val flexibleGap = resultFlexibleGap(maxHeight, showAds)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 720.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = ScreenPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    style = RetroTitleStyle,
                    color = RetroWhite,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(flexibleGap))
                DestinationDisplay(
                    month = monthFormat.format(travelDate),
                    year = yearFormat.format(travelDate),
                    investedMoney = investedMoney,
                )
                Spacer(Modifier.height(16.dp))
                ProfitDisplay(profitMoney = profitMoney)
                Spacer(Modifier.height(flexibleGap))
                if (showAds) {
                    AdBanner()
                    Spacer(Modifier.height(8.dp))
                }
                RetroButton(
                    text = stringResource(R.string.button_share_title),
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
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                RetroButton(
                    text = stringResource(R.string.button_start_over_title),
                    onClick = onStartOver,
                    modifier = Modifier.fillMaxWidth(),
                )
                TextButton(
                    onClick = {
                        onPriceProvider()
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(priceProviderUrl)),
                        )
                    },
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.powered_by_price_provider),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Default,
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                        ),
                        color = RetroWhite,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Composable
private fun DestinationDisplay(month: String, year: String, investedMoney: String) {
    Column(
        modifier = Modifier.fillMaxWidth().border(2.dp, RetroLightGray),
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val compact = maxWidth < 310.dp
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
            ) {
                DisplayField(
                    label = stringResource(R.string.result_mon_title),
                    value = month,
                    modifier = Modifier.width(if (compact) 66.dp else 74.dp).padding(start = 2.dp),
                )
                DisplayField(
                    label = stringResource(R.string.result_year_title),
                    value = year,
                    modifier = Modifier.width(if (compact) 90.dp else 100.dp),
                )
                DisplayField(
                    label = stringResource(R.string.result_invested_money_title),
                    value = investedMoney,
                    modifier = Modifier.weight(1f),
                )
                DollarField(modifier = Modifier.padding(end = 2.dp))
            }
        }
        RetroDivider(modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp))
        DisplayCaption(stringResource(R.string.result_destination_time))
    }
}

@Composable
private fun ProfitDisplay(profitMoney: String) {
    Column(
        modifier = Modifier.fillMaxWidth().border(2.dp, RetroLightGray),
    ) {
        Text(
            text = stringResource(R.string.result_profit_money_title).uppercase(Locale.US),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            style = RetroDisplayLabelStyle,
            color = RetroWhite,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            RetroDisplayValue(
                value = profitMoney,
                modifier = Modifier.weight(1f),
            )
            DollarField(showLabelSpacer = false)
        }
        RetroDivider(modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp))
        DisplayCaption(stringResource(R.string.result_present_time))
    }
}

@Composable
private fun DisplayField(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label.uppercase(Locale.US),
            modifier = Modifier.padding(top = 8.dp),
            style = RetroDisplayLabelStyle,
            color = RetroWhite,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        RetroDisplayValue(
            value = value.uppercase(Locale.US),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun DollarField(modifier: Modifier = Modifier, showLabelSpacer: Boolean = true) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (showLabelSpacer) {
            Spacer(Modifier.height(24.dp))
        }
        Row(
            modifier = Modifier.height(44.dp).padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.result_dollar_symbol),
                style = RetroDescriptionStyle,
                color = RetroWhite,
            )
        }
    }
}

@Composable
private fun DisplayCaption(text: String) {
    Text(
        text = text,
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        style = RetroDescriptionStyle,
        color = RetroWhite,
        textAlign = TextAlign.Center,
    )
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

private fun resultFlexibleGap(maxHeight: Dp, showAds: Boolean): Dp {
    val fixedHeight = if (showAds) 590.dp else 520.dp
    return ((maxHeight - fixedHeight) / 2).coerceAtLeast(8.dp)
}
