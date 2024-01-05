package org.recherche.smart_auth_android_part.services

import android.graphics.Bitmap.Config
import org.recherche.smart_auth_android_part.models.AdminRequest
import org.recherche.smart_auth_android_part.models.AdminResponse
import org.recherche.smart_auth_android_part.models.AdminUpdate
import org.recherche.smart_auth_android_part.models.DetailsModel
import org.recherche.smart_auth_android_part.models.HistoryResponse
import org.recherche.smart_auth_android_part.models.TokenModel
import org.recherche.smart_auth_android_part.network.RetrofitInstance
import org.recherche.smart_auth_android_part.network.api.AdminAPI
import retrofit2.Response
import java.time.LocalDate

class AdminService(
    val adminAPI: AdminAPI = RetrofitInstance.getInstance("http://192.168.56.97:8089/")
        .getAdminAPI() // in big project you can't hard code like this dependency injection take place
) {


    suspend fun addAdmin(admin: AdminRequest): Response<AdminResponse> {
        return adminAPI.signup(admin)
    }

    suspend fun login(username: String, password: String): Response<TokenModel> {
        return adminAPI.login(username, password)
    }

    suspend fun getCurrentAdmin(token: String): Response<AdminResponse> {
        return adminAPI.getCurrentAdmin(token)
    }


    suspend fun getAdminById(id_: Int): Response<AdminResponse> {
        return adminAPI.getAdminById(id_)
    }

    suspend fun adminUpdate(token: String, adminUpdate: AdminUpdate): Response<AdminResponse> {
        return adminAPI.updateAdmin(token, adminUpdate)
    }

    suspend fun deleteAdmin(token: String): Response<DetailsModel> {
        return adminAPI.deleteAdmin(token)
    }


    suspend fun activeAdmin(token: String, id: Int): Response<DetailsModel> {
        return adminAPI.activeAdmin(token, id)
    }


    suspend fun dis_activeAdmin(token: String, id: Int): Response<DetailsModel> {
        return adminAPI.dis_activeAdmin(token, id)
    }


    suspend fun getAllAdmins(token: String): Response<List<AdminResponse>> {
        return adminAPI.getAllAdmins(token)
    }


}