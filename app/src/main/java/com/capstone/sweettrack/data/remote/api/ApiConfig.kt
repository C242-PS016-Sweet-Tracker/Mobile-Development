package com.capstone.sweettrack.data.remote.api

//import com.coding.sweettrack.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
//        private const val BASE_URL = BuildConfig.BASE_URL // Ganti dengan pake URL API
//        private const val BASE_URL = "http://192.168.249.243:8080/" // endpoint sementara isp public

        fun getApiService(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().apply { HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
//                .baseUrl(BASE_URL)
                .baseUrl("https://event-api.dicoding.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}
