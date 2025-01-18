package com.eunhye.composeplayground.map

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GSMapMainScreen() {
    val context = LocalContext.current
    var grant by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            grant = isGranted
        }
    )

    if(ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
        grant = true
    }

    if(grant) {
        val viewModel = viewModel<GsMapViewModel>()
//        lifecycle.addObserver(viewModel) // 원래는 ComponentActivity의 lifecycle을 이용해야함 (Activity내부에서 선언 필요)
        GsMapScreen(viewModel = viewModel)
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("권한이 허용되지 않았습니다")
            Button(onClick = {
                launcher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }) {
                Text("권한 요청")
            }
        }
    }
}
