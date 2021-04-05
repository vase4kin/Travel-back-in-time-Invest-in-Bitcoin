/*
 * Copyright 2021  Andrey Tolpeev
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

package com.travelbackintime.buybitcoin.impl

import com.github.vase4kin.coindesk.remoteconfig.RemoteConfigService
import com.github.vase4kin.crashlytics.Crashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class RemoteConfigServiceImpl(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val crashlytics: Crashlytics,
    private val cacheSecs: Long
) : RemoteConfigService {

    override val isAdsEnabled: Boolean
        get() = firebaseRemoteConfig.getBoolean(RemoteConfigService.CONFIG_VALUE_ADS_ENABLED)

    init {
        fetch()
    }

    private fun fetch() {
        firebaseRemoteConfig.fetch(cacheSecs).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseRemoteConfig.activate()
            } else {
                task.exception?.let {
                    crashlytics.recordException(it)
                }
            }
        }
    }
}
