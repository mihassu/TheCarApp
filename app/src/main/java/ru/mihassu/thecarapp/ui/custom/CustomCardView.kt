package ru.mihassu.thecarapp.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView

class CustomCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : CardView(context, attrs, defStyleAttr) {

    init {
//        orientation = VERTICAL

        addView(RectFrame(context))
    }

}