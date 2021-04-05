package com.travelbackintime.buybitcoin.dagger

import android.app.Application
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachine
import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachineFactory
import com.github.vase4kin.shared.tracker.NativeAnalytics
import com.github.vase4kin.shared.tracker.Tracker
import com.github.vase4kin.shared.tracker.TrackerImpl
import com.google.firebase.analytics.FirebaseAnalytics
import com.travelbackintime.buybitcoin.impl.NativeAnalyticsImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object SharedAppModule {

    @Singleton
    @Provides
    fun providesTimeTravelMachine(
        app: Application
    ): TimeTravelMachine {
        return TimeTravelMachineFactory.create(
            eventWithAbsentPrice = TimeTravelMachine.Event.RealWorldEvent(
                title = app.resources.getString(R.string.text_oops),
                description = app.resources.getString(R.string.text_basically_nothing),
                isDonate = false
            )
        )
    }

    @Provides
    fun provideNativeAnalytics(analytics: FirebaseAnalytics): NativeAnalytics {
        return NativeAnalyticsImpl(analytics)
    }

    @Singleton
    @Provides
    fun providesTracker(analytics: NativeAnalytics): Tracker {
        return TrackerImpl(analytics)
    }
}
