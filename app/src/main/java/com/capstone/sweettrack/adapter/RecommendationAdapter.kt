package com.capstone.sweettrack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.sweettrack.data.remote.response.Recommendation
import com.coding.sweettrack.R

class RecommendationAdapter(private val onItemClicked: (Recommendation) -> Unit) :
    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    private val recommendationList = mutableListOf<Recommendation>()

    fun setRecommendations(recommendations: List<Recommendation>) {
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

        fun bind(recommendation: Recommendation) {
            tvTitle.text = recommendation.nama_makanan
            tvDescription.text = "${recommendation.kalori} kcal"

            Glide.with(itemView.context)
                .load(recommendation.img)
                .placeholder(R.drawable.ic_place_holder)
                .into(imgPhoto)

            // Handle the button click
            btnDetail.setOnClickListener {
                onItemClicked(recommendation)
            }
        }
    }
}
