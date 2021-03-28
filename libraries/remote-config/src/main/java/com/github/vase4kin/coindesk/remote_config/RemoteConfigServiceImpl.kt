package com.github.vase4kin.coindesk.remote_config

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

private const val CONFIG_VALUE_ADS_ENABLED = "ads_enabled"

class RemoteConfigServiceImpl(private val firebaseRemoteConfig: FirebaseRemoteConfig,
                              private val cacheSecs: Long) : RemoteConfigService {

    override val isAdsEnabled: Boolean
        get() = firebaseRemoteConfig.getBoolean(CONFIG_VALUE_ADS_ENABLED)

    init {
        fetch()
    }

    private fun fetch() {
        firebaseRemoteConfig.fetch(cacheSecs).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseRemoteConfig.activate()
            } else {
                task.exception?.let {
                    FirebaseCrashlytics.getInstance().recordException(it)
                }
            }
        }
    }
}