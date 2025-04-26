package com.kurly.android.commerce

import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.kurly.android.commerce.presentation.home.HomeScreen
import com.kurly.android.commerce.presentation.theme.KurlyColor
import com.kurly.android.commerce.presentation.theme.KurlyCommerceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        // 배경색 지정 (여기선 흰색 예시)
        val desiredColor = KurlyColor.toArgb()
        WindowCompat.setDecorFitsSystemWindows(window, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
            window.insetsController?.apply {
                setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
            window.statusBarColor = desiredColor // 여전히 필요함
        } else {
            @Suppress("DEPRECATION")
            window.statusBarColor = desiredColor

            WindowInsetsControllerCompat(window, window.decorView).apply {
                isAppearanceLightStatusBars = false // 흰 배경에 검정 아이콘
            }
        }

        setContent {
            KurlyCommerceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}