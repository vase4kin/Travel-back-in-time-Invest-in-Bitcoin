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

package com.travelbackintime.buybitcoin.loading.router;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.travelbackintime.buybitcoin.home_coming.view.HomeComingFragment;
import com.travelbackintime.buybitcoin.loading.view.LoadingFragment;
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult;
import com.travelbackintime.buybitcoin.transition.TransitionUtilsKt;

import javax.inject.Inject;

import bitcoin.backintime.com.backintimebuybitcoin.R;

import static com.travelbackintime.buybitcoin.home_coming.view.HomeComingFragment.EXTRA_RESULT;

public class LoadingRouterImpl implements LoadingRouter {

    private final LoadingFragment fragment;

    @Inject
    LoadingRouterImpl(LoadingFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void openHomeComingActivity() {
        Bundle args = fragment.getArguments();
        if (args != null) {
            final TimeTravelResult result = args.getParcelable(EXTRA_RESULT);
            Fragment homeComingFragment = HomeComingFragment.createInstance(result);
            FragmentActivity activity = fragment.getActivity();
            if (activity != null) {
                TransitionUtilsKt.addTransitions(homeComingFragment, activity.getApplicationContext());
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .remove(fragment)
                        .commit();
                fragmentManager
                        .beginTransaction()
                        .add(R.id.container, homeComingFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}
