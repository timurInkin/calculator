package com.example.calculator.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryItem(
    val expression: String,
    val result: String
) : Parcelable