/*
 * Copyright 2021  Andrey Tolpeev
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

package com.travelbackintime.buybitcoin.ui.timetravel.dagger

import com.travelbackintime.buybitcoin.ui.error.dagger.ErrorModule
import com.travelbackintime.buybitcoin.ui.error.view.ErrorFragment
import com.travelbackintime.buybitcoin.ui.homecoming.dagger.HomeComingFragmentModule
import com.travelbackintime.buybitcoin.ui.homecoming.view.HomeComingFragment
import com.travelbackintime.buybitcoin.ui.loading.LoadingFragment
import com.travelbackintime.buybitcoin.ui.splash.dagger.SplashModule
import com.travelbackintime.buybitcoin.ui.splash.view.SplashFragment
import com.travelbackintime.buybitcoin.ui.timetravel.view.InvestMoneyBottomSheetDialog
import com.travelbackintime.buybitcoin.ui.timetravel.view.TimeTravelFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class TimeTravelActivityBindingModule {

    @ContributesAndroidInjector(modules = [SplashModule::class])
    abstract fun splashFragment(): SplashFragment

    @ContributesAndroidInjector(modules = [TimeTravelFragmentModule::class])
    abstract fun timeTravelFragment(): TimeTravelFragment

    @ContributesAndroidInjector(modules = [HomeComingFragmentModule::class])
    abstract fun homeComingFragment(): HomeComingFragment

    @ContributesAndroidInjector
    abstract fun setAmountBottomSheetDialogFragment(): InvestMoneyBottomSheetDialog

    @ContributesAndroidInjector(modules = [ErrorModule::class])
    abstract fun errorFragment(): ErrorFragment

    @ContributesAndroidInjector
    abstract fun loadingFragment(): LoadingFragment
}
