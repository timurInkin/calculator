package com.example.calculator.domain

import com.example.calculator.domain.entity.ResultPanelType

interface SettingsDao {

    /**
     * устанавливает тип отображения панели результата
     **/

    suspend fun setResultPanelType(resultPanelType: ResultPanelType)

    /**
     * получает тип отображения панели результата
     **/

    suspend fun getResultPanelType(): ResultPanelType
}
