package com.siresystems.furtherwestschool

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.siresystems.furtherwestschool.adapters.TransportPaymentAdapter

class PaymentsOptions : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payments_options)

        // ✅ TOOLBAR
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Buttons
        val btnFees = findViewById<Button>(R.id.btnFees)
        val btnFood = findViewById<Button>(R.id.btnFood)
        val btnTransport = findViewById<Button>(R.id.btnTransport)
        val btnOther = findViewById<Button>(R.id.btnOther)
        val btnHistory = findViewById<Button>(R.id.btnhistory)
        val btnReceipts = findViewById<Button>(R.id.btnreceipts)

        // Click actions
        btnFees.setOnClickListener {
            // Example:
            startActivity(Intent(this, PaymentsActivity::class.java))
        }

        btnTransport.setOnClickListener {
            startActivity(Intent(this, TransportPaymentActivity::class.java))
        }

        btnFood.setOnClickListener {
            startActivity(Intent(this, TransportPaymentActivity::class.java))
        }

        btnOther.setOnClickListener {
            Toast.makeText(this, "Feature Coming soon", Toast.LENGTH_SHORT).show()
            // startActivity(Intent(this, OtherPaymentsActivity::class.java))
        }

        btnHistory.setOnClickListener {
            startActivity(Intent(this, PaymentHistoryActivity::class.java))
            //Toast.makeText(this, "Feature Coming soon", Toast.LENGTH_SHORT).show()
        }

        btnReceipts.setOnClickListener {
            //startActivity(Intent(this, TransportPaymentActivity::class.java))
            Toast.makeText(this, "Feature Coming soon", Toast.LENGTH_SHORT).show()
        }
    }
}