package com.amandaluz.ui.bottomnavigation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

class HideBottomNavigationBehavior<V: View>(context: Context, attrs: AttributeSet)
    : CoordinatorLayout.Behavior<V>(context, attrs) {

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int,
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: V,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)

        child.translationY = 0f.coerceAtLeast(
            child.height.toFloat().coerceAtMost(child.translationY + dy)
        )

        // Ajustar a transparência da barra de navegação inferior enquanto rola
        val alpha = ((child.height + child.translationY) / child.translationY)
        child.alpha = alpha
    }
}