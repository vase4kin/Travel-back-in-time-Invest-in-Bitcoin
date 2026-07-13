package com.travelbackintime.buybitcoin.ui.timetravel.compose

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import bitcoin.backintime.com.backintimebuybitcoin.R
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimeTravelScreenTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun initialStateShowsRequiredActionsAndDisablesInvestment() {
        composeRule.setContent {
            MaterialTheme {
                TimeTravelScreen(
                    uiState = TimeTravelUiState(),
                    onDateSelected = {},
                    onInvestmentSelected = {},
                    onInvest = {},
                )
            }
        }

        composeRule.onNodeWithText(string(R.string.button_set_date_title)).assertIsDisplayed()
        composeRule.onNodeWithText(string(R.string.button_set_amount_title)).assertIsDisplayed()
        composeRule.onNodeWithText(string(R.string.button_buy_bitcoin_title)).assertIsNotEnabled()
    }

    @Test
    fun amountSheetReturnsSelectedSuggestion() {
        var selectedAmount: Double? = null
        composeRule.setContent {
            MaterialTheme {
                TimeTravelScreen(
                    uiState = TimeTravelUiState(),
                    onDateSelected = {},
                    onInvestmentSelected = { selectedAmount = it },
                    onInvest = {},
                )
            }
        }

        composeRule.onNodeWithText(string(R.string.button_set_amount_title)).performClick()
        composeRule.onNodeWithText("$10").performClick()
        composeRule.onNodeWithText(string(R.string.button_confirm_amount_title)).performClick()

        composeRule.runOnIdle { assertEquals(10.0, selectedAmount ?: 0.0, 0.0) }
    }

    private fun string(resource: Int): String = composeRule.activity.getString(resource)
}
