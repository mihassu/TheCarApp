package ru.mihassu.thecarapp.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.annotation.Dimension.PX
import org.jetbrains.anko.dip
import ru.mihassu.thecarapp.R

class RectFrame @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr)  {

    companion object {
        @Dimension(unit = DP) private const val defaultCornerRadiusDp = 3
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 12F
        color = context.getColor(R.color.colorAccent)
    }
    private val rect = Rect()
    private val rectF = RectF()

    @Dimension(unit = PX) var cornerRadius: Float = dip(defaultCornerRadiusDp).toFloat()

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.RectFrame)
        attributes.getDimension(R.styleable.RectFrame_rectRadius, cornerRadius)

        attributes.recycle()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        rect.set(0, 0, width, height)
//        canvas?.drawRect(rect, paint)

        rectF.set(0F, 0F, width.toFloat(), height.toFloat())
        canvas?.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)
    }
}