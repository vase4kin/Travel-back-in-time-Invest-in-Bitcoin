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

package com.github.vase4kin.shared.database

import com.github.vase4kin.shared.database.models.TimeTravelEvent
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private const val REF_EVENTS = "events"

class LocalDatabaseImpl : LocalDatabase {
    private val database: FirebaseDatabase = Firebase.database.apply {
        setLoggingEnabled(true)
        setPersistenceEnabled(true)
    }

    private fun eventsFlow(): Flow<Map<String, TimeTravelEvent>> =
        database
            .reference("")
            .valueEvents
            .map {
                if (it.exists) {
                    it.child(REF_EVENTS).value()
                } else {
                    emptyMap()
                }
            }

    override suspend fun getTimeEvent(date: String): LocalDatabase.TimeTravelEvent {
        val event = eventsFlow().firstOrNull()?.get(date)
        return if (event == null) {
            LocalDatabase.TimeTravelEvent.NoEvent
        } else {
            LocalDatabase.TimeTravelEvent.Event(
                title = event.title,
                description = event.description,
                isDonate = event.isDonate
            )
        }
    }
}
