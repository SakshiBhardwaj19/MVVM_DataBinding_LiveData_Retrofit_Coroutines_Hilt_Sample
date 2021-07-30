package com.example.photoupload.data.network

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadApi {

    @Multipart
    @POST("media/upload")
    suspend fun uploadPhoto(@Part file: MultipartBody.Part):Response<UploadPhotoResponse>

    companion object{

        private const val BASE_URL="https://api-test.myapp.com/sample/api/"

        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : UploadApi{

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UploadApi::class.java)
        }
    }
}