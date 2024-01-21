package com.example.festa.view.subeventsupdate.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.festa.view.subeventsupdate.viewmodel.subeventupdate.SubEventUpdateBody
import com.example.festa.view.subeventsupdate.viewmodel.subeventupdate.SubEventUpdateViewModel
import com.example.festa.view.subeventsupdate.viewmodel.subeventdetails.SubEventGetViewModel
import com.example.festa.application.Festa
import com.example.festa.databinding.ActivitySubEventBinding
import com.freqwency.promotr.utils.ErrorUtil
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubEventBinding
    private lateinit var activity: Activity
    private var subEventId = ""
    private var eventId = ""
    private var subTitle = ""
    private var eventName = ""
    private var eventLocationTxt = ""
    private var eventDate = ""
    private var eventStartTime = ""
    private var eventEndTime = ""
    private var strPopUpdate: String = ""
    private val subEventViewModel: SubEventUpdateViewModel by viewModels()
    private val getSubEventViewModel: SubEventGetViewModel by viewModels()
    private lateinit var loinBody: SubEventUpdateBody
    val progressDialog by lazy { CustomProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this

        subEventId = intent.getStringExtra("subEventId").toString()
        eventId = Festa.encryptedPrefs.eventIds
        Log.e("SubEvent", "subEventId  $subEventId eventId $eventId")


        binding.backBtn.setOnClickListener {

            finish()
        }

        binding.venueUpdateBtn.setOnClickListener {

            subTitle = binding.venueTitlePopUp.text.toString()
            eventName = binding.venueNamePopUp.text.toString()
            eventLocationTxt = binding.venueLocationPopUp.text.toString()
            eventDate = binding.venueDatePopUp.text.toString()
            eventStartTime = binding.venueStartTimePopUp.text.toString()
            eventEndTime = binding.venueEndTimePopUp.text.toString()
            updateSubEventApi()
        }



        binding.venueDatePopUp.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
               this@SubEventActivity,
                { _, selectedYear, monthOfYear, dayOfMonth ->
                    // Display Selected date in textbox
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

                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, monthOfYear, dayOfMonth)

                    // Check if the selected date is before the current date
                    if (selectedDate.before(c)) {
                        // Show a message or handle the case where the selected date is before the current date
                        Toast.makeText(
                            this@SubEventActivity,
                            "Please select a date not before the current date.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Update UI and variables if the selected date is valid
                        val strDatePopup = "$dayOfMonth ${MONTHSS[monthOfYear]}, $selectedYear"
                        binding.venueDatePopUp.text = strDatePopup

                        val formattedMonth = String.format("%02d", month + 1)
                        val formatDay = String.format("%02d", day)
                        strPopUpdate = "$year-$formattedMonth-$formatDay"
                    }
                },
                year,
                month,
                day
            )

            // Set the minimum date for the DatePickerDialog to the current date
            dpd.datePicker.minDate = c.timeInMillis

            dpd.show()
        }


        binding.venueStartTimePopUp.setOnClickListener {
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
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                this@SubEventActivity,
                { _, selectedHour, selectedMinute ->
                    val formattedhour = String.format("%02d", selectedHour)
                    val formatMinutes = String.format("%02d", selectedMinute)
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    val StrStartTimePOpUp = ("$formattedhour:$formatMinutes $amPm")
                    binding.venueStartTimePopUp.text = StrStartTimePOpUp
                },
                hour,
                minute,
                true // Yes, 24-hour time
            )
            // mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        binding.venueEndTimePopUp.setOnClickListener {
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
            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(
                this@SubEventActivity,
                { _, selectedHour, selectedMinute ->
                    val formattedhour = String.format("%02d", selectedHour)
                    val formatMinutes = String.format("%02d", selectedMinute)
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    val StrEnddTimePOpUp = ("$formattedhour:$formatMinutes $amPm")
                    binding.venueEndTimePopUp.text = StrEnddTimePOpUp
                },
                hour,
                minute,
                true // Yes, 24-hour time
            )
            // mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }







        getSubEventDetailsApi(subEventId, eventId)
        subEventObserver()
        updateObserver()
    }

    private fun updateSubEventApi() {
        loinBody = SubEventUpdateBody(
            sub_event_title = subTitle,
            venue_Name = eventName,
            venue_location = eventLocationTxt,
            date = eventDate,
            start_time = eventStartTime,
            end_time = eventEndTime,
        )
        subEventViewModel.getaddcohost(progressDialog, activity, subEventId, eventId, loinBody)
    }

    private fun updateObserver() {
        subEventViewModel.progressIndicator.observe(this) {
        }
        subEventViewModel.maddcohostresponse.observe(
            this
        ) {
            val message = it.peekContent().message
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        }
        subEventViewModel.errorResponse.observe(this) {
            ErrorUtil.handlerGeneralError(this@SubEventActivity, it)
            // errorDialogs()
        }
    }


    private fun getSubEventDetailsApi(guestId: String, eventId: String) {
        getSubEventViewModel.deleteGuest(progressDialog, activity, guestId, eventId)

    }

    private fun subEventObserver() {
        getSubEventViewModel.progressIndicator.observe(this@SubEventActivity) {
        }
        getSubEventViewModel.mDeleteResponse.observe(this@SubEventActivity) {
            val message = it.peekContent().message
            val subEventTitle = it.peekContent().subEventDetails?.subEventTitle.toString()
            val venueName = it.peekContent().subEventDetails?.venueName.toString()
            val venueLocation = it.peekContent().subEventDetails?.venueLocation.toString()
            val venueStart = it.peekContent().subEventDetails?.startTime.toString()
            val venueEndTime = it.peekContent().subEventDetails?.endTime.toString()
            val venueDate = it.peekContent().subEventDetails?.date.toString()


            binding.venueTitlePopUp.text = Editable.Factory.getInstance().newEditable(subEventTitle)
            binding.venueNamePopUp.text = Editable.Factory.getInstance().newEditable(venueName)
            binding.venueLocationPopUp.text =
                Editable.Factory.getInstance().newEditable(venueLocation)
            binding.venueStartTimePopUp.text =
                Editable.Factory.getInstance().newEditable(venueStart)
            binding.venueEndTimePopUp.text =
                Editable.Factory.getInstance().newEditable(venueEndTime)

            binding.venueDatePopUp.text = Editable.Factory.getInstance().newEditable(venueDate)

        }
        getSubEventViewModel.errorResponse.observe(this@SubEventActivity) {
            com.freqwency.promotr.utils.ErrorUtil.handlerGeneralError(this@SubEventActivity, it)

        }
    }
}