package com.example.calculator.presentation.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.R
import com.example.calculator.databinding.HistoryItemBinding
import com.example.calculator.domain.entity.HistoryItem

class HistoryAdapter (
    private val onItemClick: (HistoryItem) -> Unit
        ) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val data: List<HistoryItem> = emptyList()

    fun setData(data : List<HistoryItem>) {
        this.data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent)
        return HistoryViewHolder(
            HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = data[position]
        with(holder.bindings) {
            expression.text = item.expression
            result.text = item.result
            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun getItemCount(): Int = data.size


    class HistoryViewHolder(val bindings: HistoryItemBinding) : RecyclerView.ViewHolder(bindings.root)
}
