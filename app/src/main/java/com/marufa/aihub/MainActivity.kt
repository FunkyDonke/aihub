package com.marufa.aihub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.marufa.aihub.data.store.AppPreferences
import com.marufa.aihub.ui.screen.MainScreen
import com.marufa.aihub.ui.theme.AIHubTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var prefs: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val darkThemePref by prefs.darkTheme.collectAsState(initial = null)
            val systemDark = isSystemInDarkTheme()
            val isDark = darkThemePref ?: systemDark

            AIHubTheme(darkTheme = isDark) {
                MainScreen()
            }
        }
    }
}
