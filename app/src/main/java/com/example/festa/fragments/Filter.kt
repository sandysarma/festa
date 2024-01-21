package com.example.festa.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.festa.view.events.ui.EventListFragment
import com.example.festa.R
import com.example.festa.databinding.FragmentFilterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Filter : Fragment() {

    private lateinit var binding : FragmentFilterBinding
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentFilterBinding.inflate(inflater,container,false)
        binding.backfilter.setOnClickListener {
            val paymentsFragment = EventListFragment()
            val fr = fragmentManager!!.beginTransaction()
            fr.replace(R.id.containers, paymentsFragment)
            fr.commit()

        }

        return binding.root
    }


}