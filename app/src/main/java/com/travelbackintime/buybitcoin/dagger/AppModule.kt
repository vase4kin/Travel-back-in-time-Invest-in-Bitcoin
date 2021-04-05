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
import bitcoin.backintime.com.backintimebuybitcoin.BuildConfig
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.coindesk.remoteconfig.RemoteConfigService
import com.github.vase4kin.crashlytics.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.travelbackintime.buybitcoin.impl.CrashlyticsImpl
import com.travelbackintime.buybitcoin.impl.RemoteConfigServiceImpl
import com.travelbackintime.buybitcoin.utils.ClipboardUtils
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import com.travelbackintime.buybitcoin.utils.ResourcesProviderUtils
import com.travelbackintime.buybitcoin.utils.ToastUtils
import dagger.Module
import dagger.Provides
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Singleton

private const val DEBUG_CACHE_SECS = 30L
private const val PROD_CACHE_SECS = 43200L

@Suppress("TooManyFunctions")
@Module
class AppModule {

    @Provides
    fun providesNumberFormat(): NumberFormat {
        return NumberFormat.getCurrencyInstance(Locale.US)
    }

    @Singleton
    @Provides
    fun providesFirebaseAnalytics(app: Application): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(app.applicationContext)
    }

    @Provides
    fun provideCrashlytics(): Crashlytics {
        return CrashlyticsImpl(FirebaseCrashlytics.getInstance())
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
            .build()
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        return FirebaseRemoteConfig.getInstance()
    }

    @Singleton
    @Provides
    fun providesRemoteConfigService(
        firebaseRemoteConfig: FirebaseRemoteConfig,
        crashlytics: Crashlytics
    ): RemoteConfigService {
        val cacheSecs = if (BuildConfig.DEBUG) DEBUG_CACHE_SECS else PROD_CACHE_SECS
        return RemoteConfigServiceImpl(firebaseRemoteConfig, crashlytics, cacheSecs)
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
