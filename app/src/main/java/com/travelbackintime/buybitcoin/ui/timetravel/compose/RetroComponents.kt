package com.travelbackintime.buybitcoin.ui.timetravel.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bitcoin.backintime.com.backintimebuybitcoin.R
import java.util.Locale

internal val ScreenPadding = 16.dp
internal val RetroButtonHeight = 64.dp

@Composable
internal fun RetroScreenBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxSize().background(RetroBlack)) {
        Image(
            painter = painterResource(R.drawable.ic_splash),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.2f,
            modifier = Modifier.fillMaxSize(),
        )
        content()
    }
}

@Composable
internal fun RetroButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    compactText: Boolean = false,
) {
    val contentColor = if (enabled) RetroWhite else RetroWhite.copy(alpha = 0.38f)
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(RetroButtonHeight),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = RetroBlack,
            contentColor = RetroWhite,
            disabledContainerColor = RetroBlack,
            disabledContentColor = contentColor,
        ),
        border = BorderStroke(1.dp, contentColor),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            disabledElevation = 0.dp,
        ),
        contentPadding = PaddingValues(horizontal = 12.dp),
    ) {
        Text(
            text = text.uppercase(Locale.US),
            modifier = Modifier.clearAndSetSemantics { this.text = AnnotatedString(text) },
            style = if (compactText) RetroSuggestionStyle else RetroButtonStyle,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
internal fun RetroDisplayValue(value: String, modifier: Modifier = Modifier, textAlign: TextAlign = TextAlign.End) {
    val fontSize = when {
        value.length <= 5 -> 34.sp
        value.length <= 9 -> 28.sp
        value.length <= 13 -> 22.sp
        else -> 16.sp
    }
    Box(
        modifier = modifier
            .height(44.dp)
            .padding(2.dp)
            .border(2.dp, RetroGray)
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Text(
            text = value,
            style = RetroMoneyStyle.copy(fontSize = fontSize),
            color = RetroRed,
            textAlign = textAlign,
            maxLines = 1,
            overflow = TextOverflow.Clip,
        )
    }
}

@Composable
internal fun RetroDivider(modifier: Modifier = Modifier) {
    Box(modifier = modifier.height(2.dp).background(RetroGray))
}
