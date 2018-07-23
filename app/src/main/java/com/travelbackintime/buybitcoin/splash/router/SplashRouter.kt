package com.travelbackintime.buybitcoin.splash.router

import com.travelbackintime.buybitcoin.splash.view.SplashActivity
import com.travelbackintime.buybitcoin.time_travel.view.TimeTravelActivity
import javax.inject.Inject

interface SplashRouter {

    fun openTimeTravelActivity()
}

class SplashRouterImpl @Inject constructor(private val activity: SplashActivity) : SplashRouter {

    override fun openTimeTravelActivity() {
        TimeTravelActivity.start(activity)
    }
}