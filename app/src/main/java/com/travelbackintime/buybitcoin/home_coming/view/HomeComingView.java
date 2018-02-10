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

package com.travelbackintime.buybitcoin.home_coming.view;

public interface HomeComingView {

    void initViews(ViewListener listener);

    void showShareView();

    void hideShareView();

    void showParamInfo();

    void hideParamInfo();

    void showProfitView();

    void hideProfitView();

    void showAmIMagicianToKnowText();

    void showNotBornText();

    void showBasicallyNothingText();

    void showHelloSatoshiText();

    void showPizzaLoverText();

    void hideInfoView();

    void loadAds();

    void showDonateView();

    void hideDonateView();

    void showWalletCopiedToast();

    void setDisplayValues(String profit, String amount, String month, String year);

    interface ViewListener {
        void onStartOver();

        void onShareWithFriends();

        void onShareOnFacebook();

        void onShareOnTwitter();

        void onCopyWalletAddress();
    }
}
