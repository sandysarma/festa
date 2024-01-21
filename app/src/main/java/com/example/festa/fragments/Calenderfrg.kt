package com.example.festa.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.festa.adapters.CalendereventAdapter
import com.example.festa.view.events.ui.EventListFragment
import com.example.festa.view.events.viewmodel.eventlist.EventListResponse
import com.example.festa.models.Eventlist_Model
import com.example.festa.R
import com.example.festa.application.Festa
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Calenderfrg : Fragment() {
    private lateinit var activity: Activity
    var dataList: List<EventListResponse.Event> = ArrayList()
    var venueList: List<EventListResponse.VenueDateAndTime> = ArrayList()
        // val eventListViewModel: EventListViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    var calendereventAdapter: CalendereventAdapter? = null
    private lateinit var calendereventrecycle: RecyclerView
    //val eventListViewModel = ViewModelProvider(this).get(EventListViewModel::class.java)

    private var userId = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calenderfrg, container, false)
        calendereventrecycle = view.findViewById(R.id.calendereventrecycle)

        activity = requireActivity()

        userId = Festa.encryptedPrefs.UserId

        Log.e("userId", "onCreateView: " + userId)

        val backarrowcalender=view.findViewById<ImageView>(R.id.backarrowcalender)
        backarrowcalender.setOnClickListener {
            //img.setVisibility(View.VISIBLE)
            val paymentsFragment = EventListFragment()
            val fr = requireFragmentManager().beginTransaction()
            fr.replace(R.id.containers, paymentsFragment)
            fr.commit()
        }


        val data = ArrayList<Eventlist_Model>()
        for (i in 1..20) {
            // data.add(Eventlist_Model(R.drawable.autresimg, "Bijouterie" + i))
        }

      //  CalenderListObserver()
        //getCalenderList()
        return view

    }

  /*  private fun getCalenderList() {
        eventListViewModel.getEventList(
            progressDialog,
            activity,
            userId,
        )

        Log.e("aaaaa", "userId" + userId)
    }*/

   /* @SuppressLint("FragmentLiveDataObserve")
    private fun CalenderListObserver() {
        eventListViewModel.progressIndicator.observe(viewLifecycleOwner, Observer {
        })
        eventListViewModel.mEventListResponse.observe(viewLifecycleOwner) {
            val message = it.peekContent().message
            dataList = it.peekContent().events ?: emptyList()
            calendereventrecycle.layoutManager = LinearLayoutManager(context)
            calendereventAdapter = CalendereventAdapter(
                activity,
                requireActivity().supportFragmentManager,
                dataList,
                venueList
            )

            for (event in dataList) {
                val venueList = event.venueDateAndTime ?: emptyList()

                // Now you can iterate through venueList
                for (venue in venueList) {
                    // Access properties of the venue
                    val venueId = venue.id

                    // Perform operations on each venue
                }
                calendereventrecycle.adapter = calendereventAdapter



                *//*  binding.eventlistrecycle.isVerticalScrollBarEnabled = true
            binding.eventlistrecycle.isVerticalFadingEdgeEnabled = true
            binding.eventlistrecycle.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
             eventListAdapter = Event_list_Adapter(requireActivity().supportFragmentManager,dataList)
            binding.eventlistrecycle.adapter = eventListAdapter*//*
                Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT)
                    .show()
                *//* val intent= Intent(this, SignInverify::class.java)
             startActivity(intent)*//*
            }
            eventListViewModel.errorResponse.observe(viewLifecycleOwner) {
                ErrorUtil.handlerGeneralError(requireActivity(), it)
                // errorDialogs()
            }
        }


        *//*   private fun showBoottomSheet() {
           dialogcircle = Dialog(requireContext())
           dialogcircle.setContentView(R.layout.filterandrefinelayout)
           dialogcircle.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
           dialogcircle.show()

       }*//*


    }*/
}