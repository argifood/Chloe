package com.chloeirrigation.chloe.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chloeirrigation.chloe.R
import kotlinx.android.synthetic.main.fragment_stats.*

class StatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSensorCharts()
        setupWeatherCharts()
    }

    private fun setupSensorCharts() {
        sensorSegmentedControl.addOnSegmentClickListener {
            val position = it.absolutePosition
            updateSensorChart(position)
        }

        sensorChart // TODO: 06/04/2019 Do something

        updateSensorChart(0)
    }


    private fun setupWeatherCharts() {
        weatherSegmentedControl.addOnSegmentClickListener {
            val position = it.absolutePosition
            updateWeatherChart(position)
        }

        weatherChart // TODO: 06/04/2019 Do something

        updateSensorChart(0)
    }



    private fun updateSensorChart(position: Int) {
        when (position) {
            1 -> return // TODO: 06/04/2019 Fix the values here
            2 -> return
            else -> return
        }
    }

    private fun updateWeatherChart(position: Int) {
        when (position) {
            1 -> return
            2 -> {
                // Precipitation, show the correct chart
                weatherRainfallChart.visibility = View.VISIBLE
                weatherChart.visibility = View.GONE
            }
            else -> return
        }
    }

}
