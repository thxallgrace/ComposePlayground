package com.eunhye.tiltsensor

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eunhye.tiltsensor.ui.TiltSensorScreen
import com.eunhye.tiltsensor.ui.TiltSensorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        //화면이 가로모드로 고정되게 하기
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        super.onCreate(savedInstanceState)

        setContent {
            val viewModel = viewModel<TiltSensorViewModel>()
            lifecycle.addObserver(viewModel) // 라이프사이클 설정!
            TiltSensorScreen(
                x = viewModel.x.value,
                y = viewModel.y.value
            )
        }
    }
}
