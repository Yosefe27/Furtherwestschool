package com.siresystems.furtherwestschool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.siresystems.furtherwestschool.adapters.PupilAdapter
import com.siresystems.furtherwestschool.models.Pupil
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)

        // ✅ CHECK LOGIN SESSION
        val isLoggedIn = prefs.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // ✅ GET USER INFO
        val name = prefs.getString("name", "Parent")
        val email = prefs.getString("email", "")

        // ✅ WELCOME TEXT
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        welcomeText.text = "Welcome Parent $name"

        // ✅ TOOLBAR (LOGOUT)
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)

        toolbar.setNavigationOnClickListener {
            prefs.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // ✅ LOAD STUDENTS FROM API
        loadStudents(email ?: "")

        // Navigation buttons
        findViewById<Button>(R.id.newsBtn).setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
        }

        findViewById<Button>(R.id.calendarBtn).setOnClickListener {
            startActivity(Intent(this, Calendar_Activity::class.java))
        }

        findViewById<Button>(R.id.paymentsBtn).setOnClickListener {
            //Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, PaymentsOptions::class.java))
        }
    }

    // ✅ LOAD STUDENTS FUNCTION
    private fun loadStudents(parentEmail: String) {

        val listView = findViewById<ListView>(R.id.pupilList)

        Thread {

            try {

                val url = URL(
                    "https://yosefe27-001-site1.ktempurl.com/apis/get_students.php?parent_id=$parentEmail"
                )

                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                val reader = conn.inputStream.bufferedReader()
                val response = reader.readText()

                val json = JSONObject(response)

                if (json.getString("status") == "success") {

                    val studentsArray = json.getJSONArray("students")
                    val pupils = ArrayList<Pupil>()

                    for (i in 0 until studentsArray.length()) {

                        val obj = studentsArray.getJSONObject(i)

                        pupils.add(
                            Pupil(
                                obj.getString("StudentID"),
                                obj.getString("StudentName"),
                                obj.getString("StudentGrade"),
                                obj.getString("StudentClass"),
                                obj.getString("TotalFees"),
                                obj.getString("PaidFees"),
                                obj.optString("StudentImage")
                            )
                        )
                    }

                    runOnUiThread {
                        val adapter = PupilAdapter(this, pupils)
                        listView.adapter = adapter
                    }

                } else {

                    runOnUiThread {
                        Toast.makeText(this, "No students found", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {

                runOnUiThread {
                    Toast.makeText(this, "Error loading students", Toast.LENGTH_SHORT).show()
                }

                e.printStackTrace()
            }
        }.start()
    }
}