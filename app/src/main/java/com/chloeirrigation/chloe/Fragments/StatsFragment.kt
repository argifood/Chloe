package com.chloeirrigation.chloe.Fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.chloeirrigation.chloe.Helpers.*
import com.chloeirrigation.chloe.R
import com.chloeirrigation.chloe.ViewModels.StatsViewModel
import com.github.kittinunf.fuel.httpGet
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.fragment_stats.*
import me.akatkov.kotlinyjson.JSON


class StatsFragment : Fragment() {

    private val viewModel: StatsViewModel by lazy {
        ViewModelProviders.of(this).get(StatsViewModel::class.java)
    }

    val sensorDataUrl = "http://api.chloeirrigation.com/chloe/field/sensor/5bc4ff031023c1b16a71554c"
    val weatherDataUrl = "http://api.chloeirrigation.com/chloe/field/weather/5bc4ff031023c1b16a71554c"

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
        if (!Companion.useLocalResources) {
            if (viewModel.sensorDataIsEmpty())
                try {
                    sensorDataUrl.httpGet().responseString { req, res, result ->
                        val jsonString = result.get()
                        val json = JSON(jsonString)
                        parseSensorDataFrom(json)
                        Log.d(TAG, "loadData: Sensor Data fetched! Count: ${viewModel.moistureHighData.size}")

                        setupSensorCharts()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "loadData: Sensor Request failed", e)
                }

            if (viewModel.weatherDataIsEmpty()) {
                try {
                    weatherDataUrl.httpGet().responseString { req, res, result ->
                        val jsonString = result.get()
                        val json = JSON(jsonString)
                        parseWeatherDataFrom(json)
                        Log.d(TAG, "loadData: Weather Data fetched! Count: ${viewModel.temperatureData.size}")

                        setupWeatherCharts()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "loadData: Weather Request failed", e)
                }
            }

        } else {
            val sensorJsonString = resources.openRawResource(R.raw.sensor_data).bufferedReader().use { it.readText() }
            var json = JSON(sensorJsonString)

            parseSensorDataFrom(json)

            // Load the weather data
            val weatherJsonString = resources.openRawResource(R.raw.weather_data).bufferedReader().use { it.readText() }
            json = JSON(weatherJsonString)
            parseWeatherDataFrom(json)
            Log.d(TAG, "loadData: Data loaded successfully!")
        }

    }

    private fun parseSensorDataFrom(json: JSON) {
        val jsonData = json.listValue
        if (jsonData.isNotEmpty()) {
            viewModel.moistureHighData.clear()
            viewModel.moistureLowData.clear()

            viewModel.soilTemperatureHighData.clear()
            viewModel.soilTemperatureLowData.clear()
        }
        viewModel.referenceSensorTimestamp = jsonData[0]["timestamp"].longValue

        for (jObj in jsonData) {
            val timestamp = (jObj["timestamp"].longValue).toFloat() - viewModel.referenceSensorTimestamp
            val moistureH = jObj["swvl1"].doubleValue.toFloat()
            val moistureL = jObj["swvl2"].doubleValue.toFloat()
            val temperatureH = (jObj["stl1"].doubleValue - 273).toFloat()
            val temperatureL = (jObj["stl2"].doubleValue - 273).toFloat()

            viewModel.moistureHighData.add(Entry(timestamp, moistureH))
            viewModel.moistureLowData.add(Entry(timestamp, moistureL))
            viewModel.soilTemperatureHighData.add(Entry(timestamp, temperatureH))
            viewModel.soilTemperatureLowData.add(Entry(timestamp, temperatureL))
        }
    }

    private fun parseWeatherDataFrom(json: JSON) {
        val jsonData = json.listValue
        if (jsonData.isNotEmpty()) {
            viewModel.temperatureData.clear()
            viewModel.humidityData.clear()
            viewModel.rainData.clear()
        }

        viewModel.referenceWeatherTimestamp = jsonData[0]["timestamp"].longValue

        for (jObj in jsonData) {
            val timestamp = (jObj["timestamp"].longValue).toFloat() - viewModel.referenceWeatherTimestamp
            val temperature = (jObj["2t"].doubleValue).toFloat()
            val rain = jObj["tp"].doubleValue.toFloat()
            val humidity = jObj["hum"].doubleValue.toFloat()

            viewModel.temperatureData.add(Entry(timestamp, temperature))
            viewModel.humidityData.add(Entry(timestamp, humidity))
            viewModel.rainData.add(BarEntry(timestamp, rain))
        }
    }

    private fun setMoistureData() {
        val dataSetMH = LineDataSet(viewModel.moistureHighData, "Soil Moisture 30cm")
        dataSetMH.color = context!!.getColor(R.color.blue)
        dataSetMH.lineWidth = 4.0f
        dataSetMH.setDrawCircles(false)
        dataSetMH.setDrawCircleHole(false)

        val dataSetML = LineDataSet(viewModel.moistureLowData, "Soil Moisture 90cm")
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
        sensorChart.xAxis.valueFormatter = DateAxisValueFormatter(viewModel.referenceSensorTimestamp)
        sensorChart.invalidate()
    }

    private fun setSoilTemperatureData() {
        val dataSetTH = LineDataSet(viewModel.soilTemperatureHighData, "Soil Temperature 30cm")
        dataSetTH.color = context!!.getColor(R.color.blue).adjustColor(1.2f)
        dataSetTH.lineWidth = 4.0f
        dataSetTH.setDrawCircles(false)
        dataSetTH.setDrawCircleHole(false)

        val dataSetTL = LineDataSet(viewModel.soilTemperatureLowData, "Soil Temperature 90cm")
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
        sensorChart.xAxis.valueFormatter = DateAxisValueFormatter(viewModel.referenceSensorTimestamp)
        sensorChart.invalidate()
    }

    private fun setTemperatureData() {
        val datasetTemperature = LineDataSet(viewModel.temperatureData, "Temperature")
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
        weatherChart.setVisibleXRangeMinimum(1541030400.0f)
//        weatherChart.xAxis.valueFormatter = DateAxisValueFormatter(referenceWeatherTimestamp)
        weatherChart.axisRight.setDrawLabels(false)
        weatherChart.xAxis.setDrawLabels(false)
        weatherChart.invalidate()
    }

    private fun setHumidityData() {
        val datasetHumidity = LineDataSet(viewModel.humidityData, "Humidity")
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
        val datasetRain = BarDataSet(viewModel.rainData, "Precipitation") //LineDataSet(humidityData, "Humidity")
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
        weatherRainfallChart.axisRight.setDrawLabels(false)
        weatherRainfallChart.xAxis.setDrawLabels(false)
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

    companion object {
        const val useLocalResources = false
    }

}
