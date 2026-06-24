package com.siresystems.furtherwestschool.models

data class PaymentHistory (
    val receipt_no: String,
    val amount: String,
    val pay_date: String,
    val channel: String,
    val status: String,
    val student_name: String,
    val student_id: String,
    val image: String = ""
)
