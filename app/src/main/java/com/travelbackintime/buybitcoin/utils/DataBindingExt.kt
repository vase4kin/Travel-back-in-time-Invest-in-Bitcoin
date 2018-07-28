package com.travelbackintime.buybitcoin.utils

import android.databinding.BaseObservable
import android.databinding.BindingAdapter
import android.databinding.Observable
import android.view.View
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