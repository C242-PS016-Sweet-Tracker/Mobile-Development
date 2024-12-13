package com.capstone.sweettrack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.coding.sweettrack.R

class ActivityLevelAdapter(
    private val activityLevels: List<Pair<String, String>>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ActivityLevelAdapter.ActivityLevelViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION

    inner class ActivityLevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleActTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        fun bind(activityLevel: Pair<String, String>, isSelected: Boolean) {
            titleTextView.text = activityLevel.first
            descriptionTextView.text = activityLevel.second

            if (isSelected) {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.green))
            } else {
                itemView.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        android.R.color.white
                    )
                )
            }

            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onItemClick(activityLevel.first)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityLevelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity_level, parent, false)
        return ActivityLevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityLevelViewHolder, position: Int) {
        holder.bind(activityLevels[position], position == selectedPosition)
    }

    override fun getItemCount() = activityLevels.size
}


