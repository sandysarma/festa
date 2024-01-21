package com.example.festa.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.festa.R
import com.example.festa.guest.ui.Contact
import com.example.festa.guest.ui.GuestFragment


class Contactfrg : Fragment() {

    private var cursor: Cursor? = null
    private lateinit var listView: ListView

    private val selectedContacts = HashSet<Contact>()
    private val arrayname = ArrayList<String>()
    private var stringArrayname = ArrayList<String>()
    private lateinit var button: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_contactfrg, container, false)

        // declaring listView using findViewById()
        listView = rootView.findViewById(R.id.ListView)

        val searchheader=rootView.findViewById<RelativeLayout>(R.id.header)
        val close=rootView.findViewById<ImageView>(R.id.close)
        close.setOnClickListener {
            button.setVisibility(View.VISIBLE)
            searchheader.setVisibility(View.GONE)

        }
        // declaring button using findViewById()
        button = rootView.findViewById(R.id.Button)
        // set OnClickListener() to the button
        button.setOnClickListener {
            // Check for permission before accessing contacts
            button.setVisibility(View.GONE)
            searchheader.setVisibility(View.VISIBLE)

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf( android.Manifest.permission.READ_CONTACTS),
                    1
                )
            } else {
                // Permission granted, calling getContacts()
                getContacts()
            }
        }

        return rootView
    }

    private fun getContacts() {
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
        listView.adapter = adapter
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        listView.setOnItemClickListener { parent, view, position, id ->
            // Get the cursor associated with the clicked item
            val cursor = adapter.getItem(position) as Cursor

            // Get the phone number, display name, and ID from the cursor
            val phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val displayNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)

            val phoneNumber = cursor.getString(phoneNumberIndex)
            val displayName = cursor.getString(displayNameIndex)
            val contactId = cursor.getLong(contactIdIndex)
            Log.d("selectedContactsList", "navigateToGuestFragment: "+phoneNumber)


            saveContactDetails(phoneNumber, displayName, contactId)


            // Create a Contact object and add it to the selectedContacts set
            /*val contact = Contact(displayName, phoneNumber, contactId)
            if (selectedContacts.contains(contact)) {
                selectedContacts.remove(contact) // Unselect if already selected
            } else {
                selectedContacts.add(contact) // Select if not selected
            }*/
            arrayname.add(displayName)
                arrayname.add(phoneNumber)
           // val  stringArrayStr1 = "[\"" + addServiceName.joinToString("\", \"") + "\"]"
            // val yourArray: List<String> = stringArrayStr1.split("")
            stringArrayname = arrayname
            Log.e("editTexts", "stringArrayStr...  " + stringArrayname)

            selectedContacts.forEach {
                println("Selected Contact: ${it.displayName}, ${it.phoneNumber}, ${it.contactId}")
            }
            // Now you can use phoneNumber, displayName, and contactId as needed
            // For example, you can log them or display them.
            println("Name: $displayName, Number: $phoneNumber, ID: $contactId")

            navigateToGuestFragment(displayName, phoneNumber, contactId)

            // If you want to pass the values to another fragment, you can use a callback or Bundle.
            // For simplicity, let's assume you have a function like navigateToOtherFragment

        }


    }

    private fun saveContactDetails(phoneNumber: String, displayName: String, contactId: Long) {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Save data in SharedPreferences
        editor.putString("phoneNumber", phoneNumber)
        editor.putString("displayName", displayName)
        editor.putLong("contactId", contactId)

        // Commit the changes
        editor.apply()
    }

    private fun navigateToGuestFragment(displayName: String?, phoneNumber: String?, contactId: Long) {
       /* val fragment = GuestFragment()

        // Create a Bundle to pass data to the fragment
        val bundle = Bundle()
        bundle.putString("displayName", displayName)
        bundle.putString("phoneNumber", phoneNumber)
        bundle.putLong("contactId", contactId)

        // Set the arguments for the fragment
        fragment.arguments = bundle

        // Use a FragmentTransaction to replace the current fragment with the new one
           *//* val gueFragment = GuestFragment()
            val fr = requireFragmentManager().beginTransaction()
            fr.replace(R.id.containers, gueFragment)
            fr.commit()*//*

        val fr = requireFragmentManager().beginTransaction()
        fr.replace(R.id.containers, fragment)
        fr.commit()*/

        val fragment = GuestFragment()

        // Convert the set to an ArrayList to pass to the next fragment
        val selectedContactsList = ArrayList(selectedContacts)
        Log.d("selectedContactsList", "navigateToGuestFragment: "+selectedContacts)

        // Create a Bundle to pass data to the fragment
        val bundle = Bundle()
        bundle.putParcelableArrayList("selectedContacts", selectedContactsList)

        // Set the arguments for the fragment
        fragment.arguments = bundle

      /*  // Use a FragmentTransaction to replace the current fragment with the new one
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.containers, fragment)
        transaction.addToBackStack(null) // Optional: Add to back stack if you want to navigate back
        transaction.commit()
*/
    }

    // Handle the permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, calling getContacts()
                getContacts()
            } else {
                // Permission denied, handle it appropriately
                // You may want to show a message or disable functionality
            }
        }
    }
}