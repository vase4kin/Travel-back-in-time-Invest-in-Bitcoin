package com.travelbackintime.buybitcoin.impl

import com.github.vase4kin.crashlytics.Crashlytics
import com.github.vase4kin.shared.database.LocalDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

private const val REF_EVENTS = "events"

class LocalDatabaseImpl(
    private val database: com.google.firebase.database.FirebaseDatabase,
    private val crashlytics: Crashlytics
) : LocalDatabase {

    private var timeTravelEvents: Map<String, TimeTravelEvent> = HashMap()

    init {
        fetchData()
    }

    override suspend fun getTimeEvent(date: String): LocalDatabase.TimeTravelEvent {
        val event = timeTravelEvents[date]
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

    private fun fetchData() {
        database.reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val timeEventsGenericTypeIndicator = object :
                    GenericTypeIndicator<Map<@JvmSuppressWildcards String, @JvmSuppressWildcards TimeTravelEvent>>() {}
                timeTravelEvents =
                    dataSnapshot.child(REF_EVENTS).getValue(timeEventsGenericTypeIndicator)
                        ?: HashMap()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                crashlytics.recordException(databaseError.toException())
            }
        })
    }
}
