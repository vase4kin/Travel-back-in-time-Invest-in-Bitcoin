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

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.travelbackintime.buybitcoin.tracker.Tracker;

import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;

import bitcoin.backintime.com.backintimebuybitcoin.R;
import dagger.android.support.DaggerAppCompatDialogFragment;

public class SetAmountBottomSheetDialog extends DaggerAppCompatDialogFragment {

    @Inject
    NumberFormat numberFormat;

    @Inject
    Tracker tracker;

    private SetAmountListener listener;
    private int errorRichCount = 0;

    public SetAmountBottomSheetDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext(), getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_amount, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextInputLayout editTextWrapper = view.findViewById(R.id.edit_text_wrapper);

        String hint = getString(R.string.hint_set_amount, numberFormat.getCurrency().getDisplayName(Locale.ENGLISH));
        editTextWrapper.setHint(hint);

        editTextWrapper.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveAmount(editTextWrapper);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                return true;
            }
            return false;
        });

        view.findViewById(R.id.button_set_amount).setOnClickListener(v -> saveAmount(editTextWrapper));

        editTextWrapper.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Window window = getDialog().getWindow();
                if (window != null) {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
    }

    private void saveAmount(TextInputLayout editTextWrapper) {
        editTextWrapper.setError(null);
        String amount = editTextWrapper.getEditText().getText().toString();
        if (!TextUtils.isEmpty(amount)) {
            Double investment = Double.valueOf(amount);
            if (investment.intValue() == 0) {
                editTextWrapper.setError(getString(R.string.error_set_amount_zero, numberFormat.getCurrency().getDisplayName(Locale.ENGLISH)));
                tracker.trackUserSeesAtLeastDollarError();
            } else if (investment.intValue() >= 1000000 && errorRichCount == 0) {
                editTextWrapper.setError(getString(R.string.error_set_amount_rich));
                errorRichCount++;
                tracker.trackUserSeesYouReachError();
            } else {
                listener.onAmountSet(Double.valueOf(amount));
                errorRichCount = 0;
                dismiss();
            }
        } else {
            editTextWrapper.setError(getString(R.string.error_set_amount_empty));
            tracker.trackUserSeesEmptyAmountError();
        }
    }

    public interface SetAmountListener {
        void onAmountSet(Double amount);
    }

    public void setListener(SetAmountListener listener) {
        this.listener = listener;
    }
}
