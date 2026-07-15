package com.travelbackintime.buybitcoin.ui.timetravel.compose

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bitcoin.backintime.com.backintimebuybitcoin.R

internal val RetroBlack = Color(RETRO_BLACK_ARGB)
internal val RetroRed = Color(RETRO_RED_ARGB)
internal val RetroWhite = Color(RETRO_WHITE_ARGB)
internal val RetroSurface = Color(RETRO_SURFACE_ARGB)
internal val RetroDateHeader = Color(RETRO_DATE_HEADER_ARGB)
internal val RetroGray = Color(RETRO_GRAY_ARGB)
internal val RetroLightGray = Color(RETRO_LIGHT_GRAY_ARGB)

internal val Vt323FontFamily = FontFamily(Font(R.font.vt323))
internal val PressStartFontFamily = FontFamily(Font(R.font.press_start_2p))
internal val DigitalDreamFontFamily = FontFamily(Font(R.font.digitaldreamfatnarrow))

internal val RetroTitleStyle = TextStyle(
    fontFamily = Vt323FontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 34.sp,
    lineHeight = 40.sp,
    color = RetroWhite,
)

internal val RetroDescriptionStyle = TextStyle(
    fontFamily = Vt323FontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
    lineHeight = 32.sp,
    color = RetroWhite,
)

internal val RetroButtonStyle = RetroTitleStyle

internal val RetroSuggestionStyle = RetroDescriptionStyle

internal val RetroDisplayLabelStyle = TextStyle(
    fontFamily = PressStartFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 10.sp,
    lineHeight = 16.sp,
    color = RetroWhite,
)

internal val RetroMoneyStyle = TextStyle(
    fontFamily = DigitalDreamFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 34.sp,
    lineHeight = 38.sp,
    color = RetroRed,
)

private val RetroColorScheme = darkColorScheme(
    primary = RetroBlack,
    onPrimary = RetroWhite,
    primaryContainer = RetroBlack,
    onPrimaryContainer = RetroWhite,
    secondary = RetroBlack,
    onSecondary = RetroWhite,
    secondaryContainer = RetroSurface,
    onSecondaryContainer = RetroWhite,
    background = RetroBlack,
    onBackground = RetroWhite,
    surface = RetroSurface,
    onSurface = RetroWhite,
    surfaceVariant = RetroSurface,
    onSurfaceVariant = RetroWhite,
    error = RetroWhite,
    onError = RetroBlack,
    outline = RetroWhite,
    outlineVariant = RetroGray,
)

private val RetroTypography = Typography(
    headlineLarge = RetroTitleStyle,
    headlineMedium = RetroTitleStyle,
    headlineSmall = RetroDescriptionStyle,
    titleLarge = RetroTitleStyle,
    titleMedium = RetroDescriptionStyle,
    bodyLarge = RetroDescriptionStyle,
    bodyMedium = RetroDescriptionStyle.copy(fontSize = 20.sp, lineHeight = 26.sp),
    labelLarge = RetroButtonStyle,
    labelMedium = RetroDisplayLabelStyle,
)

private val RetroShapes = Shapes(
    extraSmall = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(0.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(0.dp),
    extraLarge = RoundedCornerShape(0.dp),
)

@Composable
internal fun BitcoinTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RetroColorScheme,
        typography = RetroTypography,
        shapes = RetroShapes,
        content = content,
    )
}

private const val RETRO_BLACK_ARGB = 0xFF000000
private const val RETRO_RED_ARGB = 0xFFF44336
private const val RETRO_WHITE_ARGB = 0xFFFFFFFF
private const val RETRO_SURFACE_ARGB = 0xFF212121
private const val RETRO_DATE_HEADER_ARGB = 0xFF412121
private const val RETRO_GRAY_ARGB = 0xFF979797
private const val RETRO_LIGHT_GRAY_ARGB = 0xFFF6F1F1
