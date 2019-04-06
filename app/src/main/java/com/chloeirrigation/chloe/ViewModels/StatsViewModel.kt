package com.chloeirrigation.chloe.ViewModels

import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry

/**
 * Created by Lucas Paul on 07/04/2019.
 * Copyright © 2019 Chloe Irrigation Systems. All rights reserved.
 */
class StatsViewModel : ViewModel() {

    fun sensorDataIsEmpty(): Boolean {
        return moistureHighData.isEmpty() || moistureLowData.isEmpty() || soilTemperatureHighData.isEmpty() || soilTemperatureLowData.isEmpty()
    }

    fun weatherDataIsEmpty(): Boolean {
        return temperatureData.isEmpty() || humidityData.isEmpty() || rainData.isEmpty()
    }

    val moistureHighData = arrayListOf<Entry>()
    val moistureLowData = arrayListOf<Entry>()
    val soilTemperatureHighData = arrayListOf<Entry>()
    val soilTemperatureLowData = arrayListOf<Entry>()

    val temperatureData = arrayListOf<Entry>()
    val humidityData = arrayListOf<Entry>()
    val rainData = arrayListOf<BarEntry>()

    var referenceSensorTimestamp = 0L
    var referenceWeatherTimestamp = 0L
}