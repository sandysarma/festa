package com.example.festa.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.festa.models.Eventlist_Model
import com.example.festa.R
import com.example.festa.databinding.PhotoslayoutBinding

class PhotosAdapter(private val mList: List<Eventlist_Model>, private val context: Context) :
    RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: PhotoslayoutBinding =
            PhotoslayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val ItemsViewModel = mList[position]
        Glide
            .with(context)
            .load(R.drawable.girl5)
            .into(holder.albumImg)
    }

    override fun getItemCount(): Int {
        return 20
    }

    class ViewHolder(view: PhotoslayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val albumImg: ImageView = view.albumImg
    }
}