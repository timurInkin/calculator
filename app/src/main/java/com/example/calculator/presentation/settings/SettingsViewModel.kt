
package com.example.calculator.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.domain.HistoryRepository
import com.example.calculator.domain.SettingsDao
import com.example.calculator.domain.entity.ResultPanelType
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsDao: SettingsDao,
    get: HistoryRepository
):ViewModel() {
    
    private val _resultPanelState = MutableLiveData<ResultPanelType>()
    val resultPanelType: LiveData<ResultPanelType> = _resultPanelState

    private val _openResultPanelAction = SingleLiveEvent<ResultPanelType>()
    val openResultPanelAction: LiveData<ResultPanelType> = _openResultPanelAction

    init {
        viewModelScope.launch {
            settingsDao.getResultPanelType()
        }
    }

    fun onResultPanelTypeChanged(resultPanelType: ResultPanelType) {
        _resultPanelState.value = resultPanelType
        viewModelScope.launch {
            settingsDao.setResultPanelType(resultPanelType)
        }
    }
    fun onResultPanelTypeClicked() {
        _openResultPanelAction.value = _resultPanelState.value
    }
}
