package com.example.festa.view.cohost.viewmodel.addcohost

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.festa.guest.viewmodel.guests.GuestResponse
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
class AddCoHostViewModel @Inject constructor(application: Application, private val customerRepository: AddcohostRepository
) : AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val maddcohostresponse = MutableLiveData<Event<GuestResponse>>()
    var context: Context? = null

    fun getaddcohost(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId:String,
        body: AddCoHostBody
    ) =
        viewModelScope.launch {
            addcohost(progressDialog, activity,eventId, body)
        }

     suspend fun addcohost(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId:String,
        body: AddCoHostBody
    ) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        customerRepository.getaddcohost(eventId,body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<GuestResponse>() {
                override fun onNext(value: GuestResponse) {
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