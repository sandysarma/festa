package com.example.festa.view.profile.viewmodel.feedback

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
class FeedBackViewModel @Inject constructor(
    application: Application,
    private val feedbackRepository: FeedBackRepository
) : AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mfeedbackresponse = MutableLiveData<Event<FeedBackResponse>>()
    var context: Context? = null

    fun getfeedback(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        userId:String,
        eventId:String,
        body: FeedBackBody
    ) =
        viewModelScope.launch {
            addcohost(progressDialog, activity,userId,eventId, body)
        }

    suspend fun addcohost(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        userId:String,
        eventId:String,
        body: FeedBackBody
    ) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        feedbackRepository.getfeedback(userId,eventId,body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<FeedBackResponse>() {
                override fun onNext(value: FeedBackResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mfeedbackresponse.value = Event(value)
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