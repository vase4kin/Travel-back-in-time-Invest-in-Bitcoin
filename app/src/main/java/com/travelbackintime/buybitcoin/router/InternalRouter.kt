package com.travelbackintime.buybitcoin.router

import bitcoin.backintime.com.backintimebuybitcoin.R
import com.github.vase4kin.timetravelmachine.TimeTravelMachine
import com.travelbackintime.buybitcoin.error.view.ErrorFragment
import com.travelbackintime.buybitcoin.homecoming.view.HomeComingFragment
import com.travelbackintime.buybitcoin.loading.LoadingFragment
import com.travelbackintime.buybitcoin.timetravel.view.TimeTravelActivity
import com.travelbackintime.buybitcoin.timetravel.view.TimeTravelFragment
import com.travelbackintime.buybitcoin.utils.addFragmentFadeTransitions
import com.travelbackintime.buybitcoin.utils.addFragmentSlideTransitions

interface InternalRouter {
    fun openTimeTravelFromSplash()
    fun openError()
    fun closeError()
    fun openLoading(event: TimeTravelMachine.Event)
    fun openHomeComingFromLoading(event: TimeTravelMachine.Event)
    fun openTimeTravelFromHomeComing()
}

class InternalRouterImpl(
    private val activity: TimeTravelActivity
) : InternalRouter {
    override fun openTimeTravelFromSplash() {
        val fragment = TimeTravelFragment.create()
        addFragmentFadeTransitions(fragment, activity.applicationContext)
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commitAllowingStateLoss()
    }

    override fun openError() {
        val fragment = ErrorFragment.create()
        addFragmentSlideTransitions(fragment, activity.applicationContext)
        activity.supportFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    override fun closeError() {
        activity.supportFragmentManager.popBackStack()
    }

    override fun openLoading(event: TimeTravelMachine.Event) {
        val fragment = LoadingFragment.create(event)
        addFragmentSlideTransitions(fragment, activity.applicationContext)
        activity.supportFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    override fun openHomeComingFromLoading(event: TimeTravelMachine.Event) {
        val homeComingFragment = HomeComingFragment.create(event)
        addFragmentSlideTransitions(homeComingFragment, activity.applicationContext)
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.popBackStack()
        fragmentManager
                .beginTransaction()
                .add(R.id.container, homeComingFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    override fun openTimeTravelFromHomeComing() {
        val fragmentManager = activity.supportFragmentManager
        fragmentManager.popBackStack()
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, TimeTravelFragment.create())
                .commitAllowingStateLoss()
    }
}
