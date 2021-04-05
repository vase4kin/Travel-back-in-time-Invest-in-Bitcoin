package com.github.vase4kin.shared.database

interface LocalDatabase {
    /**
     * Get time travel event by date
     *
     * @param date - the date, format is yyyy-MM-dd
     */
    suspend fun getTimeEvent(date: String): TimeTravelEvent

    sealed class TimeTravelEvent {
        data class Event(
            val title: String,
            val description: String,
            val isDonate: Boolean
        ) : TimeTravelEvent()

        object NoEvent : TimeTravelEvent()
    }
}
