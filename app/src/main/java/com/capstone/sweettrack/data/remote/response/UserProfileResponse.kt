package com.capstone.sweettrack.data.remote.response

data class UserProfileResponse(
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val describe: String? = null,
    val data: UserProfile? = null
)

data class UserProfile(
    val username: String,
    val tipe_diabetes: String?,
    val user_umur: Int,
    val nama_lengkap_user: String,
    val user_email: String,
    val jenis_kelamin: String,
    val foto: String
)

data class ApiResponse(
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val describe: String? = null
)

data class DetailUser(
    val nama_lengkap_user: String,
    val jenis_kelamin: String,
    val user_umur: Int,
    val tinggi_badan: Double,
    val berat_badan: Double,
    val tingkat_aktivitas: String,
    val tipe_diabetes: String,
    val kadar_gula: Double,
    val kalori: Double,
    val user_id: Int
)

data class DetailUserResponse(
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val describe: String? = null,
    val data: DetailUser? = null
)


data class AddDetailUserRequest(
    val namaLengkap: String? = null,
    val jenisKelamin: String? = null,
    val umur: Int? = null,
    val tinggiBadan: Double? = null,
    val beratBadan: Double? = null,
    val tingkatAktifitas: String? = null,
    val tipeDiabetes: String? = null,
    val kadarGula: Double? = null,
    val user_id: Int? = null
)

data class EditDetailUserRequest(
    val namaLengkap: String? = null,
    val jenisKelamin: String? = null,
    val umur: Int? = null,
    val tinggiBadan: Double? = null,
    val beratBadan: Double? = null,
    val tingkatAktifitas: String? = null,
    val tipeDiabetes: String? = null,
    val kadarGula: Double? = null,
    val kalori: Double? = 0.0,
)
