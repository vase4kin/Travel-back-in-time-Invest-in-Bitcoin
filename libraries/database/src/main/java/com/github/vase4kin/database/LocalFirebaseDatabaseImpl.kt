package com.github.vase4kin.database

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single

private const val REF_EVENTS = "events"

class LocalFirebaseDatabaseImpl(
    private val database: com.google.firebase.database.FirebaseDatabase
) : LocalFirebaseDatabase {

    private var timeTravelEvents: Map<String, TimeTravelEvent> = HashMap()

    init {
        fetchData()
    }

    override fun getTimeEvent(date: String): Single<LocalFirebaseDatabase.TimeTravelEvent> {
        val event = timeTravelEvents[date]
        return if (event == null) {
            Single.just(LocalFirebaseDatabase.TimeTravelEvent.NoEvent)
        } else {
            Single.just(
                LocalFirebaseDatabase.TimeTravelEvent.Event(
                    title = event.title,
                    description = event.description,
                    isDonate = event.isDonate
                )
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
                FirebaseCrashlytics.getInstance().recordException(databaseError.toException())
            }
        })
    }
}
