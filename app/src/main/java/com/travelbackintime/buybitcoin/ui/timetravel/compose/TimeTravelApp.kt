package com.travelbackintime.buybitcoin.ui.timetravel.compose

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import bitcoin.backintime.com.backintimebuybitcoin.R
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Serializable
private data object SplashRoute : NavKey

@Serializable
private data object TimeTravelRoute : NavKey

@Serializable
private data object ErrorRoute : NavKey

@Serializable
internal data class ResultRoute(val profitMoney: Double, val investedMoney: Double, val timeToTravel: Long) : NavKey

@Serializable
private data class LoadingRoute(val profitMoney: Double, val investedMoney: Double, val timeToTravel: Long) : NavKey {
    fun resultRoute() = ResultRoute(
        profitMoney = profitMoney,
        investedMoney = investedMoney,
        timeToTravel = timeToTravel,
    )
}

@Composable
fun TimeTravelApp(viewModel: TimeTravelViewModel) {
    val backStack = rememberNavBackStack(SplashRoute)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.navigation.collect { destination ->
            when (destination) {
                TimeTravelNavigation.Error -> backStack.add(ErrorRoute)
                is TimeTravelNavigation.Result -> backStack.add(
                    LoadingRoute(
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
            transitionSpec = { retroForwardTransform() },
            popTransitionSpec = { retroBackTransform() },
            entryProvider = entryProvider {
                entry<SplashRoute> {
                    LaunchedEffect(Unit) {
                        delay(SPLASH_DURATION_MILLIS)
                        backStack.clear()
                        backStack.add(TimeTravelRoute)
                    }
                    SplashScreen()
                }
                entry<TimeTravelRoute>(
                    metadata = NavDisplay.transitionSpec { retroFadeTransform() },
                ) {
                    TimeTravelScreen(
                        uiState = uiState,
                        onDateSelected = viewModel::selectDate,
                        onInvestmentSelected = viewModel::selectInvestment,
                        onInvest = viewModel::invest,
                    )
                }
                entry<LoadingRoute> { loading ->
                    LoadingScreen(
                        onFinished = {
                            backStack.removeLastOrNull()
                            backStack.add(loading.resultRoute())
                        },
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
private fun SplashScreen() {
    Image(
        painter = painterResource(R.drawable.ic_splash),
        contentDescription = stringResource(R.string.app_name),
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
    )
}

private fun retroForwardTransform(): ContentTransform = slideInHorizontally(
    animationSpec = tween(durationMillis = TRANSITION_DURATION_MILLIS, delayMillis = TRANSITION_DELAY_MILLIS),
    initialOffsetX = { it },
) togetherWith slideOutHorizontally(
    animationSpec = tween(durationMillis = TRANSITION_DURATION_MILLIS, delayMillis = TRANSITION_DELAY_MILLIS),
    targetOffsetX = { -it },
)

private fun retroBackTransform(): ContentTransform = slideInHorizontally(
    animationSpec = tween(durationMillis = TRANSITION_DURATION_MILLIS, delayMillis = TRANSITION_DELAY_MILLIS),
    initialOffsetX = { -it },
) togetherWith slideOutHorizontally(
    animationSpec = tween(durationMillis = TRANSITION_DURATION_MILLIS, delayMillis = TRANSITION_DELAY_MILLIS),
    targetOffsetX = { it },
)

private fun retroFadeTransform(): ContentTransform = fadeIn(
    animationSpec = tween(durationMillis = TRANSITION_DURATION_MILLIS, delayMillis = TRANSITION_DELAY_MILLIS),
) togetherWith fadeOut(
    animationSpec = tween(durationMillis = TRANSITION_DURATION_MILLIS, delayMillis = TRANSITION_DELAY_MILLIS),
)

private const val SPLASH_DURATION_MILLIS = 1_500L
private const val TRANSITION_DURATION_MILLIS = 800
private const val TRANSITION_DELAY_MILLIS = 200
