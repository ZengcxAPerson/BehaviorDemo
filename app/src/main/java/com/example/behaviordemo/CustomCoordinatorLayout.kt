package com.example.behaviordemo

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.coordinatorlayout.widget.CoordinatorLayout

class CustomCoordinatorLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attributeSet, defStyleAttr) {

    var mListener: OnInterceptTouchListener? = null

    val targetSave = TargetSave()

    interface OnInterceptTouchListener {
        fun onIntercept(ev: MotionEvent?)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (mListener != null) {
            mListener?.onIntercept(ev)
        }
        return super.onInterceptTouchEvent(ev)
    }
}
