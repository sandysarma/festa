package com.example.festa.view.profile.viewmodel.updateprofile

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
class UserUpdateViewModel  @Inject constructor(
    application: Application,
    private val customerRepository: UserUpdateRepository
) : AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mcreateEventResponse = MutableLiveData<Event<UserUpdateResponse>>()
    var context: Context? = null

    fun userUpdateDetails(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        userId: String,
        fullName: RequestBody,
        phone_no: RequestBody,
        email: RequestBody,
        profileImage: MultipartBody.Part
    ) =
        viewModelScope.launch {
            userUpdate(progressDialog, activity,userId, fullName, phone_no,email,profileImage)
        }

    suspend fun userUpdate(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        userId: String,
        fullName: RequestBody,
        phone_no: RequestBody,
        email: RequestBody,
        profileImage: MultipartBody.Part
    )
    {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        customerRepository.updateProfile(userId, fullName, phone_no,email,profileImage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<UserUpdateResponse>() {
                override fun onNext(value: UserUpdateResponse) {
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