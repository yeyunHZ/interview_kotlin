package com.yun.interview.view

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder
import com.yun.interview.R

class MyBGAStickinessRefreshViewHolder(context: Context, isLoadingMoreEnabled: Boolean) :
    BGAStickinessRefreshViewHolder(context,isLoadingMoreEnabled) {
    private lateinit var mStickinessRefreshView: MyBGAStickinessRefreshView

    private var mRotateImageResId = -1
    private var mStickinessColorResId = -1
    override fun getRefreshHeaderView(): View? {
        if (mRefreshHeaderView == null) {
            mRefreshHeaderView =
                View.inflate(mContext, R.layout.view_refresh_header_stickiness, null)
            mRefreshHeaderView.setBackgroundColor(Color.TRANSPARENT)
            if (mRefreshViewBackgroundColorRes != -1) {
                mRefreshHeaderView.setBackgroundResource(mRefreshViewBackgroundColorRes)
            }
            if (mRefreshViewBackgroundDrawableRes != -1) {
                mRefreshHeaderView.setBackgroundResource(mRefreshViewBackgroundDrawableRes)
            }
            mStickinessRefreshView = mRefreshHeaderView.findViewById(R.id.stickinessRefreshView)
            mStickinessRefreshView.setStickinessRefreshViewHolder(this)
            if (mRotateImageResId != -1) {
                mStickinessRefreshView.setRotateImage(mRotateImageResId)
            } else {
                throw RuntimeException("请调用" + BGAStickinessRefreshViewHolder::class.java.simpleName + "的setRotateImage方法设置旋转图片资源")
            }
            if (mStickinessColorResId != -1) {
                mStickinessRefreshView.setStickinessColor(mStickinessColorResId)
            } else {
                throw RuntimeException("请调用" + BGAStickinessRefreshViewHolder::class.java.simpleName + "的setStickinessColor方法设置黏性颜色资源")
            }
        }
        return mRefreshHeaderView
    }

    /**
     * 设置旋转图片资源
     *
     * @param resId
     */
    override fun setRotateImage(@DrawableRes resId: Int) {
        mRotateImageResId = resId
    }

    /**
     * 设置黏性颜色资源
     *
     * @param resId
     */
    override fun setStickinessColor(@ColorRes resId: Int) {
        mStickinessColorResId = resId
    }

    override fun handleScale(scale: Float, moveYDistance: Int) {
        mStickinessRefreshView!!.setMoveYDistance(moveYDistance)
    }

    override fun changeToIdle() {
        mStickinessRefreshView!!.smoothToIdle()
    }

    override fun changeToPullDown() {}

    override fun changeToReleaseRefresh() {}

    override fun changeToRefreshing() {
        mStickinessRefreshView!!.startRefreshing()
    }

    override fun onEndRefreshing() {
        mStickinessRefreshView!!.stopRefresh()
    }

    override fun canChangeToRefreshingStatus(): Boolean {
        return mStickinessRefreshView!!.canChangeToRefreshing()
    }

}