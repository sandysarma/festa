package com.example.festa.guest.Uploadexcelapi

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
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class UploadexcelModel@Inject constructor(application: Application, private val repository: UploadexcelRepository):
    AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mUploadexcel = MutableLiveData<Event<UploadexcelResponse>>()
    var context: Context? = null

    fun uploadExcel(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String,
        identify_document: MultipartBody.Part
        
    ) =
        viewModelScope.launch {
            uploadexcel(progressDialog, activity,eventId,identify_document)
        }

    private suspend fun uploadexcel(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String,
        identifyDocument: MultipartBody.Part) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        repository.getUploadExcel(eventId,identifyDocument)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<UploadexcelResponse>() {
                override fun onNext(value: UploadexcelResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mUploadexcel.value = Event(value)
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