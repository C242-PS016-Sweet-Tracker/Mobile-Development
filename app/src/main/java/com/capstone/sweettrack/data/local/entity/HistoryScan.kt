package com.capstone.sweettrack.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "history_scan")
data class HistoryScan(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_hasil") val id_hasil: Int, // Sama seperti id_hasil dari API
    @ColumnInfo(name = "user_id") val user_id: Int,
    @ColumnInfo(name = "nama_makanan") val nama_makanan: String,
    @ColumnInfo(name = "gula") val gula: Double,
    @ColumnInfo(name = "protein") val protein: Double,
    @ColumnInfo(name = "lemak") val lemak: Double,
    @ColumnInfo(name = "gambar_analisa_makanan") val gambar_analisa_makanan: String,
    @ColumnInfo(name = "kalori") val kalori: Double,
) : Parcelable
