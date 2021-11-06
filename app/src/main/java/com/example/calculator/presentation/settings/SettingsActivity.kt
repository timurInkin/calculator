package com.example.calculator.presentation.settings

import android.app.AlertDialog
import android.os.Bundle
import android.system.Os.bind

import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.calculator.presentation.common.BaseActivity
import com.example.calculator.R
import com.example.calculator.databinding.SettingsActivityBinding
import com.example.calculator.di.HistoryRepositoryProvider
import com.example.calculator.di.SettingsDaoProvider
import com.example.calculator.domain.entity.ResultPanelType

class SettingsActivity : BaseActivity() {
    private val viewBinding by viewBinding(SettingsActivityBinding::bind)
    private val viewModel by viewModels<SettingsViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SettingsViewModel(SettingsDaoProvider.get(this@SettingsActivity)) as T
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        viewBinding.settingBack.setOnClickListener {
            finish()
        }
        viewBinding.resultPanelContainer.setOnClickListener {
            viewModel.onResultPanelTypeClicked()
        }

        viewModel.resultPanelType.observe(this) { state ->
            viewBinding.resultPanelDescription.text =
                resources.getStringArray(R.array.result_panel_types)[state.ordinal]
        }
        viewModel.openResultPanelAction.observe(this) { type ->
            showDialog(type)
        }
    }
    private fun showDialog(type: ResultPanelType) {
        var result: Int? = null
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.settings_result_panel_title))
//            .setMessage("Message")
            .setPositiveButton("OK") { dialog, id ->
                result?.let {viewModel.onResultPanelTypeChanged(ResultPanelType.values()[it])}
            }
            .setNegativeButton("Fuck U") {dialog, id ->

            }

            .setSingleChoiceItems(R.array.result_panel_types, type.ordinal) { dialog, id ->
                result = id

            }
            .show()

    }
}

