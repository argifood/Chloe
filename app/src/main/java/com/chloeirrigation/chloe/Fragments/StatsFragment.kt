package com.chloeirrigation.chloe.Fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chloeirrigation.chloe.Helpers.*
import com.chloeirrigation.chloe.R
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_stats.*
import me.akatkov.kotlinyjson.JSON


class StatsFragment : Fragment() {

    private val moistureHightData = arrayListOf<Entry>()
    private val moistureLowData = arrayListOf<Entry>()
    private val soilTemperatureHighData = arrayListOf<Entry>()
    private val soilTemperatureLowData = arrayListOf<Entry>()

    val temperatureData = arrayListOf<Entry>()
    val humidityData = arrayListOf<Entry>()
    val rainData = arrayListOf<Entry>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()

        setupSensorCharts()
        setupWeatherCharts()
    }

    private fun loadData() {
        var sensorJsonString = resources.openRawResource(R.raw.sensor_data).bufferedReader().use { it.readText() }

        var jsonData = JSON(sensorJsonString).listValue

        if (jsonData.isNotEmpty()) {
            moistureHightData.clear()
            moistureLowData.clear()

            soilTemperatureHighData.clear()
            soilTemperatureLowData.clear()
        }

        for (jObj in jsonData) {
            val timestamp = (jObj["timestamp"].intValue * 1000L).toFloat()
            val moistureH = jObj["swvl1"].doubleValue.toFloat()
            val moistureL = jObj["swvl2"].doubleValue.toFloat()
            val temperatureH = (jObj["stl1"].doubleValue - 273).toFloat()
            val temperatureL = (jObj["stl2"].doubleValue - 273).toFloat()

            moistureHightData.add(Entry(timestamp, moistureH))
            moistureLowData.add(Entry(timestamp, moistureL))
            soilTemperatureHighData.add(Entry(timestamp, temperatureH))
            soilTemperatureLowData.add(Entry(timestamp, temperatureL))
        }

        // Load the weather data
        sensorJsonString = resources.openRawResource(R.raw.weather_data).bufferedReader().use { it.readText() }
        jsonData = JSON(sensorJsonString).listValue

        for (jObj in jsonData) {
            val timestamp = (jObj["timestamp"].intValue * 1000L).toFloat()
            val temperature = (jObj["2t"].doubleValue).toFloat()
            val rain = jObj["tp"].doubleValue.toFloat()
            val humidity = jObj["hum"].doubleValue.toFloat()

            temperatureData.add(Entry(timestamp, temperature))
            rainData.add(Entry(timestamp, rain))
            humidityData.add(Entry(timestamp, humidity))
        }
        
        Log.d(TAG, "loadData: Data loaded successfully!")
    }

    private fun setMoistureData() {
        val dataSetMH = LineDataSet(moistureHightData, "Soil Moisture 30cm")
        dataSetMH.setColor(context!!.getColor(R.color.blue))
        dataSetMH.lineWidth = 4.0f
        dataSetMH.setDrawCircles(false)
        dataSetMH.setDrawCircleHole(false)

        val dataSetML = LineDataSet(moistureLowData, "Soil Moisture 90cm")
        dataSetML.setColor(context!!.getColor(R.color.green))
        dataSetML.lineWidth = 4.0f
        dataSetML.setDrawCircles(false)
        dataSetML.setDrawCircleHole(false)
//        dataSet.setValueTextColor(...); // styling, ...

        val lineData = LineData(dataSetMH, dataSetML)
        val desc = Description()
        desc.text = "Soil Moisture"
        sensorChart.description = desc
        sensorChart.animateX(1000)
        sensorChart.animateY(1000)
        sensorChart.data = lineData
        sensorChart.invalidate()
    }

    private fun setTemperatureData() {
        val dataSetTH = LineDataSet(soilTemperatureHighData, "Soil Temperature 30cm")
        dataSetTH.color = context!!.getColor(R.color.blue).adjustColor(1.2f)
        dataSetTH.lineWidth = 4.0f
        dataSetTH.setDrawCircles(false)
        dataSetTH.setDrawCircleHole(false)

        val dataSetTL = LineDataSet(soilTemperatureLowData, "Soil Temperature 90cm")
        dataSetTL.color = context!!.getColor(R.color.green).adjustColor(1.2f)
        dataSetTL.lineWidth = 4.0f
        dataSetTL.setDrawCircles(false)
        dataSetTL.setDrawCircleHole(false)

        val lineData = LineData(dataSetTH, dataSetTL)
        val desc = Description()
        desc.text = "Soil Temperature"
        sensorChart.description = desc
        sensorChart.animateX(1000)
        sensorChart.animateY(1000)
        sensorChart.data = lineData
        sensorChart.invalidate()
    }

    private fun setupSensorCharts() {
        sensorSegmentedControl.addOnSegmentClickListener {
            val position = it.absolutePosition
            updateSensorChart(position)
        }

        sensorSegmentedControl.setSelectedSegment(0)
        updateSensorChart(0)
    }


    private fun setupWeatherCharts() {
        weatherSegmentedControl.addOnSegmentClickListener {
            val position = it.absolutePosition
            updateWeatherChart(position)
        }

        weatherSegmentedControl.setSelectedSegment(0)
        updateWeatherChart(0)
    }


    private fun updateSensorChart(position: Int) {
        when (position) {
            1 -> setMoistureData() // Moisture
            2 -> setTemperatureData() // Temperature
            else -> return //TODO: Add EC data
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
