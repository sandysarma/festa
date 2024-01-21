package com.example.festa.view.createevents.multipleeventsharepre

import android.content.Context
import com.example.festa.view.createevents.multipleeventsharepre.customclass.CreateMultipleEventModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MultipleEventSharePreContact(private val context: Context) {
    private val PREFS_NAME = "multiple_my_shared_preferences"

    // Function to save contacts to shared preferences
    fun saveMultipleEvent(contacts: List<CreateMultipleEventModel>) {
        val gson = Gson()
        val eventsJson = gson.toJson(contacts)
        val editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
        editor.putString("events", eventsJson)
        editor.apply()
    }

    // Function to get contacts from shared preferences


    fun getMultipleEvent(): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val eventsJson = prefs.getString("events", "") ?: ""
        val gson = Gson()
        val listType = object : TypeToken<List<CreateMultipleEventModel>>() {}.type

        // Retrieve the list of AddPersonContact objects
        val contactsList =
            gson.fromJson<List<CreateMultipleEventModel>>(eventsJson, listType) ?: emptyList()

        // Convert the list to a list of maps with "Name" and "Mobile" keys
        val listOfMaps = contactsList.map {
            mapOf(
                "VenueTitle" to it.venueTitle,
                "VenueName" to it.venueName,
                "Locations" to it.venueLocation,
                "VenueDate" to it.venueDate,
                "VenueStartTime" to it.venueStartTime,
                "VenueEndTime" to it.venueEndTime,
            )
        }

        // Create a JSON string representation
        return "[" + listOfMaps.joinToString(", ") { map ->
            "{\"sub_event_title\": \"${map["VenueTitle"]}\", \"venue_Name\": \"${map["VenueName"]}\", " + "\"venue_location\": \"${map["Locations"]}\"," + " \"date\": \"${map["VenueDate"]}\", " + "\"start_time\": \"${map["VenueStartTime"]}\"," + " \"end_time\": \"${map["VenueEndTime"]}\"}"
        } + "]"
    }

}