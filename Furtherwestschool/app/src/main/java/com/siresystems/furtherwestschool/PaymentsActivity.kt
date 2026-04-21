package com.siresystems.furtherwestschool

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.siresystems.furtherwestschool.models.Pupil
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class PaymentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        // Toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)
        val parentEmail = prefs.getString("email", "")

        val listView = findViewById<ListView>(R.id.paymentList)

        if (parentEmail.isNullOrEmpty()) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        loadPayments(parentEmail, listView)
    }

    // ✅ LOAD FROM DATABASE
    private fun loadPayments(parentEmail: String, listView: ListView) {

        Thread {

            try {

                val url = URL(
                    "https://yosefe27-001-site1.ktempurl.com/apis/get_students.php?parent_id=$parentEmail"
                )

                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                val response = conn.inputStream.bufferedReader().readText()

                val json = JSONObject(response)

                if (json.getString("status") == "success") {

                    val array = json.getJSONArray("students")
                    val paymentsList = ArrayList<Pupil>()

                    for (i in 0 until array.length()) {

                        val obj = array.getJSONObject(i)

                        paymentsList.add(
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
                        val adapter = PaymentsAdapter(this, paymentsList)
                        listView.adapter = adapter
                    }

                } else {

                    runOnUiThread {
                        Toast.makeText(this, "No payments found", Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()

                runOnUiThread {
                    Toast.makeText(this, "Error loading payments", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}