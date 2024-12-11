package com.capstone.sweettrack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.sweettrack.data.local.entity.HistoryScan
import com.coding.sweettrack.databinding.ItemHistoryBinding

class HistoryAdapter : ListAdapter<HistoryScan, HistoryAdapter.HistoryViewHolder>(DiffCallback()) {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryScan) {
            binding.apply {
                tvName.text = history.name
                tvKalori.text = "Kalori: ${history.kalori}"
                tvGula.text = "Gula: ${history.gula}"
                tvProtein.text = "Protein: ${history.protein}"
                tvLemak.text = "Lemak: ${history.lemak}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    class DiffCallback : DiffUtil.ItemCallback<HistoryScan>() {
        override fun areItemsTheSame(oldItem: HistoryScan, newItem: HistoryScan): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoryScan, newItem: HistoryScan): Boolean {
            return oldItem == newItem
        }
    }
}
