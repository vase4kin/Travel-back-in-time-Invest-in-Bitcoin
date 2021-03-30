package com.travelbackintime.buybitcoin.timetravel.dagger

import com.travelbackintime.buybitcoin.router.InternalRouter
import com.travelbackintime.buybitcoin.router.InternalRouterImpl
import com.travelbackintime.buybitcoin.timetravel.view.TimeTravelActivity
import dagger.Module
import dagger.Provides

@Module
class TimeTravelActivityModule {

    @Provides
    fun provideInternalRouter(activity: TimeTravelActivity): InternalRouter {
        return InternalRouterImpl(activity)
    }
}
