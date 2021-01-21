package com.yun.interview.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yun.interview.api.ApiServiceManager
import com.yun.interview.model.CategoryModel
import com.yun.interview.model.dataConvert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * 主页面viewmodel
 */
class HomeViewModel : ViewModel(){
    var categoryList = MutableLiveData<List<CategoryModel>>()

    /**
     * 获取题库分类
     */
    fun getCategoryList(){
        viewModelScope.launch {
            try {
                val data = withContext(Dispatchers.IO){
                    ApiServiceManager.getInstance().getApiService().getCategoryList().dataConvert()
                }
                categoryList.value = data
            }catch (e:Exception){

            }
        }
    }
}