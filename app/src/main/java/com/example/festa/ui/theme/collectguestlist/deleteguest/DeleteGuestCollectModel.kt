package com.example.festa.ui.theme.collectguestlist.deleteguest

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.festa.R
import com.example.festa.guest.viewmodel.bookmarkpost.BookMarkPostResponse
import com.example.festa.guest.viewmodel.deleteguest.DeleteGuestRepository
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
class DeleteGuestCollectModel @Inject constructor(
    application: Application,
    private val deleteRepository: CollectionGuestDeleteRepository
) : AndroidViewModel(application) {

    val progressIndicator = MutableLiveData<Boolean>()
    val errorResponse = MutableLiveData<Throwable>()
    val mDeleteResponse = MutableLiveData<Event<BookMarkPostResponse>>()

    fun deleteGuest(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        collection_id: String,
        guestId: String

    ) =
        viewModelScope.launch {
            getDelete(progressDialog, activity, collection_id, guestId)
        }

    private suspend fun getDelete(
        progressDialog: CustomProgressDialog,
        activity: Activity,
        collection_id: String,
        guestId: String
    ) {
        progressDialog.start(activity.getString(R.string.please_wait))
        progressIndicator.value = true
        deleteRepository.getDelete(collection_id, guestId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<BookMarkPostResponse>() {
                override fun onNext(value: BookMarkPostResponse) {
                    progressIndicator.value = false
                    progressDialog.stop()
                    mDeleteResponse.value = Event(value)
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