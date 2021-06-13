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

package com.travelbackintime.buybitcoin.dagger

import com.travelbackintime.buybitcoin.utils.FormatterUtils
import com.travelbackintime.buybitcoin.utils.FormatterUtilsImpl
import com.travelbackintime.buybitcoin.utils.ResourcesProvider
import com.travelbackintime.buybitcoin.utils.ResourcesProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UtilsModule {

    @Singleton
    @Binds
    fun providesFormatterUtils(impl: FormatterUtilsImpl): FormatterUtils

    @Singleton
    @Binds
    fun providesResourceProvider(impl: ResourcesProviderImpl): ResourcesProvider

    companion object {
        @Singleton
        @Provides
        fun providesNumberFormat(): NumberFormat {
            return NumberFormat.getCurrencyInstance(Locale.US)
        }
    }
}
