package com.yun.interview.model

/**
 * 文章详情
 */
data class ArticleDetailModel(
    val nextId: Int,
    val isLike: Boolean,
    val prevId: Int,
    val isFavorite: Boolean,
    val likeNum: Int,
    var article: ArticleModel
)