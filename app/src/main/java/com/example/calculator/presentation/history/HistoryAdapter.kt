package com.example.calculator.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.R
import com.example.calculator.databinding.HistoryItemBinding
import com.example.calculator.domain.entity.HistoryItem
import com.example.calculator.domain.historyFormat

class HistoryAdapter(
    private val onItemClick: (HistoryItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var data: List<HistoryItem> = emptyList()

    fun setData(data: List<HistoryItem>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = data[position]
        with(holder.bindings) {
            expression.text = item.expression
            result.text = item.result
            createdAt.text = historyFormat(item.createdAt)
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    class HistoryViewHolder(val bindings:HistoryItemBinding): RecyclerView.ViewHolder(bindings.root)
}
