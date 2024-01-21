package com.example.festa.guest.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festa.guest.adapters.GuestAdapter
import com.example.festa.guest.customclass.AddPersonContact
import com.example.festa.guest.viewmodel.bookmarkpost.BookMarkPostBody
import com.example.festa.guest.viewmodel.bookmarkpost.BookMarkPostViewModel
import com.example.festa.guest.viewmodel.guestlist.GuestListResponse
import com.example.festa.guest.viewmodel.guestlist.GuestListViewModel
import com.example.festa.guest.Uploadexcelapi.UploadexcelModel
import com.example.festa.guest.viewmodel.guests.GuestBody
import com.example.festa.guest.viewmodel.guests.GuestViewModel
import com.example.festa.guest.viewmodel.deleteguest.DeleteGuestViewModel
import com.example.festa.guest.interfacedelete.OnItemClickListenerDelete
import com.example.festa.guest.shareprefer.SharePreContact
//import com.example.festa.Manifest
import com.example.festa.R
import com.example.festa.application.Festa
import com.example.festa.databinding.FragmentGuestBinding
import com.example.festa.ui.theme.bookmark.ui.BookMark
import com.example.festa.view.createevents.ui.CreateEventFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class GuestFragment : Fragment(), OnItemClickListenerDelete {
    private lateinit var activity: Activity
    private var profileIdentity: File? = null
    private val guestlistViewModel: GuestListViewModel by viewModels()
    private val addGuestModel: GuestViewModel by viewModels()
    private val uploadExcelModel: UploadexcelModel by viewModels()
    private val bookMarkModel: BookMarkPostViewModel by viewModels()
    private val deleteModel: DeleteGuestViewModel by viewModels()

    var guestList: List<GuestListResponse.GuestDatum> = ArrayList()

    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    lateinit var dialog: BottomSheetDialog

    lateinit var bookmarkrecycle: RecyclerView
    private var eventId = " "

    private lateinit var binding: FragmentGuestBinding
    private val REQUEST_FILE_PICK = 1

    // Add Guest Name and Phone number
    private var personList = mutableListOf<AddPersonContact>()
    private var customAdapter: GuestAdapter? = null
    private lateinit var saveDataPre: SharePreContact

    // Add Number and name by phonebook
    private var cursor: Cursor? = null
    private val selectedContacts = mutableListOf<Pair<String, String>>()
    private val READ_CONTACTS_PERMISSION_REQUEST_CODE = 123
    private lateinit var addPhoneBookList: ListView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGuestBinding.inflate(inflater, container, false)
        activity = requireActivity()


        eventId = Festa.encryptedPrefs.eventIds
        // Add Phone number and Name using PhoneBook
        val yourContext: Context = requireContext()
        saveDataPre = SharePreContact(yourContext)

        // Return List Data
        val jsonString = saveDataPre.getContacts()
        Log.d("GuestFragment", "eventIdList: " + eventId)


        // Search number by Name Code here

        binding.eventlist.text = "Guest(0)"

        binding.backarrowhost.setOnClickListener {
            val paymentsFragment = CreateEventFragment()
            val fr = requireFragmentManager().beginTransaction()
            fr.replace(R.id.containers, paymentsFragment)
            fr.commit()
        }


        // Create and set the adapter

        binding.phonebook.setOnClickListener {


            addPhoneBook()
        }

        //end access contact
        eventId = arguments?.getString("eventId")!!
        Log.e("eventId", "onCreateViewijkj: " + eventId)

        //this is api for add guest
        binding.addGuest.setOnClickListener {
            Addguest()
            Log.e("AddGuest", "addGuest..")
        }
        binding.uploadexcel.setOnClickListener {
            selectExcelFile()
        }
        binding.bookMarkBtn.setOnClickListener {

            val intent = Intent(requireContext(), BookMark::class.java)
            startActivity(intent)
        }

        //....................................................//
        binding.addBookMark.setOnClickListener {
            saveBookMark()
        }

        // Add Guest Observer......................................................................//
        getGuestList()
        addGuestObserver()
        guestListObserver()
        uploadExelObserver()
        addBookMarkObserver()
        deleteObserver()
        return binding.root


    }

    private fun selectExcelFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // for XLSX files

        try {
            startActivityForResult(intent, REQUEST_FILE_PICK)
        } catch (e: ActivityNotFoundException) {
            // Handle the case where the file picker is not available on the device
            Toast.makeText(
                requireActivity(),
                "File picker not available on this device",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_FILE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val selectedFileUri = data.data

            if (selectedFileUri != null) {
                try {
                    // Get the file name and create a File object
                    val fileName = getFileName(selectedFileUri)
                    profileIdentity = File(requireContext().cacheDir, fileName)

                    // Copy the content of the selected file to a local file
                    requireActivity().contentResolver.openInputStream(selectedFileUri)?.use { input ->
                        FileOutputStream(profileIdentity).use { output ->
                            input.copyTo(output)
                        }
                    }

                    uploadExcelApi()
                    // Now, you can use the 'selectedExcelFile' for further processing
                    Log.d("SelectedImage", "Selected file path: ${profileIdentity?.absolutePath}")

                    // Continue with your logic or call 'uploadExcelApi(selectedExcelFile!!)'
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireActivity(),
                        "Error while processing the selected file",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }



    // Add Guest name and Phone number method..........................................................
    fun Addguest() {
        val dialogView = layoutInflater.inflate(R.layout.add_guest, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val clearCross = dialogView.findViewById<ImageView>(R.id.clearCross)
        val addGuestNameEdit = dialogView.findViewById<EditText>(R.id.AddGuestNameEdit)
        val addPhoneNoEdit = dialogView.findViewById<EditText>(R.id.AddPhoneNoEdit)
        val saveBtnTxt = dialogView.findViewById<TextView>(R.id.saveBtnTxt)

        saveBtnTxt.setOnClickListener {
            val name = addGuestNameEdit.text.toString()
            val phoneNumber = addPhoneNoEdit.text.toString()
            if (name.isNullOrBlank()) {
                Toast.makeText(requireActivity(), "Please enter name", Toast.LENGTH_SHORT).show()

            } else if (phoneNumber.isNullOrBlank()) {
                Toast.makeText(requireActivity(), "Please enter number", Toast.LENGTH_SHORT).show()
            } else if (phoneNumber.length != 10) {
                Toast.makeText(
                    requireActivity(),
                    "Please enter 10 digit phone number",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                addGuestApi(name, phoneNumber)
                // Notify the adapter of the new data
                dialog.dismiss()
            }


        }

        clearCross.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        clearCross.setOnClickListener { dialog.dismiss() }

    }


    // Add Guest name and Phone number using Phonebook method...........................................
    @SuppressLint("MissingInflatedId")
    fun addPhoneBook() {
        val dialogView = layoutInflater.inflate(R.layout.add_guest_phonebook, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        val saveBtnTxt = dialogView.findViewById<TextView>(R.id.saveBtnTxt)
        addPhoneBookList = dialogView.findViewById<ListView>(R.id.addPhoneBookList)
        val phoneBookClose = dialogView.findViewById<ImageView>(R.id.phoneBookClose)
        val searchIcon = dialogView.findViewById<ImageView>(R.id.searchIcon)
        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, proceed with loading contacts
            getContacts(addPhoneBookList)
        } else {
            // Request the READ_CONTACTS permission
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                READ_CONTACTS_PERMISSION_REQUEST_CODE
            )
        }

        // Initialize your original contact list and adapter


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterContacts(s.toString(), addPhoneBookList)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        saveBtnTxt.setOnClickListener {
            // Iterate over selected contacts and perform actions as needed
            for ((displayName, phoneNumber) in selectedContacts) {
                // Do something with displayName and phoneNumber, e.g., send them using addGuestApi
                Log.d(
                    "GuestFragmentAS",
                    "NameApi: $displayName phoneNumberApi: $phoneNumber selected"
                )
                addGuestApi(displayName, phoneNumber)
            }

            // Clear the list of selected contacts
            selectedContacts.clear()
            customAdapter?.updateDataSet(guestList)

            // Dismiss the dialog or perform any other actions needed
            dialog.dismiss()
        }

        dialog.show()

        phoneBookClose.setOnClickListener {
            dialog.dismiss()
        }
    }


    // Get all Contact method here.....................................................
    private fun getContacts(addPhoneBookList: ListView) {
        // create cursor and query the data
        cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        requireActivity().startManagingCursor(cursor)

        // data is an array of String type which is
        // used to store Number, Names, and id.
        val data = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone._ID
        )
        //   val phoneNumberPosition = data.indexOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)


        // creation of adapter using SimpleCursorAdapter class
        val adapter = SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_list_item_2,
            cursor,
            data,
            to
        )

        // Calling setAdapter() method to set created adapter
        addPhoneBookList.adapter = adapter
        addPhoneBookList.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        addPhoneBookList.setOnItemClickListener { _, _, position, _ ->
            val cursor = adapter.getItem(position) as Cursor
            handleItemClick(cursor, position, addPhoneBookList)
        }
    }

    // select and add item in list method.................................................

    private fun handleItemClick(cursor: Cursor, position: Int, listView: ListView) {
        val phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val displayNameIndex =
            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)

        cursor.moveToPosition(position)

        val phoneNumber = cursor.getString(phoneNumberIndex)
        val displayName = cursor.getString(displayNameIndex)
        val contactId = cursor.getLong(contactIdIndex)
        Log.d("GuestFragment", "NameOuter: $displayName phoneNumberOuter: $phoneNumber selected")
        // Toggle the selection state
        val checkedItemPositions = listView.checkedItemPositions
        val isCurrentlySelected = checkedItemPositions.get(position)

        val backgroundColor = if (isCurrentlySelected) Color.YELLOW else Color.TRANSPARENT
        listView.getChildAt(position)?.setBackgroundColor(backgroundColor)

        // Update the background color based on the selection state
        if (isCurrentlySelected) {
            selectedContacts.add(Pair(displayName, phoneNumber))
            Log.d("GuestFragmentAS", "Name: $displayName phoneNumber: $phoneNumber selected")
        } else {
            selectedContacts.remove(Pair(displayName, phoneNumber))
            Log.d("GuestFragmentAS", "Name: $displayName phoneNumber: $phoneNumber unselected")
        }

        for (i in 0 until listView.childCount) {
            val childView = listView.getChildAt(i)
            val resetColor = if (checkedItemPositions.get(i)) Color.YELLOW else Color.TRANSPARENT
            childView.setBackgroundColor(resetColor)
        }

        // Update the background color based on the selection state
    }

    private fun filterContacts(query: String, addPhoneBookList: ListView) {
        cursor?.let {
            // Requery with the new filter
            val filteredCursor = requireActivity().contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ? OR ${ContactsContract.CommonDataKinds.Phone.NUMBER} LIKE ?",
                arrayOf("%$query%", "%$query%"),
                null
            )

            // Update the adapter with the filtered cursor
            (addPhoneBookList.adapter as SimpleCursorAdapter).changeCursor(filteredCursor)
        }
    }


    // Add Guest Api..................................................................
    private fun addGuestApi(name: String, phoneNumber: String) {
        val acceptBody = GuestBody(
            Guest_Name = name,
            phone_no = phoneNumber
        )

        Log.e("GuestFragmentAS", " name " + name + " eventId " + eventId)
        addGuestModel.addNewGuest(progressDialog, activity, eventId, acceptBody)
    }

    private fun addGuestObserver() {
        addGuestModel.progressIndicator.observe(requireActivity(), Observer {
        })
        addGuestModel.mAddGuest.observe(requireActivity()) {
            val message = it.peekContent().message
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            getGuestList()
        }
        addGuestModel.errorResponse.observe(requireActivity()) {
            com.freqwency.promotr.utils.ErrorUtil.handlerGeneralError(requireActivity(), it)
            // errorDialogs()
        }
    }

    // Add Guest List  API..........................................................................
    private fun getGuestList() {
        guestlistViewModel.getguestlist(progressDialog, activity, eventId)

    }

    private fun guestListObserver() {
        guestlistViewModel.progressIndicator.observe(requireActivity(), Observer {
        })
        guestlistViewModel.mguestlist.observe(requireActivity()) {
            val message = it.peekContent().message
            guestList = it.peekContent().guestData!!

            val guestSize = guestList.size
            Log.e("eventId", "onCreateSize: " + guestList.size)
            //binding.joinDate.text=joinDate.toString()
            binding.guestrecycle.isVerticalScrollBarEnabled = true
            binding.guestrecycle.isVerticalFadingEdgeEnabled = true
            binding.guestrecycle.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            customAdapter = GuestAdapter(requireContext(), guestList, this)
            binding.guestrecycle.adapter = customAdapter

            binding.eventlist.text = "Guest($guestSize)"
        }
        guestlistViewModel.errorResponse.observe(requireActivity()) {
            com.freqwency.promotr.utils.ErrorUtil.handlerGeneralError(requireActivity(), it)
            // errorDialogs()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == READ_CONTACTS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your code
                if (::addPhoneBookList.isInitialized) {
                    getContacts(addPhoneBookList)
                } else {
                    // Handle the case where the ListView is not initialized
                    Log.e("YourTag", "addPhoneBookList is not initialized")
                }
            } else {
                // Permission denied, handle it accordingly
                // You may want to inform the user or take alternative actions
                Toast.makeText(
                    requireContext(),
                    "Permission denied. Cannot load contacts.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // upload excel api............................................................................

    @SuppressLint("SuspiciousIndentation")
    private fun uploadExcelApi() {

        val address_document = MultipartBody.Part.createFormData(
            "file",
            profileIdentity?.name, RequestBody.create("file".toMediaTypeOrNull(), profileIdentity!!)
        )

        uploadExcelModel.uploadExcel(
            progressDialog,
            activity,
            eventId,
            address_document
        )


        Log.e("selectedImageFile", "selectedImageFile.." + profileIdentity)

    }

    private fun uploadExelObserver() {
        uploadExcelModel.progressIndicator.observe(requireActivity(), Observer {
        })
        uploadExcelModel.mUploadexcel.observe(requireActivity()) {
            val message = it.peekContent().message

            Log.e("selectedImageFile", "message.." + message)
            Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
            getGuestList()
        }
        uploadExcelModel.errorResponse.observe(requireActivity()) {
            com.freqwency.promotr.utils.ErrorUtil.handlerGeneralError(requireActivity(), it)
            // errorDialogs()
        }
    }

    // save event as a book mark method....................................................

    @SuppressLint("MissingInflatedId")
    fun saveBookMark() {

        val dialogView = layoutInflater.inflate(R.layout.save_bookmark_popup, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView)
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val clearCross = dialogView.findViewById<ImageView>(R.id.clearCross)
        val eventName = dialogView.findViewById<EditText>(R.id.eventName)
        val saveBtnTxt = dialogView.findViewById<TextView>(R.id.saveBtnTxt)

        saveBtnTxt.setOnClickListener {
            val name = eventName.text.toString()
            if (name.isNullOrBlank()) {
                Toast.makeText(requireActivity(), "Please enter event name", Toast.LENGTH_SHORT)
                    .show()

            } else {
                addBookMarkApi(name)
                // Notify the adapter of the new data
                dialog.dismiss()
            }
        }

        clearCross.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        clearCross.setOnClickListener { dialog.dismiss() }

    }

    private fun addBookMarkApi(name: String) {
        val acceptBody = BookMarkPostBody(
            collectionName = name
        )

        bookMarkModel.getBookMark(progressDialog, activity, eventId, acceptBody)
    }

    private fun addBookMarkObserver() {
        bookMarkModel.progressIndicator.observe(requireActivity(), Observer {
        })
        bookMarkModel.maddcohostresponse.observe(requireActivity()) {
            val message = it.peekContent().message
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        bookMarkModel.errorResponse.observe(requireActivity()) {
            com.freqwency.promotr.utils.ErrorUtil.handlerGeneralError(requireActivity(), it)
            // errorDialogs()
        }
    }

    override fun onDeleteClick(position: Int, id: String) {
        val guestId = id
        deleteGuest(guestId)
    }

    // Delete Guest Api
    private fun deleteGuest(guestId: String) {
        deleteModel.deleteGuest(progressDialog, activity, eventId!!, guestId)

    }

    private fun deleteObserver() {
        deleteModel.progressIndicator.observe(requireActivity(), Observer {
        })
        deleteModel.mDeleteResponse.observe(requireActivity()) {
            val message = it.peekContent().message
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            getGuestList()
        }
        deleteModel.errorResponse.observe(requireActivity()) {
            com.freqwency.promotr.utils.ErrorUtil.handlerGeneralError(requireActivity(), it)

        }
    }

    private fun getFileName(uri: Uri): String {
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            return it.getString(nameIndex)
        }
        throw IllegalArgumentException("Invalid URI")
    }
}
