package com.chloeirrigation.chloe.Fragments


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.chloeirrigation.chloe.ChloeApp
import com.chloeirrigation.chloe.FieldActivity
import com.chloeirrigation.chloe.Helpers.*
import com.chloeirrigation.chloe.Objects.Field
import com.chloeirrigation.chloe.Objects.FieldData
import com.chloeirrigation.chloe.R
import com.github.kittinunf.fuel.httpGet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.irrigation_status_layout.*
import kotlinx.android.synthetic.main.weather_layout.*
import me.akatkov.kotlinyjson.JSON
import java.text.SimpleDateFormat
import java.util.*


class InfoFragment : Fragment(), SeekBar.OnSeekBarChangeListener {
    lateinit var field: Field
    val fieldData = arrayListOf<FieldData>()

    var startTime: Long = 1549459927L
    var endTime: Long = 1554555600L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        field = (activity as FieldActivity).field

        Log.d(TAG, "onViewCreated: Loaded field: $field")

        if (fieldData.isEmpty()) {
            getDataFromApi()
        }

        setupWeather()

        setupField()

        setupRoot()
    }

    private fun getDataFromApi() {
        val url =
            "http://api.agromonitoring.com/agro/1.0/image/search?start=$startTime&end=$endTime&polyid=${field.polyId}&appid=${ChloeApp.agroApiKey}"

        try {
            url.httpGet().responseString { request, response, result ->
                val jsonString = result.get()
                val jsonArray = JSON(jsonString)

                if (jsonArray.listValue.isNotEmpty()) {
                    fieldData.clear()
                }

                for (day in jsonArray.listValue) {
                    val jImage = day["image"]

                    val timestamp = day["dt"].intValue * 1000L
                    val trueColor = jImage["truecolor"].stringValue
                    val falseColor = jImage["falsecolor"].stringValue
                    val ndvi = jImage["ndvi"].stringValue
                    val evi = jImage["evi"].stringValue

                    val data = FieldData(timestamp, trueColor, falseColor, ndvi, evi)
                    fieldData.add(data)
                }

                Log.v(TAG, "Field data parsed: ${fieldData.size}")

                preloadImages()

                try {
                    dateSeekBar.max = fieldData.size
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        dateSeekBar.min = 1
                    }
                } catch (e: IllegalStateException) {
                    Log.e(TAG, "getDataFromApi: Could not load seekBar", e)
                }

                segmentedPositionChanged(segmentedControl.selectedAbsolutePosition)
                updateDateTextView()
            }
        } catch (e: Exception) {
            Log.e(TAG, "getDataFromApi: Server request failed", e)
        }
    }

    private fun preloadImages() {
        val picasso = Picasso.get()
        fieldData.forEach {
            picasso.load(it.trueColorUrl).fetch()
        }

        fieldData.forEach {
            picasso.load(it.falseColorUrl).fetch()
            picasso.load(it.ndviUrl).fetch()
            picasso.load(it.eviUrl).fetch()
        }
    }

    private fun setupRoot() {
        val drawable = when (field.devPeriod) {
            1 -> R.drawable.ic_root_2
            2 -> R.drawable.ic_root_3
            3 -> R.drawable.ic_root_4
            4 -> R.drawable.ic_root_5
            else -> R.drawable.ic_root_1 // 0 and others
        }

        rootImageView.setImageDrawable(context?.getDrawable(drawable))
        sensor1TextView.text = field.sensor1Value
        sensor2TextView.text = field.sensor2Value
        sensorNutrientsTextView.text = field.sensorNutrientsValue
    }

    private fun setupField() {
        segmentedControl.addOnSegmentClickListener {
            segmentedPositionChanged(it.absolutePosition)
        }

        segmentedControl.setSelectedSegment(0)

        fieldImageView.setImageDrawable(context?.getDrawable(field.fieldDrawable))

        dateSeekBar.setOnSeekBarChangeListener(this)
    }

    private fun segmentedPositionChanged(position: Int) {
        if (position >= fieldData.size) {
            return
        }

        when (position) {
            1 -> loadFalseColor()
            2 -> loadNDVI()
            3 -> loadEVI()
            else -> loadTrueColor()
        }
    }

    private fun loadTrueColor() {
        try {
            val url = fieldData[dateSeekBar.progress - 1].trueColorUrl
            loadSatelliteImageFrom(url)
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, "loadTrueColor: ", e)
        }
    }

    private fun loadFalseColor() {
        try {
            val url = fieldData[dateSeekBar.progress - 1].falseColorUrl
            loadSatelliteImageFrom(url)
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, "loadTrueColor: ", e)
        }
    }

    private fun loadNDVI() {
        try {
            val url = fieldData[dateSeekBar.progress - 1].ndviUrl
            loadSatelliteImageFrom(url)
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, "loadTrueColor: ", e)
        }
    }


    private fun loadEVI() {
        try {
            val url = fieldData[dateSeekBar.progress - 1].eviUrl
            loadSatelliteImageFrom(url)
        } catch (e: IndexOutOfBoundsException) {
            Log.e(TAG, "loadTrueColor: ", e)
        }
    }

    private fun loadSatelliteImageFrom(url: String) {
//        Log.d(TAG, "loadSatelliteImageFrom: Loading image form $url")
        Picasso.get().load(url).into(fieldSateliteImage)
    }


    private fun setupWeather() {
        val url = "https://api.openweathermap.org/data/2.5/forecast?lat=${field.lat}&lon=${field.lon}&appid=6395bfdf79d06275382f2d4cf27fa375"
        try {
            url.httpGet().responseString { req, res, result ->
                val jObj = JSON(result.get())

                val weatherList = jObj["list"].listValue

                val today = weatherList.firstOrNull()
                today?.let { jObj ->
                    val weather = jObj["weather"]["main"].stringValue
                    val currTemp = (jObj["main"]["temp"].doubleValue - 273.0).toInt()
                    val humidity = jObj["main"]["humidity"].doubleValue
                    val wind = jObj["wind"]["speed"].doubleValue

                    todayWeatherImageView.setImageDrawable(
                        context?.getDrawable(
                            when (weather) {
                                "Rain" -> R.drawable.w_rain
                                "Clouds" -> R.drawable.w_cloud
                                else -> R.drawable.w_sun
                            }
                        )
                    )

                    todayTempTextView.text = "$currTemp C"
                    todayWindTextView.text = "$wind km/h"
                }

                for (jObj in weatherList.drop(1)) {
                    val timestamp = jObj["dt"].stringValue
                    val weather = jObj["weather"]["main"].stringValue
                    val temp = jObj["main"]["temp"].doubleValue
                }

                Log.d(TAG, "setupWeather: Fetched weather from api! ${weatherList.size}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "setupWeather: Failed to fetch weather from API", e)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (progress == 0) {
            seekBar.progress = 1
        }

        updateDateTextView()

        segmentedPositionChanged(segmentedControl.selectedAbsolutePosition)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        val days = fieldData.size - seekBar.progress
        Log.d(TAG, "onStopTrackingTouch: Progress changed: $days | Prog: ${seekBar.progress}")

//        segmentedPositionChanged(segmentedControl.selectedAbsolutePosition)
        // TODO: 06/04/2019 Load current image here
    }

    private fun updateDateTextView() {
        if (fieldData.size > dateSeekBar.progress) {
            val date = Date(fieldData[dateSeekBar.progress].timestamp)
            val format = SimpleDateFormat("EEE dd/MM/yyyy")
            satelliteDateTextView.text = format.format(date)
        }
    }


}
