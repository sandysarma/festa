package com.example.festa.guest.shareprefer

import android.content.Context
import com.example.festa.guest.customclass.AddPersonContact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharePreContact(private val context: Context) {

    private val PREFS_NAME = "my_shared_preferences"

    // Function to save contacts to shared preferences
    fun saveContacts(contacts: List<AddPersonContact>) {
        val gson = Gson()
        val contactsJson = gson.toJson(contacts)
        val editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
        editor.putString("contacts", contactsJson)
        editor.apply()
    }

    // Function to get contacts from shared preferences


    fun getContacts(): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val contactsJson = prefs.getString("contacts", "") ?: ""
        val gson = Gson()
        val listType = object : TypeToken<List<AddPersonContact>>() {}.type

        // Retrieve the list of AddPersonContact objects
        val contactsList =
            gson.fromJson<List<AddPersonContact>>(contactsJson, listType) ?: emptyList()

        // Convert the list to a list of maps with "Name" and "Mobile" keys
        val listOfMaps = contactsList.map {
            mapOf("Name" to it.name, "Mobile" to it.contact)
        }

        // Create a JSON string representation
        return "[" + listOfMaps.joinToString(", ") { map ->
            "{\"Guest_Name\": \"${map["Name"]}\", \"phone_no\": \"${map["Mobile"]}\"}"
        } + "]"
    }


}