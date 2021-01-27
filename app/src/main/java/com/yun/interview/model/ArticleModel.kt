package com.yun.interview.model

import java.io.Serializable

/**
 * 问题列表数据
 */
data class ArticleModel(
    var id: Int,
    var categoryId: Int,
    var title: String,
    var content: String,
    var imageUrl: String,
    var viewNum: Int
) : Serializable