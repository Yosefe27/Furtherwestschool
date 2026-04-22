package com.siresystems.furtherwestschool.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.siresystems.furtherwestschool.R
import com.siresystems.furtherwestschool.models.Pupil

class TransportPaymentAdapter(
    context: Context,
    private val pupils: ArrayList<Pupil>
) : ArrayAdapter<Pupil>(context, 0, pupils) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.transportpayment_item, parent, false)

        val pupil = pupils[position]

        val studentId = view.findViewById<TextView>(R.id.studentId)
        val studentName = view.findViewById<TextView>(R.id.studentName)
        val transportBalance = view.findViewById<TextView>(R.id.transportBalance)
        val payBtn = view.findViewById<Button>(R.id.payBtn)

        // ✅ FIX: SHOW DATA PROPERLY
        studentId.text = "ID: ${pupil.StudentID}"
        studentName.text = pupil.StudentName

        transportBalance.text = "Balance: ${pupil.TransportFees}"

        // 💳 Payment action
        payBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:*115%23")
            context.startActivity(intent)
        }

        return view
    }
}