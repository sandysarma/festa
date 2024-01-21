package com.example.festa.guest.viewmodel.bookmarkpost

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
class BookMarkPostViewModel @Inject constructor(application: Application, private val customerRepository: BookMarkPostRepository
) : AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val maddcohostresponse = MutableLiveData<Event<BookMarkPostResponse>>()
    var context: Context? = null

    fun getBookMark(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId:String,
        body: BookMarkPostBody
    ) =
        viewModelScope.launch {
            addBookMark(progressDialog, activity,eventId, body)
        }

    private suspend fun addBookMark(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId:String,
        body: BookMarkPostBody
    ) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        customerRepository.getBookMark(eventId,body)
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