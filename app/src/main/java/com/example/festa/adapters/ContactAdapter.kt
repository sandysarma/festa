package com.example.festa.adapters

// ContactsAdapterGuest.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.festa.guest.ui.Contact
import com.example.festa.R

class ContactAdapter(private val contacts: List<Contact>) : RecyclerView.Adapter<ContactAdapter.ContactViewHolderGuest>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ContactViewHolderGuest {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.guest_layout, parent, false)
        return ContactViewHolderGuest(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolderGuest, position: Int) {
        val contact = contacts[position]
        holder.displayNameTextView.text = contact.displayName
        holder.phoneNumberTextView.text = contact.phoneNumber
    }

    override fun getItemCount(): Int = contacts.size

    class ContactViewHolderGuest(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val displayNameTextView: TextView = itemView.findViewById(R.id.guestname)
        val phoneNumberTextView: TextView = itemView.findViewById(R.id.guestmobno)
    }
}