package com.example.photoupload.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitmapUtils @Inject constructor(private val fileUtils: FileUtils) {


    val MAX_HEIGHT_HD = 720
    val MAX_WIDTH_HD = 1275

    /**
     * Function to compress image
     */
     suspend fun compressImage(uri: Uri) : String?{

        return withContext(Dispatchers.IO){

            var filePath: String? = null

            filePath = fileUtils.getRealPathFromURI(uri)

            // First decode with inJustDecodeBounds=true to check dimensions
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(
                options, MAX_WIDTH_HD,
                MAX_HEIGHT_HD
            )

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            var scaledBitmap: Bitmap?
            scaledBitmap = BitmapFactory.decodeFile(filePath, options)

            try {
                scaledBitmap =
                    Bitmap.createBitmap(scaledBitmap!!, 0, 0, scaledBitmap.width, scaledBitmap.height)

            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                if (scaledBitmap != null) {
                    var out: FileOutputStream? = null
                    filePath = fileUtils.getFilePath()
                    out = FileOutputStream(filePath)
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
                    scaledBitmap.recycle()
                    return@withContext filePath
                }
            }


            return@withContext filePath

        }

    }

    /**
     * Function to calculate in sample size
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2


            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize += 1
            }
        }
        return inSampleSize
    }

}