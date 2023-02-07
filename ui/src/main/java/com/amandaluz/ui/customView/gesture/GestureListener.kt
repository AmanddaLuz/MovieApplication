package com.amandaluz.ui.customView.gesture

import android.view.GestureDetector
import android.view.MotionEvent

class GestureListener(private val next: ()-> Unit , private val previous: ()-> Unit, private val dots: () -> Unit) : GestureDetector.SimpleOnGestureListener() {
    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        if (e1.x > e2.x) {
            next.invoke()
        } else {
            previous.invoke()
        }
        dots.invoke()
        return true
    }
}