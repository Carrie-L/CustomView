package com.carrie.practicecustomview.view

import android.animation.Animator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.carrie.practicecustomview.R
import com.carrie.practicecustomview.dp
import kotlin.math.sqrt

/**
 * 一秒钟改变一次图标, 圆，正方形，三角形
 */
class IconChangeAnimateView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var colors: List<Int> = listOf()
    private var radius = 25f.dp
    private val roundPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        isAntiAlias = true
        isDither = true
    }
    private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val trianglePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        strokeWidth = 10f.dp
    }

    private val path = Path()
    private var rectF = RectF()
    private var y = 0f  // 三角形内高
    private var centerX = 0f
    private var centerY = 0f
    private var animateInternal = 1  // 动画间隔 s
    private var shape = Shape.ROUND

    private enum class Shape {
        ROUND, RECTANGLE, TRIANGLE
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.IconChangeAnimateView).apply {
            val colorResId = getResourceId(R.styleable.IconChangeAnimateView_iconColors, 0)
            if (colorResId != 0) {
                val typedArray = resources.obtainTypedArray(colorResId)
                colors = List(typedArray.length()) { i ->
                    typedArray.getColor(i, Color.BLACK)
                }
                typedArray.recycle()
            }
            recycle()
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        roundPaint.color = colors[0]
        rectPaint.color = colors[1]
        trianglePaint.color = colors[2]

        centerX = width / 2f
        centerY = height / 2f
        rectF = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        // 画三角形. 矩形和三角形的边长都是 2R
        y = (2 * radius * sqrt(3.0) / 2).toFloat()
        println("IconChangeAnimateView y: $y , radius = $radius")
        path.moveTo(centerX, centerY - radius) // 顶点
        path.lineTo(centerX - radius, centerY + y / 2) // 左下角
        path.lineTo(centerX + radius, centerY + y / 2) // 右下角
        path.close()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        when (shape) {
            Shape.ROUND -> canvas.drawCircle(centerX, centerY, radius, roundPaint)
            Shape.RECTANGLE -> canvas.drawRect(rectF, rectPaint)
            Shape.TRIANGLE -> canvas.drawPath(path, trianglePaint)
        }
        canvas.restore()
    }

    fun setColors(colorArray: List<Int>) {
        colors = colorArray
    }

    fun setAnimateInternal(internal: Int) {
        animateInternal = internal
        when (animateInternal % 3) {
            1 -> shape = Shape.ROUND
            2 -> shape = Shape.RECTANGLE
            0 -> shape = Shape.TRIANGLE
        }
        invalidate()
    }

}