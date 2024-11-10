package com.carrie.practicecustomview.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * 实现 [小球跟手滑动]
 * 用属性动画#ObjectAnimator实现惯性滑动，松手后回到原处
 */
class DragView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var isDraggable = false  // 如果触摸点在小球内，则可以拖动
    private var ballX = 100F
    private var ballY = 100F
    private var ballRadius = 30F

    private var lastX = 0F // 手指按下时的 X 坐标
    private var lastY = 0F // 手指按下时的 Y 坐标

    // 透明度 0-255
    private var alpha = 0
    private var animator: ValueAnimator? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        color = Color.RED
    }

    init {
        // 改变透明度，使其逐渐显示
        animator = ValueAnimator.ofInt(0, 255)
        animator?.apply {
            duration = 6000
            addUpdateListener { animation ->
                alpha = animation.animatedValue as Int
                invalidate()
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.alpha = alpha
        canvas.drawCircle(ballX, ballY, ballRadius, paint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                isDraggable = isTouchInsideBall(event.x, event.y)
                // 记录按下的位置
                lastX = event.x
                lastY = event.y
                println(
                    "DragView: x = ${event.x} , y = ${event.y} , rawX = ${event.rawX} , rawY = ${event.rawY}, ballX = $ballX, ballY = $ballY")
            }

            MotionEvent.ACTION_MOVE -> {
                if (isDraggable) {
                    // 手指移动时计算移动的距离
                    val dx = event.x - lastX
                    val dy = event.y - lastY

                    // 更新上一次手指位置
                    ballX = (dx + event.x).coerceIn(ballRadius, width - ballRadius)
                    ballY += dy
                    lastX = event.x
                    lastY = event.y
                    invalidate()

                    println(
                        "DragView: x = ${event.x} , y = ${event.y} , rawX = ${event.rawX} , rawY = ${event.rawY}, ballX = $ballX, ballY = $ballY")
                }
            }

            MotionEvent.ACTION_UP -> {
                // 松手后小球回到原处
                // 如果不需要回到原处，而是留在原地，则不需要这个动画
//                if (isDragging) {
//                    performFlingAnimation()
//                }
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

    private fun ballAlphaAnimation() {

    }

    /**
     * 使用属性动画实现惯性滑动，效果：松手后小球回到原处
     */
    private fun performFlingAnimation() {
        // 目标位置可以是回到初始位置，也可以是滑动到某个位置
        val targetX = 0f
        val targetY = 0f

        val animatorX = ObjectAnimator.ofFloat(this, "translationX", translationX, targetX)
        val animatorY = ObjectAnimator.ofFloat(this, "translationY", translationY, targetY)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animatorX, animatorY)
        animatorSet.duration = 500
        animatorSet.interpolator = DecelerateInterpolator() // 使用减速插值器
        animatorSet.start()
    }

    private fun startFlingAnimation(vX: Float, vY: Float) {
        animator = ValueAnimator.ofFloat(1f, 0f).apply {
            duration = 500
            addUpdateListener { animation ->
                val fraction = animation.animatedFraction
                // 使用初始速度模拟惯性滑动
                // 1-fraction : 让滑动的速度随着时间逐渐减小。
                // 0.1f ： vX 缩放因子。用于调整速度的大小。如果没有这个缩放，视图滑动的距离会过大。
                x += vX * (1 - fraction) * 0.1f
                y += vY * (1 - fraction) * 0.1f
            }
            start()
        }
    }


}