package com.siresystems.furtherwestschool.models

data class NewsItem(
    val date: String,
    val title: String,
    val content: String,
    var expanded: Boolean = false
)