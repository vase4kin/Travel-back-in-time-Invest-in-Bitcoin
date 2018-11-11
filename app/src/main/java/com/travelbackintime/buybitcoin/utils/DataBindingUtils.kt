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

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import androidx.databinding.Observable
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

inline fun <T : BaseObservable> T.onChanged(crossinline block: () -> Unit) = apply {
    addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            block()
        }
    })
}

@BindingAdapter("showAds")
fun AdView.showAds(isAdsEnabled: Boolean) {
    if (isAdsEnabled) {
        val adRequest = AdRequest.Builder().build()
        loadAd(adRequest)
        visibility = View.VISIBLE
    }
}

@BindingAdapter("visibility")
fun setVisibility(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}