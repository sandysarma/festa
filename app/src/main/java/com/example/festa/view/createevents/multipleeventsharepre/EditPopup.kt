package com.example.festa.view.createevents.multipleeventsharepre

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Build
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import com.example.festa.R

class EditPopup (private val context: Context, private val originalEvent: MultipleEvent, private val onUpdate: (MultipleEvent) -> Unit) {

    fun show() {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.edit_multiple_event, null)

        // Initialize UI elements

        val titleName: EditText = dialogView.findViewById(R.id.venueTitlePopUp)
        val eventName: EditText = dialogView.findViewById(R.id.venueNamePopUp)
        val eventLocationTxt: EditText = dialogView.findViewById(R.id.venueLocationPopUp)
        val eventDate: TextView = dialogView.findViewById(R.id.venueDatePopUp)
        val eventStartTime: TextView = dialogView.findViewById(R.id.venueStartTimePopUp)
        val eventEndTime: TextView = dialogView.findViewById(R.id.venueEndTimePopUp)



        eventDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { _, selectedYear, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox
                    //DateCreateEvent.text = "$dayOfMonth ${MONTHS[monthOfYear]}, $selectedYear"
                    val MONTHSS = arrayOf(
                        "Jan",
                        "Feb",
                        "Mar",
                        "Apr",
                        "May",
                        "Jun",
                        "Jul",
                        "Aug",
                        "Sep",
                        "Oct",
                        "Nov",
                        "Dec"
                    )
                  val  StrDatepopup = "$dayOfMonth ${MONTHSS[monthOfYear]}, $selectedYear"
                    eventDate.text = StrDatepopup
                    val formattedMonth = String.format("%02d", month + 1)
                    val formatDay = String.format("%02d", day)
                 //   strPopUpdate = "$year-$formattedMonth-$formatDay"
                },
                year,
                month,
                day
            )

            dpd.show()
        }


        eventStartTime.setOnClickListener {
            var mcurrentTime: Calendar? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mcurrentTime = Calendar.getInstance()
            }
            var hour = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                hour = mcurrentTime!!.get(Calendar.HOUR_OF_DAY)
            }
            var minute = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                minute = mcurrentTime!!.get(Calendar.MINUTE)
            }
            var mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    val formattedhour = String.format("%02d", selectedHour)
                    val formatMinutes = String.format("%02d", selectedMinute)
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                   val StrStartTimePOpUp = (formattedhour + ":" + formatMinutes + " " + amPm)
                 //   strStartTimePopupp = (formattedhour + ":" + formatMinutes)
                    eventStartTime.text = StrStartTimePOpUp
                },
                hour,
                minute,
                true // Yes, 24-hour time
            )
            // mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        eventEndTime.setOnClickListener {
            var mcurrentTime: Calendar? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mcurrentTime = Calendar.getInstance()
            }
            var hour = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                hour = mcurrentTime!!.get(Calendar.HOUR_OF_DAY)
            }
            var minute = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                minute = mcurrentTime!!.get(Calendar.MINUTE)
            }
            var mTimePicker: TimePickerDialog = TimePickerDialog(
                context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    val formattedhour = String.format("%02d", selectedHour)
                    val formatMinutes = String.format("%02d", selectedMinute)
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    val StrEnddTimePOpUp = (formattedhour + ":" + formatMinutes + " " + amPm)
                    //StrEndTimePoppp = (formattedhour + ":" + formatMinutes)
                    eventEndTime.text = StrEnddTimePOpUp
                },
                hour,
                minute,
                true // Yes, 24-hour time
            )
            // mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }




        // Add other EditText fields as needed

        // Set initial values
        titleName.setText(originalEvent.venueTitle)
        eventName.setText(originalEvent.venueName)
        eventLocationTxt.setText(originalEvent.venueLocation)
        eventDate.setText(originalEvent.venueDate)
        eventStartTime.setText(originalEvent.venueStartTime)
        eventEndTime.setText(originalEvent.venueEndTime)
        // Set other initial values

        val customTitleView = inflater.inflate(R.layout.custom_title_layout, null)

        // Set background color for the custom title view
        customTitleView.setBackgroundResource(R.drawable.header_color)
        val titleTextView: TextView = customTitleView.findViewById(R.id.customTitleTextView)
        titleTextView.text = "Edit Event"
        builder.setCustomTitle(customTitleView)
        builder.setView(dialogView)
        builder.setPositiveButton("Save") { _, _ ->
            // Retrieve updated values from EditText fields
            val updatedEvent = MultipleEvent(
                venueTitle = titleName.text.toString(),
                venueName = eventName.text.toString(),
                venueLocation = eventLocationTxt.text.toString(),
                venueDate = eventDate.text.toString(),
                venueStartTime = eventStartTime.text.toString(),
                venueEndTime = eventEndTime.text.toString()
                // Set other fields accordingly
            )

            // Invoke the callback to update the list
            onUpdate(updatedEvent)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            // Do nothing or handle cancel
        }

        builder.create().show()
    }
}