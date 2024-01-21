package com.example.festa.view.events.viewmodel.createNewAlbum

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
class CreateNewAlbumViewModel @Inject constructor(
    application: Application,
    private val repository: CreateNewAlbumReporsitory
) :
    AndroidViewModel(application) {
    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val createNewAlbumResponse = MutableLiveData<Event<CreateNewAlbumResponse>>()

    fun createNewAlbums(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String,
        paramObject: CreateAlbumBody
    ) =
        viewModelScope.launch {
            createNewAlbum(progressDialog, activity,eventId,paramObject)
        }

    private suspend fun createNewAlbum(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        eventId: String,
        paramObject: CreateAlbumBody
    ) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        repository.createNewAlbums(eventId,paramObject)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<CreateNewAlbumResponse>() {
                override fun onNext(value: CreateNewAlbumResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    createNewAlbumResponse.value = Event(value)
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