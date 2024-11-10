package com.carrie.practicecustomview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import com.carrie.practicecustomview.R
import com.carrie.practicecustomview.dp

/**
 * ViewPager 滑动，文字变色
 */
class TextChangeView(context: Context, attrs: AttributeSet? = null) : androidx.appcompat.widget.AppCompatTextView(context, attrs) {
    private var normalTextColor = Color.BLACK
    private var changedTextColor = Color.CYAN
    private var tabText: String = "Hello"
    private var flipProgress = 0f
    // 初始向左滑时，文字向右变色, 我计算的是 第一个按钮文字向右变色，第二个按钮文字向左变色，不区分是不是选中。
    private var direction = Direction.TEXT_CHANGE_TO_RIGHT
    private var progressWidth = 0
    private lateinit var fontMetrics: FontMetrics

    enum class Direction {
        TEXT_CHANGE_TO_RIGHT,  // 文字向右变色
        TEXT_CHANGE_TO_LEFT // 文字向左变色
    }

    private val rect = Rect()

    private val normalPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        isAntiAlias = true
        isDither = true
    }
    private val changePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        isAntiAlias = true
        isDither = true
    }
    private val path = Path()

    init {
        val value = context.obtainStyledAttributes(attrs, R.styleable.TextChangeView)
        normalTextColor = value.getColor(R.styleable.TextChangeView_normalTextColor, normalTextColor)
        changedTextColor = value.getColor(R.styleable.TextChangeView_changedTextColor, changedTextColor)
        value.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        normalPaint.textSize = textSize
        changePaint.textSize = textSize
        normalPaint.color = normalTextColor
        changePaint.color = changedTextColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        progressWidth = (flipProgress * width).toInt()
        if (direction == Direction.TEXT_CHANGE_TO_RIGHT) { // 左边默认颜色，右边选中颜色
            drawText(canvas, normalPaint, 0, width - progressWidth)
            drawText(canvas, changePaint, width - progressWidth, width)
            println("TextChangeView: TEXT_CHANGE_TO_RIGHT 👉")
        } else { // 左边选中颜色，右边默认颜色
            drawText(canvas, normalPaint, progressWidth, width)
            drawText(canvas, changePaint, 0, progressWidth)
            println("TextChangeView: TEXT_CHANGE_TO_LEFT 👈")
        }

    }

    private fun drawText(canvas: Canvas, paint: Paint, start: Int, end: Int) {
        paint.getTextBounds(tabText, 0, tabText.length, rect)
        fontMetrics = paint.fontMetrics
        val baseline = (height - (fontMetrics.bottom - fontMetrics.top)) / 2 - fontMetrics.top
        val x = (width - rect.width()) / 2f

        canvas.save()
        canvas.clipRect(start, 0, end, height)
        canvas.drawText(tabText, x, baseline, paint)
        canvas.restore()
    }

    /**
     * 不能直接在 TabLayoutMediator 里用 text，会直接设置默认的text
     */
    fun setTabText(text: String) {
        tabText = text
    }

    fun setFlipProgress(progress: Float) {
        flipProgress = progress
        invalidate()
    }

    fun setDirection(direction: Direction) {
        this.direction = direction
    }

    fun setNormalColor(color: Int) {
        normalTextColor = color
    }

    fun setChangeColor(color: Int) {
        changedTextColor = color
    }


}