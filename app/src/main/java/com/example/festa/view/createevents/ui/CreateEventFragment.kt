package com.example.festa.view.createevents.ui


import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.makeText
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.festa.Co_host.cohostsharePre.CoHostSharePreContact
import com.example.festa.view.cohost.ui.CoHostFragment
import com.example.festa.view.events.ui.EventListFragment
import com.example.festa.guest.ui.GuestFragment
import com.example.festa.guest.interfacedelete.OnItemClickListenerDelete
import com.example.festa.guest.shareprefer.SharePreContact
import com.example.festa.R
import com.example.festa.application.Festa
import com.example.festa.databinding.FragmentCreateEventBinding
import com.example.festa.location.LocationActivity
import com.example.festa.view.createevents.adapter.MultipleEventCreateAdapter
import com.example.festa.view.createevents.multipleadapters.MultipleEventAdapter
import com.example.festa.view.createevents.multipleeventsharepre.MultipleEvent
import com.example.festa.view.createevents.multipleeventsharepre.MultipleEventSharePreContact
import com.example.festa.view.createevents.multipleeventsharepre.customclass.CreateMultipleEventModel
import com.example.festa.view.createevents.viewmodel.createevent.CreateEventViewModel
import com.example.festa.view.createevents.viewmodel.createfinal.CreateFinalEventViewModel
import com.example.festa.view.createevents.viewmodel.createmutiple.CreateMultiEventModel
import com.example.festa.view.createevents.viewmodel.deletesubevent.DeleteSubEventModel
import com.example.festa.view.createevents.viewmodel.getedit.GetEditModel
import com.example.festa.view.createevents.viewmodel.sendinvite.SendInviteModel
import com.freqwency.promotr.utils.ErrorUtil
import com.google.gson.Gson
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateEventFragment : Fragment(), OnItemClickListenerDelete {
    private lateinit var activity: Activity
    private val createEventViewModel: CreateEventViewModel by viewModels()
    private val createFinalEventViewModel: CreateFinalEventViewModel by viewModels()
    private val createMultiEventViewModel: CreateMultiEventModel by viewModels()
    private val deleteEventViewModel: DeleteSubEventModel by viewModels()
    private val sendInviteModel: SendInviteModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    private val getEditModel: GetEditModel by viewModels()

    private val eventlist = ArrayList<String>()
    private val strEventlist = ArrayList<String>()

    private lateinit var venueNamePopUp: EditText
    private lateinit var venueTitlePopUp: EditText
    private lateinit var venueLocationPopUp: EditText
    private lateinit var venueDatePopUp: TextView
    private lateinit var venueStartTimePopUp: TextView
    private lateinit var venueEndTimePopUp: TextView
    private val PLACE_PICKUP_REQUEST_CODE = 1001
    var userId = ""
    var eventId = ""
    private var updateValue = ""
    private var indexOfLocation  = -1
    private lateinit var item: String
    private var strPopUpdate: String = ""
    private var strStartTimePopupp: String = ""
    private var StrStartTimePOpUp: String = ""
    private var StrEnddTimePOpUp: String = ""
    private var StrStartTime: String = ""
    private var StrrStartTime: String = ""
    private var StrEndTime: String = ""
    private var StrrEndTime: String = ""
    private var StrEventTitle: String = ""
    private var StrEventDescription: String = ""
    private var StrEndTimePoppp: String = ""
    private var StrDatepopup: String = ""
    private var singleOrMultArrayString: String? = null
    private var event_key = 1

    var isset: Boolean = false
    lateinit var dialog: Dialog

    private var _binding: FragmentCreateEventBinding? = null
    private val binding get() = _binding!!

    // Add Guest Name and Phone number
    private var personList = mutableListOf<CreateMultipleEventModel>()
    private val dataList = mutableListOf<MultipleEvent>()
    private lateinit var customAdapter: MultipleEventCreateAdapter

    private var multipleAdapter: MultipleEventAdapter? = null
    private lateinit var multiDataPre: MultipleEventSharePreContact
    private lateinit var saveAsCostDataPre: CoHostSharePreContact
    private lateinit var saveAsGuestDataPre: SharePreContact

    /// Select Multple Image
    companion object {
        private const val REQUEST_IMAGE_PICK = 100
    }

    private val selectedImages: ArrayList<File> = ArrayList()
    private var outputList = ArrayList<String>()
    private var eventType = ""
    private var strCheckBtn = ""

    private val PREFS_NAME = "YourPrefsFile" // Change this to your desired file name
    private val KEY_VALUE = "your_key" // Change this to your desired key

    private lateinit var sharedPreferences: SharedPreferences


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint(
        "MissingInflatedId",
        "ResourceAsColor",
        "SetTextI18n",
        "UseRequireInsteadOfGet",
        "SuspiciousIndentation"
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateEventBinding.inflate(inflater, container, false)
        val view = binding.root

        activity = requireActivity()
        personList = mutableListOf()

        // Get All Save data in SharedPreference
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        eventId = Festa.encryptedPrefs.eventIds
        // eventIds = arguments?.getString("eventId").toString()
        val adapter = arguments?.getString("adapter").toString()
        if (adapter == "adapterValue") {
            getEdit(eventId)
        }


        val yourContext: Context = requireContext()
        multiDataPre = MultipleEventSharePreContact(yourContext)
        saveAsGuestDataPre = SharePreContact(yourContext)
        saveAsCostDataPre = CoHostSharePreContact(yourContext)

        val mutipleEventRecycler = view.findViewById<RecyclerView>(R.id.mutipleEventRecycler)

        // All Botton here/...................................................

        binding.removeImageBtn.setOnClickListener {
            removeImage()
        }



        binding.backarrowcreateevent.setOnClickListener {

            //backButtonPopup()
            val paymentsFragment = EventListFragment()
            val fr = requireFragmentManager().beginTransaction()
            fr.replace(R.id.containers, paymentsFragment)
            fr.commit()
            //dialog.dismiss()
            Festa.encryptedPrefs.eventIds = ""
        }

        binding.coHostAddBtn.setOnClickListener {
            updateValue = "2"
            saveValueToSharedPreferences(updateValue)
            strCheckBtn = "2"
            if (eventId.isEmpty()) {
                singleOrMultArrayString = convertListToString(personList)
                validateHostGuestInputs(singleOrMultArrayString!!)
            } else {
                event_key == 1




                Log.e("MultipleEventData", " singleOrMultupdateValue " + updateValue)
                item = binding.spinner.selectedItem.toString()
                // createSingleEventFinalApi(title, description, venueName, venueLocation, venueDate, venueStartTime, venueEndTime)
                Log.e("ASDFGH", "SignleCoBtn")
                val venueName = binding.venueNameEdit.text.toString()
                val venueLocation = binding.venueLocationEdit.text.toString()
                val venueDate = binding.venueDateCreateEvent.text.toString()
                val venueStartTime = binding.venueStartTime.text.toString()
                val venueEndTime = binding.venueEndTime.text.toString()
                val title = binding.eventTitleEdit.text.toString()
                val description = binding.eventDiscriptionEdit.text.toString()

                createSingleEventFinalApi(
                    title,
                    description,
                    venueName,
                    venueLocation,
                    venueDate,
                    venueStartTime,
                    venueEndTime,
                    event_key
                )
            }
        }

        binding.addGuestBtn.setOnClickListener {
            strCheckBtn = "3"
            updateValue = "2"
            saveValueToSharedPreferences(updateValue)
            Log.e(
                "CreateEventSave",
                "MultiAddEventList....addGuestBtn " + strCheckBtn + "eventId" + eventId
            )

            if (eventId.isEmpty()) {
                singleOrMultArrayString = convertListToString(personList)
                validateHostGuestInputs(singleOrMultArrayString!!)
                /*if (binding.checkbox.isChecked) {
                    StrEventTitle = binding.eventTitleEdit.text.toString()
                    StrEventDescription = binding.eventDiscriptionEdit.text.toString()
                    Log.e("CreateEventSave", "MultiAddEventList.... ")


                    validateHostGuestInputs()
                } else {
                    StrEventTitle = binding.eventTitleEdit.text.toString()
                    StrEventDescription = binding.eventDiscriptionEdit.text.toString()

                    singleOrMultArrayString = convertListToString(personList)
                    createEventApi()
                    Log.e("CreateEventSave", "singleAddEventList....ACo " + singleOrMultArrayString)
                }*/
            } else {
                event_key == 1

                Log.e("ASDFGH", "MultiADDBtn")
                val venueName = binding.venueNameEdit.text.toString()
                val venueLocation = binding.venueLocationEdit.text.toString()
                val venueDate = binding.venueDateCreateEvent.text.toString()
                val venueStartTime = binding.venueStartTime.text.toString()
                val venueEndTime = binding.venueEndTime.text.toString()
                val title = binding.eventTitleEdit.text.toString()
                val description = binding.eventDiscriptionEdit.text.toString()
                item = binding.spinner.selectedItem.toString()
                createSingleEventFinalApi(
                    title,
                    description,
                    venueName,
                    venueLocation,
                    venueDate,
                    venueStartTime,
                    venueEndTime,
                    event_key
                )
            }
        }

        binding.saveMainTxtBtn.setOnClickListener {
            strCheckBtn = "1"

            if (eventId.isEmpty()) {

                Log.e("ASDFGH", "MultiplesaveMainTxtBtnNew$singleOrMultArrayString")
                validateInputs()
            } else {
                if (binding.checkbox.isChecked) {
                    event_key == 2

                    item = binding.spinner.selectedItem.toString()
                    createMultipleEventApi(event_key, singleOrMultArrayString)
                    Log.e("ASDFGH", "MultiplesaveMainTxtBtn")
                } else {
                    Log.e("ASDFGH", "SignlesaveMainTxtBtn")
                    item = binding.spinner.selectedItem.toString()
                    val venueName = binding.venueNameEdit.text.toString()
                    val venueLocation = binding.venueLocationEdit.text.toString()
                    val venueDate = binding.venueDateCreateEvent.text.toString()
                    val venueStartTime = binding.venueStartTime.text.toString()
                    val venueEndTime = binding.venueEndTime.text.toString()

                    val title = binding.eventTitleEdit.text.toString()
                    val description = binding.eventDiscriptionEdit.text.toString()

                    if (title.isEmpty()) {
                        makeText(
                            requireActivity(), "Please enter event title", Toast.LENGTH_SHORT
                        ).show()

                    } else if (description.isEmpty()) {
                        makeText(
                            requireActivity(),
                            "Please enter event description",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    } else if (venueName.isEmpty()) {
                        makeText(
                            requireActivity(),
                            "Please enter venue name",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (venueLocation.isEmpty()) {
                        makeText(
                            requireActivity(),
                            "Please enter venue location",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (venueDate.isEmpty()) {
                        makeText(
                            requireActivity(),
                            "Please enter venue date",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (venueStartTime.isEmpty()) {
                        makeText(
                            requireActivity(),
                            "Please enter venue start time",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (venueEndTime.isEmpty()) {
                        makeText(
                            requireActivity(),
                            "Please enter venue end time",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        event_key == 1
                        createSingleEventFinalApi(
                            title,
                            description,
                            venueName,
                            venueLocation,
                            venueDate,
                            venueStartTime,
                            venueEndTime,
                            event_key
                        )
                    }
                }
                Log.e("EventIDShow", "eventId.....One")
            }
        }


        val MONTHS = arrayOf(
            1,
            "Jan",
            2,
            "Feb",
            3,
            "Mar",
            4,
            "Apr",
            5,
            "May",
            6,
            "Jun",
            7,
            "Jul",
            8,
            "Aug",
            9,
            "Sep",
            10,
            "Oct",
            11,
            "Nov",
            12,
            "Dec"
        )
        val pickUpTime = "12:00:00"


        userId = Festa.encryptedPrefs.UserId
        eventId = Festa.encryptedPrefs.eventIds



        Log.e("userId", "onCreateView: " + userId)


        // sendInvite Button ClickListener......................................................................

        if (eventId.isEmpty()) {

            binding.sendInvite.visibility = View.GONE
        } else {
            binding.sendInvite.visibility = View.VISIBLE
        }
        binding.sendInvite.setOnClickListener {
            sendInviteApi()
        }

        binding.locationBtn.setOnClickListener {
            indexOfLocation = -2
            val intent = Intent(requireContext(), LocationActivity::class.java)
            startActivityForResult(intent, PLACE_PICKUP_REQUEST_CODE)
        }

        binding.checkbox.setOnClickListener {
            binding.singleEventLinear.visibility = View.GONE
            binding.addevent.visibility = View.VISIBLE
            mutipleEventRecycler.visibility = View.VISIBLE

            if (isset == false) {
                binding.singleEventLinear.visibility = View.GONE
                binding.addevent.visibility = View.VISIBLE
                mutipleEventRecycler.visibility = View.VISIBLE
                event_key = 2
                isset = true
            } else if (isset) {
                binding.singleEventLinear.visibility = View.VISIBLE
                binding.addevent.visibility = View.GONE
                mutipleEventRecycler.visibility = View.GONE
                isset = false
                event_key = 1
            }

        }


        // Select multiple image Button
        binding.addImgCreateEvent.setOnClickListener {
            openGalleryForImages()
        }


        // click multiple event popup button
        binding.addevent.setOnClickListener {


            if (eventId.isEmpty()) {

                StrEventTitle = binding.eventTitleEdit.text.toString()
                StrEventDescription = binding.eventDiscriptionEdit.text.toString()
                singleOrMultArrayString = convertListToString(personList)
                createEventApi()
                multipleEventAddPopup()
            } else {
                multipleEventAddPopup()
            }
        }


        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?, view: View?, i: Int, l: Long
            ) {
                //  Toast.makeText(requireActivity(), "Country Spinner Working **********", Toast.LENGTH_SHORT).show()
                item = binding.spinner.selectedItem.toString()

                Log.e("SpinnerItem", "item" + item)
                if (item == getString(R.string.select_event)) {

                } else {
                    if (item.equals("Business_Conference", ignoreCase = true)) {
                        eventType = "Busines_ Conference"
                    } else if (item.equals("Music_Festivals", ignoreCase = true)) {
                        eventType = "Music_Festivals"
                    } else if (item.equals("Birthday", ignoreCase = true)) {
                        eventType = "Birthday"
                    } else if (item.equals("Exhibitions", ignoreCase = true)) {
                        eventType = "Exhibitions"
                    } else if (item.equals("Wedding_Anniversary", ignoreCase = true)) {
                        eventType = "Wedding_Anniversary"
                    } else if (item.equals("Sports", ignoreCase = true)) {
                        eventType = "Sports"
                    } else if (item.equals("Marriage", ignoreCase = true)) {
                        eventType = "Marriage"
                    }
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }


        // Add EventType List
        eventlist.add("Business_Conference")
        eventlist.add("Music_Festivals")
        eventlist.add("Birthday")
        eventlist.add("Exhibitions")
        eventlist.add("Wedding_Anniversary")
        eventlist.add("Sports")
        eventlist.add("Marriage")


        // Spinner Adapter
        val dAdapter = spinnerAdapter(requireContext(), R.layout.custom_spinner_two, strEventlist)
        dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dAdapter.add("Select event type")
        dAdapter.addAll(eventlist)
        binding.spinner.adapter = dAdapter


        binding.venueDateCreateEvent.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireContext(),
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
                        makeText(
                            requireContext(),
                            "Please select a date not before the current date.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Update UI and variables if the selected date is valid
                        val strDatePopup = "$dayOfMonth ${MONTHSS[monthOfYear]}, $selectedYear"
                        binding.venueDateCreateEvent.text = strDatePopup

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


        binding.venueStartTime.setOnClickListener {
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
                requireActivity(),
                { _, selectedHour, selectedMinute ->
                    val formattedhour = String.format("%02d", selectedHour)
                    val formatMinutes = String.format("%02d", selectedMinute)
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    StrStartTime = ("$formattedhour:$formatMinutes $amPm")
                    StrrStartTime = ("$formattedhour:$formatMinutes")
                    binding.venueStartTime.text = StrStartTime
                },
                hour,
                minute,
                true // Yes, 24-hour time
            )

            mTimePicker.show()
        }

        binding.venueEndTime.setOnClickListener {
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
                requireActivity(),
                { _, selectedHour, selectedMinute ->
                    val formattedhour = String.format("%02d", selectedHour)
                    val formatMinutes = String.format("%02d", selectedMinute)
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    StrEndTime = ("$formattedhour:$formatMinutes $amPm")
                    StrrEndTime = ("$formattedhour:$formatMinutes")
                    binding.venueEndTime.text = StrEndTime
                },
                hour,
                minute,
                true // Yes, 24-hour time
            )
            // mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }


        // Create Event Observer
        createEventObserver()
        createSingleEventObserver()
        getEditObserver()
        sendInviteObserver()
        createMultiEventObserver()
        deleteSubEventObserver()
        return view
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun multipleEventAddPopup() {
        val dialogView = layoutInflater.inflate(R.layout.addvenuedateandtime, null)
        val builder = AlertDialog.Builder(requireActivity()).setView(dialogView)
        val dialog = builder.create()
        venueTitlePopUp = dialogView.findViewById(R.id.venueTitlePopUp)
        venueNamePopUp = dialogView.findViewById(R.id.venueNamePopUp)
        venueLocationPopUp = dialogView.findViewById(R.id.venueLocationPopUp)
        venueDatePopUp = dialogView.findViewById(R.id.venueDatePopUp)
        venueStartTimePopUp = dialogView.findViewById(R.id.venueStartTimePopUp)
        venueEndTimePopUp = dialogView.findViewById(R.id.venueEndTimePopUp)
        val venueSavePopUp = dialogView.findViewById<TextView>(R.id.venueSaveTextPopUp)
        val mapLocationBtn = dialogView.findViewById<ImageView>(R.id.mapLocationBtn)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog.show()

        //venueTitlePopUp.setText(StEventTitlepopup)
        mapLocationBtn.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            startActivity(intent)
        }


        venueDatePopUp.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireContext(),
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
                        makeText(
                            requireContext(),
                            "Please select a date not before the current date.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Update UI and variables if the selected date is valid
                        val strDatePopup = "$dayOfMonth ${MONTHSS[monthOfYear]}, $selectedYear"
                        venueDatePopUp.text = strDatePopup

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
        venueDatePopUp.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireActivity(),
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
                    StrDatepopup = "$dayOfMonth ${MONTHSS[monthOfYear]}, $selectedYear"
                    venueDatePopUp.text = StrDatepopup
                    val formattedMonth = String.format("%02d", month + 1)
                    val formatDay = String.format("%02d", day)
                    strPopUpdate = "$year-$formattedMonth-$formatDay"
                },
                year,
                month,
                day
            )

            dpd.show()
        }


        venueStartTimePopUp.setOnClickListener {
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
                requireActivity(),
                { _, selectedHour, selectedMinute ->
                    val formattedhour = String.format("%02d", selectedHour)
                    val formatMinutes = String.format("%02d", selectedMinute)
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    StrStartTimePOpUp = ("$formattedhour:$formatMinutes $amPm")
                    strStartTimePopupp = ("$formattedhour:$formatMinutes")
                    venueStartTimePopUp.text = StrStartTimePOpUp
                },
                hour,
                minute,
                true // Yes, 24-hour time
            )
            // mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }

        venueEndTimePopUp.setOnClickListener {
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
            val mTimePicker = TimePickerDialog(
                requireActivity(),
                { _, selectedHour, selectedMinute ->
                    val formattedhour = String.format("%02d", selectedHour)
                    val formatMinutes = String.format("%02d", selectedMinute)
                    val amPm = if (selectedHour < 12) "AM" else "PM"
                    StrEnddTimePOpUp = ("$formattedhour:$formatMinutes $amPm")
                    StrEndTimePoppp = ("$formattedhour:$formatMinutes")
                    venueEndTimePopUp.text = StrEnddTimePOpUp
                },
                hour,
                minute,
                true // Yes, 24-hour time
            )
            // mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }


        venueSavePopUp.setOnClickListener {
            val venueTitle = venueTitlePopUp.text.toString()
            val venueName = venueNamePopUp.text.toString()
            val venueLocation = venueLocationPopUp.text.toString()
            val venueDate = venueDatePopUp.text.toString()
            val venueStartTime = venueStartTimePopUp.text.toString()
            val venueEndTime = venueEndTimePopUp.text.toString()
            if (venueName.isEmpty()) {
                makeText(
                    requireActivity(), "Please enter venue name", Toast.LENGTH_SHORT
                ).show()

            } else if (venueLocation.isEmpty()) {
                makeText(requireActivity(), "Please enter location", Toast.LENGTH_SHORT)
                    .show()

            } else if (venueDate.isEmpty()) {
                makeText(requireActivity(), "Please enter date", Toast.LENGTH_SHORT).show()

            } else if (venueStartTime.isEmpty()) {
                makeText(requireActivity(), "Please enter start time", Toast.LENGTH_SHORT)
                    .show()

            } else if (venueEndTime.isEmpty()) {
                makeText(requireActivity(), "Please enter end date", Toast.LENGTH_SHORT)
                    .show()

            } else {
                // Save the new contact
                val newItem = MultipleEvent(
                    id = dataList.size + 1, // You can set the ID as per your logic
                    venueTitle = venueTitle,
                    venueName = venueName,
                    venueLocation = venueLocation,
                    venueDate = venueDate,
                    venueStartTime = venueStartTime,
                    venueEndTime = venueEndTime
                )
                dataList.add(newItem)

                // Save the updated list to shared preferences
                //   multiDataPre.saveMultipleEvent(personList)

                val gson = Gson()
                singleOrMultArrayString = gson.toJson(dataList.filterNotNull()?.map { model ->
                    mapOf(
                        "sub_event_title" to model.venueTitle,
                        "venue_Name" to model.venueName,
                        "venue_location" to model.venueLocation,
                        "date" to model.venueDate,
                        "start_time" to model.venueStartTime,
                        "end_time" to model.venueEndTime,
                        // Add other properties as needed
                    )
                })

                event_key == 2

                eventId = Festa.encryptedPrefs.eventIds
                createMultipleEventApi(event_key, singleOrMultArrayString)
                Log.e(
                    "MultipleEventData",
                    " singleOrMultArrayString  $singleOrMultArrayString" + " eventId " + eventId
                )


                // Notify the adapter of the new data
                multipleAdapter?.notifyDataSetChanged()
                dialog.dismiss()
            }
        }


    }


    // Get Details Api
    private fun getEdit(eventId: String?) {
        getEditModel.getEditdata(progressDialog, activity, eventId!!)

    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun getEditObserver() {
        getEditModel.progressIndicator.observe(viewLifecycleOwner, {})
        getEditModel.mgeteditresponse.observe(viewLifecycleOwner) {
            val message = it.peekContent().message
            val users = it.peekContent().eventData
            Festa.encryptedPrefs.eventIds = eventId
            val images = it.peekContent().eventData?.images
            val venueDateTime = it.peekContent().eventData?.venueDateAndTime
            val eventStatus = it.peekContent().eventData?.eventStatus
            val eventKey = it.peekContent().eventData?.eventKey
            val guestList = it.peekContent().eventData?.venueDateAndTime


            for (i in venueDateTime!!) {
                val venueName = i.venueName.toString()
                val venueLocation = i.venueLocation.toString()
                val venueDate = i.date.toString()
                val venueStart = i.startTime.toString()
                val venueEndTime = i.endTime.toString()
                binding.venueNameEdit.text =
                    Editable.Factory.getInstance().newEditable(venueName)
                binding.venueLocationEdit.text =
                    Editable.Factory.getInstance().newEditable(venueLocation)
                binding.venueStartTime.text =
                    Editable.Factory.getInstance().newEditable(venueStart)
                binding.venueEndTime.text =
                    Editable.Factory.getInstance().newEditable(venueEndTime)

                binding.venueDateCreateEvent.text =
                    Editable.Factory.getInstance().newEditable(venueDate)
                binding.singleEventLinear.visibility = View.VISIBLE
                binding.mutipleEventRecycler.visibility = View.GONE

            }




            Log.e("EventKeyValue", " event_key " + event_key + "eventStatus  " + eventStatus)
            if (eventKey != null) {
                // Do something with the non-null eventKey
                event_key = eventKey

                if (event_key == 1) {
                    for (i in venueDateTime!!) {
                        val venueName = i.venueName.toString()
                        val venueLocation = i.venueLocation.toString()
                        val venueDate = i.date.toString()
                        val venueStart = i.startTime.toString()
                        val venueEndTime = i.endTime.toString()
                        binding.venueNameEdit.text =
                            Editable.Factory.getInstance().newEditable(venueName)
                        binding.venueLocationEdit.text =
                            Editable.Factory.getInstance().newEditable(venueLocation)
                        binding.venueStartTime.text =
                            Editable.Factory.getInstance().newEditable(venueStart)
                        binding.venueEndTime.text =
                            Editable.Factory.getInstance().newEditable(venueEndTime)

                        binding.venueDateCreateEvent.text =
                            Editable.Factory.getInstance().newEditable(venueDate)
                        binding.singleEventLinear.visibility = View.VISIBLE
                        binding.mutipleEventRecycler.visibility = View.GONE


                    }

                } else {

                    binding.singleEventLinear.visibility = View.GONE
                    binding.mutipleEventRecycler.visibility = View.VISIBLE
                    binding.mutipleEventRecycler.isVerticalScrollBarEnabled = true
                    binding.mutipleEventRecycler.isVerticalFadingEdgeEnabled = true
                    binding.mutipleEventRecycler.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    multipleAdapter =
                        guestList?.let { it1 ->
                            MultipleEventAdapter(
                                requireContext(),
                                it1,
                                this
                            )
                        }!!
                    binding.mutipleEventRecycler.adapter = multipleAdapter
                }


            } else {
                // Handle the case where eventKey is null
            }



            eventType = it.peekContent().eventData?.eventType.toString()
            if (users != null) {
                if (users.title != null) {
                    binding.eventTitleEdit.text =
                        Editable.Factory.getInstance().newEditable(users.title)
                }

                if (users.description != null) {
                    binding.eventDiscriptionEdit.text =
                        Editable.Factory.getInstance().newEditable(users.description)
                }

                interestedInMethod(eventType)

                val zerothImage: String? = if (images?.isNotEmpty() == true) {
                    images[0]
                } else {
                    null // or provide a default value or handle the case appropriately
                }

                Glide.with(this).load("http://13.51.205.211:6002/$zerothImage")
                    .placeholder(R.drawable.user).into(binding.multipleImageSelect)




                getEditModel.errorResponse.observe(viewLifecycleOwner) {
                    ErrorUtil.handlerGeneralError(requireActivity(), it)

                }
            }


        }
    }


    // Validation All field method
    private fun validateHostGuestInputs(singleOrMultArrayString: String) {
        StrEventTitle = binding.eventTitleEdit.text.toString()
        StrEventDescription = binding.eventDiscriptionEdit.text.toString()

        val selectedOption: String = binding.spinner.selectedItem.toString()

        if (binding.eventTitleEdit.text.isNullOrBlank()) {
            makeText(
                requireActivity(), "Please enter event title", Toast.LENGTH_SHORT
            ).show()

        } else if (binding.eventDiscriptionEdit.text.isNullOrBlank()) {
            makeText(requireActivity(), "Please enter event description", Toast.LENGTH_SHORT)
                .show()

        } else if (selectedOption == resources.getString(R.string.select_event)) {
            makeText(requireActivity(), "Please select event type", Toast.LENGTH_SHORT).show()
        } else {
            createEventHostGuestApi(singleOrMultArrayString)
        }
    }

    // Create Event Api
    private fun createEventHostGuestApi(singleOrMultArrayStrings: String) {
        // val userIds = RequestBody.create(MultipartBody.FORM, userId.toString())
        val title = RequestBody.create(MultipartBody.FORM, StrEventTitle)
        val description = RequestBody.create(MultipartBody.FORM, StrEventDescription)
        val eventType = RequestBody.create(MultipartBody.FORM, item)
        val venue_Date_and_time =
            RequestBody.create(MultipartBody.FORM, singleOrMultArrayStrings.toString())
        val fileList = mutableListOf<File>()

        for (path in outputList) {
            fileList.add(File(path))
        }

        //val stringBuilder = StringBuilder()
        val fileMultipartBody = arrayOfNulls<MultipartBody.Part>(selectedImages.size)
        for (index in selectedImages.indices) {

            val file = File(selectedImages[index].path)
            val reportBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            fileMultipartBody[index] =
                MultipartBody.Part.createFormData("images", file.name, reportBody)
        }
        createEventViewModel.getCreateEvent(
            progressDialog,
            activity,
            userId,
            title,
            description,
            eventType,
            venue_Date_and_time,
            fileMultipartBody
        )

    }


    // Validation All field method With Save Button
    private fun validateInputs() {
        StrEventTitle = binding.eventTitleEdit.text.toString()
        StrEventDescription = binding.eventDiscriptionEdit.text.toString()
        item = binding.spinner.selectedItem.toString()
        val venueTitle = ""
        val venueName = binding.venueNameEdit.text.toString()
        val venueLocation = binding.venueLocationEdit.text.toString()
        val venueDate = binding.venueDateCreateEvent.text.toString()
        val venueStartTime = binding.venueStartTime.text.toString()
        val venueEndTime = binding.venueEndTime.text.toString()

        val addFillData = CreateMultipleEventModel(
            venueTitle, venueName, venueLocation, venueDate, venueStartTime, venueEndTime
        )
        personList.add(addFillData)
        val gson = Gson()
        singleOrMultArrayString = gson.toJson(personList.filterNotNull().map { model ->
            mapOf(
                "sub_event_title" to model.venueTitle,
                "venue_Name" to model.venueName,
                "venue_location" to model.venueLocation,
                "date" to model.venueDate,
                "start_time" to model.venueStartTime,
                "end_time" to model.venueEndTime,
                // Add other properties as needed
            )
        })


        val selectedOption: String = binding.spinner.selectedItem.toString()
        if (binding.eventTitleEdit.text.isNullOrBlank()) {
            makeText(
                requireActivity(), "Please enter event title", Toast.LENGTH_SHORT
            ).show()

        } else if (binding.eventDiscriptionEdit.text.isNullOrBlank()) {
            makeText(requireActivity(), "Please enter event description", Toast.LENGTH_SHORT)
                .show()

        } else if (selectedOption == resources.getString(R.string.select_event)) {
            makeText(requireActivity(), "Please select event type", Toast.LENGTH_SHORT).show()
        }
        if (binding.checkbox.isChecked) {
            createEventApi()
        } else {
            if (venueName.isNullOrBlank()) {
                Toast.makeText(
                    requireActivity(),
                    "Please enter venue name",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (venueLocation.isNullOrBlank()) {
                Toast.makeText(
                    requireActivity(),
                    "Please enter venue location",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (venueDate.isNullOrBlank()) {
                Toast.makeText(
                    requireActivity(),
                    "Please enter venue date",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (venueStartTime.isNullOrBlank()) {
                Toast.makeText(
                    requireActivity(),
                    "Please enter venue start time",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (venueEndTime.isNullOrBlank()) {
                Toast.makeText(
                    requireActivity(),
                    "Please enter venue end time",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                createEventApi()
            }
        }

    }

    // Create Event Api With Save Button
    private fun createEventApi() {
        // val userIds = RequestBody.create(MultipartBody.FORM, userId.toString())
        val title = RequestBody.create(MultipartBody.FORM, StrEventTitle)
        val description = RequestBody.create(MultipartBody.FORM, StrEventDescription)
        val eventType = RequestBody.create(MultipartBody.FORM, item)
        val venue_Date_and_time =
            RequestBody.create(MultipartBody.FORM, singleOrMultArrayString.toString())
        val fileList = mutableListOf<File>()
        if (outputList.isNullOrEmpty()) {

            makeText(context, getString(R.string.please_select_image), Toast.LENGTH_SHORT)
                .show()
            return
        }

        Log.e(
            "AllDataCreate",
            " title  $StrEventTitle description $StrEventDescription  item $item singleOrMultArrayString$singleOrMultArrayString"
        )
        for (path in outputList) {
            fileList.add(File(path))
        }

        //val stringBuilder = StringBuilder()
        val fileMultipartBody = arrayOfNulls<MultipartBody.Part>(selectedImages.size)
        for (index in selectedImages.indices) {

            val file = File(selectedImages[index].path)
            val reportBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            fileMultipartBody[index] =
                MultipartBody.Part.createFormData("images", file.name, reportBody)
        }
        createEventViewModel.getCreateEvent(
            progressDialog,
            activity,
            userId,
            title,
            description,
            eventType,
            venue_Date_and_time,
            fileMultipartBody
        )
    }


    private fun createEventObserver() {
        createEventViewModel.progressIndicator.observe(requireActivity(), {})
        createEventViewModel.mcreateEventResponse.observe(requireActivity()) {
            val message = it.peekContent().message
            eventId = it.peekContent().eventId.toString()

            Log.e("EventIdAFD", "message" + message + "eventId " + eventId.toString())

            makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            Festa.encryptedPrefs.eventIds = it.peekContent().eventId.toString()
            getEdit(eventId)

            /* val paymentsFragment = Event_list_frg()
             val fr = requireFragmentManager().beginTransaction()
             fr.replace(R.id.containers, paymentsFragment)
             fr.commit()*/

            if (strCheckBtn == "2") {
                val bundle = Bundle()
                val eventIds = Festa.encryptedPrefs.eventIds
                bundle.putString("eventId", eventIds)
                Log.e("eventIds", "onCreateViewi123: $eventId")
                val cohostfrg = CoHostFragment()
                cohostfrg.arguments = bundle
                val fr = requireFragmentManager().beginTransaction()
                fr.replace(R.id.containers, cohostfrg)
                fr.commit()
            } else if (strCheckBtn == "3") {
                val bundle = Bundle()
                val eventIds = Festa.encryptedPrefs.eventIds
                bundle.putString("eventId", eventIds)
                Log.e("eventIds", "onCreateViewadd: $eventId")
                val paymentsFragment = GuestFragment()
                paymentsFragment.arguments = bundle
                val fr = requireFragmentManager().beginTransaction()
                fr.replace(R.id.containers, paymentsFragment)
                fr.commit()
            }
        }
        createEventViewModel.errorResponse.observe(requireActivity()) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
            // errorDialogs()
        }
    }


    // Update SingleEvent Api
    private fun createSingleEventFinalApi(
        title: String,
        description: String,
        venueName: String,
        venueLocation: String,
        venueDate: String,
        venueStartTime: String,
        venueEndTime: String,
        event_key: Int
    ) {
        // val userIds = RequestBody.create(MultipartBody.FORM, userId.toString())
        val titles = RequestBody.create(MultipartBody.FORM, title)
        val descriptions = RequestBody.create(MultipartBody.FORM, description)
        val eventTypes = RequestBody.create(MultipartBody.FORM, item)
        val venueNames = RequestBody.create(MultipartBody.FORM, venueName)
        val venueLocations = RequestBody.create(MultipartBody.FORM, venueLocation)
        val venueDates = RequestBody.create(MultipartBody.FORM, venueDate)
        val venueStartTimes = RequestBody.create(MultipartBody.FORM, venueStartTime)
        val venueEndTimes = RequestBody.create(MultipartBody.FORM, venueEndTime)
        val event_key = RequestBody.create(MultipartBody.FORM, event_key.toString())
        val fileList = mutableListOf<File>()

        for (path in outputList) {
            fileList.add(File(path))
        }

        Log.e("ASDFGH", "ASDFGHEventKeySignle$event_key")

        //val stringBuilder = StringBuilder()
        val fileMultipartBody = arrayOfNulls<MultipartBody.Part>(selectedImages.size)
        for (index in selectedImages.indices) {
            // stringBuilder.append(selectedImages[index].path).append("\n")

            val file = File(selectedImages[index].path)
            val reportBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            fileMultipartBody[index] =
                MultipartBody.Part.createFormData("images", file.name, reportBody)
        }
        createFinalEventViewModel.getCreateEvent(
            progressDialog,
            activity,
            eventId,
            titles,
            descriptions,
            eventTypes,
            venueNames,
            venueLocations,
            venueDates,
            venueStartTimes,
            venueEndTimes,
            event_key,
            fileMultipartBody
        )


    }

    private fun createSingleEventObserver() {
        createFinalEventViewModel.progressIndicator.observe(requireActivity(), {})
        createFinalEventViewModel.mcreateEventResponse.observe(requireActivity()) {
            val message = it.peekContent().successMessage


            makeText(requireContext(), message, Toast.LENGTH_SHORT).show()


            if (strCheckBtn == "2") {
                val bundle = Bundle()
                val eventIds = Festa.encryptedPrefs.eventIds
                bundle.putString("eventId", eventIds)
                Log.e("eventIds", "onCreateView123: $eventId")
                val cohostfrg = CoHostFragment()
                cohostfrg.arguments = bundle
                val fr = requireFragmentManager().beginTransaction()
                fr.replace(R.id.containers, cohostfrg)
                fr.commit()
            } else if (strCheckBtn == "3") {
                val bundle = Bundle()
                val eventIds = Festa.encryptedPrefs.eventIds
                bundle.putString("eventId", eventIds)
                Log.e("eventIds", "onCreateView-add: $eventId")
                val paymentsFragment = GuestFragment()
                paymentsFragment.arguments = bundle
                val fr = requireFragmentManager().beginTransaction()
                fr.replace(R.id.containers, paymentsFragment)
                fr.commit()
            }
        }
        createFinalEventViewModel.errorResponse.observe(requireActivity()) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
            // errorDialogs()
        }
    }


    // Update MultipleEvent Api
    private fun createMultipleEventApi(event_key: Int, singleOrMultArrayString: String?) {
        // val userIds = RequestBody.create(MultipartBody.FORM, userId.toString())
        val title = RequestBody.create(MultipartBody.FORM, StrEventTitle)
        val description = RequestBody.create(MultipartBody.FORM, StrEventDescription)
        val eventType = RequestBody.create(MultipartBody.FORM, item)
        val venue_Date_and_time =
            RequestBody.create(MultipartBody.FORM, singleOrMultArrayString.toString())
        val eventKey = RequestBody.create(MultipartBody.FORM, event_key.toString())
        val fileList = mutableListOf<File>()


        Log.e("SpinnerItem", " singleOrMultiArrayString  $item")
        for (path in outputList) {
            fileList.add(File(path))
        }

        //val stringBuilder = StringBuilder()
        val fileMultipartBody = arrayOfNulls<MultipartBody.Part>(selectedImages.size)
        for (index in selectedImages.indices) {
            // stringBuilder.append(selectedImages[index].path).append("\n")

            val file = File(selectedImages[index].path)
            val reportBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            fileMultipartBody[index] =
                MultipartBody.Part.createFormData("images", file.name, reportBody)
        }
        createMultiEventViewModel.getCreateEvent(
            progressDialog,
            activity,
            eventId,
            title,
            description,
            eventType,
            venue_Date_and_time,
            eventKey,
            fileMultipartBody
        )
    }

    private fun createMultiEventObserver() {
        createMultiEventViewModel.progressIndicator.observe(requireActivity(), {})
        createMultiEventViewModel.mcreateEventResponse.observe(requireActivity()) {
            val message = it.peekContent().successMessage
            makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            getEdit(eventId)
            if (strCheckBtn == "2") {
                val bundle = Bundle()
                val eventIds = Festa.encryptedPrefs.eventIds
                bundle.putString("eventId", eventIds)
                Log.e("eventIds", "onCreateViewi123: $eventId")
                val cohostfrg = CoHostFragment()
                cohostfrg.arguments = bundle
                val fr = requireFragmentManager().beginTransaction()
                fr.replace(R.id.containers, cohostfrg)
                fr.commit()
            } else if (strCheckBtn == "3") {
                val bundle = Bundle()
                val eventIds = Festa.encryptedPrefs.eventIds
                bundle.putString("eventId", eventIds)
                Log.e("eventIds", "onCreateViewadd: $eventId")
                val paymentsFragment = GuestFragment()
                paymentsFragment.arguments = bundle
                val fr = requireFragmentManager().beginTransaction()
                fr.replace(R.id.containers, paymentsFragment)
                fr.commit()
            }
        }
        createMultiEventViewModel.errorResponse.observe(requireActivity()) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
            // errorDialogs()
        }
    }


    private fun sendInviteApi() {
        sendInviteModel.sendInvites(
            progressDialog,
            activity,
            eventId
        )

        Log.e("EventIDShow", "InviteEventId$eventId")
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun sendInviteObserver() {
        sendInviteModel.progressIndicator.observe(viewLifecycleOwner, Observer {})
        sendInviteModel.mbookmark.observe(viewLifecycleOwner) {
            val message = it.peekContent().successMessage
            makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        }
        sendInviteModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
            // errorDialogs()
        }
    }

    // Delete Guest Api/............................................................
    override fun onDeleteClick(position: Int, id: String) {
        val subEventId = id
        deleteGuest(subEventId)
    }

    private fun deleteGuest(guestId: String) {
        deleteEventViewModel.deleteGuest(progressDialog, activity, guestId, eventId!!)

    }

    private fun deleteSubEventObserver() {
        deleteEventViewModel.progressIndicator.observe(requireActivity(), {
        })
        deleteEventViewModel.mDeleteResponse.observe(requireActivity()) {
            val message = it.peekContent().message
            makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            getEdit(eventId)

        }
        deleteEventViewModel.errorResponse.observe(requireActivity()) {
            com.freqwency.promotr.utils.ErrorUtil.handlerGeneralError(requireActivity(), it)

        }
    }


    private fun openGalleryForImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), REQUEST_IMAGE_PICK)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val clipData = data?.clipData
            if (clipData != null) {
                // Multiple images selected
                for (i in 0 until clipData.itemCount) {
                    val uri = clipData.getItemAt(i).uri
                    val file = createImageFile()
                    selectedImages.add(file)
                    copyUriToFile(uri, file)
                    binding.multipleImageSelect.setImageURI(uri)
                }
            } else if (data?.data != null) {
                // Single image selected
                val uri = data.data
                val file = createImageFile()
                selectedImages.add(file)
                if (uri != null) {
                    copyUriToFile(uri, file)
                    binding.multipleImageSelect.setImageURI(uri)
                }
            }

            // Display the selected images (names) after the user has finished selecting
            displaySelectedImages()

            // Use the 'selectedImages' list for further processing
            for (imageFile in selectedImages) {
                Log.d("SelectedImage", imageFile.absolutePath)
            }
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    private fun copyUriToFile(uri: android.net.Uri, file: File) {
        requireContext().contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }

    // Add Multiple event Popup method
    private fun displaySelectedImages() {
        outputList.clear() // Clear the list before adding new items

        for (selectedImage in selectedImages) {
            // Extract the full file name
            val fileName = selectedImage.name
            outputList.add(fileName)
        }

        Log.e("AllSelectImg", outputList.toString())
    }


    private fun removeImage() {
        // Clear the list of selected images and reset the ImageView
        selectedImages.clear()
        binding.multipleImageSelect.setImageResource(R.drawable.baseline_add_circle_outline_24)

        // Optionally, you may also want to update the UI or perform additional actions.
        // For example, if you have a list of displayed image names, you may want to clear that list.
        outputList.clear()

        Log.e("AllSelectImg", outputList.toString())
    }
    // Add backButton  Popup method


    public class spinnerAdapter constructor(
        context: Context, textViewResourceId: Int, strInterestedList: List<String>
    ) : ArrayAdapter<String?>(context, textViewResourceId)


    private fun interestedInMethod(eventTypes: String?) {
        val dAdapter = spinnerAdapter(requireContext(), R.layout.custom_spinner_two, strEventlist)
        dAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dAdapter.addAll(eventlist)
        binding.spinner.adapter = dAdapter

        if (eventTypes.equals("Business_Conference")) {
            val spinnerPosition1: Int = dAdapter.getPosition("Business_Conference")
            binding.spinner.setSelection(spinnerPosition1)
        } else if (eventTypes.equals("Music_Festivals")) {
            val spinnerPosition2: Int = dAdapter.getPosition("Music_Festivals")
            binding.spinner.setSelection(spinnerPosition2)
        } else if (eventTypes.equals("Birthday")) {
            val spinnerPosition3: Int = dAdapter.getPosition("Birthday")
            binding.spinner.setSelection(spinnerPosition3)
        } else if (eventTypes.equals("Exhibitions")) {
            val spinnerPosition3: Int = dAdapter.getPosition("Exhibitions")
            binding.spinner.setSelection(spinnerPosition3)
        } else if (eventTypes.equals("Wedding_Anniversary")) {
            val spinnerPosition3: Int = dAdapter.getPosition("Wedding_Anniversary")
            binding.spinner.setSelection(spinnerPosition3)
        } else if (eventTypes.equals("Sports")) {
            val spinnerPosition3: Int = dAdapter.getPosition("Sports")
            binding.spinner.setSelection(spinnerPosition3)
        } else if (eventTypes.equals("Marriage")) {
            val spinnerPosition3: Int = dAdapter.getPosition("Marriage")
            binding.spinner.setSelection(spinnerPosition3)
        }
    }

    override fun onResume() {
        super.onResume()

        updateValue = sharedPreferences.getString(KEY_VALUE, null).toString()

        Log.e("MultipleEventData", " singleOrMultupdateValue " + updateValue)

        if (updateValue == "2") {
            getEdit(eventId)
            updateValue = ""
            clearSharedPreferences()


        }

    }


    fun convertListToString(personList: List<CreateMultipleEventModel>): String {
        val stringBuilder = StringBuilder("[")
        personList.forEachIndexed { index, person ->
            stringBuilder.append(person.toString())
            if (index < personList.size - 1) {
                stringBuilder.append(", ")
            }
        }
        stringBuilder.append("]")
        return stringBuilder.toString()
    }

    private fun onEditButtonClick(item: MultipleEvent) {
        // Handle edit button click for the item
        // You can open an edit screen or dialog here
        makeText(requireContext(), "Edit: ${item.venueTitle}", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onDeleteButtonClick(item: MultipleEvent) {
        // Handle delete button click for the item
        dataList.remove(item)
        customAdapter.notifyDataSetChanged()
        makeText(requireContext(), "Delete: ${item.venueTitle}", Toast.LENGTH_SHORT).show()
    }

    private fun saveValueToSharedPreferences(value: String) {
        // Get the editor for SharedPreferences
        val editor = sharedPreferences.edit()

        // Save the value with the specified key
        editor.putString(KEY_VALUE, value)

        // Apply the changes
        editor.apply()
    }

    private fun clearSharedPreferences() {
        // Get the editor for SharedPreferences
        val editor = sharedPreferences.edit()

        // Clear all values
        editor.clear()

        // Apply the changes
        editor.apply()
    }
}











