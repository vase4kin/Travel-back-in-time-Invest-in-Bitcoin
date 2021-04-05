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

package com.travelbackintime.buybitcoin.timetravel.dagger

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.travelbackintime.buybitcoin.timetravel.router.TimeTravelRouter
import com.travelbackintime.buybitcoin.timetravel.router.TimeTravelRouterImpl
import com.travelbackintime.buybitcoin.timetravel.view.TimeTravelFragment

import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class TimeTravelFragmentModule {

    @Binds
    abstract fun providesRouter(router: TimeTravelRouterImpl): TimeTravelRouter

    companion object {
        @Provides
        fun providesScope(fragment: TimeTravelFragment): LifecycleCoroutineScope {
            return fragment.viewLifecycleOwner.lifecycleScope
        }
    }
}
