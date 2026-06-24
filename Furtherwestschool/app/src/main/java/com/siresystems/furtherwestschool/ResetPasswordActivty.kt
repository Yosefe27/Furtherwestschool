package com.siresystems.furtherwestschool

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnReset: Button

    private val url = "https://furtherwest-001-site1.ktempurl.com/apis/reset_password.php" // CHANGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnReset = findViewById(R.id.btnReset)

        btnReset.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {

        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)
        val email = prefs.getString("email", "")

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val request = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_LONG).show()
                prefs.edit().clear().apply()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email ?: ""
                params["password"] = password
                return params
            }
        }

        Volley.newRequestQueue(this).add(request)
    }
}