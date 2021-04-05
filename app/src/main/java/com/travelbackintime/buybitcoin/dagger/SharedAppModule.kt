package com.travelbackintime.buybitcoin.dagger

import android.app.Application
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.shared.coindesk.service.CoinDeskService
import com.github.vase4kin.shared.coindesk.service.CoinDeskServiceImpl
import com.github.vase4kin.shared.repository.Repository
import com.github.vase4kin.shared.repository.RepositoryImpl
import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachine
import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachineImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object SharedAppModule {

    @Singleton
    @Provides
    fun providesTimeTravelMachine(
        repository: Repository,
        app: Application
    ): TimeTravelMachine {
        return TimeTravelMachineImpl(
            repository = repository,
            eventWithNoPrice = TimeTravelMachine.Event.RealWorldEvent(
                title = app.resources.getString(R.string.text_oops),
                description = app.resources.getString(R.string.text_basically_nothing),
                isDonate = false
            )
        )
    }

    @Provides
    fun provideRepository(
        service: CoinDeskService
    ): Repository {
        return RepositoryImpl(service)
    }

    @Provides
    fun provideCoindeskService(): CoinDeskService {
        return CoinDeskServiceImpl()
    }
}
