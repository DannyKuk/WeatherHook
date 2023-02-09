package com.example.weatherhook.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.weatherhook.data.models.Weather
import com.example.weatherhook.data.models.WeatherHookEvent
import com.example.weatherhook.data.repository.WeatherHookRepo
import com.example.weatherhook.ui.activities.HookActivity
import com.example.weatherhook.ui.components.HookInformation


class Hook : Fragment() {

    private lateinit var composeView: ComposeView

    //to be deleted
    val repo: WeatherHookRepo = WeatherHookRepo()
    var data = repo.loadAllData().events[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = activity as HookActivity
        val eventId = activity.intent.getIntExtra("currentEvent", -1)
        if (eventId >= 0){
            data = WeatherHookRepo().loadAllData().events[eventId]
        }else{
            data = WeatherHookEvent(
                eventId = eventId,
                active = true,
                title = "New Hook",
                location = Pair(13.405f,52.52f),
                timeToEvent = 3,
                relevantDays = "MO;TU;WE;TH;FR",
                triggers = listOf(Weather(weatherPhenomenon = 0, correspondingIntensity = 1f)).toMutableList()
            )
        }



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

            val newEvent = HookInformation(weatherHookEvent = data)
            Log.d("shit", newEvent.toString())

        }


    }
}