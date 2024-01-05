package org.recherche.smart_auth_android_part.view_models

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.recherche.smart_auth_android_part.models.AdminRequest
import org.recherche.smart_auth_android_part.services.AdminService

class SignUPViewModel : ViewModel() {

    val username = mutableStateOf("")
    var email = mutableStateOf("")
    val password = mutableStateOf("")
    val passwordConfirmation = mutableStateOf("")
    val adminService = AdminService()

    // live data in case of add or not
    val isError = MutableLiveData("")
    val isAdded = MutableLiveData(false)
    val stateRequest = MutableLiveData(0)
    fun signup(): Int {

        if (username.value.length < 4) {
            isError.postValue("-1")
            stateRequest.postValue(0)

            return -1
        }
        if (password.value.length < 4) {
            isError.postValue("-2")
            stateRequest.postValue(0)

            return -2
        }

        if (email.value.length < 4 && !password.value.contains("@")) {
            isError.postValue("-3")
            stateRequest.postValue(0)

            return -3
        }

        if (password.value != passwordConfirmation.value) {
            isError.postValue("-4")
            stateRequest.postValue(0)

            return -4
        }


        val newAdmin = AdminRequest(email.value, username.value, password.value)

        viewModelScope.launch {
            val response = adminService.addAdmin(newAdmin)
            stateRequest.postValue(0)
            if (response.code() == 200 && response.body() != null) {
                isAdded.postValue(true)
            } else {
                isError.postValue(response.code().toString())
            }
        }

        return 1
    }

    fun clean() {
        username.value = ""
        password.value = ""
        passwordConfirmation.value = ""
        email.value = ""
    }

}