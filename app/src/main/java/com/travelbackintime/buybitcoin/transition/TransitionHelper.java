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

package com.travelbackintime.buybitcoin.transition;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.transition.Transition;
import android.transition.TransitionInflater;

import bitcoin.backintime.com.backintimebuybitcoin.R;

public class TransitionHelper {

    public static void addTransitions(Fragment fragment, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition enterTransition = TransitionInflater.from(context).inflateTransition(R.transition.fragment_slide_right);
            fragment.setEnterTransition(enterTransition);
            Transition exitTransition = TransitionInflater.from(context).inflateTransition(R.transition.fragment_slide_left);
            fragment.setExitTransition(exitTransition);
            fragment.setAllowEnterTransitionOverlap(true);
        }
    }
}