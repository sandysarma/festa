package com.example.festa.view.events.viewmodel.particularuserlist


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
class ParticularUserEventListViewModel @Inject constructor(
    application: Application,
    private val customerRepository: ParticularUserEventListRepository
) : AndroidViewModel(application)  {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mEventListResponse = MutableLiveData<Event<ParticularUserEventListResponse>>()
    var context: Context? = null

    fun getEventList(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        userId: String,

        ) =
        viewModelScope.launch {
            userEventList(progressDialog, activity,userId)
        }
    suspend fun userEventList(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        userId: String
    )

    {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        customerRepository.getEventList(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<ParticularUserEventListResponse>() {
                override fun onNext(value: ParticularUserEventListResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mEventListResponse.value = Event(value)
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