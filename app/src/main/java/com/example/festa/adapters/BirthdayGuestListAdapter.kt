package com.example.festa.adapters

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
import com.example.festa.R
import com.example.festa.application.Festa
import com.example.festa.guest.interfacedelete.OnItemClickListenerDelete
import com.example.festa.guest.viewmodel.bookmarkname.BookMarkNameResponse
import com.example.festa.ui.theme.collectguestlist.OnItemClickListenerGuestDelete
import java.text.SimpleDateFormat
import java.util.*

class BirthdayGuestListAdapter (
    private val required: Context,
    private var bookMarkList: List<BookMarkNameResponse.CollectionGuest>,
    private val itemClickListener: OnItemClickListenerDelete
) :
    RecyclerView.Adapter<BirthdayGuestListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val guestName: TextView = itemView.findViewById(R.id.guestName)
        val phoneNumber: TextView = itemView.findViewById(R.id.phoneNumber)
        val deleteGuestBtn: ImageView = itemView.findViewById(R.id.deleteGuestBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_birthday, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = bookMarkList[position]
        // holder.bookmarkName.text = person.collectionName

        holder.guestName.text = person.guestName
        holder.phoneNumber.text = person.phoneNo

        val collectId = Festa.encryptedPrefs.collectionId

        holder.deleteGuestBtn.setOnClickListener(View.OnClickListener {
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
        })


    }

    override fun getItemCount(): Int {
        return bookMarkList.size
    }

    private fun formatDateString(inputDateString: String): String {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = inputFormat.parse(inputDateString)

            // Format the date into the desired format
            val outputFormat = SimpleDateFormat("EEE, dd MMM, yyyy", Locale.getDefault())
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun setFilteredList(countryList: List<BookMarkNameResponse.CollectionGuest>)
    {
        this.bookMarkList = countryList
        notifyDataSetChanged()
    }
}