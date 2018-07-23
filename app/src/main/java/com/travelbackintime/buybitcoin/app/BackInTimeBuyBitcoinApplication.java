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

package com.travelbackintime.buybitcoin.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.android.gms.ads.MobileAds;
import com.travelbackintime.buybitcoin.dagger.DaggerAppComponent;

import bitcoin.backintime.com.backintimebuybitcoin.R;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class BackInTimeBuyBitcoinApplication extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.ad_mob_id));
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
