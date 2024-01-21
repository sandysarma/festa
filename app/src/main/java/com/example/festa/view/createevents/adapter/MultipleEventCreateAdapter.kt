package com.example.festa.view.createevents.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.festa.R
import com.example.festa.view.createevents.multipleeventsharepre.EditPopup
import com.example.festa.view.createevents.multipleeventsharepre.MultipleEvent

class MultipleEventCreateAdapter(
    private var addMultipleEventList: MutableList<MultipleEvent>,
    private val onEditClick: (MultipleEvent) -> Unit,
    private val onDeleteClick: (MultipleEvent) -> Unit
) :
    RecyclerView.Adapter<MultipleEventCreateAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleName: TextView = itemView.findViewById(R.id.titleName)
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val eventLocationTxt: TextView = itemView.findViewById(R.id.eventLocationTxt)
        val eventDate: TextView = itemView.findViewById(R.id.eventDate)
        val eventStartTime: TextView = itemView.findViewById(R.id.eventStartTime)
        val eventEndTime: TextView = itemView.findViewById(R.id.eventEndTime)
        val deleteEvent: ImageView = itemView.findViewById(R.id.deleteEvent)
        val editEventBtn: ImageView = itemView.findViewById(R.id.editEventBtn)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.add_multiple_event_layout, parent, false)
        return ViewHolder(
            itemView
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = addMultipleEventList[position]
        holder.titleName.text = person.venueTitle
        holder.eventName.text = person.venueName
        holder.eventLocationTxt.text = person.venueLocation
        holder.eventDate.text = person.venueDate
        holder.eventStartTime.text = person.venueStartTime
        holder.eventEndTime.text = person.venueEndTime

        holder.editEventBtn.setOnClickListener {
            onEditClick(person)
            showEditPopup(person, holder)
        }

        holder.deleteEvent.setOnClickListener {
            onDeleteClick(person)
            addMultipleEventList.remove(person)
            notifyDataSetChanged() // Notify the adapter of the data change
        }
    }

    override fun getItemCount(): Int {
        return addMultipleEventList.size
    }

    private fun showEditPopup(
        person: MultipleEvent,
        holder: ViewHolder
    ) {
        EditPopup(holder.itemView.context, person) { updatedEvent ->
            // Update the list with the edited event
            val position = addMultipleEventList.indexOf(person)
            if (position != -1) {
                addMultipleEventList[position] = updatedEvent
                notifyItemChanged(position)
            }
        }.show()
    }
}


