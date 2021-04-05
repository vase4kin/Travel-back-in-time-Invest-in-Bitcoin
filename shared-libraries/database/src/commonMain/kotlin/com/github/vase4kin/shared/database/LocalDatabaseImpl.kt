package com.github.vase4kin.shared.database

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
