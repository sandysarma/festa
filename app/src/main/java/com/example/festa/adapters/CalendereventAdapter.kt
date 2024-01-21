package com.example.festa.adapters

import android.app.Activity
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
import com.example.festa.view.events.viewmodel.eventlist.EventListResponse
import com.example.festa.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendereventAdapter
    (private val activity: Activity,
     private val supportFragmentManager: FragmentManager,
     private val eventList:
       List<EventListResponse.Event>,
     private val valueList: List<EventListResponse.VenueDateAndTime>)
    : RecyclerView.Adapter<CalendereventAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val eventlinearbg=ItemView.findViewById<LinearLayout>(R.id.eventlinearbg)
        val seemore=ItemView.findViewById<LinearLayout>(R.id.seemorelinear)
        val seeless=ItemView.findViewById<LinearLayout>(R.id.seelesslinear)
        val seemoreicon=itemView.findViewById<ImageView>(R.id.seemoreicon)
        val seelessicon=itemView.findViewById<ImageView>(R.id.seelessicon)
        val Calenderprofileimg=itemView.findViewById<CircleImageView>(R.id.Calenderprofileimg)
        val profileimg1Calender=itemView.findViewById<CircleImageView>(R.id.profileimg1Calender)
        val EventNameCalenderlist=itemView.findViewById<TextView>(R.id.EventNameCalenderlist)
        val EventLocCalenderlist=itemView.findViewById<TextView>(R.id.EventLocCalenderlist)
        val EventDescriptionCalist=itemView.findViewById<TextView>(R.id.EventDescriptionCalist)
        val DateCalenderlist=itemView.findViewById<TextView>(R.id.DateCalenderlist)
        val TimeCalenderlist=itemView.findViewById<TextView>(R.id.TimeCalenderlist)
        val EventNameMoreCalenderlist=itemView.findViewById<TextView>(R.id.EventNameMoreCalenderlist)
        val EventLocMoreCalenderlist=itemView.findViewById<TextView>(R.id.EventLocMoreCalenderlist)
        val DateMoreCalenderlist=itemView.findViewById<TextView>(R.id.DateMoreCalenderlist)
        val TimeMoreCalenderlist=itemView.findViewById<TextView>(R.id.TimeMoreCalenderlist)
        val EventDescMoreCAlenderlist=itemView.findViewById<TextView>(R.id.EventDescMoreCAlenderlist)
        val chat=itemView.findViewById<ImageView>(R.id.msg)
        val feed=itemView.findViewById<ImageView>(R.id.feed)
        val guest=itemView.findViewById<ImageView>(R.id.guestuser)

        //val imageView: ImageView = itemView.findViewById(R.id.actualimg)
        //val textView: TextView = itemView.findViewById(R.id.actualnm)
    }
   // var isset: Boolean = false
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycercalenderlist, parent, false)

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
        val venueDateAndTime =
            event.venueDateAndTime?.get(0) // Assuming you want the first item from the list
        // val EventName = venueDateAndTime?.venueName
        val EventNameCalender = event.title
        val EventLocationCalender = venueDateAndTime?.venueLocation
        val EventDateCalender = venueDateAndTime?.date
        val EventTimeCalender = venueDateAndTime?.startTime
        val EventDespCalender = event.description

        val eventId = eventList[position].id!!


// Assuming that the images array contains at least one image filename
        val imageUrl = "http://13.51.205.211:6002/${event.images!!.firstOrNull()}"

        Glide.with(activity)
            .load(imageUrl)
            .placeholder(R.drawable.partypic) // Placeholder image while loading
            .into(holder.Calenderprofileimg)




        Log.e("hello", "images: "+imageUrl)

        Log.e("Event", "EventName: " + EventNameCalender + "EventLocation:  " + EventLocationCalender)


        val format1 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val dt1: Date?
        try {
            dt1 = format1.parse(EventDateCalender)
            val format2 = SimpleDateFormat("dd", Locale.getDefault())
            val format3 = SimpleDateFormat("MMM", Locale.getDefault())
            val format4 = SimpleDateFormat("yyyy", Locale.getDefault())

            val finalDay = format2.format(dt1)
            val finalMonth = format3.format(dt1)
            val finalYear = format4.format(dt1)

            val newFormatDateCalender = "$finalDay $finalMonth, $finalYear-"
            holder.DateCalenderlist.text = newFormatDateCalender
           holder.DateMoreCalenderlist.text = newFormatDateCalender


        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.EventNameCalenderlist.text = EventNameCalender
        holder.EventLocCalenderlist.text = EventLocationCalender
      //  holder.DateCalenderlist.text = EventDateCalender
        holder.TimeCalenderlist.text = EventTimeCalender
        holder.EventDescriptionCalist.text = EventDespCalender

        holder.seemoreicon.setOnClickListener {
            // Increase height and width
            /*     if (isset==false){
                     val params =  holder.profileimg.layoutParams
                     params.height *= 2
                     params.width *= 2
                     holder.seemoreicon.layoutParams = params*/
            holder.seeless.setVisibility(View.GONE)
            holder.seemore.setVisibility(View.VISIBLE)

            holder.EventNameMoreCalenderlist.text = EventNameCalender
            holder.EventLocMoreCalenderlist.text = EventLocationCalender
           // holder.DateMoreCalenderlist.text = EventDateCalender
            holder.TimeMoreCalenderlist.text = EventTimeCalender
            holder.EventDescMoreCAlenderlist.text = EventDespCalender

            Glide.with(activity)
                .load(imageUrl)
                .placeholder(R.drawable.partypic) // Placeholder image while loading
                .into(holder.profileimg1Calender)

        }

        holder.seelessicon.setOnClickListener {
            holder.seeless.setVisibility(View.VISIBLE)
            holder.seemore.setVisibility(View.GONE)

        }


    }


    override fun getItemCount(): Int {
        return eventList?.size ?: 0
    }



}