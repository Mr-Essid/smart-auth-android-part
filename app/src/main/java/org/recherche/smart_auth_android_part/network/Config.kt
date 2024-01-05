package org.recherche.smart_auth_android_part.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.recherche.smart_auth_android_part.network.api.AdminAPI
import org.recherche.smart_auth_android_part.network.api.EmployerAPI
import org.recherche.smart_auth_android_part.network.api.HistoryAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitInstance private constructor() {

    fun getEmployerAPI(): EmployerAPI {
        return retrofitInstance_!!.create(EmployerAPI::class.java)
    }

    fun getAdminAPI(): AdminAPI {
        return retrofitInstance_!!.create(AdminAPI::class.java)
    }

    fun getHistoryAPI(): HistoryAPI {
        return retrofitInstance_!!.create(HistoryAPI::class.java)
    }

    companion object {
        private var retrofitInstance_: Retrofit? = null

        @Volatile
        private var retrofitInstance: RetrofitInstance? = null

        fun getInstance(baseUrl: String): RetrofitInstance {
            retrofitInstance_ = Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient().newBuilder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .readTimeout(50, TimeUnit.MINUTES)
                        .callTimeout(50, TimeUnit.MINUTES)
                        .connectTimeout(50, TimeUnit.MINUTES)
                        .writeTimeout(50, TimeUnit.MINUTES)
                        .build()
                ).build()

            if (retrofitInstance == null) {
                retrofitInstance = RetrofitInstance()
            }

            return retrofitInstance!!
        }
    }
}

