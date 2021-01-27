package com.yun.interview

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ybq.android.spinkit.Style
import com.yun.interview.adapter.CategoryAdapter
import com.yun.interview.databinding.ActivityMainBinding
import com.yun.interview.model.CategoryModel
import com.yun.interview.view.MySpinKitView
import com.yun.interview.viewmodel.HomeViewModel

/**
 * 主页
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.homeList.layoutManager = LinearLayoutManager(this)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.categoryList.observe(this, Observer {
            dismissLoading()
            initAdapter()
        })
    }

    override fun onResume() {
        super.onResume()
        showLoading()
        homeViewModel.getCategoryList()
    }

    fun initAdapter() {
        homeAdapter = CategoryAdapter(homeViewModel.categoryList.value!!)
        homeAdapter.setHasStableIds(true)
        homeAdapter.onClickListener = View.OnClickListener {
            val categoryModel: CategoryModel = it.tag as CategoryModel
            ListActivity.start(categoryModel, this)
        }
        binding.homeList.adapter = homeAdapter
    }
}


fun AppCompatActivity.showLoading() {
    var materialDialogView = this.findViewById<View>(R.id.progress)
    if (materialDialogView == null) {
        materialDialogView = RelativeLayout(this)
        materialDialogView.id = R.id.progress
        materialDialogView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT
            , ViewGroup.LayoutParams.MATCH_PARENT
        )
        val spinKitView = MySpinKitView(this)
        val layoutParams = RelativeLayout.LayoutParams(
            resources.getDimension(R.dimen.dp_30).toInt(),
            resources.getDimension(R.dimen.dp_30).toInt()
        )
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1)
        spinKitView.layoutParams = layoutParams
        materialDialogView.run {
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1)
            spinKitView.layoutParams = layoutParams
            addView(spinKitView)
        }
        spinKitView.setStyle(
            Style.WAVE,
            resources.getColor(R.color.color_3678FF)
        )
        (window.decorView as ViewGroup).addView(materialDialogView)
    }
    materialDialogView.visibility = View.VISIBLE
}

fun AppCompatActivity.dismissLoading() {
    var materialDialogView = this.findViewById<View>(R.id.progress)
    if (materialDialogView != null) {
        materialDialogView.visibility = View.GONE
    }
}
