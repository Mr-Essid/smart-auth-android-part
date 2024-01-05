package org.recherche.smart_auth_android_part.services

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.recherche.smart_auth_android_part.models.DetailsModel
import retrofit2.Response
import org.recherche.smart_auth_android_part.models.EmployerResponse
import org.recherche.smart_auth_android_part.models.EmployerUpdate
import org.recherche.smart_auth_android_part.network.RetrofitInstance
import org.recherche.smart_auth_android_part.network.api.EmployerAPI


class EmployerService(
    val employerAPI: EmployerAPI = RetrofitInstance.getInstance("http://192.168.56.97:8089/")
        .getEmployerAPI()
) {

    suspend fun addEmployer(
        token: String,
        image: MultipartBody.Part,
        name: RequestBody,
        lastname: RequestBody,
        email: RequestBody,
        identifier: RequestBody
    ): Response<EmployerResponse> {
        return employerAPI.addEmployer(
            token,
            image,
            name,
            lastname,
            email,
            identifier
        )
    }

    suspend fun getAllEmployers(token: String): Response<List<EmployerResponse>> {
        return employerAPI.getAllEmployers(token)
    }

    suspend fun getEmployerById(token: String, id: Int): Response<EmployerResponse> {
        return employerAPI.getEmployerById(token, id)
    }

    suspend fun getEmployerByIdentifier(
        token: String,
        identifier: String
    ): Response<EmployerResponse> {
        return employerAPI.getEmployerByIdentifier(token, identifier)
    }


    suspend fun softDelete(token: String, id_: Int): Response<EmployerResponse> {
        return employerAPI.softDeletedEmployer(token, id_)
    }

    suspend fun updateEmployer(token: String, update: EmployerUpdate): Response<EmployerResponse> {
        return employerAPI.updateEmployer(token, update)
    }

    suspend fun getEmployerByEmail(token: String, email: String): Response<EmployerResponse> {
        return employerAPI.getEmployerByEmail(token, email)
    }


    suspend fun searchEmployerByName(
        token: String,
        nameKeyword: String
    ): Response<List<EmployerResponse>> {
        return employerAPI.searchEmployerByName(token, nameKeyword)
    }

    suspend fun getEmployerWithState(
        token: String,
        state: String
    ): Response<List<EmployerResponse>> {
        return employerAPI.getEmployerByState(token, state)
    }


}