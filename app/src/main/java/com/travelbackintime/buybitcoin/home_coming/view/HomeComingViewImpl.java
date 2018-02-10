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

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import javax.inject.Inject;

import bitcoin.backintime.com.backintimebuybitcoin.R;

public class HomeComingViewImpl implements HomeComingView {

    private final HomeComingFragment fragment;

    private View paramView;
    private View profitView;
    private TextView info;
    private View shareView;
    private AdView adView;
    private View donateView;

    @Inject
    HomeComingViewImpl(HomeComingFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void initViews(ViewListener listener) {
        paramView = fragment.getView().findViewById(R.id.paramView);
        profitView = fragment.getView().findViewById(R.id.profitView);
        info = fragment.getView().findViewById(R.id.info);
        shareView = fragment.getView().findViewById(R.id.share_view);
        adView = fragment.getView().findViewById(R.id.adView);
        donateView = fragment.getView().findViewById(R.id.donate_view);
        fragment.getView().findViewById(R.id.button_donate).setOnClickListener(v -> listener.onCopyWalletAddress());
        fragment.getView().findViewById(R.id.button_start_over).setOnClickListener(v -> listener.onStartOver());
        fragment.getView().findViewById(R.id.button_share).setOnClickListener(v -> listener.onShareWithFriends());
        fragment.getView().findViewById(R.id.button_share_twitter).setOnClickListener(v -> listener.onShareOnTwitter());
        ImageButton shareButton = fragment.getView().findViewById(R.id.button_share_facebook);
        shareButton.setOnClickListener(v -> listener.onShareOnFacebook());
    }

    @Override
    public void showShareView() {
        shareView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideShareView() {
        shareView.setVisibility(View.GONE);
    }

    @Override
    public void showParamInfo() {
        paramView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideParamInfo() {
        paramView.setVisibility(View.GONE);
    }

    @Override
    public void showProfitView() {
        profitView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProfitView() {
        profitView.setVisibility(View.GONE);
    }

    @Override
    public void showAmIMagicianToKnowText() {
        showInfoView(R.string.result_title_1);
    }


    @Override
    public void showNotBornText() {
        showInfoView(R.string.result_title_2);
    }

    @Override
    public void showBasicallyNothingText() {
        showInfoView(R.string.result_title_3);
    }

    @Override
    public void showHelloSatoshiText() {
        showInfoView(R.string.result_title_4);
    }

    @Override
    public void showPizzaLoverText() {
        showInfoView(R.string.result_title_5);
    }

    @Override
    public void hideInfoView() {
        info.setVisibility(View.GONE);
    }

    @Override
    public void loadAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDonateView() {
        donateView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDonateView() {
        donateView.setVisibility(View.GONE);
    }

    @Override
    public void showWalletCopiedToast() {
        Toast.makeText(fragment.getContext(), fragment.getString(R.string.donate_toast), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setDisplayValues(String profit, String amount, String month, String year) {
        ((TextView) fragment.getView().findViewById(R.id.profitValue)).setText(profit);
        ((TextView) fragment.getView().findViewById(R.id.amount)).setText(amount);
        ((TextView) fragment.getView().findViewById(R.id.month)).setText(month);
        ((TextView) fragment.getView().findViewById(R.id.year)).setText(year);
    }

    private void showInfoView(@StringRes int text) {
        info.setText(text);
        info.setVisibility(View.VISIBLE);
    }
}
