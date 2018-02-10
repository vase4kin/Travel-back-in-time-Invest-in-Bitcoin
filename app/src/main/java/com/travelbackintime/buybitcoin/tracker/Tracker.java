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

public interface Tracker {

    String EVENT_USER_SETS_TIME = "event_user_sets_time";
    String EVENT_USER_SETS_MONEY = "event_user_sets_money";
    String EVENT_USER_TRAVELS_BACK = "event_user_travels_back";
    String EVENT_USER_STARTS_OVER = "event_user_starts_over";
    String EVENT_USER_SHARES_WITH_FRIENDS = "event_user_shares_with_friends";
    String EVENT_USER_SHARES_ON_FB = "event_user_shares_on_fb";
    String EVENT_USER_SHARES_ON_TWITTER = "event_user_shares_on_twitter";
    String EVENT_USER_SEES_EMPTY_AMOUNT_ERROR = "event_user_sees_empty_mon_error";
    String EVENT_USER_SEES_AT_LEAST_DOLLAR_ERROR = "event_user_sees_one_dollar_error";
    String EVENT_USER_SEES_YOU_RICH_ERROR = "event_user_sees_you_rich_error";
    String EVENT_USER_COPIES_BTC_WALLET = "event_user_copies_btc_wallet";
    String EVENT_USER_GETS_TO_PIZZA_LOVER = "event_user_gets_to_pizza_lover";
    String EVENT_USER_GETS_TO_SATOSHI = "event_user_gets_to_satoshi";
    String EVENT_USER_GETS_TO_EXIST = "event_user_gets_to_exist";
    String EVENT_USER_GETS_TO_NOT_BORN = "event_user_gets_to_not_exist";
    String EVENT_USER_GETS_TO_BASICALLY_NOTHING = "event_user_gets_to_2009";
    String EVENT_USER_GETS_TO_MAGICIAN = "event_user_gets_to_future";
    String EVENT_USER_FETCHES_DATA = "event_user_fetch_data";
    String PARAMETER_TIME = "parameter_time";
    String PARAMETER_MONEY = "parameter_money";
    String PARAMETER_RESULT = "parameter_result";

    void trackUserSetsTime(String time);

    void trackUserSetsMoney(String money);

    void trackUserTravelsBackAndBuys();

    void trackUserSeesEmptyAmountError();

    void trackUserSeesAtLeastDollarError();

    void trackUserSeesYouReachError();

    void trackUserStartsOver();

    void trackUserSharesWithFriends();

    void trackUserCopiesBtcWalletAddress();

    void trackUserGetsToPizzaLover();

    void trackUserGetsToSatoshi();

    void trackUserGetsToExist();

    void trackUserGetsToNotBorn();

    void trackUserGetsToBasicallyNothing();

    void trackUserGetsToMagician();

    void trackUserSharesOnFb();

    void trackUserSharesOnTwitter();

    void trackDataDownloadedSuccessfully();

    void trackDataDownloadedError();

    void trackDataNotDownloaded();
}
