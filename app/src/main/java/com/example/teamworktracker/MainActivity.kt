package com.example.teamworktracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.teamworktracker.network.ApiClient
import com.example.teamworktracker.ui.theme.TeamWorkTrackerTheme
import com.example.teamworktracker.ui_screens.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Initialize Retrofit + Auth interceptor + Gson config
        ApiClient.init(applicationContext)

        enableEdgeToEdge()

        setContent {
            TeamWorkTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}
