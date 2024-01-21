package com.example.festa.view.createevents.viewmodel.deletesubevent

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
class DeleteSubEventModel @Inject constructor(
    application: Application,
    private val deleteRepository: DeleteSubEventRepository
) : AndroidViewModel(application)  {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mDeleteResponse = MutableLiveData<Event<BookMarkPostResponse>>()
    var context: Context? = null

    fun deleteGuest(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        guestId: String,
        eventId: String


    ) =
        viewModelScope.launch {
            getDelete(progressDialog, activity,guestId,eventId)
        }
    suspend fun getDelete(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        guestId: String,
        eventId: String

    )

    {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        deleteRepository.getDeleteCoHost(guestId,eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<BookMarkPostResponse>() {
                override fun onNext(value: BookMarkPostResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mDeleteResponse.value = Event(value)
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