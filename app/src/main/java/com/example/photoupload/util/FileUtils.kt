package com.example.photoupload.util

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileUtils @Inject constructor(@ApplicationContext context: Context){

    private val context : Context = context.applicationContext

    /**
     * Function to get FilePath
     */
     fun getFilePath(): String? {
        val file = File(

            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "sample/Images"
        )
        if (!file.exists()) {
            file.mkdirs()
        }
        return (file.absolutePath + "/"
                + System.currentTimeMillis() + ".jpg")
    }



    /**
     * Get real path from Uri
     */
     fun getRealPathFromURI(uri: Uri): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri, projection, null, null,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }

        return null;
    }
}