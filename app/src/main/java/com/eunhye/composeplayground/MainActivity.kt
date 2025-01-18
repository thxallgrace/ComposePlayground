package com.eunhye.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // BmiCalculatorScreen() : 비만도 계산기
            // StopWatchScreen() : 스톱워치
            // WebBrowserScreen() : 나만의 웹 브라우저
            // MyGalleryScreen() : 전자액자
            // XylophoneScreen() : 실로폰
            // GSMapMainScreen() : Gps Map
        }
    }
}
