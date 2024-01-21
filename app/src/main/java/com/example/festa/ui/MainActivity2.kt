package com.example.festa.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.festa.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity2 : AppCompatActivity() {
    companion object {
        const val CAMERA_REQUEST_CODE = 100
        const val GALLERY_REQUEST_CODE = 101
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val cameraButton= findViewById(R.id.camera_button) as Button
        val galleryButton = findViewById(R.id.gallery_button) as Button

        cameraButton.setOnClickListener {
            openCamera()
        }

        galleryButton.setOnClickListener {
            openGallery()
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                0
            )
        }

    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                val imageUri: Uri? = data?.data
                // Handle the captured image from the camera
                // For example, you can display it in an ImageView
                // imageView.setImageURI(imageUri)
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                val imageUri: Uri? = data?.data
                // Handle the selected image from the gallery
                // For example, you can display it in an ImageView
                // imageView.setImageURI(imageUri)
            }
        }
    }
}