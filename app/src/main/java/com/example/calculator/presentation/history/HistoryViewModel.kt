package com.example.calculator.presentation.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculator.domain.HistoryRepository
import com.example.calculator.domain.entity.HistoryItem
import com.example.calculator.presentation.common.SingleLiveEvent
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val historyRepository: HistoryRepository
): ViewModel() {


    private val _historyItemState = MutableLiveData<List<HistoryItem>>()
    val historyItemState = _historyItemState

    private val _closeWithResult = SingleLiveEvent<HistoryItem>()
    val closeWithResult = _closeWithResult

    init {
        viewModelScope.launch {
            _historyItemState.value = historyRepository.getAll()
        }
    }

    fun onItemClick(historyItem: HistoryItem) {
        _closeWithResult.value = historyItem
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clear()
            _historyItemState.value = historyRepository.getAll()
        }
    }
}
