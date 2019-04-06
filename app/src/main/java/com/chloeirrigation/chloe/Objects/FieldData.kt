package com.chloeirrigation.chloe.Objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Lucas Paul on 06/04/2019.
 * Copyright © 2019 Chloe Irrigation Systems. All rights reserved.
 */
@Parcelize
class FieldData(var timestamp: Long, var trueColorUrl: String, var falseColorUrl: String, var ndviUrl: String, var eviUrl: String): Parcelable