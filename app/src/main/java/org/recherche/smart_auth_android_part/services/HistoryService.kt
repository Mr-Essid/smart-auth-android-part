package org.recherche.smart_auth_android_part.services

import org.recherche.smart_auth_android_part.models.DayInfo
import org.recherche.smart_auth_android_part.models.HistoryResponse
import org.recherche.smart_auth_android_part.network.RetrofitInstance
import org.recherche.smart_auth_android_part.network.api.HistoryAPI
import retrofit2.Response
import java.time.LocalDate

class HistoryService(
    val historyAPI: HistoryAPI = RetrofitInstance.getInstance("http://192.168.56.97:8089/") // please hacker don't do it :)
        .getHistoryAPI()
) {


    suspend fun getAllHistory(token: String): Response<List<HistoryResponse>> {
        return historyAPI.getHistoryAllHistory(token)
    }


    suspend fun getHistoryById(token: String, id_: Int): Response<HistoryResponse> {
        return historyAPI.getHistoryById(token, id_)
    }

    suspend fun getHistoryOfEmployer(
        token: String,
        employerID: Int
    ): Response<List<HistoryResponse>> {
        return historyAPI.getHistoryByIdEmployer(token, employerID)
    }


    suspend fun getHistoryByDate(token: String, date: LocalDate): Response<List<HistoryResponse>> {
        return historyAPI.historyOfDate(token, date)
    }

    suspend fun getLastFiveDays(): Response<List<DayInfo>> {
        return historyAPI.lastFiveDays()
    }

}