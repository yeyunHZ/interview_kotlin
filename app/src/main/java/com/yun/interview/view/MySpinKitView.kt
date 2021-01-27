package com.yun.interview.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import com.github.ybq.android.spinkit.SpriteFactory
import com.github.ybq.android.spinkit.Style
import com.github.ybq.android.spinkit.sprite.Sprite
import com.yun.interview.R

class MySpinKitView constructor(
    context: Context, attrs: AttributeSet?,
    defStyle: Int
) : ProgressBar(context, attrs, defStyle) {

    private var mStyle: Style? = null
    private var mColor = 0
    private var mSprite: Sprite? = null

    init {

    }

    constructor(context: Context) : this(context, null, R.attr.SpinKitViewStyle)

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : this(context, attrs, R.attr.SpinKitViewStyle)

    fun setStyle(style: Style, color: Int) {
        mStyle = style
        mColor = color
        init()
        isIndeterminate = true
    }


    private fun init() {
        val sprite = SpriteFactory.create(mStyle)
        sprite.color = mColor
        setIndeterminateDrawable(sprite)
    }

    override fun setIndeterminateDrawable(d: Drawable?) {
        require(d is Sprite) { "this d must be instanceof Sprite" }
        setIndeterminateDrawable(d)
    }

    fun setIndeterminateDrawable(d: Sprite) {
        super.setIndeterminateDrawable(d)
        mSprite = d
        if (mSprite!!.color == 0) {
            mSprite!!.color = mColor
        }
        onSizeChanged(width, height, width, height)
        if (visibility == View.VISIBLE) {
            mSprite!!.start()
        }
    }

    override fun getIndeterminateDrawable(): Sprite? {
        return mSprite
    }

    fun setColor(color: Int) {
        mColor = color
        if (mSprite != null) mSprite!!.color = color
        invalidate()
    }

    override fun unscheduleDrawable(who: Drawable?) {
        super.unscheduleDrawable(who)
        if (who is Sprite) {
            who.stop()
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            if (mSprite != null && visibility == View.VISIBLE) {
                mSprite!!.start()
            }
        }
    }

    override fun onScreenStateChanged(screenState: Int) {
        super.onScreenStateChanged(screenState)
        if (screenState == View.SCREEN_STATE_OFF) {
            if (mSprite != null) {
                mSprite!!.stop()
            }
        }
    }

}