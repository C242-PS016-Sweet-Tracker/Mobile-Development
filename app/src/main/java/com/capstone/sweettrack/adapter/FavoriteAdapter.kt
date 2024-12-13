package com.capstone.sweettrack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.sweettrack.data.remote.response.Favorite
import com.coding.sweettrack.R

class FavoriteAdapter(private val onItemClicked: (Favorite) -> Unit) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private val favoriteList = mutableListOf<Favorite>()

    fun setFavorites(favorites: List<Favorite>) {
        favoriteList.clear()
        favoriteList.addAll(favorites)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_view, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteList[position])
    }

    override fun getItemCount(): Int = favoriteList.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        private val btnDetail: Button = itemView.findViewById(R.id.btn_detail)

        fun bind(favorite: Favorite) {
            tvTitle.text = favorite.nama_makanan
            tvDescription.text = "${favorite.kalori} kcal"

            Glide.with(itemView.context)
                .load(favorite.img)
                .placeholder(R.drawable.ic_place_holder)
                .into(imgPhoto)

            btnDetail.setOnClickListener {
                onItemClicked(favorite)
            }
        }
    }
}

