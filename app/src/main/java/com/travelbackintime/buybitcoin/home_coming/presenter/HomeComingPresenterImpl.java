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

package com.travelbackintime.buybitcoin.home_coming.presenter;

import com.travelbackintime.buybitcoin.home_coming.interactor.HomeComingInteractor;
import com.travelbackintime.buybitcoin.home_coming.router.HomeComingRouter;
import com.travelbackintime.buybitcoin.home_coming.view.HomeComingView;
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult;
import com.travelbackintime.buybitcoin.time_travel_machine.TimeTravelMachine;
import com.travelbackintime.buybitcoin.tracker.Tracker;
import com.travelbackintime.buybitcoin.utils.FormatterUtils;

import java.util.Date;

import javax.inject.Inject;

public class HomeComingPresenterImpl implements HomeComingPresenter {

    private final HomeComingView view;
    private final HomeComingInteractor interactor;
    private final HomeComingRouter router;
    private final Tracker tracker;
    private final FormatterUtils formatterUtils;

    @Inject
    HomeComingPresenterImpl(HomeComingView view,
                            HomeComingInteractor interactor,
                            HomeComingRouter router,
                            Tracker tracker,
                            FormatterUtils formatter) {
        this.view = view;
        this.interactor = interactor;
        this.router = router;
        this.tracker = tracker;
        this.formatterUtils = formatter;
    }

    @Override
    public void handleOnCreate() {
        TimeTravelResult result = interactor.getResult();
        if (result != null) {
            view.initViews(new HomeComingView.ViewListener() {
                @Override
                public void onStartOver() {
                    router.openTimeTravelActivity();
                    tracker.trackUserStartsOver();
                }

                @Override
                public void onShareWithFriends() {
                    String textToShare = interactor.createShareText(result);
                    router.shareWithFriends(textToShare);
                    tracker.trackUserSharesWithFriends();
                }

                @Override
                public void onShareOnFacebook() {
                    String googlePlayLink = interactor.createGooglePlayLink();
                    router.shareToFaceBook(googlePlayLink);
                    tracker.trackUserSharesOnFb();
                }

                @Override
                public void onShareOnTwitter() {
                    String textToShare = interactor.createShareText(result);
                    router.shareToTwitter(textToShare);
                    tracker.trackUserSharesOnTwitter();
                }

                @Override
                public void onCopyWalletAddress() {
                    interactor.copyWalletAddressToClipboard();
                    view.showWalletCopiedToast();
                    tracker.trackUserCopiesBtcWalletAddress();
                }
            });

            if (result.getEventType().equals(TimeTravelMachine.EventType.NO_EVENT)) {
                switch (result.getStatus()) {
                    case AM_I_A_MAGICIAN_TO_KNOW:
                        view.hideShareView();
                        view.hideParamInfo();
                        view.hideProfitView();
                        view.showAmIMagicianToKnowText();
                        view.hideDonateView();
                        tracker.trackUserGetsToMagician();
                        loadAds();
                        break;
                    case NOT_BORN:
                        view.hideShareView();
                        view.hideParamInfo();
                        view.hideProfitView();
                        view.showNotBornText();
                        view.hideDonateView();
                        tracker.trackUserGetsToNotBorn();
                        loadAds();
                        break;
                    case EXIST:
                    default:
                        Double profitValue = result.getProfit();
                        String profit = formatterUtils.formatPriceAsOnlyDigits(profitValue);
                        String amount = formatterUtils.formatPriceAsOnlyDigits(result.getInvestedMoney());
                        Date date = result.getTimeToTravel();
                        view.setDisplayValues(profit, amount, formatterUtils.formatMonth(date), formatterUtils.formatYear(date));
                        view.showShareView();
                        view.showParamInfo();
                        view.showProfitView();
                        view.hideInfoView();
                        view.hideDonateView();
                        tracker.trackUserGetsToExist();
                        loadAds();
                        break;
                }
            } else {
                switch (result.getEventType()) {
                    case HELLO_SATOSHI:
                        view.hideShareView();
                        view.hideParamInfo();
                        view.hideProfitView();
                        view.showHelloSatoshiText();
                        view.showDonateView();
                        tracker.trackUserGetsToSatoshi();
                        break;
                    case PIZZA_LOVER:
                        view.hideShareView();
                        view.hideParamInfo();
                        view.hideProfitView();
                        view.showPizzaLoverText();
                        view.hideDonateView();
                        tracker.trackUserGetsToPizzaLover();
                        loadAds();
                        break;
                    default:
                    case BASICALLY_NOTHING:
                        view.hideShareView();
                        view.hideParamInfo();
                        view.hideProfitView();
                        view.showBasicallyNothingText();
                        view.hideDonateView();
                        tracker.trackUserGetsToBasicallyNothing();
                        loadAds();
                        break;
                }
            }
        }
    }

    private void loadAds() {
        if (interactor.isAdsEnabled()) {
            view.loadAds();
        }
    }
}
