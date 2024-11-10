package com.carrie.practicecustomview.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Build.VERSION_CODES.M
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.LinearLayout
import android.widget.Scroller
import androidx.core.view.marginLeft
import com.carrie.practicecustomview.R
import com.carrie.practicecustomview.dp
import kotlin.math.abs
import kotlin.math.min


class MyTextView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private var width = 0
    private val paint = Paint()
    private var textColor: Int = Color.BLACK
    private var text: String = "Hello, Carrie!"
    private var textSize: Float = 14F
    private var textWidth = 0f
    private var textHeight = 0f
    private val bounds = Rect()
    private var velocityTracker: VelocityTracker? = null
    private final val FLING_THRESHOLD = 1500

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MyTextView)
            textColor = typedArray.getColor(R.styleable.MyTextView_cTextColor, Color.BLACK)
            textSize = typedArray.getDimension(R.styleable.MyTextView_cTextSize, 14F)
            text = typedArray.getString(R.styleable.MyTextView_cText) ?: text
            val backgroundResource = typedArray.getResourceId(R.styleable.MyTextView_cBackground, 0)

            if (backgroundResource != 0) {
                setBackgroundResource(backgroundResource)
            }


            typedArray.recycle()
        }

        paint.color = textColor
        paint.textSize = textSize
        paint.isAntiAlias = true

        setWillNotDraw(false)
    }

    fun setText(text: String) {
        this.text = text
        invalidate()
    }

    fun setTextSize(size: Float) {
        this.textSize = size
        paint.textSize = textSize
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 1. 获取父布局传递的宽高模式和尺寸
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)  // EXACTLY
        var widthSize = MeasureSpec.getSize(widthMeasureSpec) // 1080
        var heightMode = MeasureSpec.getMode(heightMeasureSpec) // 先是AT_MOST, 2209 ； 然后是 EXACTLY, 1159
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        Log.d("MyTextView", "widthSize: $widthSize， heightSize=$heightSize")

        // 2. 根据文本边界获取文本的宽高
        paint.getTextBounds(text, 0, text.length, bounds)
        val textWidth = paint.measureText(text).toInt()
        val fontMetrics = paint.fontMetrics
        val textHeight = (fontMetrics.bottom - fontMetrics.top).toInt()
//        val textWidth = bounds.width()
//        val textHeight = bounds.height()
//        val textTop = bounds.top
//        val textBottom = bounds.bottom
//        // 199 , 31, -24, 7
//        Log.d("MyTextView", "textWidth: $textWidth， textHeight=$textHeight ,textTop=$textTop, textBottom=$textBottom")


        // 3. 修改为AT_MOST
        var modifiedWidthSize = widthSize
        var modifiedHeightSize = heightSize

        if (widthMode == MeasureSpec.EXACTLY) {
            widthMode = MeasureSpec.AT_MOST
            modifiedWidthSize = minOf(textWidth + paddingLeft + paddingRight, widthSize)
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            heightMode = MeasureSpec.AT_MOST
            modifiedHeightSize = minOf(textHeight + paddingTop + paddingBottom, heightSize)
        }
        Log.d("MyTextView", "modifiedWidthSize: $modifiedWidthSize， modifiedHeightSize=$modifiedHeightSize")

        // 4. 使用修改后的MeasureSpec进行测量
        val finalWidth = MeasureSpec.makeMeasureSpec(modifiedWidthSize, widthMode)
        val finalHeight = MeasureSpec.makeMeasureSpec(modifiedHeightSize, heightMode)
        Log.d("MyTextView", "finalWidth: $finalWidth， finalHeight=$finalHeight")
        setMeasuredDimension(finalWidth, finalHeight)
        Log.d("MyTextView", "-onMeasure修改完成-")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 获取视图的宽高
        val viewWidth = width
        val viewHeight = height

        textWidth = paint.measureText(text)
        val fontMetrics = paint.fontMetrics
        val bottom = fontMetrics.bottom
        val top = fontMetrics.top
        val ascent = fontMetrics.ascent
        val descent = fontMetrics.descent
        val leading = fontMetrics.leading
        println("MyTextView: textWidth=$textWidth, viewWidth=$viewWidth, viewHeight=$viewHeight， height=$height")
        println("MyTextView: bottom=$bottom, top=$top, ascent=$ascent, descent=$descent, leading=$leading")

        textHeight = bottom - top
        val baseline = (height - textHeight) / 2 - top
        val y = (height / 2) - (paint.descent() + paint.ascent()) / 2
        Log.d("MyTextView:onDraw::", "textHeight: $textHeight， baseline=$baseline, y=$y")

        canvas.drawText(text, 0f, y, paint)

        // 将 MyTextView 设置为居中显示，无论 MyTextView 在屏幕的哪个位置，它的文本的坐标起始点都是那个文本Rect的左上角
        // 因为我们在OnMeasure()里重新绘制了MyTextView的宽高，所以它只会是文本宽高大小
        // 无论在代码中用LayoutParams怎么移动MyTextView，它的drawText起始点都是 0f, baseline。

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                velocityTracker = VelocityTracker.obtain()
                scrollTo(textWidth.toInt(),0)
            }
            MotionEvent.ACTION_MOVE -> velocityTracker?.addMovement(event)
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 计算速读：1000ms内手指所滑过的像素数
                velocityTracker?.apply {
                    computeCurrentVelocity(1000)
                    val xVelocity = xVelocity
                    val yVelocity = yVelocity
                    println("xVelocity=$xVelocity, yVelocity=$yVelocity")

                    if (abs(xVelocity) > FLING_THRESHOLD && abs(xVelocity) > abs(yVelocity)) {
                        if (xVelocity > 0) {
                            println("Velocity：用户快速向👉滑动")
//                            smoothScrollTo(textWidth.toInt(), 0)
                        } else {
                            scrollTo(textWidth.toInt()-100,0)
                            println("Velocity：用户快速向👈滑动")
                        }
                    } else if (abs(yVelocity) > FLING_THRESHOLD && abs(yVelocity) > abs(xVelocity)) {
                        if (yVelocity > 0) {
                            println("Velocity：用户快速向👇滑动")
                            val distanceY = yVelocity / 20
                            translationY += distanceY
                        } else {
                            println("Velocity：用户快速向👆滑动")
                            val distanceY = yVelocity / 20
                            translationY -= abs(distanceY)
                        }
                    }
                    // 回收 VelocityTracker
                    recycle()
                }
                velocityTracker = null
            }

        }
        return true
    }

    private val scroller = Scroller(context)

    /**
     * 缓慢滚动到指定位置
     */
    private fun smoothScrollTo(destX: Int, destY: Int) {
        val scrollX = scrollX
        val scrollY = scrollY
        val deltaX = destX - scrollX
        val deltaY = destY - scrollY
        // 1000ms内滑向destX, 效果就是慢慢滑动
        scroller.startScroll(scrollX, scrollY, deltaX, deltaY, 3000)
        while (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)


    }
}