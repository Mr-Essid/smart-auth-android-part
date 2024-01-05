package org.recherche.smart_auth_android_part.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.recherche.smart_auth_android_part.SessionManagement
import org.recherche.smart_auth_android_part.models.AdminResponse
import org.recherche.smart_auth_android_part.models.DayInfo
import org.recherche.smart_auth_android_part.services.AdminService
import org.recherche.smart_auth_android_part.services.HistoryService

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    val currentAdmin = MutableLiveData<AdminResponse>(null)
    val lastFiveDaysData = MutableLiveData<List<DayInfo>>(null)
    val token = "Bearer ${SessionManagement(application).getToken()}"
    val expTimeAsString = SessionManagement(application).getExpTimeAsString()
    val adminService = AdminService()
    val error = MutableLiveData("")

    init {
        loadCurrentAdmin()
    }

    private fun loadCurrentAdmin() {
        viewModelScope.launch {
            val response = adminService.getCurrentAdmin(token)

            if (response.code() == 200 || response.body() != null) {
                currentAdmin.postValue(response.body())
            }
        }
    }

}