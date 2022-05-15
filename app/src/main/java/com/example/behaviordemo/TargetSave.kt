package com.example.behaviordemo

import android.view.View
import android.view.ViewParent
import androidx.coordinatorlayout.widget.CoordinatorLayout

class TargetSave {

    private val parents = mutableMapOf<CoordinatorLayout.Behavior<View>, View>()

    fun saveParent(
        parent: View,
        behavior: CoordinatorLayout.Behavior<View>
    ) {
        parents[behavior] = parent
    }

    fun isChild(
        behavior: CoordinatorLayout.Behavior<View>,
        child: View
    ): Boolean {
        val parent = parents[behavior] ?: return false
        var result = false
        var tmp: ViewParent? = null
        do {
            tmp = if (tmp == null) {
                child.parent
            } else {
                tmp.parent
            }
            if (tmp == parent) {
                result = true
            }
        } while (result.not() && tmp != null)

        return result
    }

}
