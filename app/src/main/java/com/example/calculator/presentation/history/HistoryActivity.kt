package com.example.calculator.presentation.history

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.R
import com.example.calculator.databinding.HistorActivityBinding
import com.example.calculator.di.HistoryRepositoryProvider
import com.example.calculator.di.SettingsDaoProvider
import com.example.calculator.presentation.common.BaseActivity
import com.example.calculator.presentation.main.MainViewModel
import com.example.calculator.presentation.settings.SettingsViewModel

class HistoryActivity : BaseActivity() {

    private val viewBinding by viewBinding(HistorActivityBinding::bind)
    private val viewModel by viewModels<HistoryViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HistoryViewModel(
                    HistoryRepositoryProvider.get(this@HistoryActivity)
                ) as T
            }
        }
    }
    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate (savedInstanceState)
        setContentView (R.layout.histor_activity)


        val historyAdapter = HistoryAdapter (viewModel::onItemClick)
        with(viewBinding.list) {
            with(viewBinding.list) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = historyAdapter
            }
        }
    viewModel.historyItemState.observe(this) {state ->
        historyAdapter.setData(state)
    }
        viewModel.closeWithResult.observe(this) {state ->
            setResult(RESULT_OK, Intent().putExtra(HISTORY_ACTIVITY_KEY, state))
        }
    }

    companion object {
        const val  HISTORY_ACTIVITY_KEY = "HISTORY_ACTIVITY_KEY"
    }
}