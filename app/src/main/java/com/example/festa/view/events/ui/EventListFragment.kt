package com.example.festa.view.events.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.festa.view.events.adapters.EventListAdapter
import com.example.festa.view.events.viewmodel.particularuserlist.ParticularUserEventListViewModel
import com.example.festa.view.events.viewmodel.particularuserlist.ParticularUserEventListResponse
import com.example.festa.fragments.Filter
import com.example.festa.fragments.Notificationfrg
import com.example.festa.R
import com.example.festa.application.Festa
import com.example.festa.databinding.FragmentEventListBinding
import com.freqwency.promotr.utils.ErrorUtil
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventListFragment : Fragment() {
    private lateinit var activity: Activity
    var dataList: List<ParticularUserEventListResponse.Event> = ArrayList()
    private val eventListViewModel: ParticularUserEventListViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    private var eventListAdapter: EventListAdapter? = null

    private var userId = ""
    private var eventIds = ""

    private lateinit var binding: FragmentEventListBinding
    lateinit var dialogcircle: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        for (i in 1..20) {
            // data.add(Eventlist_Model(R.drawable.autresimg, "Bijouterie" + i))
        }
        activity = requireActivity()


        userId = Festa.encryptedPrefs.UserId

        Log.e("userId", "onCreateView: $userId")

        binding = FragmentEventListBinding.inflate(inflater, container, false)

        binding.searchicon.setOnClickListener {
            binding.search.visibility = View.VISIBLE
            binding.festalogo.visibility = View.GONE
            binding.eventlist.visibility = View.GONE
            binding.eventicon.visibility = View.GONE
        }
        binding.close.setOnClickListener {

            binding.search.visibility = View.GONE
            binding.festalogo.visibility = View.VISIBLE
            binding.eventlist.visibility = View.VISIBLE
            binding.eventicon.visibility = View.VISIBLE
        }
        binding.filter.setOnClickListener {
            // showBoottomSheet()
            val filter = Filter()
            val fr = requireFragmentManager().beginTransaction()
            fr.replace(R.id.containers, filter)
            fr.commit()
        }

        binding.notification.setOnClickListener {
            //img.setVisibility(View.VISIBLE)
            val paymentsFragment = Notificationfrg()
            val fr = requireFragmentManager().beginTransaction()
            fr.replace(R.id.containers, paymentsFragment)
            fr.commit()
        }

        EventListObserver()
        getEventList()


        return binding.root
    }

    private fun getEventList() {
        eventListViewModel.getEventList(progressDialog, activity, userId)
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun EventListObserver() {
        eventListViewModel.progressIndicator.observe(viewLifecycleOwner) {
        }
        eventListViewModel.mEventListResponse.observe(viewLifecycleOwner) {
            val message = it.peekContent().message
            val activitiesList = it.peekContent().events
            Festa.encryptedPrefs.EventId = eventIds

            binding.eventListRecycle.layoutManager = LinearLayoutManager(context)
            eventListAdapter = activitiesList?.let { it1 ->
                EventListAdapter(
                    activity, requireActivity().supportFragmentManager,
                    it1
                )
            }
            binding.eventListRecycle.adapter = eventListAdapter

            eventListViewModel.errorResponse.observe(viewLifecycleOwner) {
                ErrorUtil.handlerGeneralError(requireActivity(), it)
                // errorDialogs()
            }
        }
    }
}