package com.example.festa.view.cohost.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.festa.view.cohost.viewmodel.cohostlist.CostHostListResponse
import com.example.festa.guest.interfacedelete.OnItemClickListenerDelete
import com.example.festa.R

class CoHostAdapter(
    private val required: Context,
    private var personList: List<CostHostListResponse.CoHostsDatum>,
    private val itemClickListener: OnItemClickListenerDelete
) :
    RecyclerView.Adapter<CoHostAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.guestname)
        val contactTextView: TextView = itemView.findViewById(R.id.guestmobno)
        val deleteGuestBtn: ImageView = itemView.findViewById(R.id.deleteGuestBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.guest_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = personList[position]
        holder.nameTextView.text = person.coHostName
        holder.contactTextView.text = person.phoneNo.toString()

        holder.deleteGuestBtn.setOnClickListener {
            val logoutDialog = Dialog(required)
            logoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            logoutDialog.setContentView(R.layout.delete_guest_layout)
            val noDialog = logoutDialog.findViewById<LinearLayout>(R.id.noDialog)
            val yesDialog = logoutDialog.findViewById<LinearLayout>(R.id.yesDialog)

            val guestId = person.id.toString()

            val window = logoutDialog.window
            window!!.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            yesDialog.setOnClickListener {
                itemClickListener.onDeleteClick(position, guestId)
                logoutDialog.dismiss()

            }
            noDialog.setOnClickListener { logoutDialog.dismiss() }

            logoutDialog.show()
        }
    }


    override fun getItemCount(): Int {
        return personList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(newGuestList: List<CostHostListResponse.CoHostsDatum>) {
        personList = newGuestList // Assign the new list directly
        notifyDataSetChanged()
    }
}