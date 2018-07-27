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

package com.travelbackintime.buybitcoin.time;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.travelbackintime.buybitcoin.time.model.TimeEvent;

import java.util.Date;

public interface TimeTravelManager {

    void initFlowCapacitor(@NonNull FlowCapacitorInitListener listener);

    double getBitcoinPrice(@Nullable Date date);

    double getBitcoinCurrentPrice();

    BitcoinStatus getBitcoinStatus(@Nullable Date date);

    TimeEvent getTimeEvent(@Nullable Date date);

    interface FlowCapacitorInitListener {
        void onSuccess();

        void onError();

        void onDataNotDownloaded();
    }

    enum BitcoinStatus {
        EXIST, NOT_BORN, AM_I_A_MAGICIAN_TO_KNOW
    }

    enum EventType {
        BASICALLY_NOTHING, HELLO_SATOSHI, PIZZA_LOVER, NO_EVENT
    }
}
