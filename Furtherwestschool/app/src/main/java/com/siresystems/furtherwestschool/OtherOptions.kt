package com.siresystems.furtherwestschool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.siresystems.furtherwestschool.adapters.TransportPaymentAdapter

class OtherOptions : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.other_options)

        val prefs = getSharedPreferences("UserSession", MODE_PRIVATE)

        // ✅ TOOLBAR
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Buttons
        val btnCalendar = findViewById<Button>(R.id.btnCalendar)
        val btnChangePassword = findViewById<Button>(R.id.btnChangePassword)
        val btnAppSupport = findViewById<Button>(R.id.btnAppSupport)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // Click actions
        btnCalendar.setOnClickListener {
            // Example:
            startActivity(Intent(this, Calendar_Activity::class.java))
        }

        btnChangePassword.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        btnAppSupport.setOnClickListener {
            //Toast.makeText(this, "Feature Coming soon", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, TechSupportActivity::class.java))
        }

        btnLogout.setOnClickListener {
            prefs.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}