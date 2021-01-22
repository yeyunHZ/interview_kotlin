package com.yun.interview.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.view.ViewCompat
import cn.bingoogolapple.refreshlayout.BGARefreshLayout
import cn.bingoogolapple.refreshlayout.BGAStickinessRefreshViewHolder

class MyBGAStickinessRefreshView constructor(
    context: Context, attrs: AttributeSet?,
    defStyle: Int
) : View(context, attrs, defStyle) {
    private var mStickinessRefreshViewHolder: BGAStickinessRefreshViewHolder? = null
    private var mTopBound: RectF? = null
    private var mBottomBound: RectF? = null
    private var mRotateDrawableBound: Rect? = null
    private var mCenterPoint: Point? = null

    private var mPaint: Paint? = null
    private var mPath: Path? = null

    private var mRotateDrawable: Drawable? = null

    /**
     * 旋转图片的大小
     */
    private var mRotateDrawableSize = 0

    private var mMaxBottomHeight = 0
    private var mCurrentBottomHeight = 0

    /**
     * 是否正在旋转
     */
    private var mIsRotating = false
    private var mIsRefreshing = false

    /**
     * 当前旋转角度
     */
    private var mCurrentDegree = 0

    private var mEdge = 0
    private var mTopSize = 0

    init {
        initBounds()
        initPaint()
        initSize()
    }

    constructor(context: Context) : this(context, null, 0)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : this(context, attrs, 0)


    private fun initBounds() {
        mTopBound = RectF()
        mBottomBound = RectF()
        mRotateDrawableBound = Rect()
        mCenterPoint = Point()
    }

    private fun initPaint() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPath = Path()
    }

    private fun initSize() {
        mEdge = BGARefreshLayout.dp2px(context, 5)
        mRotateDrawableSize = BGARefreshLayout.dp2px(context, 20)
        mTopSize = mRotateDrawableSize + 2 * mEdge
        mMaxBottomHeight = (2.4f * mRotateDrawableSize).toInt()
    }

    fun setStickinessColor(@ColorRes resId: Int) {
        mPaint!!.color = resources.getColor(resId)
    }

    fun setRotateImage(@DrawableRes resId: Int) {
        mRotateDrawable = resources.getDrawable(resId)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = mTopSize + paddingLeft + paddingRight
        val height = mTopSize + paddingTop + paddingBottom + mMaxBottomHeight
        setMeasuredDimension(width, height)
        measureDraw()
    }

    private fun measureDraw() {
        mCenterPoint!!.x = measuredWidth / 2
        mCenterPoint!!.y = measuredHeight / 2
        mTopBound!!.left = mCenterPoint!!.x - mTopSize / 2.toFloat()
        mTopBound!!.right = mTopBound!!.left + mTopSize
        mTopBound!!.bottom = measuredHeight - paddingBottom - mCurrentBottomHeight.toFloat()
        mTopBound!!.top = mTopBound!!.bottom - mTopSize
        var scale = 1.0f - mCurrentBottomHeight * 1.0f / mMaxBottomHeight
        scale = Math.min(Math.max(scale, 0.2f), 1.0f)
        val mBottomSize = (mTopSize * scale).toInt()
        mBottomBound!!.left = mCenterPoint!!.x - mBottomSize / 2.toFloat()
        mBottomBound!!.right = mBottomBound!!.left + mBottomSize
        mBottomBound!!.bottom = mTopBound!!.bottom + mCurrentBottomHeight
        mBottomBound!!.top = mBottomBound!!.bottom - mBottomSize
    }

    override fun onDraw(canvas: Canvas) {
        if (mRotateDrawable == null) {
            return
        }
        mPath!!.reset()
        mTopBound!!.round(mRotateDrawableBound)
        mRotateDrawable!!.bounds = mRotateDrawableBound!!
        if (mIsRotating) {
            mPath!!.addOval(mTopBound, Path.Direction.CW)
            canvas.drawPath(mPath!!, mPaint!!)
            canvas.save()
            canvas.rotate(
                mCurrentDegree.toFloat(),
                mRotateDrawable!!.bounds.centerX().toFloat(),
                mRotateDrawable!!.bounds.centerY().toFloat()
            )
            mRotateDrawable!!.draw(canvas)
            canvas.restore()
        } else {
            // 移动到drawable左边缘的中间那个点
            mPath!!.moveTo(mTopBound!!.left, mTopBound!!.top + mTopSize / 2)
            // 从drawable左边缘的中间那个点开始画半圆
            mPath!!.arcTo(mTopBound, 180f, 180f)
            // 二阶贝塞尔曲线，第一个是控制点，第二个是终点
//            mPath.quadTo(mTopBound.right - mTopSize / 8, mTopBound.bottom, mBottomBound.right, mBottomBound.bottom - mBottomBound.height() / 2);

            // mCurrentBottomHeight   0 到 mMaxBottomHeight
            // scale                  0.2 到 1
            val scale =
                Math.max(mCurrentBottomHeight * 1.0f / mMaxBottomHeight, 0.2f)
            val bottomControlXOffset = mTopSize * ((3 + Math.pow(
                scale.toDouble(),
                7.0
            ).toFloat() * 16) / 32)
            val bottomControlY = mTopBound!!.bottom / 2 + mCenterPoint!!.y / 2
            // 三阶贝塞尔曲线，前两个是控制点，最后一个点是终点
            mPath!!.cubicTo(
                mTopBound!!.right - mTopSize / 8,
                mTopBound!!.bottom,
                mTopBound!!.right - bottomControlXOffset,
                bottomControlY,
                mBottomBound!!.right,
                mBottomBound!!.bottom - mBottomBound!!.height() / 2
            )
            mPath!!.arcTo(mBottomBound, 0f, 180f)

//            mPath.quadTo(mTopBound.left + mTopSize / 8, mTopBound.bottom, mTopBound.left, mTopBound.bottom - mTopSize / 2);
            mPath!!.cubicTo(
                mTopBound!!.left + bottomControlXOffset,
                bottomControlY,
                mTopBound!!.left + mTopSize / 8,
                mTopBound!!.bottom,
                mTopBound!!.left,
                mTopBound!!.bottom - mTopSize / 2
            )
            canvas.drawPath(mPath!!, mPaint!!)
            mRotateDrawable!!.draw(canvas)
        }
    }

    fun setMoveYDistance(moveYDistance: Int) {
        val bottomHeight = moveYDistance - mTopSize - paddingBottom - paddingTop
        mCurrentBottomHeight = if (bottomHeight > 0) {
            bottomHeight
        } else {
            0
        }
        postInvalidate()
    }

    /**
     * 是否能切换到正在刷新状态
     *
     * @return
     */
    fun canChangeToRefreshing(): Boolean {
        return mCurrentBottomHeight >= mMaxBottomHeight * 0.98f
    }

    fun startRefreshing() {
        val animator = ValueAnimator.ofInt(mCurrentBottomHeight, 0)
        animator.duration = mStickinessRefreshViewHolder!!.topAnimDuration.toLong()
        animator.addUpdateListener { animation ->
            mCurrentBottomHeight = animation.animatedValue as Int
            postInvalidate()
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                mIsRefreshing = true
                if (mCurrentBottomHeight != 0) {
                    mStickinessRefreshViewHolder!!.startChangeWholeHeaderViewPaddingTop(
                        mCurrentBottomHeight
                    )
                } else {
                    mStickinessRefreshViewHolder!!.startChangeWholeHeaderViewPaddingTop(-(mTopSize + paddingTop + paddingBottom))
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                mIsRotating = true
                startRotating()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animator.start()
    }

    private fun startRotating() {
        ViewCompat.postOnAnimation(this, Runnable {
            mCurrentDegree += 10
            if (mCurrentDegree > 360) {
                mCurrentDegree = 0
            }
            if (mIsRefreshing) {
                startRotating()
            }
            postInvalidate()
        })
    }

    fun stopRefresh() {
        mIsRotating = true
        mIsRefreshing = false
        postInvalidate()
    }

    fun smoothToIdle() {
        val animator = ValueAnimator.ofInt(mCurrentBottomHeight, 0)
        animator.duration = mStickinessRefreshViewHolder!!.topAnimDuration.toLong()
        animator.addUpdateListener { animation ->
            mCurrentBottomHeight = animation.animatedValue as Int
            postInvalidate()
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                mIsRotating = false
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        animator.start()
    }

    fun setStickinessRefreshViewHolder(stickinessRefreshViewHolder: BGAStickinessRefreshViewHolder?) {
        mStickinessRefreshViewHolder = stickinessRefreshViewHolder
    }

}