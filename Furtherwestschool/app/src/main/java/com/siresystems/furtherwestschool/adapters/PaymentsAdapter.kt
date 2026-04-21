package com.siresystems.furtherwestschool

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.siresystems.furtherwestschool.models.Pupil

class PaymentsAdapter(
    context: Context,
    private val pupils: ArrayList<Pupil>
) : ArrayAdapter<Pupil>(context, 0, pupils) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.payment_item, parent, false)

        val pupil = pupils[position]

        val studentId = view.findViewById<TextView>(R.id.studentId)
        val studentName = view.findViewById<TextView>(R.id.studentName)
        val studentBalance = view.findViewById<TextView>(R.id.studentBalance)
        val payBtn = view.findViewById<Button>(R.id.payBtn)

        // ✅ FIX: SHOW DATA PROPERLY
        studentId.text = "ID: ${pupil.StudentID}"
        studentName.text = pupil.StudentName

        val total = pupil.TotalFees.toDoubleOrNull() ?: 0.0
        val paid = pupil.PaidFees.toDoubleOrNull() ?: 0.0
        val balance = total - paid

        studentBalance.text = "Balance: $balance"

        // 💳 Payment action
        payBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:*115%23")
            context.startActivity(intent)
        }

        return view
    }
}