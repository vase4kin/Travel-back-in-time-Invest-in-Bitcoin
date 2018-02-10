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

package com.travelbackintime.buybitcoin.time_travel.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.travelbackintime.buybitcoin.time.TimeTravelManager;

import java.util.Date;

public class TimeTravelResult implements Parcelable {

    @NonNull
    private final TimeTravelManager.BitcoinStatus status;
    @NonNull
    private final TimeTravelManager.EventType eventType;
    @Nullable
    private final Double profit;
    @Nullable
    private final Double investedAmount;
    @Nullable
    private final Date date;

    public TimeTravelResult(@NonNull TimeTravelManager.BitcoinStatus status,
                            @Nullable Double profit,
                            @Nullable Double investedAmount,
                            @Nullable Date date) {
        this.status = status;
        this.eventType = TimeTravelManager.EventType.NO_EVENT;
        this.profit = profit;
        this.investedAmount = investedAmount;
        this.date = date;
    }

    public TimeTravelResult(@NonNull TimeTravelManager.BitcoinStatus status) {
        this.status = status;
        this.eventType = TimeTravelManager.EventType.NO_EVENT;
        this.profit = null;
        this.investedAmount = null;
        this.date = null;
    }

    public TimeTravelResult(@NonNull TimeTravelManager.EventType eventType) {
        this.status = TimeTravelManager.BitcoinStatus.EXIST;
        this.eventType = eventType;
        this.profit = null;
        this.investedAmount = null;
        this.date = null;
    }

    @NonNull
    public TimeTravelManager.BitcoinStatus getStatus() {
        return status;
    }

    @Nullable
    public Double getProfit() {
        return profit;
    }

    @Nullable
    public Double getInvestedAmount() {
        return investedAmount;
    }

    @Nullable
    public Date getDate() {
        return date;
    }

    @NonNull
    public TimeTravelManager.EventType getEventType() {
        return eventType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
        dest.writeInt(this.eventType == null ? -1 : this.eventType.ordinal());
        dest.writeValue(this.profit);
        dest.writeValue(this.investedAmount);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
    }

    protected TimeTravelResult(Parcel in) {
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : TimeTravelManager.BitcoinStatus.values()[tmpStatus];
        int tmpEventType = in.readInt();
        this.eventType = tmpEventType == -1 ? null : TimeTravelManager.EventType.values()[tmpEventType];
        this.profit = (Double) in.readValue(Double.class.getClassLoader());
        this.investedAmount = (Double) in.readValue(Double.class.getClassLoader());
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<TimeTravelResult> CREATOR = new Creator<TimeTravelResult>() {
        @Override
        public TimeTravelResult createFromParcel(Parcel source) {
            return new TimeTravelResult(source);
        }

        @Override
        public TimeTravelResult[] newArray(int size) {
            return new TimeTravelResult[size];
        }
    };
}
