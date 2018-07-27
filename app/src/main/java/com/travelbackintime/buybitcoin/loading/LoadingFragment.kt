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

package com.travelbackintime.buybitcoin.loading


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bitcoin.backintime.com.backintimebuybitcoin.R
import com.crashlytics.android.Crashlytics
import com.travelbackintime.buybitcoin.home_coming.view.HomeComingFragment
import com.travelbackintime.buybitcoin.home_coming.view.HomeComingFragment.EXTRA_RESULT
import com.travelbackintime.buybitcoin.time_travel.entity.TimeTravelResult
import com.travelbackintime.buybitcoin.utils.addTransitions
import pl.droidsonroids.gif.GifDrawable

private const val LOOP_COUNT = 1
private const val SPEED: Float = 0.8f

fun createLoadingFragment(result: TimeTravelResult): Fragment {
    val bundle = Bundle()
    bundle.putParcelable(EXTRA_RESULT, result)
    val loadingFragment = LoadingFragment()
    loadingFragment.arguments = bundle
    return loadingFragment
}

class LoadingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
        } catch (e: Exception) {
            Crashlytics.logException(e)
            openHomecoming()
        }
    }

    private fun openHomecoming() {
        val args = arguments
        if (args != null) {
            val result: TimeTravelResult = args.getParcelable(EXTRA_RESULT)
            val homeComingFragment = HomeComingFragment.createInstance(result)
            val activity = activity as AppCompatActivity
            addTransitions(homeComingFragment, activity.applicationContext)
            val fragmentManager = activity.supportFragmentManager
            fragmentManager
                    .beginTransaction()
                    .remove(this)
                    .commit()
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container, homeComingFragment)
                    .addToBackStack(null)
                    .commit()
        }
    }
}
