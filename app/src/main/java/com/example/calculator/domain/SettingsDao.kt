package com.example.calculator.domain

import com.example.calculator.domain.entity.ResultPanelType

interface SettingsDao {

    suspend fun setResultPanelType(resultPanelType: ResultPanelType)
    suspend fun getResultPanelType(): ResultPanelType


    suspend fun getPrecision() : Int
    suspend fun setPrecision(precision:Int)

    suspend fun getVibration() : Int
    suspend fun setVibration(vibration:Int)

}
