package com.example.festa.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.festa.adapters.NotificationAdapter
import com.example.festa.view.events.ui.EventListFragment
import com.example.festa.models.Eventlist_Model
import com.example.festa.R
import com.example.festa.databinding.FragmentNotificationfrgBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Notificationfrg : Fragment() {
    private lateinit var binding : FragmentNotificationfrgBinding

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentNotificationfrgBinding.inflate(inflater,container,false)
        binding.backarrow.setOnClickListener {
            val paymentsFragment = EventListFragment()
            val fr = fragmentManager!!.beginTransaction()
            fr.replace(R.id.containers, paymentsFragment)
            fr.commit()
        }

        binding.notificationrecycle.layoutManager = LinearLayoutManager(context)
        val data = ArrayList<Eventlist_Model>()
        for (i in 1..20) {
            // data.add(Eventlist_Model(R.drawable.autresimg, "Bijouterie" + i))
        }
        val adapter = NotificationAdapter(data)
        binding.notificationrecycle.adapter = adapter
        return binding.root
    }
}