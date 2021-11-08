
package com.example.calculator.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.domain.SettingsDao
import com.example.calculator.domain.entity.ResultPanelType
import com.example.calculator.presentation.common.SingleLiveEvent
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDao: SettingsDao
    ):ViewModel() {
    
    private val _resultPanelState = MutableLiveData<ResultPanelType>(ResultPanelType.RIGHT)
    val resultPanelState: LiveData<ResultPanelType> = _resultPanelState

    private val _openResultPanelAction = SingleLiveEvent<ResultPanelType>()
    val openResultPanelAction: LiveData<ResultPanelType> = _openResultPanelAction

    private val _precisionResult = MutableLiveData<Int>()
    val precisionResult = _precisionResult

    private val _vibrationTime = MutableLiveData<Int>()
    val vibrationTime = _vibrationTime


    init {
        viewModelScope.launch {
            _resultPanelState.value = settingsDao.getResultPanelType()
            _precisionResult.value = settingsDao.getPrecision()
            _vibrationTime.value = settingsDao.getVibration()
        }
    }

    fun onResultPanelTypeChanged(resultPanelType: ResultPanelType) {
        _resultPanelState.value = resultPanelType
        viewModelScope.launch {
            settingsDao.setResultPanelType(resultPanelType)
        }
    }

    fun onPrecisionChanged(precision: Int) {
        _precisionResult.value = precision
        viewModelScope.launch {
            settingsDao.setPrecision(precision)
        }
    }

    fun onVibrationChanged(vibration: Int) {
        _vibrationTime.value = vibration
        viewModelScope.launch {
            settingsDao.setVibration(vibration)
        }
    }

    fun onResultPanelTypeClicked() {
        _openResultPanelAction.value = resultPanelState.value
    }


}
