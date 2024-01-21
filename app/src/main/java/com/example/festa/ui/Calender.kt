package com.example.festa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festa.adapters.AlbumAdapter
import com.example.festa.AlbumItem
import com.example.festa.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Calender : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)
        val recycle=findViewById<RecyclerView>(R.id.recyclerView)

        val albumItems = listOf(
            AlbumItem("https://example.com/image1.jpg", "Image 1", "Description 1"),
            AlbumItem("https://example.com/image2.jpg", "Image 2", "Description 2"),
            // Add more AlbumItems as needed
        )

        val adapter = AlbumAdapter(albumItems)
        recycle.layoutManager = LinearLayoutManager(this)
        recycle.adapter = adapter
    }
}