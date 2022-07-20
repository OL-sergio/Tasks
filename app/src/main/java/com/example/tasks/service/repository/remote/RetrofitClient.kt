package com.example.tasks.service.repository.remote

import com.example.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient private constructor() {

    companion object {

        private lateinit var INSTANCE : Retrofit
        private const val BASE_URL = "http://devmasterteam.com/CursoAndroidAPI/"
        private var personKey = ""
        private var tonkenKey = ""

        private fun   getRetrofitInstance() : Retrofit {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(object: Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, tonkenKey)
                        .build()

                    return chain.proceed(request)
                }
            })

            if (!::INSTANCE.isInitialized){
                synchronized(RetrofitClient::class) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }

        fun addHeader(token: String, personKey: String){
            this.personKey = personKey
            this.tonkenKey = token
        }

        fun <T> createService(serviceClass: Class<T>): T {
            return getRetrofitInstance()
                .create(serviceClass)

        }

    }

}