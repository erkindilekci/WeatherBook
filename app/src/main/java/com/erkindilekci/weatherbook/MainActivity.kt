package com.erkindilekci.weatherbook

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erkindilekci.weatherbook.presentation.WeatherCard
import com.erkindilekci.weatherbook.presentation.WeatherForecast
import com.erkindilekci.weatherbook.presentation.WeatherViewModel
import com.erkindilekci.weatherbook.presentation.ui.theme.Light
import com.erkindilekci.weatherbook.presentation.ui.theme.Medium
import com.erkindilekci.weatherbook.presentation.ui.theme.WeatherBookTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            viewModel.loadWeatherInfo()
        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        setContent {
            WeatherBookTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    val state = viewModel.state.collectAsState().value

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Light)
                    ) {
                        WeatherCard(state = state, backgroundColor = Medium)

                        Spacer(modifier = Modifier.height(16.dp))

                        WeatherForecast(state = state)
                    }

                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.White
                        )
                    }

                    state.error?.let { error ->
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sad),
                                contentDescription = null,
                                modifier = Modifier.size(200.dp),
                                tint = Color.White,
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = error,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
        }
    }
}