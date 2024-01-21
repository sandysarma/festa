package com.example.festa.ui.theme.bookmark

import android.annotation.SuppressLint

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.festa.R
import com.example.festa.ui.theme.collectguestlist.BirthdayGuestList
import com.example.festa.ui.theme.bookmark.model.BookMarkGetResponse
import java.text.SimpleDateFormat
import java.util.*

class BookMarkGetAdapter (
    private val required: Context,
    private var bookMarkList: List<BookMarkGetResponse.AllCollection>
) :
    RecyclerView.Adapter<BookMarkGetAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookmarkName: TextView = itemView.findViewById(R.id.bookmarkName)
        val createdDate: TextView = itemView.findViewById(R.id.createdDate)
        val clickToMoveBtn: ImageView = itemView.findViewById(R.id.clickToMoveBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.collection_bookmark_layout, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = bookMarkList[position]
        // holder.bookmarkName.text = person.collectionName

        val strCollectionName = person.collectionName
        val strGuestNo = person.collectionEntriesCount
        val strDate = person.collectionCreatedDate

        val inputDateString = strDate.toString()
        val formattedDate = formatDateString(inputDateString)
        holder.createdDate.text = formattedDate
        holder.bookmarkName.text = "$strCollectionName($strGuestNo)"
        Log.e("bookMarkLists", "bookMarkLists " + strDate)

        holder.clickToMoveBtn.setOnClickListener {
            val id = person.collectionId.toString()
            val strCollectionName = person.collectionName
            val intent = Intent(required, BirthdayGuestList::class.java)
            intent.putExtra("collectionId",id)
            intent.putExtra("strCollectionName",strCollectionName)
            required.startActivity(intent)
        }

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

    fun setFilteredList(countryList: List<BookMarkGetResponse.AllCollection>)
    {
        this.bookMarkList = countryList
        notifyDataSetChanged()
    }
}