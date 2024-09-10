package com.example.myapplication.QC.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.QC.data.model.HistoryItem
import com.example.myapplication.databinding.ItemHistoryBinding

class HistoricAdapter : ListAdapter<HistoryItem, HistoricAdapter.ViewHolder>(HistoryItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HistoryItem) {
            binding.textViewDate.text = "Date : "+item.date
            binding.textViewLine.text = "Line : "+item.line
            binding.textViewTeam.text = "Team : "+item.team
            binding.textViewDefectsCount.text = "Defects count : "+item.defectsCount.toString()

            binding.executePendingBindings()
        }
    }

    private class HistoryItemDiffCallback : DiffUtil.ItemCallback<HistoryItem>() {
        override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
            return oldItem.defectsCount == newItem.defectsCount
        }

        override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem): Boolean {
            return oldItem == newItem
        }
    }
}
