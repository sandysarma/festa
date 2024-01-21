package com.example.festa.view.signup.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.festa.databinding.ActivitySignUpVerifyBinding
import com.example.festa.view.signup.viewmodel.SignUpViewModel
import com.example.festa.view.logins.ui.SignInActivity
import com.freqwency.promotr.utils.ErrorUtil
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignUpVerifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpVerifyBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var activity: Activity
    private val signUpViewModel: SignUpViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var selectedImageFile: File? = null
    private var strPhoneno: String = ""
    private var strUserName: String = ""
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var verificationId = ""

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                binding.profileImageCreateAcc.setImageBitmap(imageBitmap)
            } else {
                Toast.makeText(this@SignUpVerifyActivity, "dlfsdjlf", Toast.LENGTH_SHORT).show()
            }
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpVerifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseApp.initializeApp(this)
        mAuth = FirebaseAuth.getInstance()

        activity = this
        verificationId = intent.getStringExtra("verificationId").toString()
        strPhoneno = intent.getStringExtra("strPhoneno").toString()
        strUserName = intent.getStringExtra("strUserName").toString()

        binding.resendOtpBtn.setOnClickListener {
            resendVerificationCode(strPhoneno)

        }

        binding.addImageSignUp.setOnClickListener {
            selectImage()
        }

        binding.verify.setOnClickListener {
            val otp = binding.verifySignUpOtp.text.toString()

            if (otp.isEmpty()) {
                Toast.makeText(this, "Please enter otp", Toast.LENGTH_SHORT).show()
            } else {
                val credential: PhoneAuthCredential =
                    PhoneAuthProvider.getCredential(verificationId, otp)
                signInWithPhoneAuthCredential(credential)
            }

        }
        signUpObserver()

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in successful, go to the next activity or perform desired actions
                    getSignUp()
                } else {
                    // Sign in failed
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }


    //main api part
    @SuppressLint("SuspiciousIndentation")
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
            ErrorUtil.handlerGeneralError(this@SignUpVerifyActivity, it)
            // errorDialogs()
        }
    }

    private fun resendVerificationCode(phoneNumber: String) {
        val options = resendToken?.let {
            PhoneAuthOptions.newBuilder(mAuth)
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
                        resendToken: PhoneAuthProvider.ForceResendingToken
                    ) {
                        // Save the new verification ID and resend token
                        this@SignUpVerifyActivity.verificationId = verificationId
                        this@SignUpVerifyActivity.resendToken = resendToken
                        Toast.makeText(this@SignUpVerifyActivity, "OTP Resent", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
                .setForceResendingToken(it)
                .build()
        }

        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(this@SignUpVerifyActivity)
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


}