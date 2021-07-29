package com.example.photoupload.ui

import android.app.Application
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.lifecycle.*
import com.example.photoupload.R
import com.example.photoupload.data.network.UploadPhotoResponse
import com.example.photoupload.data.PhotoRepository
import com.example.photoupload.util.BitmapUtils
import com.example.photoupload.util.Exceptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class PhotoUploadViewModel @Inject constructor(
    private val photoRepository: PhotoRepository, private val bitmapUtils: BitmapUtils,
) : ViewModel() {

    private val _showProgress:MutableLiveData<Boolean> = MutableLiveData()
    val showProgress: LiveData<Boolean>
        get() =_showProgress

    private val _photoUploadedUrl: MutableLiveData<UploadPhotoResponse> = MutableLiveData()
    val photoUploadUrl: LiveData<UploadPhotoResponse>
        get() = _photoUploadedUrl


    private val _errorMessage:MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String>
        get() =_errorMessage



    /**
     * Method to upload photo
     */
    fun uploadPhoto(uri: Uri) {

        _showProgress.value=true


        viewModelScope.launch {

           val filePath= bitmapUtils.compressImage(uri)

            filePath?.let{
                File(it)
                val file = File(it)

                if (file != null) {
                    try {
                        val uploadPhotoResponse: UploadPhotoResponse?

                        val quote = "The eagle has landed."
                        println("The length of the quote is $quote.length")

                        withContext(Dispatchers.IO) {
                            uploadPhotoResponse = photoRepository.uploadPhoto(file)
                        }

                        if (uploadPhotoResponse?.mediaURL != null) {
                            _showProgress.value=false
                            _photoUploadedUrl.postValue(uploadPhotoResponse)


                        }


                    } catch (e: Exceptions.ApiException) {
                        _showProgress.value=false
                        _errorMessage.value=e.message
                    } catch (e: Exceptions.NoInternetException) {
                        _showProgress.value=false
                        _errorMessage.value=e.message
                    }


                }

            }



        }
    }







}