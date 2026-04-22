package com.siresystems.furtherwestschool

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso

class Calendar_Activity : AppCompatActivity() {

    private lateinit var calendarImage: ImageView
    private val url = "https://yosefe27-001-site1.ktempurl.com/apis/get_calendar.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity)

        calendarImage = findViewById(R.id.calendarImage)

        fetchCalendar()
    }

    private fun fetchCalendar() {
        val queue = Volley.newRequestQueue(this)

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val status = response.getString("status")

                    if (status == "success") {
                        val imageUrl = response.getString("image_url")

                        Picasso.get()
                            .load(imageUrl)
                            .into(calendarImage)

                    } else {
                        Toast.makeText(this, "No calendar found", Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error loading calendar", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }
}