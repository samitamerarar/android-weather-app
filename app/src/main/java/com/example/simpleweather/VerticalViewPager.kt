package com.example.simpleweather

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager

class VerticalViewPager(context: Context, attrs: AttributeSet?): ViewPager(context, attrs) {

    init {
        setPageTransformer(true, VerticalPage())
        overScrollMode = OVER_SCROLL_NEVER
    }

    private fun getNewXY(motionEvent: MotionEvent): MotionEvent {
        val newX = (motionEvent.y / height) * width
        val newY = (motionEvent.x / width) * height

        motionEvent.setLocation(newX, newY)
        return motionEvent
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted: Boolean = super.onInterceptTouchEvent(getNewXY(ev))
        getNewXY(ev)
        return intercepted
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(getNewXY(ev))
    }

    inner class VerticalPage: PageTransformer {
        override fun transformPage(view: View, position: Float) {
            when {
                position < -1 -> view.alpha = 0f
                position <= 1 -> {
                    view.alpha = 1f
                    view.translationX = view.width * -position
                    val yPos: Float = position * view.height
                    view.translationY = yPos
                }
                else -> view.alpha = 0f
            }
        }

    }
}