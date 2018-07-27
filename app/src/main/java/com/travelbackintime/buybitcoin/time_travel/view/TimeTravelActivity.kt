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

package com.travelbackintime.buybitcoin.time_travel.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import bitcoin.backintime.com.backintimebuybitcoin.R
import dagger.android.support.DaggerAppCompatActivity

fun startTimeTravelActivity(activity: Activity) {
    val intent = Intent(activity, TimeTravelActivity::class.java)
    activity.startActivity(intent)
}

class TimeTravelActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_travel)
        supportFragmentManager
                .beginTransaction()
                .add(R.id.container, createTimeTravelFragment())
                .commit()
    }
}
