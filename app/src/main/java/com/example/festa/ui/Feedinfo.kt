package com.example.festa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.festa.adapters.feedviewAdapter
import com.example.festa.models.Eventlist_Model
import com.example.festa.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Feedinfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedinfo)
        val feedrecycle=findViewById<RecyclerView>(R.id.feedviewrecycle)
        feedrecycle.layoutManager = LinearLayoutManager(this)
        val data = ArrayList<Eventlist_Model>()
        for (i in 1..20) {
            // data.add(Eventlist_Model(R.drawable.autresimg, "Bijouterie" + i))
        }
        val adapter = feedviewAdapter(this,data)
        feedrecycle.adapter = adapter

    }
}