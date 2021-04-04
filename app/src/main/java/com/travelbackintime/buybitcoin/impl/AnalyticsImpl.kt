package com.travelbackintime.buybitcoin.impl

import android.os.Bundle
import com.github.vase4kin.coindesk.tracker.Analytics
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsImpl(
    private val analytics: FirebaseAnalytics
) : Analytics {

    override fun logEvent(eventName: String, parameters: Map<String, String>) {
        analytics.logEvent(eventName, parametersToBundle(parameters))
    }

    private fun parametersToBundle(parameters: Map<String, String>): Bundle {
        val bundle = Bundle()
        parameters.forEach {
            bundle.putString(it.key, it.value)
        }
        return bundle
    }
}