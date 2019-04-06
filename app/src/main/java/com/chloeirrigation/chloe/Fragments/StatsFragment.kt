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
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.fragment_stats.*
import me.akatkov.kotlinyjson.JSON


class StatsFragment : Fragment() {

    private val moistureHightData = arrayListOf<Entry>()
    private val moistureLowData = arrayListOf<Entry>()
    private val soilTemperatureHighData = arrayListOf<Entry>()
    private val soilTemperatureLowData = arrayListOf<Entry>()

    private val temperatureData = arrayListOf<Entry>()
    private val humidityData = arrayListOf<Entry>()
    private val rainData = arrayListOf<BarEntry>()

    private var referenceSensorTimestamp = 0L
    private var referenceWeatherTimestamp = 0L

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
        referenceSensorTimestamp = jsonData[0]["timestamp"].longValue

        for (jObj in jsonData) {
            val timestamp = (jObj["timestamp"].longValue).toFloat() - referenceSensorTimestamp
            val moistureH = jObj["swvl1"].doubleValue.toFloat()
            val moistureL = jObj["swvl2"].doubleValue.toFloat()
            val temperatureH = (jObj["stl1"].doubleValue - 273).toFloat()
            val temperatureL = (jObj["stl2"].doubleValue - 273).toFloat()

            moistureHightData.add(Entry(timestamp, moistureH))
            moistureLowData.add(Entry(timestamp, moistureL))
            soilTemperatureHighData.add(Entry(timestamp, temperatureH))
            soilTemperatureLowData.add(Entry(timestamp, temperatureL))
        }

        sensorJsonString = ""

        // Load the weather data
        val weatherJsonString = resources.openRawResource(R.raw.weather_data).bufferedReader().use { it.readText() }
        jsonData = JSON(weatherJsonString).listValue
        referenceWeatherTimestamp = jsonData[0]["timestamp"].longValue

        for (jObj in jsonData) {
            val timestamp = (jObj["timestamp"].longValue).toFloat() - referenceWeatherTimestamp
            val temperature = (jObj["2t"].doubleValue).toFloat()
            val rain = jObj["tp"].doubleValue.toFloat()
            val humidity = jObj["hum"].doubleValue.toFloat()

            temperatureData.add(Entry(timestamp, temperature))
            humidityData.add(Entry(timestamp, humidity))
            rainData.add(BarEntry(timestamp, rain))
        }

        Log.d(TAG, "loadData: Data loaded successfully!")
    }

    private fun setMoistureData() {
        val dataSetMH = LineDataSet(moistureHightData, "Soil Moisture 30cm")
        dataSetMH.color = context!!.getColor(R.color.blue)
        dataSetMH.lineWidth = 4.0f
        dataSetMH.setDrawCircles(false)
        dataSetMH.setDrawCircleHole(false)

        val dataSetML = LineDataSet(moistureLowData, "Soil Moisture 90cm")
        dataSetML.color = context!!.getColor(R.color.green)
        dataSetML.lineWidth = 4.0f
        dataSetML.setDrawCircles(false)
        dataSetML.setDrawCircleHole(false)

        val lineData = LineData(dataSetMH, dataSetML)
        val desc = Description()
        desc.text = "Soil Moisture"
        sensorChart.description = desc
        sensorChart.animateX(1000)
        sensorChart.animateY(1000)
        sensorChart.data = lineData
        sensorChart.xAxis.valueFormatter = DateAxisValueFormatter(referenceSensorTimestamp)
        sensorChart.invalidate()
    }

    private fun setSoilTemperatureData() {
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
        sensorChart.xAxis.valueFormatter = DateAxisValueFormatter(referenceSensorTimestamp)
        sensorChart.invalidate()
    }

    private fun setTemperatureData() {
        val datasetTemperature = LineDataSet(temperatureData, "Temperature")
        datasetTemperature.color = context!!.getColor(R.color.blue).adjustColor(0.8f)
        datasetTemperature.lineWidth = 4.0f
        datasetTemperature.setDrawCircles(false)
        datasetTemperature.setDrawCircleHole(false)

        val lineData = LineData(datasetTemperature)
        val desc = Description()
        desc.text = "Air Temperature"
        weatherChart.description = desc
        weatherChart.animateX(1000)
        weatherChart.animateY(1000)
        weatherChart.data = lineData
//        weatherChart.xAxis.valueFormatter = DateAxisValueFormatter(referenceWeatherTimestamp)
        weatherChart.axisRight.setDrawLabels(false)
        weatherChart.xAxis.setDrawLabels(false)
        weatherChart.invalidate()
    }

    private fun setHumidityData() {
        val datasetHumidity = LineDataSet(humidityData, "Humidity")
        datasetHumidity.color = context!!.getColor(R.color.yellow).adjustColor(0.7f)
        datasetHumidity.lineWidth = 4.0f
        datasetHumidity.setDrawCircles(false)
        datasetHumidity.setDrawCircleHole(false)

        val lineData = LineData(datasetHumidity)
        val desc = Description()
        desc.text = "Air Humidity"
        weatherChart.description = desc
        weatherChart.animateX(1000)
        weatherChart.animateY(1000)
        weatherChart.data = lineData
//        weatherChart.xAxis.valueFormatter = DateAxisValueFormatter(referenceWeatherTimestamp)
        weatherChart.axisRight.setDrawLabels(false)
        weatherChart.xAxis.setDrawLabels(false)
        weatherChart.invalidate()
    }

    private fun setRainData() {
        val datasetRain = BarDataSet(rainData, "Precipitation") //LineDataSet(humidityData, "Humidity")
        datasetRain.color = context!!.getColor(R.color.yellow).adjustColor(0.7f)
//        dataSetTemperature.lineWidth = 4.0f
//        dataSetTemperature.setDrawCircles(false)
//        dataSetTemperature.setDrawCircleHole(false)

        val lineData = BarData(datasetRain)
        val desc = Description()
        desc.text = "Total Precipitation (Rainfall)"
        weatherRainfallChart.description = desc
        weatherRainfallChart.animateX(1000)
        weatherRainfallChart.animateY(1000)
        weatherRainfallChart.data = lineData
//        weatherChart.xAxis.valueFormatter = DateAxisValueFormatter(referenceWeatherTimestamp)
        weatherChart.axisRight.setDrawLabels(false)
        weatherChart.xAxis.setDrawLabels(false)
        weatherRainfallChart.invalidate()
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
            1 -> setSoilTemperatureData() // Temperature
            2 -> return  //TODO: Add EC data
            else -> setMoistureData() // Moisture
        }
    }

    private fun updateWeatherChart(position: Int) {
        when (position) {
            1 -> { // Humidity
                weatherRainfallChart.visibility = View.GONE
                weatherChart.visibility = View.VISIBLE
                setHumidityData()
            }
            2 -> { // Precipitation, show the correct chart
                weatherRainfallChart.visibility = View.VISIBLE
                weatherChart.visibility = View.GONE
                setRainData()
            }
            else -> {
                weatherRainfallChart.visibility = View.GONE
                weatherChart.visibility = View.VISIBLE
                setTemperatureData() // Temperature
            }
        }
    }

}
