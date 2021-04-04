package com.github.vase4kin.crashlytics

interface Crashlytics {
    /**
     * Record exception to crashlytics service impl
     *
     * @param throwable - the throwable to record
     */
    fun recordException(throwable: Throwable)
}
