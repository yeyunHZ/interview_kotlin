package com.yun.interview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bingoogolapple.refreshlayout.BGARefreshLayout
import com.yun.interview.adapter.ArticleAdapter
import com.yun.interview.databinding.ActivityListBinding
import com.yun.interview.model.ArticleModel
import com.yun.interview.model.CategoryModel
import com.yun.interview.view.MyBGAStickinessRefreshViewHolder
import com.yun.interview.viewmodel.ListViewModel

/**
 * 列表页
 */
class ListActivity : AppCompatActivity(), BGARefreshLayout.BGARefreshLayoutDelegate {
    private lateinit var binding: ActivityListBinding
    private var isRefreshing: Boolean = false
    private lateinit var listViewModel: ListViewModel
    private var page: Int = 1
    private lateinit var categoryModel: CategoryModel
    private var listAdapter: ArticleAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listViewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        categoryModel = intent.getSerializableExtra("data") as CategoryModel
        initView()
    }

    fun initView() {
        binding.refreshLayout.setDelegate(this)
        val stickinessRefreshViewHolder =
            MyBGAStickinessRefreshViewHolder(this, true)
        stickinessRefreshViewHolder.setStickinessColor(R.color.color_3678FF)
        stickinessRefreshViewHolder.setRotateImage(R.mipmap.bga_refresh_stickiness)
        stickinessRefreshViewHolder.setLoadingMoreText("正在加载更多")
        stickinessRefreshViewHolder.setLoadMoreBackgroundColorRes(R.color.color_3678FF)
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.refreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
        listViewModel.articleList.observe(this, Observer {
            if (binding.refreshLayout.currentRefreshStatus == BGARefreshLayout.RefreshStatus.REFRESHING || isRefreshing) {
                if (listAdapter == null) {
                    var array = ArrayList<ArticleModel>()
                    array.addAll(listViewModel.articleList.value!!)
                    listAdapter = ArticleAdapter(array)
                    binding.list.adapter = listAdapter
                } else {
                    listAdapter!!.articleList.clear()
                    listAdapter!!.articleList.addAll(listViewModel.articleList.value!!)
                    listAdapter!!.notifyDataSetChanged()
                }
            } else {
                if (listAdapter == null) {
                    var array = ArrayList<ArticleModel>()
                    array.addAll(listViewModel.articleList.value!!)
                    listAdapter = ArticleAdapter(array)
                    binding.list.adapter = listAdapter
                } else {
                    listAdapter!!.articleList.addAll(listViewModel.articleList.value!!)
                    listAdapter!!.notifyDataSetChanged()
                }
            }
            isRefreshing = false
            binding.refreshLayout.endRefreshing()
            binding.refreshLayout.endLoadingMore()
        })
    }

    override fun onResume() {
        super.onResume()
        binding.refreshLayout.beginRefreshing()
    }

    companion object {

        fun start(categoryModel: CategoryModel, activity: Activity) {
            var intent: Intent = Intent(activity, ListActivity::class.java)
            intent.putExtra("data", categoryModel)
            activity.startActivity(intent)
        }
    }

    override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout?): Boolean {
        isRefreshing = false
        page += 1
        listViewModel.getCategoryList(categoryModel.id, page)
        return true
    }

    override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout?) {
        isRefreshing = true
        page = 1
        listViewModel.getCategoryList(categoryModel.id, page)
    }

}