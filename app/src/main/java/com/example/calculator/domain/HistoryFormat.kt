package com.example.calculator.domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun historyFormat(localDateTime: LocalDateTime): String {
    return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss MMMM dd", Locale("en")))
}