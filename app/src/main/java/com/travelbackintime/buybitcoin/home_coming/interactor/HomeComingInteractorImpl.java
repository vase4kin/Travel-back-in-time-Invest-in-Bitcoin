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

package com.travelbackintime.buybitcoin.home_coming.interactor;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.travelbackintime.buybitcoin.home_coming.view.HomeComingFragment;
import com.travelbackintime.buybitcoin.remote_config.RemoteConfigService;
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult;
import com.travelbackintime.buybitcoin.utils.FormatterUtils;

import java.util.Date;

import javax.inject.Inject;

import bitcoin.backintime.com.backintimebuybitcoin.R;

import static com.travelbackintime.buybitcoin.home_coming.view.HomeComingFragment.EXTRA_RESULT;

public class HomeComingInteractorImpl implements HomeComingInteractor {

    private final HomeComingFragment fragment;
    private final RemoteConfigService configService;
    private final FormatterUtils formatterUtils;

    @Inject
    HomeComingInteractorImpl(HomeComingFragment fragment, RemoteConfigService configService, FormatterUtils formatter) {
        this.fragment = fragment;
        this.configService = configService;
        this.formatterUtils = formatter;
    }

    @Nullable
    @Override
    public TimeTravelResult getResult() {
        Bundle args = fragment.getArguments();
        if (args != null) {
            return args.getParcelable(EXTRA_RESULT);
        } else {
            return null;
        }
    }

    @Override
    public void copyWalletAddressToClipboard() {
        ClipboardManager clipboard = (ClipboardManager) fragment.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(fragment.getString(R.string.donate_copy_label), fragment.getString(R.string.donate_btc_wallet_address));
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    @Override
    public boolean isAdsEnabled() {
        return configService.isAdsEnabled();
    }

    @Override
    public String createGooglePlayLink() {
        return fragment.getString(R.string.url_google_play, fragment.getActivity().getPackageName());
    }

    @Override
    public String createShareText(TimeTravelResult result) {
        String googlePlayLink = createGooglePlayLink();
        Date date = result.getTimeToTravel();
        Double profitValue = result.getProfit();
        String profit = formatterUtils.formatPrice(profitValue);
        return fragment.getString(R.string.text_share, formatterUtils.formatDateToShareText(date), profit, googlePlayLink);
    }
}
