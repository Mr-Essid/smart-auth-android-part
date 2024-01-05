package org.recherche.smart_auth_android_part.view_models

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.recherche.smart_auth_android_part.SessionManagement
import org.recherche.smart_auth_android_part.models.EmployerResponse
import org.recherche.smart_auth_android_part.models.EmployerUpdate
import org.recherche.smart_auth_android_part.models.ErrorMessage
import org.recherche.smart_auth_android_part.services.EmployerService

class EmployerDetailViewModel(application: Application) : AndroidViewModel(application) {
    val token = SessionManagement(application).getToken()
    private val _isEmployerInSearch = MutableLiveData(false)
    val isEmployerInSearch: LiveData<Boolean> = _isEmployerInSearch
    val employer = MutableLiveData<EmployerResponse>(null)
    val isUpdate = MutableLiveData(false)
    val isDeleted = MutableLiveData(false)
    var isUpdatedBackResponse = false
    private val timeExpAsString = SessionManagement(application).getExpTimeAsString()
    private val employerService = EmployerService()
    private val _error = MutableLiveData<ErrorMessage?>(null)
    val error: LiveData<ErrorMessage?> = _error
    val employerName = mutableStateOf("")
    val employerLastName = mutableStateOf("")


    fun getEmployerById(id_: Int) {
        _isEmployerInSearch.postValue(true)
        viewModelScope.launch {
            val response = employerService.getEmployerById(token, id_)
            if (response.body() != null && response.code() == 200) {
                employer.postValue(response.body())
                employerName.value = response.body()!!.name_employer
                employerLastName.value = response.body()!!.lastname_employer
                _isEmployerInSearch.postValue(false)
            } else {
                if (response.code() == 400) {
                    _error.postValue(
                        ErrorMessage(
                            "BAD REQUEST",
                            "Small Problem Within Your Request, Call Your root Admin"
                        )
                    )
                }
            }
        }
    }


    fun updateEmployer() {
        _isEmployerInSearch.postValue(true)
        val employerUpdate = EmployerUpdate(employer.value!!.id_employer)
        var isThereUpdate = false

        if (employerName.value.length < 4 && employerLastName.value.length < 4) {
            employerName.value = employer.value!!.name_employer
            employerName.value = employer.value!!.lastname_employer
            _error.postValue(
                ErrorMessage(
                    "USERNAME INVALID",
                    "Check Your Username/Lastname length at least 4 chars"
                )
            )
        }
        if (employerName.value != employer.value!!.name_employer) {
            employerUpdate.name_employer = employerName.value
            isThereUpdate = true
        }

        if (employerLastName.value != employer.value!!.lastname_employer) {
            employerUpdate.lastname_employer = employerLastName.value
            isThereUpdate = true
        }

        if (!isThereUpdate) {
            employerName.value = employer.value!!.name_employer
            employerName.value = employer.value!!.lastname_employer
            _error.postValue(
                ErrorMessage(
                    "UPDATE VALUE",
                    "No Value Specified to Update, Check Your Update Fields"
                )

            )
        }
        viewModelScope.launch {
            val response = employerService.updateEmployer(token, employerUpdate)
            if (response.code() == 200 && response.body() != null) {
                employer.postValue(response.body())
                isUpdatedBackResponse = true
                isUpdate.postValue(true)
            } else {
                _error.postValue(
                    ErrorMessage(
                        "BAD REQUEST",
                        "Small Problem In Your Request, Like Your Data Not Valid"
                    )
                )
            }
            _isEmployerInSearch.postValue(false)
        }

    }


    fun inActiveEmployer() {
        _isEmployerInSearch.postValue(true)
        viewModelScope.launch {
            val response = employerService.softDelete(
                token,
                employer.value!!.id_employer
            )
            if (response.code() == 200 && response.body() != null) {
                employer.postValue(response.body())
                isUpdatedBackResponse = true
                isDeleted.postValue(true)
            } else
                _error.postValue(
                    ErrorMessage(
                        "BAD REQUEST",
                        "Call The Admin root For Latest Tech Support"
                    )
                )
            _isEmployerInSearch.postValue(false)
        }
    }

    fun clearError() {
        _error.postValue(null)
    }
}