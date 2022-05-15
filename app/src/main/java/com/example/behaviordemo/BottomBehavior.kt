package com.example.behaviordemo

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
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

    companion object {
        private const val TAG = "DetailBottomBehavior"
    }

    private var detailTopAppBarLayout: TopAppBarLayout? = null

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        (parent as? CustomCoordinatorLayout)?.targetSave?.saveParent(
            child,
            this
        )
        for (i in 0 until parent.childCount) {
            if (parent.getChildAt(i) is TopAppBarLayout) {
                detailTopAppBarLayout = parent.getChildAt(i) as TopAppBarLayout
                break
            }
        }
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
        val top = (dependency.bottom + dependency.translationY).toInt()
        Log.e(TAG, "关联到DetailTopAppBarLayout下面")
        //TODO 如果存在特殊情况，这里如何进行交互
        // 是否才用判断dependency是否实现了某个接口
        Log.e(TAG, "childTop:$top child:$child")
        // 让child一直处在被依赖的View下面
        child.layout(
            0,
            top,
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
        Log.e(TAG, "嵌套滑动传递到了$TAG")
        (coordinatorLayout as? CustomCoordinatorLayout)?.targetSave?.isChild(
            this, target
        )?.let {
            if (it.not()) {
                Log.e(TAG, "发送滑动的不属于本behavior负责处理，终止嵌套处理")
                return
            }
        }
        // 判断是否到顶或者到底
//        if (target is NestedScrollingChild2 || target is NestedScrollingChild3) {
        val isToTop = dy < 0
        val isToBottom = isToTop.not()
        if (isToTop) {
            Log.e(TAG, "手指往下滑，查看上面的内容")
            processToTop(target, child.translationY, child, consumed, dy)
        }
        if (isToBottom) {
            Log.e(TAG, "手指往上滑，查看下面的内容")
            processToBottom(target, child.translationY, child, consumed, dy)
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
        // 参数：-1 代表查看上面的内容，
        // 返回值：true是还能够继续往上。false代表不能往上了
        val isCanTop = target.canScrollVertically(-1)
        //TODO 下面behavior是否需要处理 手指下滑，查看上面的内容的事件
        Log.e(TAG, "内容上面是否还有$isCanTop")
        if (isCanTop.not()) {
            // 上方没有内容了，判断topAppLayout是否需要进行处理
            // 不能往上了，就把topAppLayout进行偏移
            val layout = detailTopAppBarLayout ?: return
            // 还差desireHeight距离将完全移回屏幕内
            val desireHeight = layout.translationY
            if (dy.absoluteValue <= desireHeight.absoluteValue) {
                // 将dy全部消耗掉
                layout.translationY = desireHeight - dy
                consumed[1] = dy
//                Log.e(TopAppBarLayoutBehavior.TAG, "当前总滑动距离:$dy,自身消耗滑动距离$:$dy")
            } else {
                // 消耗一部分的的dy
                layout.translationY = 0F
                consumed[1] = desireHeight.toInt()
//                Log.e(TopAppBarLayoutBehavior.TAG, "当前总滑动距离:$dy,自身消耗滑动距离$:$desireHeight")
            }
        } else {
            Log.e(TAG, "滑动触发的控件自己消耗滑动距离，不进行嵌套滑动")
        }
    }

    private fun processToBottom(
        target: View,
        translationY: Float,
        child: View,
        consumed: IntArray,
        dy: Int,
    ) {
        // 下方的逻辑为，先让topAppLayout位移完在让targetView消耗
        // isCanBottom 变量现在没有作用
        val layout = detailTopAppBarLayout ?: return
        // 参数：1代表查看下面的内容
        // 返回值：true是还能够继续往上。false代表不能往上了
//        val isCanBottom = target.canScrollVertically(1)
//        if (isCanBottom) {
//            Log.e(TAG, "内容下面还有")
//        } else {
//            Log.e(TAG, "内容下面没有了")
//        }

        val layoutTranslationY = layout.translationY
        // 如果当前topAppLayout没有全部位移完。
        // 先把topAppLayout往上位移
        // TODO  如果这个topAppLayout想留一部分内容不被折叠 如何解耦处理
        if (layoutTranslationY.toInt() != layout.measuredHeight.absoluteValue) {
            val desireHeight = layout.measuredHeight - layoutTranslationY.absoluteValue
            // 判断layout剩余没有TranslationY的高度和当前滑动距离大小关系
            // 看是否能够全部消费
            if (dy.absoluteValue <= desireHeight) {
                // 将dy全部消耗掉
                layout.translationY = layoutTranslationY - dy
                consumed[1] = dy
            } else {
                // 消耗一部分的的dy
                layout.translationY = -layout.measuredHeight.toFloat()
                consumed[1] = desireHeight.toInt()
            }
            Log.e(TAG, "topAppLayout可见高度:$desireHeight,当前滑动距离$dy")
        } else {
            Log.e(TAG, "topAppLayout完全不可见")
        }
    }
}
