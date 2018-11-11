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

package com.travelbackintime.buybitcoin.utils

import android.content.Context
import android.os.Build
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import bitcoin.backintime.com.backintimebuybitcoin.R

fun addFragmentSlideTransitions(fragment: Fragment, context: Context) {
    addFragmentTransitions(fragment, context, R.transition.fragment_slide_right, R.transition.fragment_slide_left)
}

fun addFragmentFadeTransitions(fragment: androidx.fragment.app.Fragment, context: Context) {
    addFragmentTransitions(fragment, context, R.transition.fragment_fade_in, R.transition.fragment_fade_out)
}

private fun addFragmentTransitions(fragment: Fragment, context: Context, enterTransitionRes: Int, exitTransitionRes: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val enterTransition = TransitionInflater.from(context).inflateTransition(enterTransitionRes)
        fragment.enterTransition = enterTransition
        val exitTransition = TransitionInflater.from(context).inflateTransition(exitTransitionRes)
        fragment.exitTransition = exitTransition
        fragment.allowEnterTransitionOverlap = true
    }
}