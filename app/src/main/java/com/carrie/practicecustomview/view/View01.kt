package com.carrie.practicecustomview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import com.carrie.practicecustomview.px




class View01(context: Context) :
    View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val RADIUS = 100f.px

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
    }

    /**
     * path 初始化
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        path.reset()

        evenOddExample()
//        windingContraryDirectionExample()
//        windingSameDirectionExample()

    }

    /**
     * 与 CW CCW 方向无关
     * 直线穿过图形   奇数 填充； 偶数 不填充
     */
    private fun evenOddExample() {
        path.addCircle(200f, 200f, 100f, Path.Direction.CW)
        path.addCircle(300f, 200f, 100f, Path.Direction.CCW)
        path.fillType = Path.FillType.EVEN_ODD
    }

    /**
     * 环绕数相加为偶数，点在内部，填充
     */
    private fun windingSameDirectionExample() {
        path.addCircle(200f, 200f, 100f, Path.Direction.CW)
        path.addCircle(300f, 200f, 100f, Path.Direction.CW)
        path.fillType = Path.FillType.WINDING
    }

    private fun windingContraryDirectionExample() {
        path.addCircle(200f, 200f, 100f, Path.Direction.CW)
        path.addCircle(300f, 200f, 100f, Path.Direction.CCW)
//        path.addCircle(400f, 200f, 100f, Path.Direction.CW)
//        path.addCircle(300f, 300f, 100f, Path.Direction.CW)
        path.addCircle(250f, 300f, 100f, Path.Direction.CW)
        path.fillType = Path.FillType.WINDING
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)


    }

}