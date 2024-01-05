package org.recherche.smart_auth_android_part.network.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.recherche.smart_auth_android_part.models.DetailsModel
import org.recherche.smart_auth_android_part.models.EmployerResponse
import org.recherche.smart_auth_android_part.models.EmployerUpdate
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface EmployerAPI {


    @GET("/employer")
    suspend fun getAllEmployers(@Header("Authentication") token: String): Response<List<EmployerResponse>>

    @GET("/employer/{email}")
    suspend fun getEmployerByEmail(
        @Header("Authorization") token: String,
        @Query("email") email: String
    ): Response<EmployerResponse>


    @PUT("/employer")
    suspend fun updateEmployer(
        @Header("Authorization") token: String,
        @Body update: EmployerUpdate
    ): Response<EmployerResponse>


    @Multipart
    @POST("/employer")
    suspend fun addEmployer(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("lastname") lastname: RequestBody,
        @Part("email") email: RequestBody,
        @Part("identifier") identifier: RequestBody
    ): Response<EmployerResponse>


    @DELETE("/employer/{id_}")
    suspend fun softDeletedEmployer(
        @Header("Authorization") token: String,
        @Path("id_") id_: Int
    ): Response<EmployerResponse>


    @GET("/employer/{id_}")
    suspend fun getEmployerById(
        @Header("Authorization") token: String,
        @Path("id_") id_: Int
    ): Response<EmployerResponse>


    @GET("/employer/identifier/{identifier}")
    suspend fun getEmployerByIdentifier(
        @Header("Authorization") token: String,
        @Path("identifier") identifier: String
    ): Response<EmployerResponse>


    @GET("/employer/search/name")
    suspend fun searchEmployerByName(
        @Header("Authorization") token: String,
        @Query("keyword") nameKeyWord: String
    ): Response<List<EmployerResponse>>

    @GET("/employer/employer/state")
    suspend fun getEmployerByState(
        @Header("Authorization") token: String,
        @Query("state") state: String
    ): Response<List<EmployerResponse>>


}