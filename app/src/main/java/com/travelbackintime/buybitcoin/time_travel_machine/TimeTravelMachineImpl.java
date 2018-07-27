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

package com.travelbackintime.buybitcoin.time_travel_machine;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.travelbackintime.buybitcoin.time_travel_machine.model.TimeTravelEvent;
import com.travelbackintime.buybitcoin.time_travel_machine.model.TimeTravelInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TimeTravelMachineImpl implements TimeTravelMachine {

    private static final String REF_CONNECTION = ".info/connected";
    private static final String REF_TIME = "time";
    private static final String REF_EVENTS = "events";
    private static final String KEY_DATA_DOWNLOADED = "key_data_downloaded";
    private static final String PATTERN_TIME_DATE = "yyyy-MM";
    private static final String PATTERN_TIME_EVENT_DATE = "yyyy-MM-dd";
    private static final String DATE_FIRST = "2009-01";

    private final FirebaseDatabase database;
    private final SharedPreferences sharedPreferences;

    private Map<String, TimeTravelInfo> timeInfos = new HashMap<>();
    private Map<String, TimeTravelEvent> timeEvents = new HashMap<>();

    public TimeTravelMachineImpl(FirebaseDatabase database, SharedPreferences sharedPreferences) {
        this.database = database;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void initFlowCapacitor(@NonNull final FlowCapacitorInitListener listener) {
        database.getReference(REF_CONNECTION).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected) {
                    fetchData(listener);
                    setDataDownloaded();
                } else {
                    if (isDataDownloaded()) {
                        fetchData(listener);
                    } else {
                        listener.onDataNotDownloaded();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Crashlytics.logException(databaseError.toException());
                listener.onError();
            }
        });
    }

    @Override
    public double getBitcoinPrice(@Nullable final Date date) {
        if (date == null) return Double.NaN;
        String serverDate = convertDateToServerDate(date);
        return getBitcoinPrice(serverDate);
    }

    @Override
    public double getBitcoinCurrentPrice() {
        String todayDate = convertDateToServerDate(new Date());
        return getBitcoinPrice(todayDate);
    }

    private double getBitcoinPrice(String serverDate) {
        TimeTravelInfo timeTravelInfo = timeInfos.get(serverDate);
        if (timeTravelInfo != null) {
            return timeTravelInfo.getPrice();
        } else {
            return Double.NaN;
        }
    }

    @Override
    public BitcoinStatus getBitcoinStatus(@Nullable Date date) {
        // TODO: Return error here
        if (date == null) return BitcoinStatus.EXIST;
        Date dateFirst = parseServerDateToDate(DATE_FIRST);
        if (dateFirst.after(date)) {
            return BitcoinStatus.NOT_BORN;
        }
        if (new Date().before(date)) {
            return BitcoinStatus.AM_I_A_MAGICIAN_TO_KNOW;
        }
        return BitcoinStatus.EXIST;
    }

    @Override
    public TimeTravelEvent getTimeEvent(@Nullable Date date) {
        if (date == null) return new TimeTravelEvent(EventType.NO_EVENT.name());
        String eventServerDate = convertDateToEventServerDate(date);
        TimeTravelEvent timeTravelEvent = timeEvents.get(eventServerDate);
        return timeTravelEvent != null ? timeTravelEvent : new TimeTravelEvent(EventType.NO_EVENT.name());
    }

    private boolean isDataDownloaded() {
        return sharedPreferences.getBoolean(KEY_DATA_DOWNLOADED, false);
    }

    private void setDataDownloaded() {
        sharedPreferences.edit().putBoolean(KEY_DATA_DOWNLOADED, true).apply();
    }

    private void fetchData(final FlowCapacitorInitListener listener) {
        database.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, TimeTravelInfo>> timeGenericTypeIndicator = new GenericTypeIndicator<Map<String, TimeTravelInfo>>() {
                };
                timeInfos = dataSnapshot.child(REF_TIME).getValue(timeGenericTypeIndicator);
                GenericTypeIndicator<Map<String, TimeTravelEvent>> timeEventsGenericTypeIndicator = new GenericTypeIndicator<Map<String, TimeTravelEvent>>() {
                };
                timeEvents = dataSnapshot.child(REF_EVENTS).getValue(timeEventsGenericTypeIndicator);
                listener.onSuccess();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Crashlytics.logException(databaseError.toException());
                listener.onError();
            }
        });
    }

    private String convertDateToServerDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(PATTERN_TIME_DATE, Locale.US);
        return dateFormat.format(date);
    }

    private String convertDateToEventServerDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(PATTERN_TIME_EVENT_DATE, Locale.US);
        return dateFormat.format(date);
    }

    private Date parseServerDateToDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat(PATTERN_TIME_DATE, Locale.US);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
