package com.yun.interview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yun.interview.databinding.ItemArticleBinding
import com.yun.interview.databinding.ItemCategoryBinding
import com.yun.interview.model.ArticleModel
import com.yun.interview.model.CategoryModel

class ArticleAdapter(var articleList: ArrayList<ArticleModel>) :
    RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {
    var onClickListener: View.OnClickListener? = null
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(articleList[position],onClickListener)
    }

    override fun getItemCount(): Int {
        return articleList?.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemArticleBinding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemArticleBinding)
    }

    class MyViewHolder(private val itemCategoryBinding: ItemArticleBinding) :
        RecyclerView.ViewHolder(itemCategoryBinding.root) {
        fun bind(articleModel: ArticleModel,onClickListener:View.OnClickListener?) {
            itemCategoryBinding.itemArticleTitle.text = articleModel.title
            itemCategoryBinding.itemArticleShowNum.text = articleModel.viewNum.toString()
            itemCategoryBinding.root.setOnClickListener(onClickListener)
        }

    }
}