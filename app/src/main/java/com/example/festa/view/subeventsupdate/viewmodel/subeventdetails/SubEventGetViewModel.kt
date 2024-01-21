package com.example.festa.view.subeventsupdate.viewmodel.subeventdetails

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
class SubEventGetViewModel @Inject constructor(
    application: Application,
    private val deleteRepository: SubEventGetRepository
) : AndroidViewModel(application)  {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mDeleteResponse = MutableLiveData<Event<SubEventResponse>>()
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
        deleteRepository.getSubEventDetails(guestId,eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<SubEventResponse>() {
                override fun onNext(value: SubEventResponse) {
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