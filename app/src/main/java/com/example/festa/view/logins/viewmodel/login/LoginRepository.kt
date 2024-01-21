package com.example.festa.view.logins.viewmodel.login

import com.example.festa.services.ApiServices
import io.reactivex.Observable
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiServices: ApiServices) {
    suspend fun getLogin (body: LoginBody):Observable<LoginResponse>{
        return apiServices.getuserLogin(body)
    }
}