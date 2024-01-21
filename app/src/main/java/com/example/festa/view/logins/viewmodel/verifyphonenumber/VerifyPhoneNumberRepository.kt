package com.example.festa.view.logins.viewmodel.verifyphonenumber

import com.example.festa.view.logins.viewmodel.login.LoginBody
import com.example.festa.services.ApiServices
import com.example.festa.view.createevents.viewmodel.sendinvite.SendInviteResponse
import io.reactivex.Observable
import javax.inject.Inject

class VerifyPhoneNumberRepository @Inject constructor(private val apiServices: ApiServices) {
    suspend fun getLogin (body: LoginBody): Observable<SendInviteResponse> {
        return apiServices.verifyPhoneNumber(body)
    }
}