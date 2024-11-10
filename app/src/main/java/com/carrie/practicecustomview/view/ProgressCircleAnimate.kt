package com.carrie.practicecustomview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.carrie.practicecustomview.R
import com.carrie.practicecustomview.dp
import kotlin.math.min


class ProgressCircleAnimate(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val TAG = "ProgressCircleAnimate"
    private var startAngle = 0
    private var innerCircleColor = Color.BLACK
    private var outerCircleColor = Color.GREEN
    private var progressTextColor = Color.GREEN
    private var maxValue = 10000
    private var progressValue = 0

    private final val RADIUS = 120f.dp
    private var sweepAngle = 130f
    private var progressText = ""


    private val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f.dp
    }
    private val outerPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f.dp
        isDither = true
        isAntiAlias = true
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        textSize = 100f
    }
    private val path = Path()


    init {
        val typedValue = context.obtainStyledAttributes(attrs, R.styleable.ProgressCircleAnimate)
        startAngle = typedValue.getInt(R.styleable.ProgressCircleAnimate_startAngle, startAngle)
        maxValue = typedValue.getInt(R.styleable.ProgressCircleAnimate_maxValue, maxValue)
        progressValue = typedValue.getInt(R.styleable.ProgressCircleAnimate_progressValue, progressValue)
        innerCircleColor = typedValue.getColor(R.styleable.ProgressCircleAnimate_innerCircleColor, innerCircleColor)
        outerCircleColor = typedValue.getColor(R.styleable.ProgressCircleAnimate_outerCircleColor, outerCircleColor)
        progressTextColor = typedValue.getColor(R.styleable.ProgressCircleAnimate_progressTextColor, progressTextColor)
        typedValue.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        innerPaint.color = innerCircleColor
        outerPaint.color = outerCircleColor
        textPaint.color = progressTextColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(min(width, height), min(width, height))
        println("$TAG onMeasure: width=$width, height=$height")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.drawCircle(width / 2f, height / 2f, RADIUS, innerPaint)
        canvas.restore()

        if (progressValue == 0) return
        canvas.save()
        sweepAngle = (360 * progressValue).toFloat() / maxValue
        println("$TAG sweepAngle=$sweepAngle")
        // top: 不能是0
        path.addArc(width / 2f - RADIUS, height / 2 - RADIUS, width / 2f + RADIUS, height / 2 + RADIUS, startAngle.toFloat(), sweepAngle)
        canvas.drawPath(path, outerPaint)
        canvas.restore()

        // 测量文字宽高
        val fontMetrics = textPaint.fontMetrics
        val baseline = height / 2 - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top
        canvas.drawText(progressText, (width - textPaint.measureText(progressText)) / 2, baseline, textPaint)
    }

    fun setProgressValue(value: Int) {
        progressText = "${(value * 100 / maxValue)}%"
        progressValue = value
        invalidate()
    }

}