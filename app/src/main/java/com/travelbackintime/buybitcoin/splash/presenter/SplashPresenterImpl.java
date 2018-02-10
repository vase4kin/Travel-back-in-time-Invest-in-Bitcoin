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

package com.travelbackintime.buybitcoin.splash.presenter;

import android.os.Handler;
import android.os.Looper;

import com.travelbackintime.buybitcoin.splash.router.SplashRouter;
import com.travelbackintime.buybitcoin.splash.view.SplashView;
import com.travelbackintime.buybitcoin.time.TimeTravelManager;
import com.travelbackintime.buybitcoin.tracker.Tracker;

import javax.inject.Inject;

public class SplashPresenterImpl implements SplashPresenter {

    private static final int TIMEOUT_SPLASH = 2500;

    private final SplashView view;
    private final TimeTravelManager manager;
    private final Tracker tracker;
    private final SplashRouter router;

    @Inject
    SplashPresenterImpl(SplashView view,
                        TimeTravelManager manager,
                        Tracker tracker,
                        SplashRouter router) {
        this.view = view;
        this.manager = manager;
        this.tracker = tracker;
        this.router = router;
    }

    @Override
    public void handleOnCreate() {
        view.initViews(() -> {
            view.hideRetryView();
            initFlowCapacitor();
        });

        // Welcome to real world of real programming
        new Handler(Looper.getMainLooper()).postDelayed(this::initFlowCapacitor, TIMEOUT_SPLASH);
    }

    private void initFlowCapacitor() {
        manager.initFlowCapacitor(new TimeTravelManager.FlowCapacitorInitListener() {
            @Override
            public void onSuccess() {
                tracker.trackDataDownloadedSuccessfully();
                router.openTimeTravelActivity();
            }

            @Override
            public void onError() {
                tracker.trackDataDownloadedError();
                view.showRetryView();
            }

            @Override
            public void onDataNotDownloaded() {
                tracker.trackDataNotDownloaded();
                view.showRetryView();
            }
        });
    }
}
