package com.siresystems.furtherwestschool

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // UI Elements
        val emailEditText = findViewById<EditText>(R.id.email)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val signupText = findViewById<TextView>(R.id.signupText)
        val callText = findViewById<TextView>(R.id.callText)
        val whatsappText = findViewById<TextView>(R.id.whatsappText)

        // Replace with your real number
        val phoneNumber = "260978231172"

        // ✅ LOGIN BUTTON LOGIC
        loginBtn.setOnClickListener {

            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Thread {
                try {
                    // 🔥 IMPORTANT: Replace with YOUR IP ADDRESS
                    val url = URL("https://yosefe27-001-site1.ktempurl.com/apis/login_user.php")
                    val conn = url.openConnection() as HttpURLConnection

                    conn.requestMethod = "POST"
                    conn.doOutput = true

                    val data = "email=$email&password=$password"

                    val os = conn.outputStream
                    os.write(data.toByteArray())
                    os.flush()
                    os.close()

                    val reader = BufferedReader(InputStreamReader(conn.inputStream))
                    val response = StringBuilder()
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }

                    runOnUiThread {
                        val result = response.toString()

                        try {
                            val json = org.json.JSONObject(result)

                            if (json.getString("status") == "success") {

                                val user = json.getJSONObject("user")
                                val name = user.getString("name")
                                val email = user.getString("email")

                                // ✅ SAVE SESSION HERE (ONLY HERE)
                                val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)
                                val editor = prefs.edit()
                                editor.putString("name", name)
                                editor.putString("email", email)
                                editor.putBoolean("isLoggedIn", true)
                                editor.apply()

                                Toast.makeText(this, "Welcome $name", Toast.LENGTH_SHORT).show()

                                startActivity(Intent(this, MainActivity::class.java))
                                finish()

                            } else {
                                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                            }

                        } catch (e: Exception) {
                            Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }.start()
        }

        // 👉 Go to Signup
        signupText.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // 📞 Open Dialer
        callText.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }

        // 💬 Open WhatsApp
        whatsappText.setOnClickListener {
            try {
                val url = "https://wa.me/$phoneNumber"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}