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

    companion object {
        private const val TAG = "DetailTopBehavior"
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        (parent as? CustomCoordinatorLayout)?.targetSave?.saveParent(
            child,
            this
        )
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        val isNestedScroll = axes == ViewCompat.SCROLL_AXIS_VERTICAL
        // 竖着的则开启嵌套滑动
        Log.e(TAG, "判断是否开启嵌套滑动:$isNestedScroll")
        return isNestedScroll
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
        Log.e(TAG, "嵌套滑动传递到了${TAG}")
        (coordinatorLayout as? CustomCoordinatorLayout)?.targetSave?.isChild(
            this, target
        )?.let {
            if (it.not()) {
                Log.e(TAG, "发送滑动的不属于本behavior负责处理，终止嵌套处理")
                return
            }
        }
        val translationY = child.translationY
        if (-translationY >= child.measuredHeight) {
            // child已经滚动到屏幕外了，就不去消耗滚动了
            Log.e(TAG, "${child}不可见，不消耗滚动")
            return
        }
        // 判断是否到顶或者到底
//        if (target is NestedScrollingChild2 || target is NestedScrollingChild3) {
        val isToTop = dy < 0
        val isToBottom = isToTop.not()
        if (isToTop) {
            processToTop(target, translationY, child, consumed, dy)
        }
        if (isToBottom) {
            processToBottom(target, translationY, child, consumed, dy)
        }
//        }
    }

    private fun processToTop(
        target: View,
        translationY: Float,
        child: View,
        consumed: IntArray,
        dy: Int,
    ) {
        Log.e(TAG, "手指往下滑，查看上面的内容")
        // 参数：-1 代表查看上面的内容，
        // 返回值：true是还能够继续往上。false代表不能往上了
        val isCanTop = target.canScrollVertically(-1)
        Log.e(TAG, "内容上面是否还有$isCanTop")
        // TODO 这里是否可以使用接口来实现
        //如果存在偏移
        if (child.translationY < 0) {
            Log.e(TAG, "该view$child 存在偏移，还差${child.translationY}完全展示，此次滑动总距离:$dy")
            if (dy.absoluteValue <= child.translationY.absoluteValue) {
                child.translationY = translationY - dy
                consumed[1] = dy
            } else {
                child.translationY = 0F
                consumed[1] = translationY.toInt()
            }
        } else {
            Log.e(TAG, "该view$child 不存在偏移")
        }

    }

    private fun processToBottom(
        target: View,
        translationY: Float,
        child: View,
        consumed: IntArray,
        dy: Int,
    ) {
        Log.e(TAG, "手指往上滑，查看下面的内容")
        // 参数：1代表查看下面的内容
        // 返回值：true是还能够继续往上。false代表不能往上了
        val isCanBottom = target.canScrollVertically(1)
        if (isCanBottom.not()) {
            Log.e(TAG, "内容下面没有了")
            // 不能往下了，就把自身进行偏移
            // 还差desireHeight距离将会移出屏幕外
            val desireHeight = translationY + child.measuredHeight
            if (dy <= desireHeight) {
                // 将dy全部消耗掉
                child.translationY = translationY - dy
                consumed[1] = dy
                Log.e(TAG, "当前总滑动距离:$dy,自身消耗滑动距离$:$dy")
            } else {
                // 消耗一部分的的dy
                child.translationY = translationY - desireHeight
                consumed[1] = desireHeight.toInt()
                Log.e(TAG, "当前总滑动距离:$dy,自身消耗滑动距离$:$desireHeight")
            }
        } else {
            Log.e(TAG, "内容下面还有,直接让${target}消费滑动,不进行嵌套处理")
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
        //TODO 这里的嵌套滑动是处理什么？
        (coordinatorLayout as? CustomCoordinatorLayout)?.targetSave?.isChild(
            this, target
        )?.let {
            if (it.not()) {
//                Log.e(TAG, "发送滑动的不属于本behavior负责处理，终止嵌套处理")
                return
            }
        }
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
