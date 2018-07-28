package com.travelbackintime.buybitcoin.utils

import android.databinding.BaseObservable
import android.databinding.Observable

inline fun <T : BaseObservable> T.onChanged(crossinline block: () -> Unit) = apply {
    addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
            block()
        }
    })
}