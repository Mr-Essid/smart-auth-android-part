package org.recherche.smart_auth_android_part.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.recherche.smart_auth_android_part.SessionManagement
import org.recherche.smart_auth_android_part.models.EmployerResponse
import org.recherche.smart_auth_android_part.services.EmployerService

class HistoryDetailsViewModel(application: Application) : AndroidViewModel(application) {
    val employer = MutableLiveData<EmployerResponse>(null)
    val employersSearched = MutableLiveData<List<EmployerResponse>>(emptyList())
    val error = MutableLiveData("")
    val employerService = EmployerService()
    val token = "Bearer ${SessionManagement(application).getToken()}"

    fun loadEmployer(id: Int) {
        viewModelScope.launch {
            val response = employerService.getEmployerById(token, id)
            if (response.body() != null || response.code() == 200) {
                employer.postValue(response.body())
            } else {
                error.postValue(response.code().toString())
            }
        }
    }


}