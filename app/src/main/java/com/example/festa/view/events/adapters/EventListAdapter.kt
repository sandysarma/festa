package com.example.festa.view.events.adapters

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.festa.view.events.viewmodel.particularuserlist.ParticularUserEventListResponse
import com.example.festa.guest.ui.GuestFragment
import com.example.festa.R
import com.example.festa.application.Festa
import com.example.festa.view.createevents.ui.CreateEventFragment
import com.example.festa.view.events.ui.EventListDetailsFragment
import de.hdodenhof.circleimageview.CircleImageView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventListAdapter(
    private val activity: Activity,
    private val supportFragmentManager: FragmentManager,
    private val eventList: List<ParticularUserEventListResponse.Event>
) : RecyclerView.Adapter<EventListAdapter.ViewHolder>() {
    var eventId = ""

    //  var isset: Boolean = false
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your views here
        val eventlinearbg = itemView.findViewById<LinearLayout>(R.id.eventlinearbg)
        val seemore = itemView.findViewById<LinearLayout>(R.id.seemorelinear)
        val seeless = itemView.findViewById<LinearLayout>(R.id.seelesslinear)
        val seemoreicon = itemView.findViewById<ImageView>(R.id.seemoreicon)
        val seelessicon = itemView.findViewById<ImageView>(R.id.seelessicon)
        val chat = itemView.findViewById<ImageView>(R.id.msg)
        val feed = itemView.findViewById<ImageView>(R.id.feed)
        val feed1 = itemView.findViewById<ImageView>(R.id.feed1)
        val guestlg = itemView.findViewById<ImageView>(R.id.guestuser)
        val editBtn = itemView.findViewById<ImageView>(R.id.editBtn)
        val clickEditBtn = itemView.findViewById<ImageView>(R.id.clickView)
        val guestsm = itemView.findViewById<ImageView>(R.id.guest1)
        val EventNameElist = itemView.findViewById<TextView>(R.id.EventNameElist)
        val EventNameMorelist = itemView.findViewById<TextView>(R.id.EventNameMorelist)
        val EventLocElist = itemView.findViewById<TextView>(R.id.EventLocElist)
        val EventLocMorelist = itemView.findViewById<TextView>(R.id.EventLocMorelist)
        val EventDescriptionElist = itemView.findViewById<TextView>(R.id.EventDescriptionElist)
        val EventDescMorelist = itemView.findViewById<TextView>(R.id.EventDescMorelist)
        val DateElist = itemView.findViewById<TextView>(R.id.DateElist)
        val DateMorelist = itemView.findViewById<TextView>(R.id.DateMorelist)
        val TimeElist = itemView.findViewById<TextView>(R.id.TimeElist)
        val TimeMorelist = itemView.findViewById<TextView>(R.id.TimeMorelist)
        val profileimg = itemView.findViewById<CircleImageView>(R.id.profileimg)
        val profileimg1 = itemView.findViewById<CircleImageView>(R.id.profileimg1)
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.eventlist_layout, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // val ItemsViewModel = mList[position]
        if (position % 2 == 0) {
            holder.eventlinearbg.setBackgroundResource(R.drawable.borderradious)// Even position
        } else {
            //  holder.linearReqBackground.setBackgroundColor(Color.parseColor("#FDF2D6")) // Odd position
            holder.eventlinearbg.setBackgroundResource(R.drawable.borderradiousy) // Odd position
        }

        val event = eventList[position]


        val venueDateAndTime = event?.venueDateAndTime?.getOrNull(0) // Assuming you want the first item from the list
        var EventName = ""
        var EventLocation = ""
        var EventDate = ""
        var EventTime = ""
        var EventDesp = event.description
        var formattedDate = ""

        if (venueDateAndTime != null) {
            // The list is not null and not empty
            EventName = event.title.toString()
            EventLocation = venueDateAndTime.venueLocation.toString()
            EventDate = venueDateAndTime.date.toString()
            EventTime = venueDateAndTime.startTime.toString()
            EventDesp = event.description

            holder.DateElist.text = EventDate
            holder.EventNameElist.text = EventName
            // Use the values for further operations

        } else {
            println("No data at position 0 or venueDateAndTime is null or empty")
        }


// Assuming that the images array contains at least one image filename
        val imageUrl = "http://13.51.205.211:6002/${event.images!!.firstOrNull()}"

        Glide.with(activity)
            .load(imageUrl)
            .placeholder(R.drawable.partypic) // Placeholder image while loading
            .into(holder.profileimg)




        holder.EventNameElist.text = EventName
        holder.EventLocElist.text = EventLocation

        holder.TimeElist.text = EventTime
        holder.EventDescriptionElist.text = EventDesp


        Log.e("EventLISTSK", " EventTime  :  " + EventTime)

        holder.seemoreicon.setOnClickListener {
            holder.seeless.visibility = View.GONE
            holder.seemore.visibility = View.VISIBLE
            holder.EventNameMorelist.text = EventName
            holder.EventLocMorelist.text = EventLocation
            holder.TimeMorelist.text = EventTime
            formattedDate = formatDate(EventDate)
            holder.DateMorelist.text = EventDate
            holder.EventDescMorelist.text = EventDesp

            Glide.with(activity)
                .load(imageUrl)
                .placeholder(R.drawable.partypic) // Placeholder image while loading
                .into(holder.profileimg1)

        }

        holder.seelessicon.setOnClickListener {
            holder.seeless.setVisibility(View.VISIBLE)
            holder.seemore.setVisibility(View.GONE)

        }

        holder.feed.setOnClickListener {
            val paymentsFragment = EventListDetailsFragment()
            val fr = supportFragmentManager!!.beginTransaction()
            fr.replace(R.id.containers, paymentsFragment)
            fr.commit()
        }
        holder.guestlg.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("eventId", eventId) // Replace 'eventId' with the actual variable holding the event ID
            Log.e("hellofd", "event: " + eventId)

            val guestFragment = GuestFragment()
            guestFragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.containers, guestFragment)
            transaction.addToBackStack(null) // Add to back stack to allow navigating back
            transaction.commit()
        }

        holder.editBtn.setOnClickListener {
            val eventId = event.id!!
            val bundle = Bundle()
            Festa.encryptedPrefs.eventIds = eventId
            bundle.putString("eventId", eventId) // Replace 'eventId' with the actual variable holding the event ID
            bundle.putString("adapter", "adapterValue"
            ) // Replace 'eventId' with the actual variable holding the event ID
            Log.e("hellofd", "event: " + eventId)
            val guestFragment = CreateEventFragment()
            guestFragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.containers, guestFragment)
            transaction.addToBackStack(null) // Add to back stack to allow navigating back
            transaction.commit()
        }

        holder.clickEditBtn.setOnClickListener {
            val eventId = event.id!!
            val bundle = Bundle()
            Festa.encryptedPrefs.eventIds = eventId
            bundle.putString(
                "eventId",
                eventId
            ) // Replace 'eventId' with the actual variable holding the event ID
            bundle.putString(
                "adapter",
                "adapterValue"
            ) // Replace 'eventId' with the actual variable holding the event ID
            Log.e("hellofd", "event: " + eventId)
            val guestFragment = CreateEventFragment()
            guestFragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.containers, guestFragment)
            transaction.addToBackStack(null) // Add to back stack to allow navigating back
            transaction.commit()
        }

        /*holder.guest.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("eventId", eventId)
            val paymentsFragment = GuestFragment()
            paymentsFragment.arguments = bundle
            val fr = supportFragmentManager!!.beginTransaction()
            fr.replace(R.id.containers, paymentsFragment)
            fr.commit()
        }*/
        holder.feed1.setOnClickListener {
            val eventIds = event.id
            Festa.encryptedPrefs.eventIds = eventIds.toString()
            val bundle = Bundle()
            bundle.putString(
                "eventId",
                eventIds
            )
            val eventListDetailsFragment = EventListDetailsFragment()
           // eventListDetailsFragment.arguments = bundle
            val fr = supportFragmentManager.beginTransaction()
            fr.replace(R.id.containers, eventListDetailsFragment)
            fr.commit()
        }

        holder.guestsm.setOnClickListener {
            // eventId = event.id!!
            val bundle = Bundle()
            bundle.putString(
                "eventId",
                eventId
            ) // Replace 'eventId' with the actual variable holding the event ID
            val guestFragment = GuestFragment()
            guestFragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.containers, guestFragment)
            transaction.addToBackStack(null) // Add to back stack to allow navigating back
            transaction.commit()
        }

    }


    override fun getItemCount(): Int {
        return eventList?.size ?: 0
    }

    private fun formatDate(inputDate: String): String {
        if (inputDate.isBlank()) {
            return "Invalid Date"
        }

        val inputFormat =
            SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.getDefault())

        try {
            val date: Date? = inputFormat.parse(inputDate)

            return if (date != null) {
                val outputFormat = SimpleDateFormat("E, dd MMM, yyyy", Locale.getDefault())
                outputFormat.format(date)
            } else {
                "Invalid Date"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            return "Invalid Date"
        }
    }


}