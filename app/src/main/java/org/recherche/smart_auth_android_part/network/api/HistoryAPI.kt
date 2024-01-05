package org.recherche.smart_auth_android_part.network.api

import org.recherche.smart_auth_android_part.models.DayInfo
import org.recherche.smart_auth_android_part.models.HistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

interface HistoryAPI {
    @GET("/history")
    suspend fun getHistoryAllHistory(@Header("Authorization") token: String): Response<List<HistoryResponse>>

    @GET("/history/{id_}")
    suspend fun getHistoryById(
        @Header("Authorization") token: String,
        @Path("id_") id_: Int
    ): Response<HistoryResponse>

    @GET("/history/employer/{id_}")
    suspend fun getHistoryByIdEmployer(
        @Header("Authorization") token: String,
        @Path("id_") id_: Int
    ): Response<List<HistoryResponse>>


    @GET("/history/date")
    suspend fun historyOfDate(
        @Header("Authorization") token: String,
        @Query("date") date: LocalDate
    ): Response<List<HistoryResponse>>


    @GET("/history/date/last-five-days")
    suspend fun lastFiveDays(): Response<List<DayInfo>>


}