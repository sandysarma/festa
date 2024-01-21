package com.example.festa.view.createevents.viewmodel.addvenue

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
class AddVenueViewModel @Inject constructor(application: Application, private val repository: AddVenueRepository):
    AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mAddNewVenueResponse = MutableLiveData<Event<AddVenueResponse>>()
    var context: Context? = null

    fun addNewVenue(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        userId:String,
        body: AddVenueBody
    ) =
        viewModelScope.launch {
            AddNewVenue(progressDialog, activity,userId,body)
        }

    suspend fun AddNewVenue(progressDialog: CustomProgressDialog,activity: Activity, userId:String,body: AddVenueBody){
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        repository.addNewVenue(userId,body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<AddVenueResponse>() {
                override fun onNext(value: AddVenueResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mAddNewVenueResponse.value = Event(value)
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