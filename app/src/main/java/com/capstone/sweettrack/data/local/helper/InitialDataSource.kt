package com.capstone.sweettrack.data.local.helper

import com.capstone.sweettrack.data.local.entity.FavoriteFood
import com.capstone.sweettrack.data.local.entity.HistoryScan


object InitialDataSource {

    fun getDummyHistoryScans(): List<HistoryScan> {
        return listOf(
            HistoryScan(
                id = 1,
                userId = 102,
                imageUri = "file://path/to/image1.jpg",
                name = "Nasi Goreng",
                kalori = 300.0,
                gula = 5.0,
                lemak = 10.0,
                protein = 8.0,
                timestamp = System.currentTimeMillis()
            )
            ,
            HistoryScan(
                id = 2,
                userId = 102,
                imageUri = "file://path/to/image2.jpg",
                name = "Ayam Bakar",
                kalori = 200.0,
                gula = 3.0,
                lemak = 5.0,
                protein = 20.0,
                timestamp = System.currentTimeMillis() - 3600000 // 1 hour ago
            ),
            HistoryScan(
                id = 3,
                userId = 102,
                imageUri = "file://path/to/image3.jpg",
                name = "Pecel Lele",
                kalori = 250.0,
                gula = 2.0,
                lemak = 8.0,
                protein = 15.0,
                timestamp = System.currentTimeMillis() - 7200000 // 2 hours ago
            )
        )
    }






    fun getDummyFavoriteFoods(): List<FavoriteFood> {
        return listOf(
            FavoriteFood(
                id = 1,
                userId = 101,
                name = "Salad Buah",
                kalori = 150.0,
                gula = 10.0,
                lemak = 2.0,
                protein = 3.0
            ),
            FavoriteFood(
                id = 2,
                userId = 101,
                name = "Ikan Bakar",
                kalori = 200.0,
                gula = 0.0,
                lemak = 5.0,
                protein = 25.0
            ),
            FavoriteFood(
                id = 3,
                userId = 102,
                name = "Smoothie Alpukat",
                kalori = 250.0,
                gula = 15.0,
                lemak = 10.0,
                protein = 4.0
            )
        )
    }

    fun getDummyRecommendations(): List<FavoriteFood> {
        return listOf(
            FavoriteFood(1, 1, "Ayam Goreng", 300.0, 10.0, 20.0, 30.0),
            FavoriteFood(2, 1, "Nasi Goreng", 500.0, 20.0, 15.0, 10.0),
            FavoriteFood(3, 1, "Sate Ayam", 400.0, 15.0, 25.0, 20.0)
        )
    }
}
