package org.recherche.smart_auth_android_part.view_models

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.recherche.smart_auth_android_part.SessionManagement
import org.recherche.smart_auth_android_part.services.AdminService

class SignINViewModel(application: Application) : AndroidViewModel(application) {

    var username: MutableState<String> = mutableStateOf("")
        private set
    var password: MutableState<String> = mutableStateOf("")
        private set
    private val sessionManagement = SessionManagement(application)

    val isDataLoad = MutableLiveData(0)

    var isAuthenticated = MutableLiveData(false)
        private set
    var err = MutableLiveData("")
        private set
    private val adminService = AdminService()
    fun LoginAdmin(): Int {
        if (username.value.length < 4 || !username.value.contains("@")) {
            isDataLoad.postValue(0)
            return -1 // mean invalid username

        }

        if (password.value.length < 4) {
            isDataLoad.postValue(0)
            return -2 // mean invalid password
        }


        viewModelScope.launch {
            val response = adminService.login(username.value, password.value)
            isDataLoad.postValue(0)
            if (response.code() == 200 && response.body() != null) {
                val token = response.body()!!
                val tokenAsStringCoding = "${token.access_token}_T_${token.date_exp}"
                sessionManagement.addToken(tokenAsStringCoding)
                isAuthenticated.value = true
            } else {
                print("Hello")
                err.postValue(response.code().toString())
            }
        }

        return 1
    }
}