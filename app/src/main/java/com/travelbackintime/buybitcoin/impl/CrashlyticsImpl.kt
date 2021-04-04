package com.travelbackintime.buybitcoin.impl

import com.github.vase4kin.crashlytics.Crashlytics
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsImpl(
    private val firebaseCrashlytics: FirebaseCrashlytics
) : Crashlytics {

    override fun recordException(throwable: Throwable) {
        firebaseCrashlytics.recordException(throwable)
    }
}
