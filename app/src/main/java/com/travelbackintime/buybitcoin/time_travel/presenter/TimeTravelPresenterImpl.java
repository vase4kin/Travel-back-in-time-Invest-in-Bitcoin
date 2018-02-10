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

package com.travelbackintime.buybitcoin.time_travel.presenter;

import com.travelbackintime.buybitcoin.format.Formatter;
import com.travelbackintime.buybitcoin.time.TimeTravelManager;
import com.travelbackintime.buybitcoin.time.model.TimeEvent;
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult;
import com.travelbackintime.buybitcoin.time_travel.router.TimeTravelRouter;
import com.travelbackintime.buybitcoin.time_travel.view.TimeTravelView;
import com.travelbackintime.buybitcoin.tracker.Tracker;

import java.util.Date;

import javax.inject.Inject;

public class TimeTravelPresenterImpl implements TimeTravelPresenter, TimeTravelView.ViewListener {

    private final Tracker tracker;
    private final Formatter formatter;
    private final TimeTravelManager timeTravelManager;
    private final TimeTravelView view;
    private final TimeTravelRouter router;

    private Double investedAmount;
    private Date dateToTravel;

    @Inject
    TimeTravelPresenterImpl(Tracker tracker,
                            Formatter formatter,
                            TimeTravelManager timeTravelManager,
                            TimeTravelView view,
                            TimeTravelRouter router) {
        this.tracker = tracker;
        this.formatter = formatter;
        this.timeTravelManager = timeTravelManager;
        this.view = view;
        this.router = router;
    }

    @Override
    public void handleOnCreate() {
        view.initViews(this);
    }

    @Override
    public void onBuyBitcoinButtonClick() {
        tracker.trackUserTravelsBackAndBuys();
        processData();
    }

    @Override
    public void onSetAmountButtonClick() {
        view.showAmountDialog();
    }

    @Override
    public void onSetDateButtonClick() {
        view.showSetDateDialog();
    }

    @Override
    public void onDateSet(Date date) {
        this.dateToTravel = date;
        String formattedDate = formatter.formatDate(date);
        tracker.trackUserSetsTime(formattedDate);
        view.setDate(formattedDate);
        enableBuyBitcoinButton();
    }

    @Override
    public void onAmountSet(Double amount) {
        this.investedAmount = amount;
        String formattedAmount = formatter.formatPrice(amount);
        tracker.trackUserSetsMoney(formattedAmount);
        view.setAmount(formattedAmount);
        enableBuyBitcoinButton();
    }

    private void enableBuyBitcoinButton() {
        if (dateToTravel != null && investedAmount != null) {
            view.enableBuyBitcoinButton();
        }
    }

    private void processData() {
        TimeEvent event = timeTravelManager.getTimeEvent(dateToTravel);
        if (event.getType().equals(TimeTravelManager.EventType.NO_EVENT)) {
            TimeTravelManager.BitcoinStatus status = timeTravelManager.getBitcoinStatus(dateToTravel);
            switch (status) {
                case EXIST:
                    double profit = calculateProfit();
                    TimeTravelResult timeTravelResult = new TimeTravelResult(status, profit, investedAmount, dateToTravel);
                    router.openLoadingActivity(timeTravelResult);
                    break;
                default:
                    router.openLoadingActivity(new TimeTravelResult(status));
                    break;
            }
        } else {
            router.openLoadingActivity(new TimeTravelResult(event.getType()));
        }
    }

    private double calculateProfit() {
        double pricePerBitcoinInThePast = timeTravelManager.getBitcoinPrice(dateToTravel);
        double pricePerBitcoinNow = timeTravelManager.getBitcoinCurrentPrice();
        double bitcoinInvestment = investedAmount / pricePerBitcoinInThePast;
        return pricePerBitcoinNow * bitcoinInvestment;
    }
}
