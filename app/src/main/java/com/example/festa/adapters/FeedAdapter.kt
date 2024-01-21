package com.example.festa.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.festa.models.Eventlist_Model
import com.example.festa.R
import com.example.festa.ui.Feedinfo


class FeedAdapter(val context: Context, private val mList: List<Eventlist_Model>) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    var isset: Boolean = false

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feedlayout, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // val ItemsViewModel = mList[position]

        holder.feedview.setOnClickListener {
            val intent= Intent(context, Feedinfo::class.java)
            context.startActivity(intent)
        }


    }


    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val feedview=ItemView.findViewById<LinearLayout>(R.id.views)
        val seemore=ItemView.findViewById<LinearLayout>(R.id.seemorelinear)
        val seeless=ItemView.findViewById<LinearLayout>(R.id.seelesslinear)
        val seemoreicon=itemView.findViewById<ImageView>(R.id.seemoreicon)
        val seelessicon=itemView.findViewById<ImageView>(R.id.seelessicon)
        val profileimg=itemView.findViewById<ImageView>(R.id.profileimg)

        //val imageView: ImageView = itemView.findViewById(R.id.actualimg)
        //val textView: TextView = itemView.findViewById(R.id.actualnm)
    }

}