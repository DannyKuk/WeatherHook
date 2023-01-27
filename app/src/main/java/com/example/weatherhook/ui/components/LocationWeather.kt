package com.example.weatherhook.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherhook.R

@Composable
fun LocationWeather(location: String, weather: Int, weatherTemp: Int) {

    val icon = arrayOf(
        painterResource(R.drawable.baseline_wb_sunny_24),
        painterResource(R.drawable.baseline_cloud_24),
        painterResource(R.drawable.baseline_grain_24),
        painterResource(R.drawable.baseline_snowboarding_24),
        painterResource(R.drawable.baseline_wind_power_24),
        painterResource(R.drawable.baseline_whatshot_24),)


    Surface(modifier = Modifier
        .padding(top = 20.dp)
        .fillMaxWidth()
        ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.background(
            colorResource(id = R.color.white)
        )) {
            Spacer(modifier = Modifier.width(5.dp))
            Text(location, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Row() {
                Icon(painter = icon[weather], contentDescription = "")
                Text(" $weatherTemp°C", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(5.dp))

        }
    }

}