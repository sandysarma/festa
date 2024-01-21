package com.example.festa.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.festa.R
import com.example.festa.databinding.ShowimageUrlBinding
import com.example.festa.view.events.viewmodel.album.AllAlbumResponse

class GalleryAdapter(
    private val context: Context,
    private val list: ArrayList<AllAlbumResponse.Albums>
) : RecyclerView.Adapter<GalleryAdapter.GalleryviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryviewHolder {
        val binding: ShowimageUrlBinding =
            ShowimageUrlBinding.inflate(LayoutInflater.from(context), parent, false)
        return GalleryviewHolder(binding)
    }

    override fun onBindViewHolder(
        galleryviewHolder: GalleryviewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        /* holder.UploadImage1.setImageURI(uriArrayList.get(position));
         */

        /*Glide
            .with(context)
            .load(uriArrayList[position])
            .placeholder(R.drawable.img)
            .into(galleryviewHolder.albumImg)
        galleryviewHolder.albumImg.setImageURI(uriArrayList[position])*/

        /*galleryviewHolder.removeImageBtn.setOnClickListener {
            try {
                uriArrayList.removeAt(position)
                notifyItemRemoved(position)
            } catch (e: IndexOutOfBoundsException) {

            }
        }*/

        galleryviewHolder.albumTitle.text = list[position].album_name
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class GalleryviewHolder(view: ShowimageUrlBinding) :
        RecyclerView.ViewHolder(view.root) {

        val albumImg: ImageView = view.albumImg
        val albumTitle: TextView = view.albumTitle
    }
}