package com.travelbackintime.buybitcoin.ui.timetravel.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.travelbackintime.buybitcoin.ui.timetravel.compose.TimeTravelApp
import com.travelbackintime.buybitcoin.ui.timetravel.compose.TimeTravelViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimeTravelActivity : ComponentActivity() {
    private val viewModel by viewModels<TimeTravelViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.BLACK),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.BLACK),
        )
        setContent {
            TimeTravelApp(viewModel = viewModel)
        }
    }
}
