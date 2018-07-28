package com.travelbackintime.buybitcoin.utils

import android.content.Context

class ResourcesProviderUtils(private val context: Context) {
    fun getString(resourceId: Int): String {
        return context.getString(resourceId)
    }
}