package com.example.festa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.festa.models.Eventlist_Model
import com.example.festa.R

class feedviewAdapter(val context: Context,private val mList: List<Eventlist_Model>) : RecyclerView.Adapter<feedviewAdapter.ViewHolder>() {
    var isset: Boolean = false
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feedviewlayout, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // val ItemsViewModel = mList[position]

    }


    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
       // val feedview=ItemView.findViewById<LinearLayout>(R.id.gdfgdg)

    }

}