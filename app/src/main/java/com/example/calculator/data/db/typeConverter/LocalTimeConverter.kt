package com.example.calculator.data.db.typeConverter

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalTimeConverter {

    @TypeConverter
    fun toString(localDateTime: LocalDateTime): String =
        localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime =
        LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

}