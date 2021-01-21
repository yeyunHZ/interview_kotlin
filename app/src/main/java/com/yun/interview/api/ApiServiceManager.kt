package com.yun.interview.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * api管理
 */
class ApiServiceManager private constructor() {
    //这里可以根据实际需求发生改变
    private var retrofit = Retrofit.Builder()
        .baseUrl("https://www.rdinterview.top/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    companion object {
        @Volatile
        private var instance: ApiServiceManager? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ApiServiceManager().also { instance = it }
            }
    }

    fun getApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }


}