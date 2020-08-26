package ru.mihassu.thecarapp.ui.animation

import android.animation.ValueAnimator
import android.view.View

class SlideAnimator(private val slideFrom: Float, private val slideTo: Float, private val animDuration: Long) {

    private val animator: ValueAnimator = ValueAnimator.ofFloat(slideFrom, slideTo).apply { duration = animDuration }

    fun animate(view: View) {
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            view.translationX = value
            view.invalidate()
        }
        animator.start()
    }
}