package com.siresystems.furtherwestschool

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var eventList: ListView

    // Sample events (you will later replace with database)
    private val events = mapOf(
        "2026-04-10" to "Sports Day",
        "2026-04-15" to "Parents Meeting",
        "2026-04-20" to "Mid-Term Exams Start"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        calendarView = findViewById(R.id.calendarView)
        eventList = findViewById(R.id.eventList)

        // Show current month events on load
        showEventsForMonth(Calendar.getInstance())

        // When user selects a date
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            val selectedDate = String.format(
                "%04d-%02d-%02d",
                year, month + 1, dayOfMonth
            )

            val event = events[selectedDate]

            if (event != null) {
                Toast.makeText(this, event, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No events for this date", Toast.LENGTH_SHORT).show()
            }
        }

        // Detect month change (when user scrolls calendar)
        calendarView.setOnDateChangeListener { _, year, month, _ ->

            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)

            showEventsForMonth(cal)
        }
    }

    private fun showEventsForMonth(cal: Calendar) {

        val monthKey = SimpleDateFormat("yyyy-MM", Locale.getDefault())
            .format(cal.time)

        val monthEvents = events.filterKeys { it.startsWith(monthKey) }
            .map { "${it.key}: ${it.value}" }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            if (monthEvents.isNotEmpty()) monthEvents else listOf("No events this month")
        )

        eventList.adapter = adapter
    }
}