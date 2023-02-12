package com.example.weatherhook.ui.screens

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.example.weatherhook.R
import com.example.weatherhook.data.repository.WeatherHookRepo


class Settings : Fragment() {

    private lateinit var composeView: ComposeView

    val repo: WeatherHookRepo = WeatherHookRepo()
    val data = repo.loadAllData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        composeView.setContent {

            val radioOptions = listOf("English", "German")
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1] ) }

            Surface() {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(start = 50.dp, end = 50.dp)) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(text = "Language", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))
                    radioOptions.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = {
                                        onOptionSelected(text)
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                                colors = RadioButtonDefaults.colors(selectedColor = colorResource(id = R.color.dark_green), unselectedColor = colorResource(
                                    id = R.color.light_green)
                                )
                            )
                            Text(
                                text = text,
                                fontSize = 17.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(text = "Notifications", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {
                        val i = Intent(Settings.ACTION_ALL_APPS_NOTIFICATION_SETTINGS)
                        context?.startActivity(i)
                    }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(
                        R.color.dark_green)
                    )
                    ) {
                        Text(text = "Open Notification Settings", color = Color.White)
                    }
                }
            }



        }
    }

}