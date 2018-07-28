package com.travelbackintime.buybitcoin.utils

import android.content.Context
import android.widget.Toast

class ToastUtils(private val context: Context) {
    fun showToast(resourceId: Int) {
        Toast.makeText(context, resourceId, Toast.LENGTH_SHORT).show()
    }
}