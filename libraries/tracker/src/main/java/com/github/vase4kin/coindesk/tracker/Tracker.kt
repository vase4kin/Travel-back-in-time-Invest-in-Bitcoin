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

package com.github.vase4kin.coindesk.tracker

import java.util.*

interface Tracker {

    fun trackUserSetsTime(time: String)

    fun trackUserSetsMoney(money: String)

    fun trackUserTravelsBackAndBuys()

    fun trackUserSeesEmptyAmountError()

    fun trackUserSeesAtLeastDollarError()

    fun trackUserSeesYouReachError()

    fun trackUserStartsOver()

    fun trackUserSharesWithFriends()

    fun trackUserCopiesBtcWalletAddress()

    fun trackUserGetsToRealWorldEvent(eventName: String)

    fun trackUserGetsToTimeTravelEvent(profitMoney: Double,
                                       investedMoney: Double,
                                       timeToTravel: Date)

    fun trackUserSharesOnFb()

    fun trackUserSharesOnTwitter()

    fun trackUserRetries()

    fun trackUserChooseMoneySuggestion(amount: String)
}