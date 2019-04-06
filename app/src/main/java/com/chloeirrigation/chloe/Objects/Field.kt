package com.chloeirrigation.chloe.Objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Lucas Paul on 06/04/2019.
 * Copyright © 2019 Chloe Irrigation Systems. All rights reserved.
 */
@Parcelize
class Field(var name: String = "", var lat: Int, var lon: Int, var polyId: String, var fieldDrawable: Int): Parcelable