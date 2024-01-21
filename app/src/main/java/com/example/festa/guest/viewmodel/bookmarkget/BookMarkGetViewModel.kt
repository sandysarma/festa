package com.example.festa.guest.viewmodel.bookmarkget

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.festa.R
import com.example.festa.guest.viewmodel.guestlist.GuestListRepository
import com.example.festa.guest.viewmodel.guestlist.GuestListResponse
import com.example.festa.ui.theme.bookmark.model.BookMarkGetRepository
import com.example.festa.ui.theme.bookmark.model.BookMarkGetResponse
import com.example.festa.utils.Event
import com.johncodeos.customprogressdialogexample.CustomProgressDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class BookMarkGetViewModel  @Inject constructor(
    application: Application,
    private val repository: BookMarkGetRepository
) :
    AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mguestlist = MutableLiveData<Event<BookMarkGetResponse>>()
    var context: Context? = null

    fun getBookMark(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String

    ) = viewModelScope.launch {
        guestlist(progressDialog, activity, eventId)
    }

    suspend fun guestlist(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String
    ) {

        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        repository.getBookMarkList(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<BookMarkGetResponse>() {
                override fun onNext(value: BookMarkGetResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mguestlist.value = Event(value)
                }

                override fun onError(e: Throwable) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    errorResponse.value = e
                }

                override fun onComplete() {
                    progressDialog.stop()
                    progressIndicator.value = false
                }
            })
    }
}
