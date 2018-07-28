package com.travelbackintime.buybitcoin.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class ClipboardUtils(private val context: Context) {

    fun copyToClipBoard(label: String, textToCopy: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, textToCopy)
        clipboard.primaryClip = clip
    }
}
