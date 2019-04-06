package com.chloeirrigation.chloe.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.EventDay
import com.chloeirrigation.chloe.Helpers.getIconImageDrawable
import com.chloeirrigation.chloe.R
import kotlinx.android.synthetic.main.calendar_event_layout.view.*


/**
 * Created by Lucas Paul on 06/04/2019.
 * Copyright © 2019 Chloe Irrigation Systems. All rights reserved.
 */
class CalendarEventAdapter(
    val context: Context?,
    var events: List<EventDay>
) : RecyclerView.Adapter<CalendarEventViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int = events.size

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarEventViewHolder {
        return CalendarEventViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.calendar_event_layout,
                parent,
                false
            )
        )
    }

    // Binds each relay in the ArrayList to a view
    override fun onBindViewHolder(holder: CalendarEventViewHolder, position: Int) {
        val event = events.get(position)

        val eventDrawable = event.getIconImageDrawable()
        val title = when (eventDrawable) {
            R.drawable.irrigation -> "Crop Irrigation."
            R.drawable.waterdrop -> "Rainfall."
            R.drawable.spray -> "Crop sprayed with pesticide."
            R.drawable.seed -> "Crop planted."
            R.drawable.soil -> "Crop root growing detected."
            else -> "Crop Event"
        }

        holder.icon.setImageDrawable(context?.getDrawable(eventDrawable))
        holder.eventTitle.text = title
    }
}

class CalendarEventViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val icon: ImageView = view.calendarEventIcon
    val eventTitle: TextView = view.calendarEventTitle
}
