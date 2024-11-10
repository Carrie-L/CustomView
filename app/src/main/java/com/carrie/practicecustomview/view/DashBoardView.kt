package com.carrie.practicecustomview.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathMeasure
import android.view.View
import com.carrie.practicecustomview.R
import com.carrie.practicecustomview.px
import kotlin.math.cos
import kotlin.math.sin

val RADIUS = 150f.px // 指定半径
private const val OPEN_ANGLE = 120f // 指定开口角度
private val DASH_WIDTH = 2f.px // 指定刻度长
private val DASH_LENGTH = 10f.px // 指定刻度宽
private const val COUNT = 20  // 指定多少个刻度间隔
private val POINT_LENGTH = 120f.px


class DashBoardView(context: Context) :
    View(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val dashPath = Path()
    private lateinit var dashPathEffect: PathDashPathEffect
    private val pathMeasure = PathMeasure()

    private val START_ANGLE = -180 - (180 - OPEN_ANGLE) / 2    // 也可以是 [90+OPEN_ANGLE]
    private val SWEEP_ANGLE = 360 - OPEN_ANGLE
    private var indicator = 10 // 自定义要指向第几个刻度。 从0开始
    private var theta = 0f // 三角函数角度，即 指针终点坐标计算时，指针与x轴 组成的角度

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        theta = Math.toRadians((START_ANGLE + indicator * SWEEP_ANGLE / COUNT).toDouble()).toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        path.reset()
        path.addArc(width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS, START_ANGLE, SWEEP_ANGLE)
        // 计算弧长
        pathMeasure.setPath(path, false)
        // 间隔。 去掉一个刻度间隔，再除
        val advance = (pathMeasure.length - DASH_WIDTH) / COUNT /* 21个刻度，20个刻度间隔 */;
        dashPath.addRect(0f, 0f, DASH_WIDTH, DASH_LENGTH, Path.Direction.CW);

        dashPathEffect = PathDashPathEffect(dashPath, advance, 0f, PathDashPathEffect.Style.ROTATE)
    }

    override fun onDraw(canvas: Canvas) {
        // 先画弧
        canvas.drawPath(path, paint)

        // 再用刻度效果画一次弧
        paint.pathEffect = dashPathEffect
        canvas.drawPath(path, paint)
        paint.pathEffect = null

        paint.color = resources.getColor(R.color.pink, null)
        // cos sin 要将 角度转弧度
        canvas.drawLine(width / 2f, height / 2f, width / 2f + (POINT_LENGTH * cos(theta)), height / 2f + (POINT_LENGTH * sin(theta)), paint)
    }


}