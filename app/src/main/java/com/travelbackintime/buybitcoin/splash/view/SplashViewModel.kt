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

package com.travelbackintime.buybitcoin.splash.view

import android.databinding.ObservableBoolean
import android.os.Handler
import android.os.Looper
import com.travelbackintime.buybitcoin.splash.router.SplashRouter
import com.travelbackintime.buybitcoin.time.TimeTravelManager
import com.travelbackintime.buybitcoin.tracker.Tracker
import javax.inject.Inject

private const val TIMEOUT_SPLASH: Long = 2500

class SplashViewModel @Inject constructor(
        private val timeTravelManager: TimeTravelManager,
        private val tracker: Tracker,
        private val router: SplashRouter) {

    val isRetryVisible = ObservableBoolean(false)

    fun onCreate() {
        Handler(Looper.getMainLooper()).postDelayed({ initFlowCapacitor() }, TIMEOUT_SPLASH)
    }

    fun onRetryButtonClick() {
        isRetryVisible.set(false)
        initFlowCapacitor()
    }

    private fun initFlowCapacitor() {
        timeTravelManager.initFlowCapacitor(object : TimeTravelManager.FlowCapacitorInitListener {
            override fun onSuccess() {
                tracker.trackDataDownloadedSuccessfully()
                router.openTimeTravelActivity()
            }

            override fun onError() {
                tracker.trackDataDownloadedError()
                isRetryVisible.set(true)
            }

            override fun onDataNotDownloaded() {
                tracker.trackDataNotDownloaded()
                isRetryVisible.set(false)
            }
        })
    }

}