package com.example.festa.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.festa.R
import com.example.festa.view.logins.ui.SignInActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Homepage : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View= inflater.inflate(R.layout.fragment_homepage, container, false)
        val eventlinearbg=view.findViewById<LinearLayout>(R.id.eventlinearbg)
        val seemore=view.findViewById<LinearLayout>(R.id.seemorelinear1)
        val seeless=view.findViewById<LinearLayout>(R.id.seelesslinear1)
        val seemoreicon=view.findViewById<ImageView>(R.id.seemoreicon1)
        val seelessicon=view.findViewById<ImageView>(R.id.seelessicon1)
        val profileimg=view.findViewById<ImageView>(R.id.msg1)
        val guest=view.findViewById<ImageView>(R.id.guestuser1)
        val login=view.findViewById<LinearLayout>(R.id.loginhomepage)

        login.setOnClickListener {
            val intent=Intent(requireActivity(), SignInActivity::class.java)
            startActivity(intent)
        }

        seemoreicon.setOnClickListener {
            seeless.setVisibility(View.GONE)
            seemore.setVisibility(View.VISIBLE)

        }

        seelessicon.setOnClickListener {
                seeless.setVisibility(View.VISIBLE)
                seemore.setVisibility(View.GONE)

        }
        return view
    }
}