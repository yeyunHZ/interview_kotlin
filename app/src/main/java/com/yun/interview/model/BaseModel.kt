package com.yun.interview.model

import java.lang.Exception

/**
 * 接口返回的base
 */
data class BaseModel<T>( var code : Int  =0,var message:String,var data : T)

/**
 * 扩展方法，校验服务器返回的数据
 */
fun <T> BaseModel<T>.dataConvert():T{
    if(code == 200){
        return  data;
    }else{
        throw  Exception(message)
    }
}