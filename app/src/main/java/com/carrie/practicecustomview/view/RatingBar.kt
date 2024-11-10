package com.carrie.practicecustomview.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import com.carrie.practicecustomview.R
import com.carrie.practicecustomview.dp
import com.carrie.practicecustomview.px
import kotlin.math.min

class RatingBar(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var normalResId: Int = 0
    private var selectedResId: Int = 0
    private var selectedDrawable: Int = 0
    private var starCount = 5  // 星星数量
    private var rating = 2f  // 评分等级
    private var starSize = 24f.px
    private var starPadding = 6f.dp
    private var starWidth = 0f
    private var starHeight = 0f
    private var totalStarWidth = 0f
    private val path = Path()
    private var normalBitmap: Bitmap? = null
    private var selectedBitmap: Bitmap? = null
    private val normalPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val selectedPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    init {
        context.obtainStyledAttributes(attrs, R.styleable.RatingBar).apply {
            normalResId = getResourceId(R.styleable.RatingBar_normalIcon, R.drawable.ic_star)
            selectedResId = getResourceId(R.styleable.RatingBar_selectedIcon, R.drawable.ic_selected_star)
            selectedDrawable = getResourceId(R.styleable.RatingBar_normalIcon, 0)
            starCount = getInt(R.styleable.RatingBar_starCount, starCount)
            rating = getFloat(R.styleable.RatingBar_rate, rating)
            starSize = getDimension(R.styleable.RatingBar_starSize, starSize)
            starPadding = getDimension(R.styleable.RatingBar_starPadding, starPadding)
            recycle()
        }

        initBitmaps()
    }

    private fun initBitmaps() {
        // 如果重新加载，则回收图片资源
        normalBitmap?.recycle()
        selectedBitmap?.recycle()
        // 将资源 ID 转换为 Bitmap
        normalBitmap = loadAndScaleBitmap(normalResId)
        selectedBitmap = loadAndScaleBitmap(selectedResId)
        // 获取图片宽高
        starWidth = normalBitmap?.width?.plus(starPadding) ?: starSize
        starHeight = normalBitmap?.height?.plus(starPadding) ?: starSize
        println("starWidth=$starWidth , starSize=$starSize, starPadding=$starPadding")
    }

    /**
     * @param EXACTLY
     * 1. 在XML中指定具体数值，如：android:layout_width= [200dp]
     * 2. 使用 [match_parent]
     * 这时必须使用给定的 widthSize
     * @param AT_MOST
     * 1. 在XML中使用 [wrap_content] ,
     * 这时可以选择任意小于等于 widthSize 的值 min() 确保不超过父容器给的最大值
     * @param UNSPECIFIED
     * 1. ScrollView 等滚动容器中
     * 2. AdapterView 测量子项时
     * 这时可以使用任意大小
     *
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
//        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
//        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
//        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val (widthSize, widthMode) = widthMeasureSpec.run {
            MeasureSpec.getSize(this) to MeasureSpec.getMode(this)
        }
        val (heightSize, heightMode) = heightMeasureSpec.run {
            MeasureSpec.getSize(this) to MeasureSpec.getMode(this)
        }

        // 1. 计算期望的自定义View尺寸
        val desiredWidth = (starSize + starPadding) * starCount
        val desiredHeight = starHeight + starPadding * 2

        // 2. 根据不同模式决定最终尺寸
        val viewWidth = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize.toFloat())
            else -> desiredWidth
        }

        val viewHeight = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize.toFloat())
            else -> desiredHeight
        }
        setMeasuredDimension(viewWidth.toInt(), viewHeight.toInt())

        println("widthMode=$widthMode")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        totalStarWidth = (starWidth + starPadding) * starCount
        println("totalStar width=$totalStarWidth")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (i in 0 until starCount) {
            if (i + 1 <= rating) {
                canvas.drawBitmap(selectedBitmap!!, i * starWidth + starPadding, starPadding, selectedPaint)
            } else {
                canvas.drawBitmap(normalBitmap!!, i * starWidth + starPadding, starPadding, normalPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val starWidth = normalBitmap?.width?.plus(starPadding)!!
                rating = (x / starWidth + 1).coerceIn(0f, starCount.toFloat())
                println("rating = $rating, starWidth=$starWidth")
                setRating(rating)
            }
        }

        return true
    }

    fun setRating(rating: Float) {
        this.rating = rating.coerceIn(0f, starCount.toFloat())
        invalidate()
    }

    private fun loadAndScaleBitmap(@DrawableRes resId: Int): Bitmap {
        try {
            // 第一次解码，只获取尺寸信息
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true  // 只解码尺寸，不加载像素
            }
            BitmapFactory.decodeResource(resources, resId, options)
            // 计算采样率
            val sampleSize = calculateInSampleSize(options, starSize.toInt(), starSize.toInt())
            println("starWidth sampleSize=$sampleSize")
            // 第二次解码，使用采样率加载图片
            options.apply {
                inJustDecodeBounds = false // 这次要加载像素了
                inSampleSize = sampleSize // 设置采样率
            }
            // 加载采样后的图片
            val sampledBitmap = BitmapFactory.decodeResource(resources, resId, options)
            // 如果还需要精确缩放
            return if (sampledBitmap.width != starSize.toInt()) {
                Bitmap.createScaledBitmap(sampledBitmap, starSize.toInt(), starSize.toInt(), true).also {
                    if (it != sampledBitmap) sampledBitmap.recycle()
                }
            } else sampledBitmap
//                ?: throw IllegalStateException("Failed to decode bitmap")
        } catch (e: Exception) {
            Log.e("RatingStarView", "Error loading bitmap", e)
            // 返回一个默认的小尺寸bitmap或抛出异常
            throw IllegalStateException("Could not load star bitmap", e)
        }
    }

    /**
     * 计算采样率
     * @param reqWidth 需要的尺寸
     */
    private fun calculateInSampleSize(options: Options, reqWidth: Int, reqHeight: Int): Int {
        // 1. 获取原始图片的高度和宽度
        val originWidth = options.outWidth
        val originHeight = options.outHeight
        var inSampleSize = 1
        println("originWidth=$originWidth , starSize=$starSize")
        // 2. 只有当原始尺寸大于需要的尺寸时才进行压缩
        if (originWidth > reqWidth || originHeight > reqHeight) {
            val halfHeight = originHeight / 2
            val halfWidth = originWidth / 2

            // 3. 循环计算采样率
            // 不断将采样率乘以2，直到采样后的尺寸小于等于需要的尺寸
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        normalBitmap?.recycle()
        selectedBitmap?.recycle()
    }


}