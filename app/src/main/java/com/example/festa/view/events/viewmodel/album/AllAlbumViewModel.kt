package com.example.festa.view.events.viewmodel.album

import android.app.Activity
import android.app.Application
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
class AllAlbumViewModel @Inject constructor(
    application: Application,
    private val repository: AllAlbumReporsitory
) :
    AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val allAlbumResponse = MutableLiveData<Event<AllAlbumResponse>>()

    fun getAllAlbum(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String,

        ) =
        viewModelScope.launch {
            AllAlbum(progressDialog, activity,eventId)
        }

    private suspend fun AllAlbum(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String
    ) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        repository.getAllAlbumList(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<AllAlbumResponse>() {
                override fun onNext(value: AllAlbumResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    allAlbumResponse.value = Event(value)
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