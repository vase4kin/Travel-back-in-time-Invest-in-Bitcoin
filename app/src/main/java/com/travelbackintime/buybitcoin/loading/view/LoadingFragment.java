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

package com.travelbackintime.buybitcoin.loading.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travelbackintime.buybitcoin.loading.presenter.LoadingPresenterImpl;
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult;

import javax.inject.Inject;

import bitcoin.backintime.com.backintimebuybitcoin.R;
import dagger.android.support.DaggerFragment;

import static com.travelbackintime.buybitcoin.home_coming.view.HomeComingFragment.EXTRA_RESULT;

public class LoadingFragment extends DaggerFragment {

    @Inject
    LoadingPresenterImpl presenter;

    public LoadingFragment() {
    }

    public static Fragment createInstance(TimeTravelResult result) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RESULT, result);
        LoadingFragment loadingFragment = new LoadingFragment();
        loadingFragment.setArguments(bundle);
        return loadingFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.handleOnCreate();
    }

}
