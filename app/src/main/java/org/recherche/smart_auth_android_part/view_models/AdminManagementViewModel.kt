package org.recherche.smart_auth_android_part.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.recherche.smart_auth_android_part.SessionManagement
import org.recherche.smart_auth_android_part.models.AdminResponse
import org.recherche.smart_auth_android_part.models.ErrorMessage
import org.recherche.smart_auth_android_part.services.AdminService

class AdminManagementViewModel(application: Application) : AndroidViewModel(application) {

    val token = "Bearer ${SessionManagement(application).getToken()}"
    val adminLists = MutableLiveData<List<AdminResponse>?>(null)
    val isUpdated = MutableLiveData(false)
    val isAdminBlocked = MutableLiveData(false)
    val isAdminActive = MutableLiveData(false)
    val error = MutableLiveData<ErrorMessage>(null)
    val serviceAdmin = AdminService()

    init {
        loadData()
    }

    fun loadData() {


        viewModelScope.launch {

            val response = serviceAdmin.getAllAdmins("Bearer $token")

            if (response.code() == 200 && response.body() != null) {
                adminLists.postValue(response.body())
            } else
                error.postValue(
                    ErrorMessage(
                        "ACCESS FORBIDDEN",
                        "Request Not Authorized By You As Admin."
                    )
                )
        }
    }


    fun changeStateOfAdmin(id: Int, newState: Boolean) {

        if (newState) {
            viewModelScope.launch {
                val response = serviceAdmin.activeAdmin(token, id)
                if (response.body() != null && response.code() == 200) {
                    isAdminActive.value = true
                    isUpdated.postValue(true)
                } else
                    error.postValue(
                        ErrorMessage(
                            "ACCESS FORBIDDEN",
                            "Request Not Authorized."
                        )
                    )

            }
        } else {
            viewModelScope.launch {
                val response = serviceAdmin.dis_activeAdmin(token, id)
                if (response.body() != null && response.code() == 200) {
                    isAdminBlocked.value = true
                    isUpdated.postValue(true)

                } else
                    error.postValue(
                        ErrorMessage(
                            "ACCESS FORBIDDEN",
                            "Request Not Authorized."
                        )
                    )
            }
        }
    }

}