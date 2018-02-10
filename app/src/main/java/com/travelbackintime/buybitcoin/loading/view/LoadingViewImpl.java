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

package com.travelbackintime.buybitcoin.loading.view;

import com.crashlytics.android.Crashlytics;

import javax.inject.Inject;

import bitcoin.backintime.com.backintimebuybitcoin.R;
import pl.droidsonroids.gif.GifDrawable;

public class LoadingViewImpl implements LoadingView {

    private final LoadingFragment fragment;

    @Inject
    LoadingViewImpl(LoadingFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void loadGif(final GifListener listener) {
        try {
            GifDrawable gifFromResource = new GifDrawable(fragment.getResources(), R.drawable.car_animation);
            gifFromResource.setLoopCount(1);
            gifFromResource.setSpeed(0.8f);
            fragment.getView().findViewById(R.id.image_view).setBackground(gifFromResource);
            gifFromResource.addAnimationListener(loopNumber -> listener.onCompleted());
        } catch (Exception e) {
            Crashlytics.logException(e);
            listener.onCompleted();
        }
    }
}
