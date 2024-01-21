package com.example.festa.view.createevents.viewmodel.sendinvite

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
class SendInviteModel @Inject constructor(application: Application, private val repository: SendInviteRepository):
    AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mbookmark = MutableLiveData<Event<SendInviteResponse>>()
    var context: Context? = null

    fun sendInvites(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String,

        ) =
        viewModelScope.launch {
            sendInvite(progressDialog, activity,eventId)
        }

    private suspend fun sendInvite(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String
    ) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        repository.getInvite(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<SendInviteResponse>() {
                override fun onNext(value: SendInviteResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mbookmark.value = Event(value)
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