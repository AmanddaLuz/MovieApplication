package com.amandaluz.core.util.recycler

import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.animateList() {
    val animationController: LayoutAnimationController =
        AnimationUtils.loadLayoutAnimation(context, com.amandaluz.ui.R.anim.layout_animation)
    this.layoutAnimation = animationController
}