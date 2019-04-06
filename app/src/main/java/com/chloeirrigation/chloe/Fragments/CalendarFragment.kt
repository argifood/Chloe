package com.chloeirrigation.chloe.Fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.EventDay
import com.chloeirrigation.chloe.Adapters.CalendarEventAdapter
import com.chloeirrigation.chloe.Helpers.TAG
import com.chloeirrigation.chloe.Helpers.randomInt
import com.chloeirrigation.chloe.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*


class CalendarFragment : Fragment() {

    var events: MutableList<EventDay> = mutableListOf()

    private lateinit var adapter: CalendarEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.chloeirrigation.chloe.R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendarView()
    }

    private fun setupCalendarView() {
        setupEvents()

        calendarView.setOnDayClickListener { eventDay ->
            val dayEvents = events.filter { it.calendar == eventDay.calendar }
            adapter.events = dayEvents
            adapter.notifyDataSetChanged()
            Log.d(TAG, "setupCalendarView: Selected day ${eventDay.calendar.time} | Events: ${dayEvents.size}")
        }

        calendarRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CalendarEventAdapter(
            context,
            events.filter { it.calendar.equals(calendarView.firstSelectedDate) }
        )
        calendarRecyclerView.adapter = adapter
    }

    private fun setupEvents() {
        val calendar = Calendar.getInstance()
        calendar.set(2019, 3, 4)

        val irrigationEvent = R.drawable.irrigation
        val rainEvent = R.drawable.waterdrop
        val cropPlantedEvent = R.drawable.seed
        val cropGrowthEvent = R.drawable.soil
        val sprayEvent = R.drawable.spray

        val eventArray = arrayListOf(irrigationEvent, cropPlantedEvent, cropGrowthEvent, sprayEvent, rainEvent)

        for (i in 0 until 20) {
            val cal = Calendar.getInstance()
            cal.set(2019, randomInt(2, 4), randomInt(1, 30))

            val event = EventDay(cal, eventArray[randomInt(0, eventArray.size - 1)])
            events.add(event)
        }

        // Irrigation Events
//        calendar.add(Calendar.DAY_OF_MONTH, 2)
//        events.add(irrigationEvent.setCalendar(calendar))
//
//        calendar.add(Calendar.DAY_OF_MONTH, -6)
//        events.add(irrigationEvent.setCalendar(calendar))
//
//        calendar.add(Calendar.DAY_OF_MONTH, -6)
//        events.add(irrigationEvent.setCalendar(calendar))
//
//        calendar.add(Calendar.DAY_OF_MONTH, -7)
//        events.add(irrigationEvent.setCalendar(calendar))

        calendarView.setEvents(events)
        Log.d(TAG, "setupEvents: Added ${events.size} events!")
    }

}
