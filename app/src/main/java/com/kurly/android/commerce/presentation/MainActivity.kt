package com.kurly.android.commerce.presentation

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
        // Splash Screen 설치
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 상태바 설정
        WindowCompat.setDecorFitsSystemWindows(window, true)

        val desiredColor = KurlyColor.toArgb() // 어두운 색상이어야 흰색 아이콘이 잘 보임

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                // 흰색 아이콘을 위해 light appearance를 꺼야 한다 (0 적용)
                setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
            window.statusBarColor = desiredColor
        } else {
            @Suppress("DEPRECATION")
            window.statusBarColor = desiredColor
            WindowInsetsControllerCompat(window, window.decorView).apply {
                isAppearanceLightStatusBars = false // 흰색 아이콘
            }
        }

        setContent {
            KurlyCommerceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
