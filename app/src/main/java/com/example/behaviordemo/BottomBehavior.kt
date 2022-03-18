package com.example.behaviordemo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.absoluteValue

/**
 * 底部的behavior
 *
 * 内容往上时：
 *
 *   如果当前topAppbarLayout可见。那么先位移appBarLayout,不可见时再消费
 *
 */
class BottomBehavior(
    context: Context,
    attr: AttributeSet
) : CoordinatorLayout.Behavior<View>(context, attr) {

    private var topAppBarLayout: TopAppBarLayout? = null

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        for (i in 0 until parent.childCount) {
            if (parent.getChildAt(i) is TopAppBarLayout) {
                topAppBarLayout = parent.getChildAt(i) as TopAppBarLayout
                break
            }
        }
        val layout = topAppBarLayout ?: return false

        child.layout(
            layout.left,
            layout.measuredHeight,
            layout.left + child.measuredWidth,
            layout.measuredHeight + child.measuredHeight
        )
        return true
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is TopAppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        // 让child一直处在被依赖的View下面
        child.layout(
            0,
            (dependency.bottom + dependency.translationY).toInt(),
            child.measuredWidth,
            child.measuredHeight
        )
        return true
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        // 竖着的则开启嵌套滑动
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
        Log.e("TAG", "Demo:4:${child.javaClass} ${target.javaClass}")

        val layout = topAppBarLayout ?: return
        if (layout.equalsSlidingTarget(target)) {
            return
        }
        // 判断是否到顶或者到底
        if (target is RecyclerView) {
            val isToTop = dy < 0
            val isToBottom = isToTop.not()
            if (isToTop) {
                processToTop(target, child.translationY, child, consumed, dy)
            }
            if (isToBottom) {
                processToBottom(target, child.translationY, child, consumed, dy)
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
        val layout = topAppBarLayout ?: return
        Log.e("TAG", "Demo:5:手指往上滑，查看下面的内容")
        // 1代表查看下面的内容，true是还能够继续往上。false代表不能往上了
        val isBottom = target.canScrollVertically(1)

        Log.e("TAG", "Demo:6:$isBottom")

        // 如果当前评论的recyclerView是不能往下滑的。而且topAppLayout没有全部位移完。
        // 自己消耗dy-(measuredHeight-child.translationY.abs)

        val layoutTranslationY = layout.translationY
        if (layoutTranslationY.toInt() != layout.measuredHeight.absoluteValue) {
            val desireHeight = layout.measuredHeight - layoutTranslationY.absoluteValue
            if (dy.absoluteValue <= desireHeight) {
                // 将dy全部消耗掉
                layout.translationY = layoutTranslationY - dy
                consumed[1] = dy
            } else {
                // 消耗一部分的的dy
                layout.translationY = -layout.measuredHeight.toFloat()
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
        Log.e("TAG", "Demo:5:手指往下滑，查看上面的内容")
        // -1 代表查看上面的内容，true是还能够继续往上。false代表不能往上了
        val isTop = target.canScrollVertically(-1)

        Log.e("TAG", "Demo:6:$isTop")

    }


//    override fun onInterceptTouchEvent(
//        parent: CoordinatorLayout,
//        child: View,
//        ev: MotionEvent
//    ): Boolean {
//        val superResult = super.onInterceptTouchEvent(parent, child, ev)
//        val layout = topAppBarLayout ?: return superResult
//        if (layout.translationY == 0F) {
//            return superResult
//        }
//        return layout.translationY.toInt().absoluteValue != layout.measuredHeight.absoluteValue
//    }
//
//    override fun onTouchEvent(parent: CoordinatorLayout, child: View, ev: MotionEvent): Boolean {
//        return true
//    }
}
