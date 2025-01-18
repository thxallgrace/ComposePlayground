package com.eunhye.composeplayground.bmiCalculator

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eunhye.composeplayground.R
import kotlin.math.pow

@Composable
fun BmiCalculatorScreen() {
    val viewModel = viewModel<BmiViewModel>()
    val bmi = viewModel.bmi.value

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable(route = "home") {
            HomeScreen(
                onResultClick = { height, weight ->
                    viewModel.calculateBmi(height, weight)
                    navController.navigate("result")
                }
            )
        }
        composable(route = "result") {
            ResultScreen(
                navController = navController,
                bmi = bmi,
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onResultClick : (Double, Double) -> Unit
) {
    val (height, setHeight) = rememberSaveable {
        mutableStateOf("")
    }
    val (weight, setWeight) = rememberSaveable {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("비만도 계산기") })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = height,
                label = { Text("키") },
                onValueChange = setHeight,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = weight,
                label = { Text("몸무게") },
                onValueChange = setWeight,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    if(height.isNotEmpty() && weight.isNotEmpty()) {
                        onResultClick(height.toDouble(), weight.toDouble())
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("결과")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavHostController, bmi: Double) {
    val resultText = when {
        bmi >= 35 -> "고도 비만"
        bmi >= 30 -> "2단계 비만"
        bmi >= 25 -> "1단계 비만"
        bmi >= 23 -> "과체중"
        bmi >= 18.5 -> "정상"
        else -> "저체중"
    }

    val resultImgResource = when {
        bmi >= 23 -> R.drawable.baseline_sentiment_very_dissatisfied_24
        bmi >= 18.5 -> R.drawable.baseline_sentiment_very_satisfied_24
        else -> R.drawable.baseline_sentiment_neutral_24
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("비만도 계산기") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "home",
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                }
            )
        },

        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = resultText, fontSize = 30.sp)
            Spacer(Modifier.height(10.dp))
            Image(
                painter = painterResource(id = resultImgResource),
                contentDescription = "",
                colorFilter = ColorFilter.tint(
                    color = Color.Blue
                ),
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

class BmiViewModel : ViewModel() {
    private val _bmi = mutableDoubleStateOf(0.0)
    val bmi : State<Double> = _bmi

    fun calculateBmi(height: Double, weight: Double){
        _bmi.doubleValue = weight / (height / 100).pow(2.0)
    }
}

