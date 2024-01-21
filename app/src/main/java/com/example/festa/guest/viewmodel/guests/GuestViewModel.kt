package com.example.festa.guest.viewmodel.guests

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
class GuestViewModel@Inject constructor(application: Application, private val repository: GuestRepository):
    AndroidViewModel(application)  {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mAddGuest = MutableLiveData<Event<GuestResponse>>()
    var context: Context? = null

    fun addNewGuest(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId:String,
        body: GuestBody
    ) =
        viewModelScope.launch {
            AddNewGuest(progressDialog, activity,eventId,body)
        }

    suspend fun AddNewGuest(progressDialog: CustomProgressDialog,activity: Activity, eventId:String,body: GuestBody){
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        repository.addGuest(eventId,body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<GuestResponse>() {
                override fun onNext(value: GuestResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mAddGuest.value = Event(value)
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