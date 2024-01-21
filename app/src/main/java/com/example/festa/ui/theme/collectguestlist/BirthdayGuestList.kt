package com.example.festa.ui.theme.collectguestlist

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.festa.adapters.BirthdayGuestListAdapter
import com.example.festa.databinding.ActivityBirthdayGuestListBinding
import com.example.festa.guest.interfacedelete.OnItemClickListenerDelete
import com.example.festa.guest.viewmodel.bookmarkname.BookMarkNameResponse
import com.example.festa.guest.viewmodel.bookmarkname.BookMarkNameViewModel
import com.example.festa.ui.theme.collectguestlist.deleteguest.DeleteGuestCollectModel
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class BirthdayGuestList : AppCompatActivity(), OnItemClickListenerDelete {
    private lateinit var binding: ActivityBirthdayGuestListBinding
    private var collectionId = ""
    private var strCollectionName = ""
    private lateinit var activity: Activity
    private val deleteModel: DeleteGuestCollectModel by viewModels()
    private val bookMarkGetViewModel: BookMarkNameViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(this) }
    private var eventId = ""
    private var customAdapter: BirthdayGuestListAdapter? = null
    var isValue = false
    private var bookMarkList: List<BookMarkNameResponse.CollectionGuest> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBirthdayGuestListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        activity = this

        collectionId = intent.getStringExtra("collectionId").toString()
        strCollectionName = intent.getStringExtra("strCollectionName").toString()


        binding.collectionMarkTxt.text = strCollectionName
        binding.brdyguestlistbackarrow.setOnClickListener {
            finish()
        }

        binding.searchicon.setOnClickListener {
            binding.brdyguestlistbackarrow.visibility = View.GONE
            binding.collectionMarkTxt.visibility = View.GONE
            binding.searchicon.visibility = View.GONE
            binding.search.visibility = View.VISIBLE
        }

        binding.close.setOnClickListener {
            binding.brdyguestlistbackarrow.visibility = View.VISIBLE
            binding.collectionMarkTxt.visibility = View.VISIBLE
            binding.search.visibility = View.GONE
            binding.searchicon.visibility = View.VISIBLE
        }

        binding.searchCountryName.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        getBooMarkGuestList()
        bookGuestMarkObserver()
        deleteObserver()
    }


    private fun getBooMarkGuestList() {
        bookMarkGetViewModel.getBookMark(progressDialog, activity, collectionId)

    }

    private fun bookGuestMarkObserver() {
        bookMarkGetViewModel.progressIndicator.observe(this, Observer {
        })
        bookMarkGetViewModel.mguestlist.observe(this) {
            val message = it.peekContent().successMessage
            bookMarkList = it.peekContent().collectionGuests!!

            binding.recyclerBirthdayList.isVerticalScrollBarEnabled = true
            binding.recyclerBirthdayList.isVerticalFadingEdgeEnabled = true
            binding.recyclerBirthdayList.layoutManager =
                LinearLayoutManager(this@BirthdayGuestList, LinearLayoutManager.VERTICAL, false)
            customAdapter =
                bookMarkList?.let { it1 ->
                    BirthdayGuestListAdapter(
                        this@BirthdayGuestList,
                        it1,
                        this
                    )
                }
            binding.recyclerBirthdayList.adapter = customAdapter


        }
        bookMarkGetViewModel.errorResponse.observe(this) {
            com.freqwency.promotr.utils.ErrorUtil.handlerGeneralError(this@BirthdayGuestList, it)
            // errorDialogs()
        }
    }

    private fun filterList(query: String?) {

        if (query != null) {
            val filterList = ArrayList<BookMarkNameResponse.CollectionGuest>()
            for (i in bookMarkList) {
                if (i.guestName?.lowercase(Locale.ROOT)
                        ?.contains(query) == true || i.phoneNo?.lowercase(Locale.ROOT)
                        ?.contains(query) == true
                ) {
                    filterList.add(i)
                }
            }
            if (filterList.isEmpty()) {
                Toast.makeText(this@BirthdayGuestList, "No Data found ", Toast.LENGTH_LONG).show()
            } else {
                customAdapter?.setFilteredList(filterList)
            }

        }

    }

    private fun toggleTextVisibility() {
        isValue = !isValue

        if (isValue) {

        } else {

        }
    }


    override fun onDeleteClick(position: Int, id: String) {

        val guestId = id
        deleteGuest(guestId)
    }

    private fun deleteGuest(guestId: String) {
        deleteModel.deleteGuest(progressDialog, activity, collectionId, guestId!!)

    }

    private fun deleteObserver() {
        deleteModel.progressIndicator.observe(this, Observer {
        })
        deleteModel.mDeleteResponse.observe(this) {
            val message = it.peekContent().message
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            getBooMarkGuestList()
        }
        deleteModel.errorResponse.observe(this) {
            com.freqwency.promotr.utils.ErrorUtil.handlerGeneralError(this, it)

        }
    }


}