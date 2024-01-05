package org.recherche.smart_auth_android_part.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.recherche.smart_auth_android_part.SessionManagement
import org.recherche.smart_auth_android_part.models.EmployerResponse
import org.recherche.smart_auth_android_part.services.EmployerService
import java.util.stream.Stream
import kotlin.streams.toList

class EmployerViewModel(application: Application) : AndroidViewModel(application) {
    private val token = SessionManagement(application).getToken()
    private val timeExpiration = SessionManagement(application).getExpTimeAsString()
    val listEmployers = MutableLiveData<List<EmployerResponse>>(emptyList())
    val searchedEmployers = MutableLiveData<List<EmployerResponse>?>(emptyList())
    val employer = MutableLiveData<EmployerResponse>(null)
    val error = MutableLiveData("")
    private val employerService = EmployerService()

    init {
        loadData()
    }


    fun loadData(name: String? = null, isActive: Boolean? = null) {

        viewModelScope.launch {
            val response = when {
                isActive != null -> employerService.getEmployerWithState(
                    token,
                    if (isActive) "active" else "inactive"
                )

                !name.isNullOrEmpty() -> employerService.searchEmployerByName(
                    token,
                    name
                )

                else -> employerService.getAllEmployers(token)
            }
            if (response.code() == 200 && response.body() != null) {
                listEmployers.postValue(response.body())
            } else {
                error.postValue(response.code().toString())
            }
        }
    }

    fun searchByName(keyword: String) {
        if (keyword == "") {
            searchedEmployers.postValue(emptyList())
            return

        }
        viewModelScope.launch {
            val response = employerService.searchEmployerByName(token, keyword)
            if (response.body() != null && response.code() == 200) {
                searchedEmployers.postValue(response.body())
            }
        }
    }

    fun getSMTPServerNames(): Stream<String> {

        val namesServers = listEmployers.value!!
            .stream().map {
                it.email_employer.subSequence(
                    it.email_employer.indexOf('@'),
                    it.email_employer.indexOf('.')
                ).toString()
            }

        return namesServers!!

    }

    fun filterBySMTPServerName(name: String) {
        val employerByNameServer = listEmployers.value!!.stream().filter {
            it.email_employer.subSequence(
                startIndex = it.email_employer.indexOf('@'),
                endIndex = it.email_employer.indexOf('.')
            ) == name
        }
    }


    fun filterByState(state: Boolean) {

        viewModelScope.launch {
            val response = employerService.getEmployerWithState(
                token, when {
                    state -> "active"
                    else -> "inactive"
                }
            )

            if (response.code() == 200 && response.body() != null) {
                listEmployers.postValue(response.body())
            } else
                error.postValue(response.body().toString())
        }

    }


}