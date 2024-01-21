package com.example.festa.view.signup.viewmodel

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
class SignUpViewModel @Inject constructor(
    application: Application,
    private val customerRepository: SignUpRepository
) : AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mSignUpResponse = MutableLiveData<Event<SignUpResponse>>()
    var context: Context? = null

    fun getSignUp(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        fullName: RequestBody,
        phone_no: RequestBody,
        identify_document: MultipartBody.Part
    ) =
        viewModelScope.launch {
            userSignUp(progressDialog, activity, fullName, phone_no, identify_document)
        }

    suspend fun userSignUp(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        fullName: RequestBody,
        phone_no: RequestBody,
        identify_document: MultipartBody.Part
    ) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        customerRepository.getUserSignUp(fullName, phone_no, identify_document)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<SignUpResponse>() {
                override fun onNext(value: SignUpResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mSignUpResponse.value = Event(value)
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