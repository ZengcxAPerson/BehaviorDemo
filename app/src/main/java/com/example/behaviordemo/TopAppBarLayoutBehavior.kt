package com.example.behaviordemo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.absoluteValue

/**
 * 头部的behavior
 *
 * 内容往上时：
 *
 *  如果能够子view能够继续滑动，就让子view消费滑动。
 *
 *  如果不能滑动，但是子view高度可见，则将子view的高度偏移
 *
 *  如果子view高度已经不可见了，则将剩余高度返回
 *
 * 内容往下时：
 *
 *  如果子view高度不可见，先将子view的高度偏移回之前的位置。
 *
 *  如果高度恢复了，子view依旧可以消费高度，就让子view消费。
 *
 *  如果消费完成了，就将剩余高度返回。
 *
 */
class TopAppBarLayoutBehavior(
    context: Context,
    attr: AttributeSet
) : CoordinatorLayout.Behavior<View>(context, attr) {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        // 竖着的则开启嵌套滑动
        Log.e("TAG", "Demo2:1:${child.javaClass}")
        Log.e("TAG", "Demo2:2:${target.javaClass}")
        Log.e("TAG", "Demo2:3:${directTargetChild.javaClass}")
        (child as? TopAppBarLayout)?.setScrollView(target)
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        Log.e("TAG", "Demo2:4:${child.javaClass} ${target.javaClass}")
        val translationY = child.translationY
        if (-translationY >= child.measuredHeight) {
            // child已经滚动到屏幕外了，就不去消耗滚动了
            return
        }
        // 判断是否到顶或者到底
        if (target is RecyclerView) {
            val isToTop = dy < 0
            val isToBottom = isToTop.not()
            if (isToTop) {
                processToTop(target, translationY, child, consumed, dy)
            }
            if (isToBottom) {
                processToBottom(target, translationY, child, consumed, dy)
            }
        }
    }

    private fun processToBottom(
        target: RecyclerView,
        translationY: Float,
        child: View,
        consumed: IntArray,
        dy: Int,
    ) {
        Log.e("TAG", "Demo2:5:手指往上滑，查看下面的内容")
        // 1代表查看下面的内容，true是还能够继续往上。false代表不能往上了
        val isBottom = target.canScrollVertically(1)
        if (!isBottom) {
            // 不能往下了，就把自身进行偏移
            // 还差desireHeight距离将会移出屏幕外
            val desireHeight = translationY + child.measuredHeight
            if (dy <= desireHeight) {
                // 将dy全部消耗掉
                child.translationY = translationY - dy
                consumed[1] = dy
            } else {
                // 消耗一部分的的dy
                child.translationY = translationY - desireHeight
                consumed[1] = desireHeight.toInt()
            }
        }
    }

    private fun processToTop(
        target: RecyclerView,
        translationY: Float,
        child: View,
        consumed: IntArray,
        dy: Int,
    ) {
        Log.e("TAG", "Demo2:5:手指往下滑，查看上面的内容")
        // -1 代表查看上面的内容，true是还能够继续往上。false代表不能往上了
        val isTop = target.canScrollVertically(-1)

        //如果存在偏移
        if (child.translationY < 0) {
            Log.e("TAG", "Demo2:6:还差${child.translationY}子view完全展示")
            if (dy.absoluteValue <= child.translationY.absoluteValue) {
                child.translationY = translationY - dy
                consumed[1] = dy
            } else {
                child.translationY = 0F
                consumed[1] = translationY.toInt()
            }
        }

    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        val translationY = child.translationY
        if (translationY >= 0 || dyUnconsumed > 0) {
            // 手指向上滚动或者child已经滚出了屏幕，不去处理
            return
        }
        if (dyUnconsumed > translationY) {
            // 全部消耗
            consumed[1] += dyUnconsumed
            child.translationY = translationY - dyUnconsumed
        } else {
            // 消耗一部分
            consumed[1] += child.translationY.toInt()
            child.translationY = 0F
        }
    }
}
