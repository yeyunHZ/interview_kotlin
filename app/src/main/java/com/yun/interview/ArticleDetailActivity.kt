package com.yun.interview

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.draggable.library.extension.ImageViewerHelper
import com.yun.interview.databinding.ActivityArticleDetailBinding
import com.yun.interview.model.ArticleDetailModel
import com.yun.interview.model.ArticleModel
import com.yun.interview.viewmodel.DetailViewModel
import com.yun.interview.viewmodel.ListViewModel

/**
 * 详情页
 */
class ArticleDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArticleDetailBinding
    private lateinit var articleModel: ArticleModel
    private lateinit var viewModel: DetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        initView()
    }
    private fun initView(){
        articleModel= intent.getSerializableExtra("data") as ArticleModel
        binding.toolBar.title = "题解"
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        viewModel.articleDetail.observe(this, Observer{
            dismissLoading()
            binding.detailScroll.scrollTo(0,0)
           articleModel = viewModel.articleDetail.value!!.article
           binding.detailTitle.text = articleModel.title
           if(articleModel.imageUrl.isEmpty()){
               binding.detailContent.text = articleModel.content
           }else{
               Glide.with(this).load(articleModel.imageUrl)
                   .into(binding.detailImg)
               binding.detailImg.setOnClickListener {
                   ImageViewerHelper.showSimpleImage(this, articleModel.imageUrl)
               }
           }
            if(viewModel.articleDetail.value!!.nextId == 0){
                binding.detailNext.visibility = View.GONE
            }else{
                binding.detailNext.visibility = View.VISIBLE
                binding.detailNext.setOnClickListener{
                    showLoading()
                    viewModel.getArticleDetail(articleModel.categoryId,viewModel.articleDetail.value!!.nextId)
                }
            }
            if(viewModel.articleDetail.value!!.prevId == 0){
                binding.detailPrev.visibility = View.GONE
            }else{
                binding.detailPrev.visibility = View.VISIBLE
                binding.detailPrev.setOnClickListener{
                    showLoading()
                    viewModel.getArticleDetail(articleModel.categoryId,viewModel.articleDetail.value!!.prevId)
                }
            }
       })
    }
    companion object{
        fun start(articleModel: ArticleModel, activity: Activity) {
            var intent: Intent = Intent(activity, ArticleDetailActivity::class.java)
            intent.putExtra("data", articleModel)
            activity.startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        showLoading()
        viewModel.getArticleDetail(articleModel.categoryId,articleModel.id)
    }
}