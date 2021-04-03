package com.github.vase4kin.database

import io.reactivex.Single

interface LocalFirebaseDatabase {
    /**
     * Get time travel event by date
     * date format is yyyy-MM-dd
     */
    fun getTimeEvent(date: String): Single<TimeTravelEvent>

    sealed class TimeTravelEvent {
        data class Event(
            val title: String,
            val description: String,
            val isDonate: Boolean
        ) : TimeTravelEvent()

        object NoEvent : TimeTravelEvent()
    }
}
