package com.carrie.practicecustomview.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.carrie.practicecustomview.R
import com.carrie.practicecustomview.dp
import com.carrie.practicecustomview.px

/**
 * 侧边栏字母索引列表
 */
class SideBarLetter(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val letters = listOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
        "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
    )
    private var textWidth = 0
    private var padding = 12f.dp
    private var textSize: Float = 12f.px
    private var normalTextColor: Int = Color.parseColor("#666666")
    private var selectedTextColor: Int = Color.parseColor("#FF0000")
    private var selectedBackgroundColor: Int = Color.parseColor("#e4f5f6")
    private var showBackground: Boolean = true

    /**
     * ((String) -> Unit) : 这是函数类型，表示一个接收 String 参数返回 Unit 的函数
     * ? : 表示这个函数类型可以为空
     * = null : 初始值为空
     */
    var onLetterSelectedListener: ((String) -> Unit)? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        textSize = this@SideBarLetter.textSize
        textAlign = Paint.Align.CENTER
    }
    private val path = Path()
    private var itemHeight = 0
    private var selectedIndex = -1

    init {
        context.obtainStyledAttributes(attrs, R.styleable.SideBarLetter)
            .apply {
                padding = getDimension(R.styleable.SideBarLetter_padding, 12f.dp)
                textSize = getDimension(R.styleable.SideBarLetter_textSize, 12f.px)
                normalTextColor = getColor(R.styleable.SideBarLetter_normalLetterColor, normalTextColor)
                selectedTextColor = getColor(R.styleable.SideBarLetter_selectedLetterColor, selectedTextColor)
                selectedBackgroundColor = getColor(R.styleable.SideBarLetter_selectedBackgroundColor, selectedBackgroundColor)
                showBackground = getBoolean(R.styleable.SideBarLetter_showBackground, showBackground)
            }.recycle()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        textWidth = paint.measureText("A").toInt()
        val width = textWidth + padding.toInt() * 2
        setMeasuredDimension(width, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        itemHeight = h / letters.size
    }

    /**
     * 画26个字母
     */
    override fun onDraw(canvas: Canvas) {
        val fontMetrics = paint.fontMetrics

        // 绘制选中时的背景
        if (showBackground && selectedIndex >= 0) {
            paint.color = selectedBackgroundColor
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        }

        letters.forEachIndexed { i, letter ->
            paint.color = if (i == selectedIndex) selectedTextColor else normalTextColor
            val baseline = i * itemHeight + (itemHeight - fontMetrics.bottom + fontMetrics.top) / 2f - fontMetrics.top
            canvas.drawText(letter, width / 2f, baseline, paint)
        }


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                // 告诉父视图不要拦截我的触摸事件
                parent.requestDisallowInterceptTouchEvent(true)
                val index = ((event.y / height) * letters.size).toInt().coerceIn(0, letters.size - 1)
                if (index != selectedIndex) {
                    selectedIndex = index
                    performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY) // 触觉反馈：震动，虚拟按键反馈
                    onLetterSelectedListener?.invoke(letters[index])
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                selectedIndex = -1
                // 恢复父视图的拦截能力
                parent.requestDisallowInterceptTouchEvent(false)
                invalidate()
            }
        }

        return true
    }

    fun setPadding(padding: Float) {
        this.padding = padding
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // 确保清理所有状态
        parent.requestDisallowInterceptTouchEvent(false)
    }

}