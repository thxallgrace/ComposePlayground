package com.eunhye.tiltsensor.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TiltSensorScreen(x: Float, y: Float) {
    val yCoord = x * 20
    val xCoord = y * 20

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width/2
        val centerY = size.height/2

        // 바깥 원
        drawCircle(
            color = Color.Black,
            radius = 100f,
            center = Offset(centerX, centerY),
            style = Stroke()
        )

        // 녹색 원
        drawCircle(
            color = Color.Green,
            center = Offset(xCoord + centerX, yCoord + centerY),
            radius = 100f,
        )

        drawLine(
            color = Color.Black,
            start = Offset(centerX - 20, centerY),
            end = Offset(centerX + 20, centerY)
        )

        drawLine(
            color = Color.Black,
            start = Offset(centerX, centerY - 20),
            end = Offset(centerX, centerY + 20)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TiltSensorScreenPreview() {
    TiltSensorScreen(30f, 50f)
}
