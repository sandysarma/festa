package com.example.festa.view.createevents.viewmodel.createevent

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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class CreateEventViewModel @Inject constructor(
    application: Application,
    private val customerRepository: CreateEventRepository
) : AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mcreateEventResponse = MutableLiveData<Event<CreateEventReponse>>()
    var context: Context? = null

    fun getCreateEvent(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        userId: String,
        title: RequestBody,
        description: RequestBody,
        event_Type: RequestBody,
        venue_Date_and_time: RequestBody,
        identify_document: Array<MultipartBody.Part?>
    ) =
        viewModelScope.launch {
            userCreateEvent(progressDialog, activity,userId, title, description,event_Type,venue_Date_and_time,identify_document)
        }

    suspend fun userCreateEvent(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        userId: String,
        title: RequestBody,
        description: RequestBody,
        event_Type: RequestBody,
        venue_Date_and_time: RequestBody,
        identify_document: Array<MultipartBody.Part?>
    )
    {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        customerRepository.getCreateEvent(userId, title,description,event_Type,venue_Date_and_time,identify_document)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<CreateEventReponse>() {
                override fun onNext(value: CreateEventReponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mcreateEventResponse.value = Event(value)
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