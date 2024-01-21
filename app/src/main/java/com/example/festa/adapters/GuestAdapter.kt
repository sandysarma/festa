package com.example.festa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.festa.guest.viewmodel.guestlist.GuestListResponse
import com.example.festa.R

class GuestAdapter(val required: Context, private val mList: List<GuestListResponse.GuestDatum>) : RecyclerView.Adapter<GuestAdapter.ViewHolder>() {
    var isset: Boolean = false
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.guest_layout, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val ItemsViewModel = mList[position]
    /* val status=ItemsViewModel.status
        if (status==0){
            holder.gueststatus.setText("Accepted")
        }else{
            holder.gueststatus.setText("Rejected")
        }*/
        holder.guestname.text=ItemsViewModel.guestName
        holder.guestmobno.text=ItemsViewModel.phoneNo.toString()


    }


    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val eventlinearbg=ItemView.findViewById<LinearLayout>(R.id.eventlinearbg)
        val seemore=ItemView.findViewById<LinearLayout>(R.id.seemorelinear)
        val seeless=ItemView.findViewById<LinearLayout>(R.id.seelesslinear)
        val seemoreicon=itemView.findViewById<ImageView>(R.id.seemoreicon)
        val seelessicon=itemView.findViewById<ImageView>(R.id.seelessicon)
        val profileimg=itemView.findViewById<ImageView>(R.id.profileimg)
        val guestname=itemView.findViewById<TextView>(R.id.guestname)
        val guestmobno=itemView.findViewById<TextView>(R.id.guestmobno)
        val gueststatus=itemView.findViewById<TextView>(R.id.gueststatus)

        //val imageView: ImageView = itemView.findViewById(R.id.actualimg)
        //val textView: TextView = itemView.findViewById(R.id.actualnm)
    }

}