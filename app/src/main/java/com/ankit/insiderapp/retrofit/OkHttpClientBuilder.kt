package com.ankit.insiderapp.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class OkHttpClientBuilder {

    companion object {
        private var basicLogInterceptor: HttpLoggingInterceptor? = null

        init {
            basicLogInterceptor = HttpLoggingInterceptor()
            basicLogInterceptor!!.level = HttpLoggingInterceptor.Level.BODY
        }


        val defaultOkHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .addInterceptor(basicLogInterceptor!!)
            .build()
    }

}