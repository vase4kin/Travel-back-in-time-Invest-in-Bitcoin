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

package com.travelbackintime.buybitcoin.dagger;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.travelbackintime.buybitcoin.format.Formatter;
import com.travelbackintime.buybitcoin.remote_config.RemoteConfigService;
import com.travelbackintime.buybitcoin.remote_config.RemoteConfigServiceImpl;
import com.travelbackintime.buybitcoin.time.TimeTravelManager;
import com.travelbackintime.buybitcoin.time.TimeTravelManagerImpl;
import com.travelbackintime.buybitcoin.tracker.Tracker;
import com.travelbackintime.buybitcoin.tracker.TrackerImpl;

import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Singleton;

import bitcoin.backintime.com.backintimebuybitcoin.BuildConfig;
import bitcoin.backintime.com.backintimebuybitcoin.R;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Singleton
    @Provides
    TimeTravelManager providesBitcoinManager(FirebaseDatabase database, SharedPreferences sharedPreferences) {
        return new TimeTravelManagerImpl(database, sharedPreferences);
    }

    @Provides
    NumberFormat providesNumberFormat() {
        return NumberFormat.getCurrencyInstance(Locale.US);
    }

    @Singleton
    @Provides
    FirebaseAnalytics providesFirebaseAnalytics(Application app) {
        return FirebaseAnalytics.getInstance(app.getApplicationContext());
    }

    @Singleton
    @Provides
    Tracker providesTracker(FirebaseAnalytics analytics) {
        return new TrackerImpl(analytics);
    }

    @Singleton
    @Provides
    Formatter providesFormat(NumberFormat numberFormat) {
        return new Formatter(numberFormat);
    }

    @Singleton
    @Provides
    FirebaseRemoteConfig providesFirebaseRemoteConfig() {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        return FirebaseRemoteConfig.getInstance();
    }

    @Singleton
    @Provides
    RemoteConfigService providesRemoteConfigService(FirebaseRemoteConfig firebaseRemoteConfig) {
        long cacheSecs = BuildConfig.DEBUG ? 30L : 43200L;
        return new RemoteConfigServiceImpl(firebaseRemoteConfig, cacheSecs);
    }

    @Singleton
    @Provides
    FirebaseDatabase providesFirebaseDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        firebaseDatabase.getReference().keepSynced(true);
        return FirebaseDatabase.getInstance();
    }

    @Singleton
    @Provides
    SharedPreferences providesSharedPreferences(Application app) {
        return PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
    }

}
