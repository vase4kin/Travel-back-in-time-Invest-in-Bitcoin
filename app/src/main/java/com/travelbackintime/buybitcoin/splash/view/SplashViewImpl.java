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

package com.travelbackintime.buybitcoin.splash.view;

import android.app.Activity;
import android.view.View;

import javax.inject.Inject;

import bitcoin.backintime.com.backintimebuybitcoin.R;

public class SplashViewImpl implements SplashView {

    private final Activity activity;

    private View retryView;

    @Inject
    SplashViewImpl(SplashActivity activity) {
        this.activity = activity;
    }

    @Override
    public void initViews(ViewListener listener) {
        retryView = activity.findViewById(R.id.retry_view);

        activity.findViewById(R.id.retry_button).setOnClickListener(v -> {
            listener.onRetryButtonClick();
        });
    }

    @Override
    public void hideRetryView() {
        retryView.setVisibility(View.GONE);
    }

    @Override
    public void showRetryView() {
        retryView.setVisibility(View.VISIBLE);
    }
}
