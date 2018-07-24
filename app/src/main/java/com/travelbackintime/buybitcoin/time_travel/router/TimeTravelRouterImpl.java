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

package com.travelbackintime.buybitcoin.time_travel.router;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.travelbackintime.buybitcoin.loading.LoadingFragmentKt;
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult;
import com.travelbackintime.buybitcoin.time_travel.view.TimeTravelFragment;
import com.travelbackintime.buybitcoin.transition.TransitionUtilsKt;

import javax.inject.Inject;

import bitcoin.backintime.com.backintimebuybitcoin.R;

public class TimeTravelRouterImpl implements TimeTravelRouter {

    private final AppCompatActivity activity;

    @Inject
    TimeTravelRouterImpl(TimeTravelFragment fragment) {
        this.activity = (AppCompatActivity) fragment.getActivity();
    }

    @Override
    public void openLoadingActivity(TimeTravelResult result) {
        Fragment fragment = LoadingFragmentKt.createInstance(result);
        TransitionUtilsKt.addTransitions(fragment, activity.getApplicationContext());
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
