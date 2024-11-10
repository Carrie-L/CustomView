package com.carrie.practicecustomview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.carrie.practicecustomview.R
import com.carrie.practicecustomview.dp

class QQStepAnimate(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var mOuterColor: Int = Color.BLACK
    private var mInnerColor: Int = Color.GREEN
    private var borderWidth = 10f.dp
    private var stepMaxValue = 5000f
    private var stepValue = 0
    private var stepTextColor = Color.RED
    private var stepTextSize = 150f
    private var stepText = ""
    private val radius = 180f.dp
    private val sweepAngle = 270f
    private val outerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        style = Paint.Style.STROKE //空心
        strokeCap = Paint.Cap.ROUND
    }
    private val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
    }
    private val path = Path()
    private val outerPath = Path()
    private val innerPath = Path()
    private val bounds = Rect()

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.QQStepAnimate)
        mOuterColor = array.getColor(R.styleable.QQStepAnimate_stepOuterColor, mOuterColor)
        mInnerColor = array.getColor(R.styleable.QQStepAnimate_stepInnerColor, mInnerColor)
        borderWidth = array.getDimension(R.styleable.QQStepAnimate_stepBorderWidth, borderWidth)
        stepMaxValue = array.getDimension(R.styleable.QQStepAnimate_stepMaxValue, stepMaxValue)
        stepValue = array.getDimension(R.styleable.QQStepAnimate_stepInnerValue, stepValue.toFloat()).toInt()
        stepTextColor = array.getColor(R.styleable.QQStepAnimate_stepTextColor, stepTextColor)
        stepTextSize = array.getDimension(R.styleable.QQStepAnimate_stepTextSize, stepTextSize)
        array.recycle()



        println("QQStepAnimate: stepTextSize=$stepTextSize, stepTextColor=$stepTextColor")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        println("QQStepAnimate: borderWidth=$borderWidth, width = $width, RADIUS=$radius")
        setPaintStyle()
        outerPath.addArc(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius, 135f, sweepAngle)




    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(outerPath, outerPaint)
        innerPath.addArc(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius, 135f,
            (sweepAngle * stepValue) / stepMaxValue)
        canvas.drawPath(innerPath, innerPaint)
        // draw text
        stepText = stepValue.toString()
        textPaint.getTextBounds(stepText, 0, stepText.length, bounds)
        canvas.drawText(stepText, (width - bounds.width()) / 2f, (height + bounds.height()) / 2f, textPaint)
    }

    private fun setPaintStyle() {
        outerPaint.color = mOuterColor
        innerPaint.color = mInnerColor
        textPaint.textSize = stepTextSize
        textPaint.color = stepTextColor
        outerPaint.strokeWidth = borderWidth
        innerPaint.strokeWidth = borderWidth
    }

    fun setBorderWidth(width: Float) {
        borderWidth = width
    }

    /**
     * 走的步数
     */
    @Synchronized
    fun setStepValue(value: Int) {
        stepValue = value
        invalidate()
    }

    /**
     * 设置总步数，通过 maxValue 和 sweepAngle ，来计算 stepValue 需要滑过的角度。
     */
    fun setStepMaxValue(maxValue: Float) {
        stepMaxValue = maxValue
    }

    fun setStepTextColor(color: Int) {
        stepTextColor = color
    }

    fun setStepTextSize(size: Float) {
        stepTextSize = size
    }


}