package com.example.festa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.festa.fragments.Contactfrg
import com.example.festa.fragments.Calenderfrg
import com.example.festa.view.events.ui.EventListFragment
import com.example.festa.fragments.Homepage
import com.example.festa.view.profile.ui.ProfileFragment
import com.example.festa.R
import com.example.festa.databinding.ActivityDashboardBinding
import com.example.festa.view.createevents.ui.CreateEventFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reserveReceived = intent.getStringExtra("login").toString()

        Log.e("UserMessage", "onCreate:reserveReceived " + reserveReceived)
        binding.homeBtn.setOnClickListener {
            Toast.makeText(applicationContext, "Please login", Toast.LENGTH_SHORT).show()
        }
        binding.calender.setOnClickListener {
            Toast.makeText(applicationContext, "Please login", Toast.LENGTH_SHORT).show()
        }
        binding.homeplus.setOnClickListener {
            Toast.makeText(applicationContext, "Please login", Toast.LENGTH_SHORT).show()
        }

        if (reserveReceived == "login") {

            val eventListFrg = EventListFragment()
            replace_fragment(eventListFrg)

            binding.homeBtn.visibility = View.GONE
            binding.homeMainBtn.visibility = View.VISIBLE

            binding.homeBtn.setOnClickListener {
                binding.homeBtn.visibility = View.GONE
                binding.homeMainBtn.visibility = View.VISIBLE
                binding.calender.visibility = View.VISIBLE
                binding.calenderMain.visibility = View.GONE
                binding.chat.visibility = View.VISIBLE
                binding.chatMain.visibility = View.GONE
                binding.profile.visibility = View.VISIBLE
                binding.profileMain.visibility = View.GONE

                val eventListFrg = EventListFragment()
                replace_fragment(eventListFrg)
            }
            binding.homeplus.setOnClickListener {
                val createEventFragment = CreateEventFragment()
                replace_fragment(createEventFragment)
            }
            binding.calender.setOnClickListener {
                binding.homeBtn.visibility = View.VISIBLE
                binding.homeMainBtn.visibility = View.GONE
                binding.calenderMain.visibility = View.VISIBLE
                binding.calender.visibility = View.GONE
                binding.chat.visibility = View.VISIBLE
                binding.chatMain.visibility = View.GONE
                binding.profile.visibility = View.VISIBLE
                binding.profileMain.visibility = View.VISIBLE

                val createEventfrg = Calenderfrg()
                replace_fragment(createEventfrg)
            }


            binding.profile.setOnClickListener {
                binding.homeBtn.visibility = View.VISIBLE
                binding.homeMainBtn.visibility = View.GONE
                binding.profileMain.visibility = View.VISIBLE
                binding.profile.visibility = View.GONE
                binding.calender.visibility = View.VISIBLE
                binding.calenderMain.visibility = View.GONE
                binding.chat.visibility = View.VISIBLE
                binding.chatMain.visibility = View.GONE

                val createEventfrg = ProfileFragment()
                replace_fragment(createEventfrg)
            }
            binding.chat.setOnClickListener {
                binding.homeBtn.visibility = View.VISIBLE
                binding.homeMainBtn.visibility = View.GONE
                binding.profileMain.visibility = View.GONE
                binding.profile.visibility = View.VISIBLE
                binding.calender.visibility = View.VISIBLE
                binding.calenderMain.visibility = View.GONE
                binding.chat.visibility = View.GONE
                binding.chatMain.visibility = View.VISIBLE
                val createEventfrg = Contactfrg()
                replace_fragment(createEventfrg)
            }
        } else {
            val eventListFrg = Homepage()
            replace_fragment(eventListFrg)
        }
    }

    fun replace_fragment(fragment: Fragment) {
        // replace fragment....
        /*val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        // fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_out_right)
        fragmentTransaction.replace(R.id.containers, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()*/

        val fragmentManager = supportFragmentManager
        (findViewById<View>(R.id.containers) as FrameLayout).removeAllViews()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.containers, fragment)
        fragmentTransaction.commit()
    }
}