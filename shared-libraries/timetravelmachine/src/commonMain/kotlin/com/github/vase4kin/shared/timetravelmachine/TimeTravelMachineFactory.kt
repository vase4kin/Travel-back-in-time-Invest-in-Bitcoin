package com.github.vase4kin.shared.timetravelmachine

import com.github.vase4kin.shared.coindesk.service.CoinDeskServiceImpl
import com.github.vase4kin.shared.database.LocalDatabaseImpl
import com.github.vase4kin.shared.repository.RepositoryImpl

object TimeTravelMachineFactory {
    fun create(
        eventWithAbsentPrice: TimeTravelMachine.Event.RealWorldEvent
    ): TimeTravelMachine {
        return TimeTravelMachineImpl(
            repository = RepositoryImpl(
                coinDeskService = CoinDeskServiceImpl(),
                localDatabase = LocalDatabaseImpl()
            ),
            eventWithNoPrice = eventWithAbsentPrice
        )
    }
}
