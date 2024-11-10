package com.carrie.practicecustomview.view

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.carrie.practicecustomview.dp
import com.carrie.practicecustomview.getAvatar
import kotlin.math.max
import kotlin.math.min

private val bitmapWidth = 300.dp.toInt()
private const val EXTRA_SCALE_FRACTION = 1.5f  // 把largeScale再放大到可以左右移动的大小
private const val TAG = "ScalableImageView"

class ScalableImageView(context: Context) : View(context), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private var originOffsetX = 0f
    private var originOffsetY = 0f
    private var offsetX = 0f  // 滑动事件 偏移后的坐标X
    private var offsetY = 0f // 滑动后的坐标Y
    private var smallScale = 0f  // 放大，放大图片到宽度与View对齐
    private var largeScale = 0f  // 放大，放大图片到高度与View对其
    private var isBig = false // 是否在放大状态

    // 添加属性动画，让其缓慢缩放
    private var scaleFraction: Float = 0f  // 缩放比例，设置属性动画，让其缓慢缩放
        set(value) {
            field = value
            invalidate()
        }
    private val scaleAnimator :ObjectAnimator by lazy {
        ObjectAnimator.ofFloat(this, "scaleFraction", 0f, 1f)
    }

    private val gestureDetector = GestureDetectorCompat(context, this)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val bitmap = getAvatar(resources, bitmapWidth)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        originOffsetX = (width - bitmapWidth) / 2f
        originOffsetY = (height - bitmap.height) / 2f

        // 本质都是放大，当 bitmapWidth 比 width 更宽，说明图片左端更接近view，需要放大的部分就少，因此是smallScale
        // 而height离view顶端更远，需要放大更多，因此是largeScale
        // 当当 bitmapWidth 比 width 小，则放大倍数反过来
        // scale大于1 放大，scale <1 ，缩小
        if (bitmapWidth / bitmap.height.toFloat() > width / height.toFloat()) { // bm 更宽
            smallScale = width / bitmapWidth.toFloat()
            largeScale = height / bitmap.height.toFloat() * EXTRA_SCALE_FRACTION
        } else {
            smallScale = height / bitmap.height.toFloat()
            largeScale = width / bitmapWidth.toFloat() * EXTRA_SCALE_FRACTION
        }


        Log.i(TAG, "onSizeChanged: smallScale=$smallScale")
        Log.i(TAG, "onSizeChanged: largeScale=$largeScale")
        Log.i(TAG, "onSizeChanged: bitmapWidth=${bitmap.width}, bitmap_height=${bitmap.height}")
        Log.i(TAG, "onSizeChanged: width=${width}, height=${height}")


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 反过来理解，从 drawBitmap 由下往上看
        canvas.translate(offsetX, offsetY)
//        val scale = if (isBig) largeScale else smallScale  // 添加属性动画后，这句不需要了
        val scale = smallScale + (largeScale - smallScale) * scaleFraction
        canvas.scale(scale, scale, width / 2f, height / 2f)
//        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
        canvas.drawBitmap(bitmap, originOffsetX , originOffsetY , paint)
    }

    /**
     * 双击， 把多点触摸监听器外挂到onTouchEvent上
     * OnGestureListener 内含有 setOnDoubleTapListener，所以这里不需要
     * .apply {
     *             gestureDetector.setOnDoubleTapListener(this@ScalableImageView)
     *         }
     *         这段代码也能运行. 但是上面仍然需要继承 OnDoubleTapListener
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    /**
     * action_down 发生时，onDown 就返回。
     * 看 onTouchEvent 要不要消费这个事件，假如返回false，那么 gestureDetector.onTouchEvent(event) 也返回false,
     * onTouchEvent 就不再获取新的事件。
     * 不管任何事件，只要想拿到，这里就得返回 true
     */
    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(e: MotionEvent) {
    }

    /**
     * 点击。不支持双击时，它比较准确
     * 返回值：是否消费了点击事件。只有 onDown的返回值有影响，这里返回true还是false 无影响。
     */
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return true
    }

    /**
     * 手指头移动时被触发
     *  @Params:
     *      - distanceX  上次调用 onScroll 后沿 X 轴滚动的距离。上一个位置的X - 这次位置的X
     */
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        if(isBig){
            // 上一次调用的X坐标为offsetX，这一次调用的X坐标为X，中间X轴上移动的距离为 distanceX，
            // 即：offsetX - X = distanceX ==> offsetX - distanceX = X .
            // 再把 X 赋值给新的 offsetX，即有如下：
            offsetX -= distanceX
            // 防止放大后的图片无限移动出边界
            // offsetX 最大不能超过左边界 ，最小不能小于右边界
            Log.e(TAG, "onScroll: offsetX=$offsetX" )
            offsetX = min(offsetX, (bitmap.width * largeScale - width)/2)  // 两者比较取小,小于边界，正数
            offsetX = max(offsetX, -(bitmap.width * largeScale - width)/2) // 两者比较取大,大于边界，复数

            offsetY -= distanceY // Y 与 X 同理
            offsetY = min(offsetY, (bitmap.height * largeScale - height)/2)
            offsetY = max(offsetY, -(bitmap.height * largeScale - height)/2)
            invalidate()

            Log.i(TAG, "onScroll: offsetX2=$offsetX , min1=${(bitmap.width * largeScale - width)/2f}, max1=${-(bitmap.width * largeScale - width)/2f}" )
        }


        return false
    }

    override fun onLongPress(e: MotionEvent) {
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    /**
     * 双击里的单击方法。当支持双击时，它比较准确，当不支持双击时，onSingleTapUp比较准确，因为onSingleTapConfirmed有确认延时
     */
    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return false
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        isBig = !isBig
        if(isBig){
            scaleAnimator.start() // 如果要放大，从小到达
        }else{
            scaleAnimator.reverse() // 如果要缩小，动画值从大到小
        }
//        invalidate()  添加 scaleAnimator.start() 后，这一句代码就不需要了
        return true
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        return false
    }
}

