package com.travelbackintime.buybitcoin.ui.timetravel.compose

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.util.TimeZone

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = "en-rUS-w400dp-h800dp-420dpi")
class TimeTravelScreenshotTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    init {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @Test
    fun initialInput() {
        captureScreen("input_initial") {
            TimeTravelScreen(
                uiState = TimeTravelUiState(),
                onDateSelected = {},
                onInvestmentSelected = {},
                onInvest = {},
            )
        }
    }

    @Test
    fun completedInput() {
        captureScreen("input_completed") {
            TimeTravelScreen(
                uiState = completedState,
                onDateSelected = {},
                onInvestmentSelected = {},
                onInvest = {},
            )
        }
    }

    @Test
    fun disabledLoadingInput() {
        captureScreen("input_loading") {
            TimeTravelScreen(
                uiState = completedState.copy(isLoading = true),
                onDateSelected = {},
                onInvestmentSelected = {},
                onInvest = {},
            )
        }
    }

    @Test
    fun compactInput() {
        captureScreen(
            name = "input_compact",
            size = DpSize(320.dp, 480.dp),
        ) {
            TimeTravelScreen(
                uiState = completedState,
                onDateSelected = {},
                onInvestmentSelected = {},
                onInvest = {},
            )
        }
    }

    @Test
    fun amountSheet() {
        setScreen {
            TimeTravelScreen(
                uiState = TimeTravelUiState(),
                onDateSelected = {},
                onInvestmentSelected = {},
                onInvest = {},
            )
        }
        composeRule.onNodeWithText(string(R.string.button_set_amount_title)).performClick()
        composeRule.onNode(isDialog()).captureRoboImage(screenshotPath("amount_sheet"))
    }

    @Test
    fun datePicker() {
        setScreen {
            TimeTravelScreen(
                uiState = TimeTravelUiState(selectedDateMillis = SCREENSHOT_DATE_MILLIS),
                onDateSelected = {},
                onInvestmentSelected = {},
                onInvest = {},
            )
        }
        composeRule.onNodeWithText(SCREENSHOT_DATE_LABEL).performClick()
        composeRule.onNode(isDialog()).captureRoboImage(screenshotPath("date_picker"))
    }

    @Test
    fun result() {
        captureScreen("result") {
            ResultScreen(
                result = sampleResult,
                showAds = false,
                onShare = {},
                onPriceProvider = {},
                onStartOver = {},
            )
        }
    }

    @Test
    fun compactResult() {
        captureScreen(
            name = "result_compact",
            size = DpSize(320.dp, 480.dp),
        ) {
            ResultScreen(
                result = sampleResult,
                showAds = false,
                onShare = {},
                onPriceProvider = {},
                onStartOver = {},
            )
        }
    }

    @Test
    fun error() {
        captureScreen("error") {
            ErrorScreen(onRetry = {})
        }
    }

    @Test
    fun loadingAnimation() {
        captureScreen("loading") {
            LoadingScreen(onFinished = {}, animationEnabled = false)
        }
    }

    private fun captureScreen(name: String, size: DpSize = DpSize(360.dp, 720.dp), content: @Composable () -> Unit) {
        setScreen(size, content)
        composeRule.onRoot().captureRoboImage(screenshotPath(name))
    }

    private fun setScreen(size: DpSize = DpSize(360.dp, 720.dp), content: @Composable () -> Unit) {
        composeRule.setContent {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                DeviceConfigurationOverride(
                    override = DeviceConfigurationOverride.ForcedSize(size),
                ) {
                    BitcoinTheme(content = content)
                }
            }
        }
    }

    private fun string(resource: Int): String = composeRule.activity.getString(resource)

    private fun screenshotPath(name: String) = "src/test/screenshots/$name.png"

    private companion object {
        // Keep screenshot content independent of the execution date and host timezone.
        // 2020-05-24T00:00:00Z
        const val SCREENSHOT_DATE_MILLIS = 1_590_278_400_000L
        const val SCREENSHOT_DATE_LABEL = "24 May 2020"

        val completedState = TimeTravelUiState(
            selectedDateMillis = SCREENSHOT_DATE_MILLIS,
            investedMoney = 100.0,
        )
        val sampleResult = ResultRoute(
            profitMoney = 71_262.57,
            investedMoney = 100.0,
            timeToTravel = SCREENSHOT_DATE_MILLIS,
        )
    }
}
