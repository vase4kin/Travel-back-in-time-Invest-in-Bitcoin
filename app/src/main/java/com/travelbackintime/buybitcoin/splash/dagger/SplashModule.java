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

package com.travelbackintime.buybitcoin.splash.dagger;

import com.travelbackintime.buybitcoin.splash.router.SplashRouter;
import com.travelbackintime.buybitcoin.splash.router.SplashRouterImpl;
import com.travelbackintime.buybitcoin.splash.view.SplashView;
import com.travelbackintime.buybitcoin.splash.view.SplashViewImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SplashModule {

    @Binds
    abstract SplashView providesSplashView(SplashViewImpl view);

    @Binds
    abstract SplashRouter providesSplashRouter(SplashRouterImpl router);
}
