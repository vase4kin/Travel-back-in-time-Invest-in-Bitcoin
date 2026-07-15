package com.travelbackintime.buybitcoin.ui.timetravel.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bitcoin.backintime.com.backintimebuybitcoin.R

@Composable
internal fun ErrorScreen(onRetry: () -> Unit) {
    RetroScreenBackground {
        Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .padding(horizontal = ScreenPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.retry_title),
                    modifier = Modifier.padding(16.dp),
                    style = RetroTitleStyle,
                    color = RetroWhite,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.retry_description),
                    style = RetroTitleStyle,
                    color = RetroWhite,
                    textAlign = TextAlign.Center,
                )
            }
            RetroButton(
                text = stringResource(R.string.retry_button_text),
                onClick = onRetry,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .padding(horizontal = ScreenPadding)
                    .padding(bottom = 16.dp),
            )
        }
    }
}
