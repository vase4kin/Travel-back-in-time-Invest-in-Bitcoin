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

package com.travelbackintime.buybitcoin.tracker;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class TrackerImpl implements Tracker {

    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_ERROR = "error";
    private static final String STATUS_NOT_DOWNLOADED = "not_downloaded";

    private final FirebaseAnalytics analytics;

    public TrackerImpl(FirebaseAnalytics analytics) {
        this.analytics = analytics;
    }

    @Override
    public void trackUserSetsTime(String time) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAMETER_TIME, time);
        analytics.logEvent(EVENT_USER_SETS_TIME, bundle);
    }

    @Override
    public void trackUserSetsMoney(String money) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAMETER_MONEY, money);
        analytics.logEvent(EVENT_USER_SETS_MONEY, bundle);
    }

    @Override
    public void trackUserTravelsBackAndBuys() {
        analytics.logEvent(EVENT_USER_TRAVELS_BACK, null);
    }

    @Override
    public void trackUserSeesEmptyAmountError() {
        analytics.logEvent(EVENT_USER_SEES_EMPTY_AMOUNT_ERROR, null);
    }

    @Override
    public void trackUserSeesAtLeastDollarError() {
        analytics.logEvent(EVENT_USER_SEES_AT_LEAST_DOLLAR_ERROR, null);
    }

    @Override
    public void trackUserSeesYouReachError() {
        analytics.logEvent(EVENT_USER_SEES_YOU_RICH_ERROR, null);
    }

    @Override
    public void trackUserStartsOver() {
        analytics.logEvent(EVENT_USER_STARTS_OVER, null);
    }

    @Override
    public void trackUserSharesWithFriends() {
        analytics.logEvent(EVENT_USER_SHARES_WITH_FRIENDS, null);
    }

    @Override
    public void trackUserCopiesBtcWalletAddress() {
        analytics.logEvent(EVENT_USER_COPIES_BTC_WALLET, null);
    }

    @Override
    public void trackUserGetsToPizzaLover() {
        analytics.logEvent(EVENT_USER_GETS_TO_PIZZA_LOVER, null);
    }

    @Override
    public void trackUserGetsToSatoshi() {
        analytics.logEvent(EVENT_USER_GETS_TO_SATOSHI, null);
    }

    @Override
    public void trackUserGetsToExist() {
        analytics.logEvent(EVENT_USER_GETS_TO_EXIST, null);
    }

    @Override
    public void trackUserGetsToNotBorn() {
        analytics.logEvent(EVENT_USER_GETS_TO_NOT_BORN, null);
    }

    @Override
    public void trackUserGetsToBasicallyNothing() {
        analytics.logEvent(EVENT_USER_GETS_TO_BASICALLY_NOTHING, null);
    }

    @Override
    public void trackUserGetsToMagician() {
        analytics.logEvent(EVENT_USER_GETS_TO_MAGICIAN, null);
    }

    @Override
    public void trackUserSharesOnFb() {
        analytics.logEvent(EVENT_USER_SHARES_ON_FB, null);
    }

    @Override
    public void trackUserSharesOnTwitter() {
        analytics.logEvent(EVENT_USER_SHARES_ON_TWITTER, null);
    }

    @Override
    public void trackDataDownloadedSuccessfully() {
        trackDataDownloaded(STATUS_SUCCESS);
    }

    @Override
    public void trackDataDownloadedError() {
        trackDataDownloaded(STATUS_ERROR);
    }

    @Override
    public void trackDataNotDownloaded() {
        trackDataDownloaded(STATUS_NOT_DOWNLOADED);
    }

    private void trackDataDownloaded(String status) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAMETER_RESULT, status);
        analytics.logEvent(EVENT_USER_FETCHES_DATA, bundle);
    }
}
