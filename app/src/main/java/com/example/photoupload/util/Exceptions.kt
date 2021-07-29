package com.example.photoupload.util

import java.io.IOException

class Exceptions {

    class ApiException(message: String) : IOException(message)
    class NoInternetException(message: String) : IOException(message)
    class UploadFailureException(message: String): IOException(message)
}