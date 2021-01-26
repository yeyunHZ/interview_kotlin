package com.yun.interview.api

import com.yun.interview.model.ArticleListModel
import com.yun.interview.model.ArticleModel
import com.yun.interview.model.BaseModel
import com.yun.interview.model.CategoryModel
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 网络请求
 */
interface ApiService {
    /**
     * 获取分类列表
     */
    @GET("category/list")
    suspend fun getCategoryList(): BaseModel<List<CategoryModel>>


    /**
     * 获取文章列表
     */
    @GET("article/list")
    suspend fun getArticleList(
        @Query("categoryId") categoryId: Int,
        @Query("page") page: Int
    ): BaseModel<ArticleListModel>


    /**
     * 搜索文章列表
     */
    @GET("article/search")
    suspend fun searchArticleList(
        @Query("categoryId") categoryId: Int,
        @Query("keyWord") keyWord: String
    ): BaseModel<List<ArticleModel>>



}