package com.chloeirrigation.chloe.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chloeirrigation.chloe.Helpers.TAG
import com.chloeirrigation.chloe.Objects.Field
import com.github.kittinunf.fuel.httpGet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_info.*
import android.widget.SeekBar
import com.chloeirrigation.chloe.ChloeApp
import com.chloeirrigation.chloe.FieldActivity
import com.chloeirrigation.chloe.Helpers.intValue
import com.chloeirrigation.chloe.Helpers.listValue
import com.chloeirrigation.chloe.Helpers.stringValue
import com.chloeirrigation.chloe.Objects.FieldData
import com.chloeirrigation.chloe.R
import me.akatkov.kotlinyjson.JSON


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

        getDataFromApi()

        setupWeather()

        setupField()

        setupRoot()
    }

    private fun getDataFromApi() {
        val url =
            "http://api.agromonitoring.com/agro/1.0/image/search?start=$startTime&end=$endTime&polyid=${field.polyId}&appid=${ChloeApp.agroApiKey}"

        Log.d(TAG, "getDataFromApi: Sending request to: $url")
        url.httpGet()
            .responseString { request, response, result ->
//            .responseJson { request, response, result ->
                val jsonStirng = result.get()

                val jsonArray = JSON(jsonStirng)

                if (jsonArray.listValue.isNotEmpty()) {
                    fieldData.clear()
                }

                for (day in jsonArray.listValue) {
                    val jImage = day["image"]

                    val timestamp = day["dt"].intValue
                    val trueColor = jImage["truecolor"].stringValue
                    val falseColor = jImage["falsecolor"].stringValue
                    val ndvi = jImage["ndvi"].stringValue
                    val evi = jImage["evi"].stringValue

                    val data = FieldData(timestamp, trueColor, falseColor, ndvi, evi)
                    fieldData.add(data)
                }

                Log.v(TAG, "Field data parsed: ${fieldData.size}")

                dateSeekBar.max = fieldData.size
            }


//            .responseString { request, response, result ->
//                when (result.component2()) {
//                    is Result.Failure -> {
//                        val ex = result.getException()
//                    }
//                    is Result.Success -> {
//                        val data = result.get()
//                    }
//                }
//            }
    }

    private fun setupRoot() {

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
        Log.d(TAG, "setupField: Selected position: $position")

        when (position) {
            1 -> loadFalseColor()
            2 -> loadNDVI()
            3 -> loadEVI()
            else -> loadTrueColor()
        }
    }

    private fun loadTrueColor() {
        val url = fieldData[dateSeekBar.progress].trueColorUrl
        loadSatelliteImageFrom(url)
    }

    private fun loadFalseColor() {
        val url = fieldData[dateSeekBar.progress].falseColorUrl
        loadSatelliteImageFrom(url)
    }

    private fun loadNDVI() {
        val url = fieldData[dateSeekBar.progress].ndviUrl
        loadSatelliteImageFrom(url)
    }


    private fun loadEVI() {
        val url = fieldData[dateSeekBar.progress].eviUrl
        loadSatelliteImageFrom(url)
    }

    private fun loadSatelliteImageFrom(url: String) {
        Log.d(TAG, "loadSatelliteImageFrom: Loading image form $url")
//        Picasso.with(context).load(url).into(fieldSateliteImage)
        Picasso.get().load(url).into(fieldSateliteImage)
//        ImageLoader.getInstance().displayImage(url, fieldSateliteImage)
    }


    private fun setupWeather() {

    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        val days = fieldData.size - seekBar.progress
        Log.d(TAG, "onStopTrackingTouch: Progress changed: $days | Prog: ${seekBar.progress}")

        segmentedPositionChanged(segmentedControl.selectedAbsolutePosition)
        // TODO: 06/04/2019 Load current image here
    }


}
