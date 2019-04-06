package com.chloeirrigation.chloe.Helpers

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.graphics.Color
import com.chloeirrigation.chloe.BuildConfig
import com.chloeirrigation.chloe.R
import me.akatkov.kotlinyjson.JSON


/**
 * Created by Lucas Paul on 05/04/2019.
 * Copyright © 2019 Chloe Irrigation Systems. All rights reserved.
 */

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

//Hide the keyboard
fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (inputMethodManager.isAcceptingText) inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

//Add Clear Button on EditText
fun EditText.setupClearButtonWithAction() {

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.ic_clear_grey_24dp else 0
            setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    })

    setOnTouchListener(View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                this.setText("")
                return@OnTouchListener true
            }
        }
        return@OnTouchListener false
    })
}

// Set Drawable Tint Color programmatically
fun Drawable.setTintColor(color: Int) {
    this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}

fun isDebug(): Boolean {
    return BuildConfig.DEBUG
}

fun Int.adjustColor(factor: Float): Int {
    val a = Color.alpha(this)
    val r = Math.round(Color.red(this) * factor)
    val g = Math.round(Color.green(this) * factor)
    val b = Math.round(Color.blue(this) * factor)
    return Color.argb(a, Math.min(r, 255), Math.min(g, 255), Math.min(b, 255))
}

// KotlinyJSON Extensions

val JSON.booleanValue: Boolean
    get() {
        return this.boolean ?: false
    }

val JSON.intValue: Int
    get() {
        return this.int ?: 0
    }

val JSON.longValue: Long
    get() {
        return this.long ?: 0L
    }

val JSON.doubleValue: Double
    get() {
        return this.double ?: 0.0
    }

val JSON.stringValue: String
    get() {
        return this.string ?: ""
    }

fun JSON.listValue(default: List<JSON>): List<JSON> {
    return this.list ?: default
}

/// Returns either the list value or an empty listOf<JSON>() if the value does not exist
val JSON.listValue: List<JSON>
    get() {
        return this.list ?: listOf()
    }

