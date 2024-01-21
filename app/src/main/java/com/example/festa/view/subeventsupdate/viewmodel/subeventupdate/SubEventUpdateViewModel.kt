package com.example.festa.view.subeventsupdate.viewmodel.subeventupdate

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.festa.guest.viewmodel.bookmarkpost.BookMarkPostResponse
import com.example.festa.R
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
class SubEventUpdateViewModel @Inject constructor(
    application: Application, private val customerRepository: SubEventUpdateRepository
) : AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val maddcohostresponse = MutableLiveData<Event<BookMarkPostResponse>>()
    var context: Context? = null

    fun getaddcohost(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        venueId: String,
        eventId: String,
        body: SubEventUpdateBody
    ) =
        viewModelScope.launch {
            addcohost(progressDialog, activity,venueId, eventId, body)
        }

    suspend fun addcohost(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        venueId: String,
        eventId: String,
        body: SubEventUpdateBody
    ) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        customerRepository.getUpdateSubEvent(venueId,eventId, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<BookMarkPostResponse>() {
                override fun onNext(value: BookMarkPostResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    maddcohostresponse.value = Event(value)
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