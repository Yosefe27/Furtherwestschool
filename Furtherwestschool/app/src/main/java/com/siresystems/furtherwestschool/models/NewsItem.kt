package com.siresystems.furtherwestschool.models

data class NewsItem(
    val date: String,
    val title: String,
    val content: String,
    val image: String = "",
    var expanded: Boolean = false
)