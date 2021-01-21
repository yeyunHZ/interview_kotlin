package com.yun.interview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yun.interview.databinding.ItemCategoryBinding
import com.yun.interview.model.CategoryModel

class CategoryAdapter(private val categoryList: List<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList?.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemCategoryBinding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemCategoryBinding)
    }

    class MyViewHolder(private val itemCategoryBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemCategoryBinding.root) {
        fun bind(categoryModel: CategoryModel) {
            itemCategoryBinding.itemCategoryTitle.text = categoryModel.name
            Glide.with(itemCategoryBinding.itemCategoryIcon.context).load(categoryModel.iconUrl)
                .into(itemCategoryBinding.itemCategoryIcon)
        }

    }
}