package com.travelbackintime.buybitcoin.homecoming.share

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.travelbackintime.buybitcoin.homecoming.view.HomeComingFragment
import com.travelbackintime.buybitcoin.impl.TimeTravelEvenWrapper
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import javax.inject.Inject

interface ShareHelper {
    fun shareWithFriends(event: TimeTravelEvenWrapper.TimeTravelEvent)
    fun shareToTwitter(event: TimeTravelEvenWrapper.TimeTravelEvent)
    fun shareToFaceBook()
}

class ShareHelperImpl @Inject constructor(
    fragment: HomeComingFragment,
    private val formatterUtils: FormatterUtils
) : ShareHelper {

    private val activity = fragment.activity as AppCompatActivity

    override fun shareWithFriends(event: TimeTravelEvenWrapper.TimeTravelEvent) {
        val textToShare = createShareText(event)
        ShareCompat.IntentBuilder.from(activity)
            .setHtmlText(textToShare)
            .setType("text/plain")
            .startChooser()
    }

    override fun shareToTwitter(event: TimeTravelEvenWrapper.TimeTravelEvent) {
        val textToShare = createShareText(event)
        val textToShareBuilder = StringBuilder(textToShare)
        textToShareBuilder.append(" #")
        textToShareBuilder.append(activity.getString(R.string.text_hashtag))
        val builder = TweetComposer.Builder(activity)
            .text(textToShareBuilder.toString())
        builder.show()
    }

    override fun shareToFaceBook() {
        val googlePlayLink = createGooglePlayLink()
        val content = ShareLinkContent.Builder()
            .setContentUrl(Uri.parse(googlePlayLink))
            .setShareHashtag(
                ShareHashtag.Builder().setHashtag(activity.getString(R.string.text_hashtag)).build()
            )
            .build()
        ShareDialog.show(activity, content)
    }

    private fun createGooglePlayLink(): String {
        return activity.getString(R.string.url_google_play, activity.packageName)
    }

    private fun createShareText(event: TimeTravelEvenWrapper.TimeTravelEvent): String {
        val googlePlayLink = createGooglePlayLink()
        val date = event.timeToTravel
        val profitValue = event.profitMoney
        val investedValue = formatterUtils.formatPrice(event.investedMoney)
        val profit = formatterUtils.formatPrice(profitValue)
        return activity.getString(
            R.string.text_share,
            formatterUtils.formatDateToShareText(date),
            investedValue, profit, googlePlayLink
        )
    }
}
