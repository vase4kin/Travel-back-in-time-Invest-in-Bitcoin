/*
 * Copyright 2018 Andrey Tolpeev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travelbackintime.buybitcoin.dagger

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import bitcoin.backintime.com.backintimebuybitcoin.BuildConfig
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.travelbackintime.buybitcoin.remote_config.RemoteConfigService
import com.travelbackintime.buybitcoin.remote_config.RemoteConfigServiceImpl
import com.travelbackintime.buybitcoin.time_travel_machine.TimeTravelMachine
import com.travelbackintime.buybitcoin.time_travel_machine.TimeTravelMachineImpl
import com.travelbackintime.buybitcoin.tracker.Tracker
import com.travelbackintime.buybitcoin.tracker.TrackerImpl
import com.travelbackintime.buybitcoin.utils.ClipboardUtils
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import com.travelbackintime.buybitcoin.utils.ResourcesProviderUtils
import com.travelbackintime.buybitcoin.utils.ToastUtils
import dagger.Module
import dagger.Provides
import java.text.NumberFormat
import java.util.*
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesTimeTravelMachine(database: FirebaseDatabase, sharedPreferences: SharedPreferences): TimeTravelMachine {
        return TimeTravelMachineImpl(database, sharedPreferences)
    }

    @Provides
    fun providesNumberFormat(): NumberFormat {
        return NumberFormat.getCurrencyInstance(Locale.US)
    }

    @Singleton
    @Provides
    fun providesFirebaseAnalytics(app: Application): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(app.applicationContext)
    }

    @Singleton
    @Provides
    fun providesTracker(analytics: FirebaseAnalytics): Tracker {
        return TrackerImpl(analytics)
    }

    @Singleton
    @Provides
    fun providesFormatterUtils(numberFormat: NumberFormat): FormatterUtils {
        return FormatterUtils(numberFormat)
    }

    @Singleton
    @Provides
    fun providesFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        firebaseRemoteConfig.setConfigSettings(configSettings)
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults)
        return FirebaseRemoteConfig.getInstance()
    }

    @Singleton
    @Provides
    fun providesRemoteConfigService(firebaseRemoteConfig: FirebaseRemoteConfig): RemoteConfigService {
        val cacheSecs = if (BuildConfig.DEBUG) 30L else 43200L
        return RemoteConfigServiceImpl(firebaseRemoteConfig, cacheSecs)
    }

    @Singleton
    @Provides
    fun providesFirebaseDatabase(): FirebaseDatabase {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.setPersistenceEnabled(true)
        firebaseDatabase.reference.keepSynced(true)
        return FirebaseDatabase.getInstance()
    }

    @Singleton
    @Provides
    fun providesSharedPreferences(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app.applicationContext)
    }

    @Singleton
    @Provides
    fun providesResourceProvider(app: Application): ResourcesProviderUtils {
        return ResourcesProviderUtils(app.applicationContext)
    }

    @Singleton
    @Provides
    fun providesToastUtils(app: Application): ToastUtils {
        return ToastUtils(app.applicationContext)
    }

    @Singleton
    @Provides
    fun providesClipboardUtils(app: Application): ClipboardUtils {
        return ClipboardUtils(app.applicationContext)
    }

}
