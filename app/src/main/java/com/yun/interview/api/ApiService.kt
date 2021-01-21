package com.yun.interview.api

import com.yun.interview.model.BaseModel
import com.yun.interview.model.CategoryModel
import retrofit2.http.GET

/**
 * 网络请求
 */
interface ApiService {
    @GET("category/list")
    suspend fun getCategoryList():BaseModel<List<CategoryModel>>
}