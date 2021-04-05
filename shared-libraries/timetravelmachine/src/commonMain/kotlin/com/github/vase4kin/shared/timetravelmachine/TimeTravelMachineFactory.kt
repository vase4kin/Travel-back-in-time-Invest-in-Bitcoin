package com.github.vase4kin.shared.timetravelmachine

import com.github.vase4kin.shared.coindesk.service.CoinDeskServiceImpl
import com.github.vase4kin.shared.database.LocalDatabase
import com.github.vase4kin.shared.repository.RepositoryImpl

object TimeTravelMachineFactory {
    fun create(
        localDatabase: LocalDatabase,
        eventWithAbsentPrice: TimeTravelMachine.Event.RealWorldEvent
    ): TimeTravelMachine {
        return TimeTravelMachineImpl(
            repository = RepositoryImpl(
                coinDeskService = CoinDeskServiceImpl(),
                localDatabase = localDatabase
            ),
            eventWithNoPrice = eventWithAbsentPrice
        )
    }
}