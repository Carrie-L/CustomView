package com.carrie.practicecustomview.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build.VERSION_CODES.M
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 实现 [小球跟手滑动]
 * DragView 使用 event.y
 * DragRawView 使用 event.rawY
 * DragAnimatorView 使用 ValueAnimator
 */
class DragAnimatorView(context: Context?, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var ballX = 100F
    private var ballY = 100F
    private var ballRadius = 30F
    private var downX = 0F // 手指按下时的 X 坐标
    private var downY = 0F // 手指按下时的 Y 坐标
    private var lastBallX = 0f  // 按下时小球的 X 坐标
    private var lastBallY = 0f  // 按下时小球的 Y 坐标
    private var isDraggable = false
    private var animator: ValueAnimator? = null

    init {
        paint.isAntiAlias = true
        paint.color = Color.RED
        animator = ValueAnimator.ofInt(0, 1).apply {
            duration = 1000
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(ballX, ballY, ballRadius, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                // 因为自定义View显示在主界面按钮下方，小球实际坐标在 ballY 位置，如果传入 event.rawY，会导致判断不起作用。
                isDraggable = isTouchInsideBall(event.x, event.y)

                // 记录按下时的屏幕坐标
                downX = event.rawX
                downY = event.rawY

                // 记录小球的初始位置
                lastBallX = ballX
                lastBallY = ballY

                println("DragView: $isDraggable")
                println(
                    "DragView: x = ${event.x} , y = ${event.y} , rawX = ${event.rawX} , rawY = ${event.rawY}, ballX = $ballX, ballY = $ballY")
            }

            MotionEvent.ACTION_MOVE -> {
                if (isDraggable) {
                    // 计算手指的位移（相对于按下时）
                    val dx = event.rawX - downX
                    val dy = event.rawY - downY

                    // 更新小球的位置
                    animator?.addUpdateListener { animation ->
                        val fraction = animation.animatedFraction
                        ballX = (lastBallX + dx * fraction).coerceIn(ballRadius, width - ballRadius)
                        ballY = (lastBallY + dy * fraction).coerceIn(ballRadius, height - ballRadius)
                    }

                }
            }

            MotionEvent.ACTION_UP -> {
                isDraggable = false
            }
        }
        return true
    }

    private fun isTouchInsideBall(touchX: Float, touchY: Float): Boolean {
        val dx = touchX - ballX
        val dy = touchY - ballY
        return dx * dx + dy * dy <= ballRadius * ballRadius
    }


}