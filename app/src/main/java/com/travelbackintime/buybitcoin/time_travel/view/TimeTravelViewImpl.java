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

package com.travelbackintime.buybitcoin.time_travel.view;

import android.widget.Button;

import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import bitcoin.backintime.com.backintimebuybitcoin.R;

public class TimeTravelViewImpl implements TimeTravelView {

    private final TimeTravelFragment fragment;
    private ViewListener listener;

    @Inject
    TimeTravelViewImpl(TimeTravelFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void initViews(ViewListener listener) {
        this.listener = listener;
        fragment.getView().findViewById(R.id.button_buy_bitcoin).setOnClickListener(v -> listener.onBuyBitcoinButtonClick());

        fragment.getView().findViewById(R.id.button_set_amount).setOnClickListener(v -> listener.onSetAmountButtonClick());

        fragment.getView().findViewById(R.id.button_set_date).setOnClickListener(v -> listener.onSetDateButtonClick());
    }

    @Override
    public void showAmountDialog() {
        SetAmountBottomSheetDialog dialog = new SetAmountBottomSheetDialog();
        dialog.setListener(this);
        dialog.show(fragment.getActivity().getSupportFragmentManager(), SetAmountBottomSheetDialog.class.getName());
    }

    @Override
    public void showSetDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog.Builder(
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .setThemeDark(true)
                .build();
        dateDialog.show(fragment.getActivity().getSupportFragmentManager(), DatePickerDialog.class.getName());
    }

    @Override
    public void enableBuyBitcoinButton() {
        fragment.getView().findViewById(R.id.button_buy_bitcoin).setEnabled(true);
        fragment.getView().findViewById(R.id.button_buy_bitcoin).setAlpha(1);
    }

    @Override
    public void setDate(String date) {
        ((Button) fragment.getView().findViewById(R.id.button_set_date)).setText(date);
    }

    @Override
    public void setAmount(String amount) {
        ((Button) fragment.getView().findViewById(R.id.button_set_amount)).setText(amount);
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar timeToTravel = Calendar.getInstance(Locale.US);
        timeToTravel.clear();
        timeToTravel.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
        listener.onDateSet(timeToTravel.getTime());
    }

    @Override
    public void onAmountSet(Double amount) {
        listener.onAmountSet(amount);
    }
}
