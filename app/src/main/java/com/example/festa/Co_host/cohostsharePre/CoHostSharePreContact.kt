package com.example.festa.Co_host.cohostsharePre

import android.content.Context
import android.content.SharedPreferences
import com.example.festa.guest.customclass.AddPersonContact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CoHostSharePreContact(private val context: Context) {
    private val PREFS_NAME = "my_shared_preferences_co"



    // Function to save contacts to shared preferences
    fun saveCoHostContacts(contacts: List<AddPersonContact>) {
        val gson = Gson()
        val contactsJson = gson.toJson(contacts)
        val editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
        editor.putString("contacts", contactsJson)
        editor.apply()
    }

    // Function to get contacts from shared preferences
    fun getCoHostContacts(): String {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val contactsJson: String = prefs.getString("contacts", "") ?: ""
        val gson = Gson()
        val listType = object : TypeToken<List<AddPersonContact>>() {}.type

        // Retrieve the list of AddPersonContact objects
        val contactsList = gson.fromJson<List<AddPersonContact>>(contactsJson, listType) ?: emptyList()

        // Filter the list based on the criteria
        val filteredContacts = contactsList.filter {
            it.name.matches(Regex("^[a-zA-Z ]+\$")) && it.contact.matches(Regex("^[0-9]+$"))
        }

        // Convert the filtered list to a list of maps with "Name" and "Mobile" keys
        val listOfMaps = filteredContacts.map {
            mapOf("Name" to it.name, "Mobile" to it.contact)
        }

        // Create a JSON string representation
        return "[" + listOfMaps.joinToString(", ") { map ->
            "{\"co_host_Name\": \"${map["Name"]}\", \"phone_no\": \"${map["Mobile"]}\"}"
        } + "]"
    }





}