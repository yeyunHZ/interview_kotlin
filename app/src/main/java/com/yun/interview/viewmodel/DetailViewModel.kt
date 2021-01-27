package com.yun.interview.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yun.interview.MyApplication
import com.yun.interview.api.ApiServiceManager
import com.yun.interview.model.ArticleDetailModel
import com.yun.interview.model.ArticleModel
import com.yun.interview.model.dataConvert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 详情页viewmodel
 */
class DetailViewModel : ViewModel() {
    var articleDetail = MutableLiveData<ArticleDetailModel>()


    /**
     * 获取题目详情
     */
    fun getArticleDetail(
        categoryId: Int,
        articleId: Int
    ) {
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    ApiServiceManager.getInstance().getApiService().getArticleDetail(categoryId, articleId)
                        .dataConvert()
                }
                articleDetail.value = data
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