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

package com.travelbackintime.buybitcoin.home_coming.router;

import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;

import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.travelbackintime.buybitcoin.home_coming.view.HomeComingFragment;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import javax.inject.Inject;

import bitcoin.backintime.com.backintimebuybitcoin.R;

import static com.travelbackintime.buybitcoin.time_travel.view.TimeTravelFragmentKt.createTimeTravelFragment;

public class HomeComingRouterImpl implements HomeComingRouter {

    private final AppCompatActivity activity;

    @Inject
    HomeComingRouterImpl(HomeComingFragment fragment) {
        this.activity = (AppCompatActivity) fragment.getActivity();
    }

    @Override
    public void openTimeTravelActivity() {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, createTimeTravelFragment())
                .commit();
    }

    @Override
    public void shareWithFriends(String textToShare) {
        ShareCompat.IntentBuilder.from(activity)
                .setHtmlText(textToShare)
                .setType("text/html")
                .startChooser();
    }

    @Override
    public void shareToTwitter(String textToShare) {
        StringBuilder textToShareBuilder = new StringBuilder(textToShare);
        textToShareBuilder.append(" #");
        textToShareBuilder.append(activity.getString(R.string.text_hashtag));
        TweetComposer.Builder builder = new TweetComposer.Builder(activity)
                .text(textToShareBuilder.toString());
        builder.show();
    }

    @Override
    public void shareToFaceBook(String googlePlayLink) {
        final ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(googlePlayLink))
                .setShareHashtag(new ShareHashtag.Builder().setHashtag(activity.getString(R.string.text_hashtag)).build())
                .build();
        ShareDialog.show(activity, content);
    }
}
