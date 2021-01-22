package com.yun.interview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.yun.interview.R
import com.yun.interview.adapter.CategoryAdapter
import com.yun.interview.databinding.ActivityMainBinding
import com.yun.interview.viewmodel.HomeViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.homeList.layoutManager = LinearLayoutManager(this)
        homeViewModel= ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.categoryList.observe(this, Observer {
            homeAdapter = CategoryAdapter(homeViewModel.categoryList.value!!)
            homeAdapter.setHasStableIds(true)
            binding.homeList.adapter = homeAdapter
        })
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getCategoryList()
    }

}
