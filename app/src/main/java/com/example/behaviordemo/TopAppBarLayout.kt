package com.example.behaviordemo

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout

class TopAppBarLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppBarLayout(context, attributeSet, defStyleAttr) {

    private var target: RecyclerView? = null

    fun setScrollView(target: View) {
        if (this.target == null) {
            this.target = target as? RecyclerView
        }
    }

    fun equalsSlidingTarget(
        target: View
    ): Boolean {
        return target == this.target
    }
}
