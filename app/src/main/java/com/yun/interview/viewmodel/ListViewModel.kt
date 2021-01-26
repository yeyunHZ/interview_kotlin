package com.yun.interview.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yun.interview.MyApplication
import com.yun.interview.api.ApiServiceManager
import com.yun.interview.model.ArticleModel
import com.yun.interview.model.dataConvert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 列表页viewmodel
 */
class ListViewModel : ViewModel() {
    var articleList = MutableLiveData<List<ArticleModel>>()

    /**
     * 获取题目列表
     */
    fun getArticleList(
        categoryId: Int,
        page: Int
    ) {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    ApiServiceManager.getInstance().getApiService().getArticleList(categoryId, page)
                        .dataConvert()
                }
                articleList.value = data.list
            } catch (e: Exception) {
                Toast.makeText(
                    MyApplication.getInstance().baseContext,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    /**
     * 搜索题目列表
     */
    fun searchArticleList(
        categoryId: Int,
        key: String
    ) {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    ApiServiceManager.getInstance().getApiService().searchArticleList(categoryId, key)
                        .dataConvert()
                }
                articleList.value = data
            } catch (e: Exception) {
                Toast.makeText(
                    MyApplication.getInstance().baseContext,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}