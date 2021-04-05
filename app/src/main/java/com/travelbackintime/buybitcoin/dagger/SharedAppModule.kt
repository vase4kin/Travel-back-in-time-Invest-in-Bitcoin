package com.travelbackintime.buybitcoin.dagger

import android.app.Application
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.crashlytics.Crashlytics
import com.github.vase4kin.shared.database.LocalDatabase
import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachine
import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachineFactory
import com.github.vase4kin.shared.tracker.NativeAnalytics
import com.github.vase4kin.shared.tracker.Tracker
import com.github.vase4kin.shared.tracker.TrackerImpl
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import com.travelbackintime.buybitcoin.impl.LocalDatabaseImpl
import com.travelbackintime.buybitcoin.impl.NativeAnalyticsImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object SharedAppModule {

    @Singleton
    @Provides
    fun providesTimeTravelMachine(
        localDatabase: LocalDatabase,
        app: Application
    ): TimeTravelMachine {
        return TimeTravelMachineFactory.create(
            localDatabase = localDatabase,
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

    @Provides
    fun provideLocaleDatabase(
        database: FirebaseDatabase,
        crashlytics: Crashlytics
    ): LocalDatabase {
        return LocalDatabaseImpl(database, crashlytics)
    }
}
