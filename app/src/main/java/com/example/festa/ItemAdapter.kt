package com.example.festa

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val itemList: List<ClipData.Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val co_name: TextView = itemView.findViewById(R.id.coHostName)
        val mobno: TextView = itemView.findViewById(R.id.cohostmobno)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.co_name.text = item.text
        holder.mobno.text = item.text
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
