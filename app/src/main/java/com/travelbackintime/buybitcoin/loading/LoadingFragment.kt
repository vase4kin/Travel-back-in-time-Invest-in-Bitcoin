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

package com.travelbackintime.buybitcoin.loading

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.crashlytics.Crashlytics
import com.github.vase4kin.shared.timetravelmachine.TimeTravelMachine
import com.travelbackintime.buybitcoin.homecoming.view.EXTRA_RESULT
import com.travelbackintime.buybitcoin.impl.shared.TimeTravelEvenWrapper
import com.travelbackintime.buybitcoin.impl.shared.wrap
import com.travelbackintime.buybitcoin.router.InternalRouter
import dagger.android.support.DaggerFragment
import pl.droidsonroids.gif.GifDrawable
import java.io.IOException
import javax.inject.Inject

private const val LOOP_COUNT = 1
private const val SPEED: Float = 0.8f

class LoadingFragment : DaggerFragment() {

    companion object {
        fun create(event: TimeTravelMachine.Event): Fragment {
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_RESULT, event.wrap())
            val loadingFragment = LoadingFragment()
            loadingFragment.arguments = bundle
            return loadingFragment
        }
    }

    @Inject
    lateinit var internalRouter: InternalRouter

    @Inject
    lateinit var crashlytics: Crashlytics

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadGif(view)
    }

    private fun loadGif(view: View) {
        try {
            val gifFromResource = GifDrawable(resources, R.drawable.car_animation)
            gifFromResource.loopCount = LOOP_COUNT
            gifFromResource.setSpeed(SPEED)
            view.findViewById<View>(R.id.image_view).background = gifFromResource
            gifFromResource.addAnimationListener { openHomecoming() }
        } catch (e: IOException) {
            logExceptionAndOpenHomeComing(e)
        } catch (e: Resources.NotFoundException) {
            logExceptionAndOpenHomeComing(e)
        }
    }

    private fun logExceptionAndOpenHomeComing(e: Exception) {
        crashlytics.recordException(e)
        openHomecoming()
    }

    private fun openHomecoming() {
        val args = arguments
        if (args != null) {
            val event: TimeTravelEvenWrapper = args.getParcelable(EXTRA_RESULT) ?: return
            internalRouter.openHomeComingFromLoading(
                event = event
            )
        }
    }
}
