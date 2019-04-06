package com.chloeirrigation.chloe.Objects

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * Created by Lucas Paul on 06/04/2019.
 * Copyright © 2019 Chloe Irrigation Systems. All rights reserved.
 */
@Parcelize
class Field(
    var id: String,
    var name: String = "",
    var lat: Double,
    var lon: Double,
    var polyId: String,
    var fieldDrawable: Int,
    var devPeriod: Int,
    var sensor1: Int,
    var sensor2: Int,
    var sensorNutrients: Double
) : Parcelable {

    @IgnoredOnParcel
    val sensor1Value: String
        get() {
            return "$sensor1%"
        }

    @IgnoredOnParcel
    val sensor2Value: String
        get() {
            return "$sensor2%"
        }

    @IgnoredOnParcel
    val sensorNutrientsValue: String
        get() {
            return "$sensorNutrients dS/cm"
        }
}