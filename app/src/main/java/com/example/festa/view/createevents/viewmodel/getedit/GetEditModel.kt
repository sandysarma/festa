package com.example.festa.view.createevents.viewmodel.getedit

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
class GetEditModel @Inject constructor(
    application: Application,
    private val getEditRepository: GetEditRepository
) : AndroidViewModel(application)  {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mgeteditresponse = MutableLiveData<Event<GetEditResponse>>()
    var context: Context? = null

    fun getEditdata(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String,

        ) =
        viewModelScope.launch {
            userEventList(progressDialog, activity,eventId)
        }
    suspend fun userEventList(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String
    )

    {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        getEditRepository.getEdit(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<GetEditResponse>() {
                override fun onNext(value: GetEditResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mgeteditresponse.value = Event(value)
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