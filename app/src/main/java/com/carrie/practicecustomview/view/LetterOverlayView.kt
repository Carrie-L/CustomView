package com.carrie.practicecustomview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.carrie.practicecustomview.dp
import com.carrie.practicecustomview.px

class LetterOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var letter: String = ""
    private var isShowing = false

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 32f.px
        textAlign = Paint.Align.CENTER
        color = Color.WHITE
        typeface = Typeface.DEFAULT_BOLD
    }

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#60c9c8")
        style = Paint.Style.FILL
    }

    private val hideRunnable = Runnable {
        isShowing = false
        visibility = View.GONE
    }

    fun show(letter: String) {
        this.letter = letter
        isShowing = true
        visibility = View.VISIBLE
        invalidate()
        removeCallbacks(hideRunnable)
        postDelayed(hideRunnable, 1000)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制背景
        // 圆角矩形的尺寸
        val boxSize = 80f.dp  // 矩形大小
        val radius = 12f.dp    // 圆角大小
        //  计算矩形的位置（居中）
        val left = (width - boxSize) / 2
        val top = (height - boxSize) / 2

        // 绘制圆角矩形
        canvas.drawRoundRect(
            left,    // 左边距离
            top,     // 上边距离
            left + boxSize,   // 右边距离
            top + boxSize,  // 下边距离
            radius,  // x方向圆角半径
            radius,  // y方向圆角半径
            bgPaint    // 画笔
        )

        // 绘制文字
        val baseline = (height - paint.fontMetrics.bottom + paint.fontMetrics.top) / 2 - paint.fontMetrics.top
        canvas.drawText(letter, width / 2f, baseline, paint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(hideRunnable)
    }

}