package com.example.festa.view.profile.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.festa.view.events.ui.EventListFragment

import com.example.festa.view.logins.ui.SignInActivity
import com.example.festa.view.profile.viewmodel.feedback.FeedBackBody
import com.example.festa.view.profile.viewmodel.feedback.FeedBackViewModel
import com.example.festa.view.profile.viewmodel.getprofile.GetProfileViewModel
import com.example.festa.view.profile.viewmodel.updateprofile.UserUpdateViewModel
import com.example.festa.R
import com.example.festa.application.Festa
import com.example.festa.databinding.FragmentProfileBinding
import com.example.festa.ui.theme.bookmark.ui.BookMark
import com.freqwency.promotr.utils.ErrorUtil
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    lateinit var radioButton: RadioButton
    private lateinit var formattedRatingText: String
    private lateinit var feedbackBody: FeedBackBody
    private lateinit var activity: Activity
    private var eventId = " "
    private var userId = " "
    private var strPhoneno = " "
    private var strUserName = " "
    private var strEmail = " "
    private var statuscheck = " "

    private val CAMERA_PERMISSION_CODE = 101
    val progressDialog by lazy { CustomProgressDialog(requireActivity()) }

    private val feedmodel: FeedBackViewModel by viewModels()
    private val getProfileModel: GetProfileViewModel by viewModels()
    private val updateProfileModel: UserUpdateViewModel by viewModels()
    private var selectedImageFile: File? = null


    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap

                if (imageBitmap != null) {
                    // Convert Bitmap to File
                    selectedImageFile = bitmapToFile(imageBitmap)
                    setImageOnImageView(selectedImageFile)
                    // Now you can use the 'imageFile' as needed
                    binding.userPic.setImageBitmap(imageBitmap)
                } else {
                    Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "Image capture cancelled", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE
            )
        } else {
            // Permission already granted, proceed with camera operation
            selectImage()
        }
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)


        eventId = Festa.encryptedPrefs.eventIds
        userId = Festa.encryptedPrefs.UserId
        activity = requireActivity()
        Log.e("eventIdddd", "eventIds: $eventId,$userId")

        binding.logoutBtn.setOnClickListener {
            val logoutDialog = Dialog(activity)
            logoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            logoutDialog.setContentView(R.layout.logoutdialog)
            val noDialog = logoutDialog.findViewById<LinearLayout>(R.id.noDialog)
            val yesDialog = logoutDialog.findViewById<LinearLayout>(R.id.yesDialog)
            logoutDialog.show()
            val window = logoutDialog.window
            window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            yesDialog.setOnClickListener {
                val intent = Intent(activity, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // To clean up all activities
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                Festa.encryptedPrefs.UserId = ""
                Toast.makeText(activity, "User logout successfully", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                logoutDialog.dismiss()
            }
            noDialog.setOnClickListener { logoutDialog.dismiss() }
        }

        binding.relativeAccEdit.setOnClickListener {
            binding.ProfFistCard.visibility = View.GONE
            binding.profileInside.visibility = View.VISIBLE
            binding.myProfileEditBtn.visibility = View.GONE
            binding.editUpdateDate.visibility = View.VISIBLE
            getUserDetailApi()

        }

        binding.editBtn.setOnClickListener {
            binding.deleteAccountBtn.visibility = View.GONE
            binding.updateBtn.visibility = View.VISIBLE
            binding.setTextViewData.visibility = View.GONE
            binding.editUpdateDate.visibility = View.VISIBLE
            binding.selectImage.visibility = View.VISIBLE
        }

        binding.Relativefeedback.setOnClickListener {
            // binding.FeedbackView.setVisibility(View.VISIBLE)
            binding.linearMyProfile.visibility = View.GONE
            binding.profile.visibility = View.GONE
        }

        binding.relativeCustomerSupport.setOnClickListener {
            binding.linearSupportRequest.visibility = View.VISIBLE
            //binding.linearMyProfile.setVisibility(View.GONE)
        }

        binding.relativeBookmarkList.setOnClickListener {
            val intent = Intent(getActivity(), BookMark::class.java)
            startActivity(intent)
        }
        binding.backBtn.setOnClickListener {
            val paymentsFragment = EventListFragment()
            val fr = requireFragmentManager().beginTransaction()
            fr.replace(R.id.containers, paymentsFragment)
            fr.commit()
            //dialog.dismiss()

        }

        binding.updateBtn.setOnClickListener {
            strEmail = binding.editUserEmail.text.toString()
            strPhoneno = binding.editUserPhoneNumber.text.toString()
            strUserName = binding.editUserName.text.toString()

            Log.e(
                "profileImageAS",
                "image....$selectedImageFile" + " strUserName " + strUserName + "strPhoneno " + strPhoneno + "strEmail " + strEmail
            )
            updateApi(strUserName, strPhoneno, strEmail)
        }
        binding.selectImage.setOnClickListener {
            requestCameraPermission()
        }

        /* if (binding.radioOne.isChecked()) {
             Log.d("QAOD", "suggestion")
             statuscheck = "suggestion"
         } else if (binding.radioTwo.isChecked()) {
             Log.d("QAOD", "Issue")
             statuscheck = "issue"
         } else if (binding.three.isChecked()) {
             Log.d("QAOD", "Other")
             statuscheck = "other"
         }
         */
        /*      binding.radioOne.setOnClickListener { checkButton(it) }
              binding.radioTwo.setOnClickListener { checkButton(it) }
              binding.three.setOnClickListener { checkButton(it) }

              binding.ratingBarId.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                  formattedRatingText = "%.1f stars".format(rating)
                  val string = "stars"
                  //binding.ratingText.text=  reviewList[position].rating.toString().format(formattedRatingText)
                  //  reviewList[position].rating.toString()

              }*//*
        addcohostObserver()
        binding.feedbacksavebtn.setOnClickListener {
            feedback()
        }*/
        getUserDetailApi()
        getUserObserver()
        updateObserver()
        return binding.root

    }

    /*  private fun checkButton(view: View) {
          val radioId = binding.radiogroup.checkedRadioButtonId
          radioButton = view.findViewById(radioId)

          Toast.makeText(requireContext(), "Selected radio button ${radioButton.text}", Toast.LENGTH_SHORT).show()
      }*/

    private fun someFunction() {
        // Check if the property is initialized before using it
        if (::formattedRatingText.isInitialized) {
            // Access the property here
            println(formattedRatingText)
        } else {
            // Handle the case where the property is not initialized
            // You may want to throw an exception, provide a default value, or initialize it here
            formattedRatingText = "Default Rating Text"
        }
    }


    private fun addcohostObserver() {
        feedmodel.progressIndicator.observe(viewLifecycleOwner) {}
        feedmodel.mfeedbackresponse.observe(viewLifecycleOwner) {
            val message = it.peekContent().message
            if (eventId.isEmpty()) {
                Toast.makeText(requireContext(), "please create event", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Co-host added successfully", Toast.LENGTH_SHORT)
                    .show()
            }


        }
        feedmodel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
            // errorDialogs()
        }
    }


    /* private fun feedback() {

         formattedRatingText = view?.findViewById<RatingBar>(R.id.rating_barId).toString()
         val mesage = view?.findViewById<EditText>(R.id.comment)

         binding.feedbacksavebtn.setOnClickListener {
             if (::formattedRatingText.isInitialized) {
                 Toast.makeText(requireActivity(), "Please enter ", Toast.LENGTH_SHORT)
                     .show()

             } else if (binding.comment.text.isNullOrBlank()) {
                 Toast.makeText(requireActivity(), "Please enter phone no", Toast.LENGTH_SHORT)
                     .show()

             } else {
                 if (mesage != null) {
                     feedbackBody =
                         FeedbackBody(
                             ratings = binding.ratingBarId.rating.toString(),
                             messages = binding.comment.text.toString(),
                             feedbackType = statuscheck
                         )


                     feedmodel.getfeedback(
                         progressDialog,
                         activity, userId, eventId, feedbackBody
                     )
                 }

             }
         }
     }*/

    // Add Guest List  API
    private fun getUserDetailApi() {
        getProfileModel.getProfile(progressDialog, activity, userId)

    }

    private fun getUserObserver() {
        getProfileModel.progressIndicator.observe(requireActivity()) {}
        getProfileModel.mguestlist.observe(requireActivity()) {
            val message = it.peekContent().success

            val strUserName = it.peekContent().userDetails?.fullName.toString()
            val strUserEmail = it.peekContent().userDetails?.email.toString()
            val strUserPhone = it.peekContent().userDetails?.phoneNo.toString()
            val strUserPic = it.peekContent().userDetails?.profileImage

            /*  binding.userName.text = strUserName.toString()

              binding.userEmail.text = strUserEmail.toString()
              binding.userPhoneNumber.text = strUserPhone.toString()*/

            binding.editUserName.text = Editable.Factory.getInstance().newEditable(strUserName)
            binding.editUserEmail.text = Editable.Factory.getInstance().newEditable(strUserEmail)
            binding.editUserPhoneNumber.text =
                Editable.Factory.getInstance().newEditable(strUserPhone)
            binding.userNamePic.text = strUserName


            Glide.with(requireContext()).load("http://13.51.205.211:6002/$strUserPic")
                .into(binding.userPic)

            getProfileModel.errorResponse.observe(requireActivity()) {
                ErrorUtil.handlerGeneralError(requireActivity(), it)
                // errorDialogs()
            }
        }
    }


    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
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
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            takePictureLauncher.launch(cameraIntent)
        }
    }

    private fun setImageOnImageView(imageFile: File?) {
        if (imageFile != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver, Uri.fromFile(imageFile)
            )
            binding.userPic.setImageBitmap(bitmap)
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
            val inputStream: InputStream? = requireActivity().contentResolver.openInputStream(uri)
            if (inputStream != null) {
                val fileName = getFileNameFromUri(uri)
                val outputFile = File(requireActivity().cacheDir, fileName)
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    private fun getFileNameFromUri(uri: Uri): String {
        var result: String? = null
        val contentResolver: ContentResolver = requireActivity().contentResolver
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


    @SuppressLint("SuspiciousIndentation")
    private fun updateApi(strUserName: String, strPhoneno: String, strEmail: String) {
        val fullNames = RequestBody.create(MultipartBody.FORM, strUserName)
        val phoneNos = RequestBody.create(MultipartBody.FORM, strPhoneno)
        val emails = RequestBody.create(MultipartBody.FORM, strEmail)

        val address_document = MultipartBody.Part.createFormData(
            "profileImage",
            selectedImageFile?.name,
            RequestBody.create("*image/*".toMediaTypeOrNull(), selectedImageFile!!)
        )
        Log.e(
            "profileImageAS",
            "image....$selectedImageFile" + " strUserName " + strUserName + "strPhoneno " + strPhoneno + "strEmail " + strEmail
        )

        updateProfileModel.userUpdateDetails(
            progressDialog, activity, userId, fullNames, phoneNos, emails, address_document
        )
    }

    private fun updateObserver() {
        updateProfileModel.progressIndicator.observe(viewLifecycleOwner) {}
        updateProfileModel.mcreateEventResponse.observe(
            viewLifecycleOwner
        ) {

            val message = it.peekContent().message
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            getUserDetailApi()

        }
        updateProfileModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
            // errorDialogs()
        }
    }

    private fun bitmapToFile(bitmap: Bitmap): File {
        // Create a file in the cache directory
        val file = File(requireContext().cacheDir, "image.jpg")

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
}