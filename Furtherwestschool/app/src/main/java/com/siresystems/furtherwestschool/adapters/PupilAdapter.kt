package com.siresystems.furtherwestschool.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.siresystems.furtherwestschool.R
import com.siresystems.furtherwestschool.models.Pupil

class PupilAdapter(
    context: Context,
    private val pupils: ArrayList<Pupil>
) : ArrayAdapter<Pupil>(context, 0, pupils) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.pupil_item, parent, false)

        val pupil = pupils[position]

        // ✅ Bind views
        val name = view.findViewById<TextView>(R.id.nameText)
        val grade = view.findViewById<TextView>(R.id.gradeText)
        val className = view.findViewById<TextView>(R.id.classText)
        val prevBalance = view.findViewById<TextView>(R.id.prevTermText)
        val currentBalance = view.findViewById<TextView>(R.id.currentTermText)
        val balance = view.findViewById<TextView>(R.id.balanceText)
        val feesPaid = view.findViewById<TextView>(R.id.feesText)

        // ✅ SET DATA (FULL FIX)
        name.text = pupil.StudentName
        grade.text = "Grade: ${pupil.StudentGrade}"
        className.text = "Class: ${pupil.StudentClass}"

        prevBalance.text = "Prev Term Balance: ${pupil.PrevTermBalance}"
        currentBalance.text = "Current Term Balance: ${pupil.CurrentTermBalance}"
        feesPaid.text = "Total Fees Paid: ${pupil.PaidFees}"
        val total = pupil.TotalFees.toDoubleOrNull() ?: 0.0
        val paid = pupil.PaidFees.toDoubleOrNull() ?: 0.0
        val bal = total - paid

        //fees.text = "Total Fees: $total"
        balance.text = "Balance: $bal"

        return view
    }
}