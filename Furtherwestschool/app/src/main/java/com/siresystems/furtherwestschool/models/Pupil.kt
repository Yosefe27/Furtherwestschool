package com.siresystems.furtherwestschool.models

import android.media.Image

data class Pupil(
    val StudentID: String,
    val StudentName: String,
    val StudentGrade: String,
    val StudentClass: String,
    val TotalFees: String,
    val PaidFees: String,
    val StudentImage: String? = null
)