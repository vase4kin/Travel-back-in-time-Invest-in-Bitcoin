package com.travelbackintime.buybitcoin.home_coming.share

import android.net.Uri
import android.support.v4.app.ShareCompat
import android.support.v7.app.AppCompatActivity
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.travelbackintime.buybitcoin.home_coming.view.HomeComingFragment
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult
import com.travelbackintime.buybitcoin.utils.FormatterUtils
import com.twitter.sdk.android.tweetcomposer.TweetComposer
import javax.inject.Inject

interface ShareHelper {

    fun shareWithFriends(result: TimeTravelResult)

    fun shareToTwitter(result: TimeTravelResult)

    fun shareToFaceBook()
}

class ShareHelperImpl @Inject constructor(fragment: HomeComingFragment, private val formatterUtils: FormatterUtils) : ShareHelper {

    private val activity = fragment.activity as AppCompatActivity

    override fun shareWithFriends(result: TimeTravelResult) {
        val textToShare = createShareText(result)
        ShareCompat.IntentBuilder.from(activity)
                .setHtmlText(textToShare)
                .setType("text/plain")
                .startChooser()
    }

    override fun shareToTwitter(result: TimeTravelResult) {
        val textToShare = createShareText(result)
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
                .setShareHashtag(ShareHashtag.Builder().setHashtag(activity.getString(R.string.text_hashtag)).build())
                .build()
        ShareDialog.show(activity, content)
    }

    private fun createGooglePlayLink(): String {
        return activity.getString(R.string.url_google_play, activity.packageName)
    }

    private fun createShareText(result: TimeTravelResult): String {
        val googlePlayLink = createGooglePlayLink()
        val date = result.timeToTravel
        val profitValue = result.profitMoney
        val profit = formatterUtils.formatPrice(profitValue)
        return activity.getString(R.string.text_share, formatterUtils.formatDateToShareText(date!!), profit, googlePlayLink)
    }
}