package com.example.festa.view.logins.viewmodel.login

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
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: LoginRepository
) : AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mloginResponse = MutableLiveData<Event<LoginResponse>>()
    var context: Context? = null

    fun getLogIn(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        body: LoginBody
    ) =
        viewModelScope.launch {
            Login(progressDialog, activity, body)
        }

    suspend fun Login(progressDialog: CustomProgressDialog, activity: Activity, body: LoginBody) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        repository.getLogin(body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<LoginResponse>() {
                override fun onNext(value: LoginResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mloginResponse.value = Event(value)
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