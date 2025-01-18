package com.eunhye.composeplayground.stopWatch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eunhye.composeplayground.R
import java.util.Timer
import kotlin.concurrent.timer

@Composable
fun StopWatchScreen() {
    val viewModel = viewModel<MainViewModel>()

    val sec = viewModel.sec.value
    val milliSec = viewModel.milSec.value
    val isRunning = viewModel.isRunning.value
    val labTimes = viewModel.lapTimes.value

    MainScreen(
        sec = sec,
        milliSec = milliSec,
        isRunning = isRunning,
        lapTimes = labTimes,
        onReset = { viewModel.reset() },
        onToggle = { running ->
            if (running) {
                viewModel.pause()
            } else {
                viewModel.start()
            }
        },
        onRecordLap = {
            viewModel.recordLapTime()
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    sec: Int,
    milliSec: Int,
    isRunning: Boolean,
    lapTimes: List<String>,
    onReset: () -> Unit,
    onToggle: (Boolean) -> Unit,
    onRecordLap: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("StopWatch") })
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = sec.toString(),
                    fontSize = 100.sp
                )
                Text(
                    text = milliSec.toString(),
                    fontSize = 20.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                lapTimes.forEach { lapTime ->
                    Text(
                        text = lapTime,
                        fontSize = 20.sp
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = onReset,
                    containerColor = Color.Green,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_refresh_24),
                        contentDescription = "reset"
                    )
                }
                FloatingActionButton(
                    onClick = { onToggle(isRunning) },
                    containerColor = Color.Red,
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isRunning) R.drawable.baseline_pause_24
                            else R.drawable.baseline_play_arrow_24
                        ),
                        contentDescription = "Start/Pause"
                    )
                }
                Button(
                    onClick = onRecordLap,
                ) {
                    Text("랩 타임 기록")
                }
            }

        }
    }
}

class MainViewModel : ViewModel() {
    private var time = 0
    private var timerTask: Timer? = null

    private val _sec = mutableIntStateOf(0)
    val sec: State<Int> = _sec

    private val _milSec = mutableIntStateOf(0)
    val milSec: State<Int> = _milSec

    private val _isRunning = mutableStateOf(false)
    val isRunning: State<Boolean> = _isRunning

    private val _lapTimes = mutableStateOf(mutableListOf<String>())
    val lapTimes: State<List<String>> = _lapTimes

    private var lap = 1

    fun start() {
        _isRunning.value = true
        timerTask = timer(period = 10) {
            time++
            _sec.intValue = time / 100
            _milSec.intValue = time % 100
        }
    }

    fun pause() {
        _isRunning.value = false
        timerTask?.cancel()
    }

    fun reset() {
        timerTask?.cancel()

        time = 0
        _isRunning.value = false
        _sec.intValue = 0
        _milSec.intValue = 0
        lap = 1
        _lapTimes.value.clear()
    }

    fun recordLapTime() {
        _isRunning.value = false

        _lapTimes.value.add("$lap LAP : ${sec.value}.${milSec.value}")
        lap++
    }
}



