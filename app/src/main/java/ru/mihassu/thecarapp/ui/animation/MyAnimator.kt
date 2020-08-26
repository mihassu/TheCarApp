package ru.mihassu.thecarapp.ui.animation

import android.animation.ValueAnimator
import android.view.View

object MyAnimator {

    val scaleAnimator: ValueAnimator = ValueAnimator.ofFloat(0F, 1F).apply { duration = 500L }

}

//fun ValueAnimator.animate(view: View) {
//    addUpdateListener { animation ->
//        val value = animation.animatedValue as Float
//        view.scaleX = value
//    }
//    start()
//}