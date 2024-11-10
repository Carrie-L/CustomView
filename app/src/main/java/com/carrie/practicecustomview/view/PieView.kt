package com.carrie.practicecustomview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.carrie.practicecustomview.R
import com.carrie.practicecustomview.px
import kotlin.math.cos
import kotlin.math.sin

class PieView(context: Context?) : View(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private var colors: IntArray = intArrayOf(R.color.yellow, R.color.purple100, R.color.pink, R.color.lime, R.color.red100)
    private var angles: FloatArray = floatArrayOf(60f, 60f, 90f, 105f, 45f)
    private var startAngle = 0f
    private var sweepAngle = 0f
    private var offset = 30f.px  // 自定义扇形推出的偏移量,也就是三角形斜边
    private var theta = 0f // 三角形角度, offset 与 x轴的夹角
    private var indicator = 2  // 自定义哪个扇形推出

    override fun onDraw(canvas: Canvas) {
        for ((index, item) in colors.withIndex()) {
            paint.color = resources.getColor(item, null)
            sweepAngle = angles[index]

            if (index == indicator) {
                canvas.save()
                theta = Math.toRadians((startAngle + sweepAngle / 2).toDouble()).toFloat()
                canvas.translate(offset * cos(theta), offset * sin(theta))
                canvas.drawArc(width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS,
                    startAngle, sweepAngle, true, paint)
            } else {
                canvas.drawArc(width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS,
                    startAngle, sweepAngle, true, paint)
            }

            Log.d("Carrie", "startAngle=${startAngle}, sweepAngle=$sweepAngle")
            startAngle += sweepAngle

            if (index == indicator) canvas.restore()
        }


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("MyView", "DOWN received - not consumed")
                return false // 仅消耗DOWN事件
            }

            MotionEvent.ACTION_MOVE -> {
                Log.d("MyView", "MOVE received -  consumed")
                return true
            }

            MotionEvent.ACTION_UP -> {
                Log.d("MyView", "UP received -  consumed")
                return true
            }
        }
        Log.d("MyView", "不会执行到这里，在 ACTION_UP 就返回了false")
        return super.onTouchEvent(event)
    }
}


