package com.example.festa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.festa.AlbumItem
import com.example.festa.R


class AlbumAdapter(private val albumItems: List<AlbumItem>) : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.bind(albumItems[position])
        val ItemsViewModel = albumItems[position]
        //holder.imageView.setImageResource(ItemsViewModel.image)
        holder.titletest.text = ItemsViewModel.title
        holder.descriptiontxt.text = ItemsViewModel.description
        Glide.with(holder.itemView).load(ItemsViewModel.imageUrl).into(holder.imageviews)
    }

    override fun getItemCount(): Int {
        return albumItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titletest=itemView.findViewById<TextView>(R.id.titleTextView)
        val descriptiontxt=itemView.findViewById<TextView>(R.id.descriptionTextView)
        val imageviews=itemView.findViewById<ImageView>(R.id.imageView)
       /* fun bind(albumItem: AlbumItem) {
            itemView.titletest.text = albumItem.title
            itemView.descriptionTextView.text = albumItem.description
            Glide.with(itemView.context).load(albumItem.imageUrl).into(itemView.imageView)
        }*/
    }
}
