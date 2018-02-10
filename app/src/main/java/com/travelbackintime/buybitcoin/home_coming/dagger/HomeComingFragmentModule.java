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

package com.travelbackintime.buybitcoin.home_coming.dagger;

import com.travelbackintime.buybitcoin.home_coming.interactor.HomeComingInteractor;
import com.travelbackintime.buybitcoin.home_coming.interactor.HomeComingInteractorImpl;
import com.travelbackintime.buybitcoin.home_coming.router.HomeComingRouter;
import com.travelbackintime.buybitcoin.home_coming.router.HomeComingRouterImpl;
import com.travelbackintime.buybitcoin.home_coming.view.HomeComingView;
import com.travelbackintime.buybitcoin.home_coming.view.HomeComingViewImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class HomeComingFragmentModule {

    @Binds
    abstract HomeComingView providesView(HomeComingViewImpl view);

    @Binds
    abstract HomeComingInteractor providesInteractor(HomeComingInteractorImpl interactor);

    @Binds
    abstract HomeComingRouter providesRouter(HomeComingRouterImpl router);
}
