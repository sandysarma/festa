package com.example.festa.view.signup.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.developer.kalert.KAlertDialog
import com.example.festa.view.logins.ui.SignInActivity
import com.example.festa.R
import com.example.festa.databinding.ActivitySignUpBinding
import com.example.festa.view.signup.viewmodel.SignUpViewModel
import com.freqwency.promotr.utils.ErrorUtil
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var activity: Activity
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var selectedImageFile: File? = null
    private lateinit var byteArrayOutputStream: ByteArrayOutputStream
    private lateinit var byteArray: ByteArray
    private lateinit var auth: FirebaseAuth
    private var verificationId: String = ""
    private var strPhoneno: String = ""
    private var strUserName: String = ""
    private val CAMERA_PERMISSION_CODE = 101
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap

                if (imageBitmap != null) {
                    // Convert Bitmap to File
                    selectedImageFile = bitmapToFile(imageBitmap)
                    setImageOnImageView(selectedImageFile)
                    // Now you can use the 'imageFile' as needed
                    binding.profileImageCreateAcc.setImageBitmap(imageBitmap)
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Image capture cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission not granted, show explanation dialog
            showCameraPermissionExplanationDialog()
        } else {
            // Permission already granted, proceed with camera operation
            selectImage()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activity = this

        auth = FirebaseAuth.getInstance()
        byteArrayOutputStream = ByteArrayOutputStream()



        Log.e("profileImage", "image....$selectedImageFile")

        binding.addImageSignUp.setOnClickListener()
        {
            requestCameraPermission()
        }
        binding.signupsendotp.setOnClickListener {
            strPhoneno = binding.userPhoneNoEditText.text.toString().trim()
            strUserName = binding.UserNameEditText.text.toString()

            if (selectedImageFile == null) {
                Toast.makeText(
                    this,
                    "Please upload image",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (strUserName.isEmpty()) {
                // User name is blank, show a message
                Toast.makeText(
                    this,
                    "Please enter user name",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (strPhoneno.isEmpty()) {
                // Mobile number is blank, show a message
                Toast.makeText(
                    this,
                    "Please enter mobile number",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (strPhoneno.length != 10) {
                // Mobile number is not 10 digits, show a different message
                Toast.makeText(
                    this,
                    "Please enter a 10-digit mobile number",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Both user name and mobile number are valid, proceed with further actions

                // sendVerificationCode(strPhoneno)
                getSignUp()
            }
        }



        binding.signupsendotptosignin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        // Obeserver
        signUpObserver()

    }

    //for getting data from api


    //end for getting from api
    private fun errorDialogs() {
        KAlertDialog(this, KAlertDialog.ERROR_TYPE)
            .setTitleText("Invalid")
            .setContentText("Register Error")
            .confirmButtonColor(R.color.purple_600)
            .setConfirmClickListener(getString(R.string.OK), R.color.white_font, null)
            .show()

    }


    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this@SignUpActivity)
        builder.setTitle("Select Image")
        builder.setItems(options) { dialog, item ->

            when {
                options[item] == "Take Photo" -> openCamera()
                options[item] == "Choose from Gallery" -> openGallery()
                options[item] == "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun showCameraPermissionExplanationDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Camera Permission Needed")
        alertDialog.setMessage("This app needs camera permission to take pictures.")
        alertDialog.setPositiveButton("OK") { _, _ ->
            // Request camera permission when the user clicks OK
            ActivityCompat.requestPermissions(
                this@SignUpActivity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*" // Allow all image types
        selectImageLauncher.launch(galleryIntent)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(this.packageManager) != null) {
            takePictureLauncher.launch(cameraIntent)
        }
    }

    private fun setImageOnImageView(imageFile: File?) {
        if (imageFile != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                Uri.fromFile(imageFile)
            )
            binding.profileImageCreateAcc.setImageBitmap(bitmap)
        }
    }


    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val selectedImageUri = data.data
                    if (selectedImageUri != null) {
                        selectedImageFile = convertUriToFile(selectedImageUri)
                        if (selectedImageFile != null) {
                            // Now you have the image file in File format, you can use it as needed.
                            setImageOnImageView(selectedImageFile)
                        } else {
                            // Handle the case where conversion to File failed
                            showToast("Error converting URI to File")
                        }
                    } else {
                        // Handle the case where the URI is null
                        showToast("Selected image URI is null")
                    }
                } else {
                    // Handle the case where data is null
                    showToast("No data received")
                }
            } else {
                // Handle the case where the result code is not RESULT_OK
                showToast("Action canceled")
            }
        }

    private fun convertUriToFile(uri: Uri): File? {
        try {
            val inputStream: InputStream? = this.contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val fileName = getFileNameFromUri(uri)
                val outputFile = File(this.cacheDir, fileName)
                val outputStream = FileOutputStream(outputFile)
                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }
                return outputFile
            }
        } catch (e: Exception) {
            // Log the exception for debugging purposes
            Log.e("ConversionError", "Error converting URI to File: ${e.message}", e)
        }
        return null
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun getFileNameFromUri(uri: Uri): String {
        var result: String? = null
        val contentResolver: ContentResolver = this.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    result = it.getString(displayNameIndex)
                }
            }
        }
        return result ?: "file"
    }


    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Automatically sign in the user when verification is done
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Handle error
                    Log.e("UserMessage", "Verification failed: ${e.message}")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    // Save the verification ID
                    this@SignUpActivity.verificationId = verificationId
                    val intent = Intent(this@SignUpActivity, SignUpVerifyActivity::class.java)
                    intent.putExtra("verificationId", verificationId)
                    intent.putExtra("strPhoneno", strPhoneno)
                    intent.putExtra("strUserName", strUserName)
                    startActivity(intent)
                    Log.e("UserMessage", "Verification failed: $verificationId")
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in successful, go to the next activity or perform desired actions
                    Log.e("UserMessage", "onCreate: Successfully")

                } else {
                    // Sign in failed
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with camera operation
                    openCamera()
                } else {
                    // Permission denied, handle accordingly (show a message, disable camera functionality, etc.)
                }
            }
            // Handle other permissions if needed
            // ...
        }
    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        // Create a file in the cache directory
        val file = File(this.cacheDir, "image.jpg")

        // Convert the bitmap to a byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bitmapData = byteArrayOutputStream.toByteArray()

        // Write the bytes into the file
        FileOutputStream(file).use { fileOutputStream ->
            fileOutputStream.write(bitmapData)
            fileOutputStream.flush()
        }

        return file
    }


    private fun getSignUp() {
        val fullName =
            RequestBody.create(MultipartBody.FORM, strUserName)
        val PhoneNo =
            RequestBody.create(MultipartBody.FORM, strPhoneno)
        val address_document = MultipartBody.Part.createFormData(
            "profileImage",
            selectedImageFile?.name,
            RequestBody.create("*image/*".toMediaTypeOrNull(), selectedImageFile!!)
        )
        Log.e("profileImage", "image....$selectedImageFile")

        signUpViewModel.getSignUp(
            progressDialog,
            activity,
            fullName,
            PhoneNo,
            address_document
        )


    }

    private fun signUpObserver() {
        signUpViewModel.progressIndicator.observe(this) {
        }
        signUpViewModel.mSignUpResponse.observe(
            this
        ) {
            val intent = Intent(this, SignInActivity::class.java)
            //  intent.putExtra("id",id)
            intent.putExtra("login", "login")
            startActivity(intent)
            finish()

        }
        signUpViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@SignUpActivity, it)
            // errorDialogs()
        }
    }
}