package com.example.photoupload.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.photoupload.R
import com.example.photoupload.databinding.ActivityPhotouploadBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.photoupload.util.snackbar
import kotlinx.android.synthetic.main.activity_photoupload.*

@AndroidEntryPoint
class PhotoUploadActivity : AppCompatActivity(), View.OnClickListener {

    val REQUEST_CODE_GALLERY=101;

    private lateinit var binding: ActivityPhotouploadBinding

    private val viewModel by viewModels<PhotoUploadViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_photoupload);
        binding.photoUploadViewModel = viewModel
        binding.lifecycleOwner=this


        btn_upload_photo.setOnClickListener(this)

    }


    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {

                val intent = result.data
                val imageUri = intent?.data

                if (imageUri != null) {
                    viewModel.uploadPhoto(imageUri)

                } else {
                    root_layout.snackbar(resources.getString(R.string.message_select_an_image))
                }


            }
        }

    /**
     * Method to open user gallery
     */
    private fun openUserGalleryToPickPhoto() {

        val galleryIntent = Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }

        startForResult.launch(galleryIntent)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {


        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                openUserGalleryToPickPhoto();
            }


        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }




    override fun onClick(v: View?) {

        when(v){
            btn_upload_photo ->  if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_GALLERY
                )
            } else {
                openUserGalleryToPickPhoto()
            }

        }
    }


}