package com.capstone.sweettrack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.sweettrack.data.remote.response.History
import com.coding.sweettrack.R
import com.coding.sweettrack.databinding.ItemHistoryBinding

class HistoryAdapter : ListAdapter<History, HistoryAdapter.HistoryViewHolder>(DiffCallback()) {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: History) {
            binding.apply {

                Glide.with(itemView.context)
                    .load(history.gambar_analisa_makanan)
                    .placeholder(R.drawable.ic_place_holder)
                    .into(imgFood)

                if (history.nama_makanan != "") {
                    tvName.text = history.nama_makanan
                    tvKalori.text = "Kalori: ${history.kalori}"
                    tvGula.text = "Gula: ${history.gula}"
                    tvLemak.text = "Lemak: ${history.lemak}"
                    tvProtein.text = "Protein: ${history.protein}"
                } else {
                    tvName.text = ""
                    tvKalori.text = ""
                    tvGula.text = "Gula: ${history.gula}"
                    tvProtein.text = ""
                    tvLemak.text = ""
                }
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

    class DiffCallback : DiffUtil.ItemCallback<History>() {
        override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem.id_hasil == newItem.id_hasil
        }

        override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
            return oldItem == newItem
        }
    }
}
