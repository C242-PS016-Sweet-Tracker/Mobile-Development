package com.capstone.sweettrack.view.ui.recomendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.sweettrack.data.remote.response.ListEventsItem
import com.coding.sweettrack.R

class RecomendationAdapter(private val onItemClicked: (ListEventsItem) -> Unit) :
    RecyclerView.Adapter<RecomendationAdapter.RecommendationViewHolder>() {

    private val recommendationList = mutableListOf<ListEventsItem>()

    fun setRecommendations(recommendations: List<ListEventsItem>) {
        recommendationList.clear()
        recommendationList.addAll(recommendations)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_view, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(recommendationList[position])
    }

    override fun getItemCount(): Int = recommendationList.size

    inner class RecommendationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val btnDetail: Button = itemView.findViewById(R.id.btn_detail)

        fun bind(recommendation: ListEventsItem) {
            // Set the title and description using name and category
            tvTitle.text = recommendation.name
            tvDescription.text = recommendation.category

            // Load media cover image using Glide
            Glide.with(itemView.context)
                .load(recommendation.mediaCover)
                .placeholder(R.drawable.ic_place_holder)
                .into(imgPhoto)

            // Handle the button click
            btnDetail.setOnClickListener {
                onItemClicked(recommendation)
            }
        }
    }
}