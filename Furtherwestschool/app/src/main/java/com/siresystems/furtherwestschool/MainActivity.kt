package com.siresystems.furtherwestschool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.messaging.FirebaseMessaging
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

        // ✅ LOGIN CHECK
        if (!prefs.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // ✅ USER INFO
        val name = prefs.getString("name", "Parent")
        val email = prefs.getString("email", "") ?: ""

        // ✅ UI
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        welcomeText.text = "Welcome Parent $name"

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener {
            prefs.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // ✅ LOAD STUDENTS
        loadStudents(email)

        // ✅ NAVIGATION
        findViewById<Button>(R.id.newsBtn).setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
        }

        findViewById<Button>(R.id.paymentsBtn).setOnClickListener {
            startActivity(Intent(this, PaymentsOptions::class.java))
        }

        findViewById<Button>(R.id.otherBtn).setOnClickListener {
            startActivity(Intent(this, OtherOptions::class.java))
        }

        // ✅ FIREBASE TOKEN
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    println("FCM Token: $token")

                    sendTokenToServer(token)
                } else {
                    println("Failed to get FCM token")
                }
            }
    }

    // =========================
    // ✅ SEND TOKEN TO SERVER
    // =========================
    private fun sendTokenToServer(token: String) {

        val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)
        val email = prefs.getString("email", "") ?: ""

        val url = "https://yosefe27-001-site1.ktempurl.com/apis/save_token.php"

        val request = object : StringRequest(
            Request.Method.POST, url,
            { response -> println("Token saved: $response") },
            { error -> error.printStackTrace() }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "email" to email,
                    "token" to token
                )
            }
        }

        Volley.newRequestQueue(this).add(request)
    }

    // =========================
    // ✅ LOAD STUDENTS
    // =========================
    private fun loadStudents(parentEmail: String) {

        val listView = findViewById<ListView>(R.id.pupilList)

        Thread {
            try {

                val url = URL(
                    "https://furtherwest-001-site1.ktempurl.com/apis/get_students.php?parent_id=$parentEmail"
                )

                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                val response = conn.inputStream.bufferedReader().readText()
                val json = JSONObject(response)

                if (json.getString("status") == "success") {

                    val array = json.getJSONArray("students")
                    val pupils = ArrayList<Pupil>()

                    for (i in 0 until array.length()) {

                        val obj = array.getJSONObject(i)

                        pupils.add(
                            Pupil(
                                obj.getString("StudentID"),
                                obj.getString("StudentName"),
                                obj.getString("StudentGrade"),
                                obj.getString("StudentClass"),
                                obj.getString("TotalFees"),
                                obj.getString("prev_term_balance"),
                                obj.getString("current_term_balance"),
                                obj.getString("PaidFees"),
                                obj.optString("StudentImage"),
                                obj.optString("food"),
                                obj.optString("transport")
                            )
                        )
                    }

                    runOnUiThread {
                        listView.adapter = PupilAdapter(this, pupils)
                    }

                } else {
                    runOnUiThread {
                        Toast.makeText(this, "No students found", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()

                runOnUiThread {
                    Toast.makeText(this, "Error loading students", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}