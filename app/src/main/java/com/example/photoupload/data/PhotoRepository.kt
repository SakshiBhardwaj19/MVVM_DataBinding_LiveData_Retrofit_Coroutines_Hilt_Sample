package com.example.photoupload.data

import com.example.photoupload.data.network.UploadApi
import com.example.photoupload.data.network.UploadPhotoResponse
import com.example.photoupload.util.Exceptions
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.StringBuilder
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val uploadApi: UploadApi) {

    suspend fun uploadPhoto(file: File): UploadPhotoResponse? {

        var body: MultipartBody.Part?

        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file);

        body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val response = uploadApi.uploadPhoto(body)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val errorMessage = StringBuilder()
            errorMessage.append("Error Code: $response.code()")
            throw Exceptions.ApiException(errorMessage.toString())
        }


    }
}