package org.recherche.smart_auth_android_part.network.api

import org.recherche.smart_auth_android_part.models.AdminRequest
import org.recherche.smart_auth_android_part.models.AdminResponse
import org.recherche.smart_auth_android_part.models.AdminUpdate
import org.recherche.smart_auth_android_part.models.DetailsModel
import org.recherche.smart_auth_android_part.models.HistoryResponse
import org.recherche.smart_auth_android_part.models.TokenModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.LocalDate

interface AdminAPI {

    // all actions of admin
    @POST("/admin/signup")
    suspend fun signup(@Body adminRequest: AdminRequest): Response<AdminResponse>

    @FormUrlEncoded
    @POST("/admin/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<TokenModel>

    @GET("/admin/current")
    suspend fun getCurrentAdmin(@Header("Authorization") token: String): Response<AdminResponse>

    @GET("/admin/admins")
    suspend fun getAllAdmins(@Header("Authorization") token: String): Response<List<AdminResponse>>

    @PUT("/admin")
    suspend fun updateAdmin(
        @Header("Authorization") token: String,
        @Body adminUpdate: AdminUpdate
    ): Response<AdminResponse>

    @DELETE("/admin")
    suspend fun deleteAdmin(@Header("Authorization") token: String): Response<DetailsModel>


    @GET("/admin/{id_}")
    suspend fun getAdminById(@Path("id_") id_: Int): Response<AdminResponse>

    @GET("/admin/admin/state/update/{id_}/active")
    suspend fun activeAdmin(
        @Header("Authorization") token: String,
        @Path("id_") id_: Int
    ): Response<DetailsModel>

    @GET("/admin/admin/state/update/{id_}/dis-active")
    suspend fun dis_activeAdmin(
        @Header("Authorization") token: String,
        @Path("id_") id_: Int
    ): Response<DetailsModel>


}