package com.example.weatherhook.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.weatherhook.data.repository.WeatherHookRepo



class Prototyping : Fragment() {


    private lateinit var composeView: ComposeView

    val repo: WeatherHookRepo = WeatherHookRepo()
    val data = repo.loadAllData().events[1]


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



    var reloadView = 1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.setContent {


            var _reloadView by remember { mutableStateOf(reloadView) }
            var _triggerList by remember { mutableStateOf(data.triggers) }


            //_triggerList = newHook(data)

            _triggerList = TriggerList(_triggerList)

            Log.d("shit", _triggerList.toString())
            //_reloadView +=1



        }


    }

}