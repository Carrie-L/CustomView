package com.carrie.practicecustomview

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.TypedValue

/**
 * px 2 dp
 */
val Float.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

val Int.dp
    get() = this.toFloat().dp

/**
 * dp to px
 */
fun dp2px(dp: Float): Float {
    return dp * Resources.getSystem().displayMetrics.density
}

/**
 * dp to px
 */
val Float.px
    get() = this * Resources.getSystem().displayMetrics.density

fun getAvatar(res: Resources, targetWidth: Int): Bitmap {
    val options = BitmapFactory.Options()
    //此选项设置为 true 时，BitmapFactory 在解码图片时并不会真正加载图片到内存中，而是仅仅返回图片的宽度和高度。
    // 这通常用于获取图片的尺寸信息，而不实际加载图片。
    options.inJustDecodeBounds = true
    // 获取图片的尺寸：使用 decodeResource 方法解析资源文件，并将结果存储在 options 中。
    // 因为 inJustDecodeBounds 是 true，所以此时并不会生成 Bitmap 对象，
    // 而只是获取图片的原始宽度和高度，并存储在 options.outWidth 和 options.outHeight 中。
    BitmapFactory.decodeResource(res, R.drawable.avatar_girl, options)
    // 将 inJustDecodeBounds 设置为 false，接下来将会真正地解码图片并生成 Bitmap 对象。
    options.inJustDecodeBounds = false
    // 设置原始密度：将 options.inDensity 设置为图片的原始宽度（即 options.outWidth），表示原始图片的密度。
    options.inDensity = options.outWidth
    // 设置目标密度：将 options.inTargetDensity 设置为目标宽度 width，表示我们想要将图片缩放到的目标密度。
    // 通过设置 inDensity 和 inTargetDensity，可以在解码图片时直接将其缩放到指定的宽度。
    options.inTargetDensity = targetWidth
    // 解码图片并返回 Bitmap：使用 decodeResource 方法根据设置的 options 解码图片，并返回一个缩放后的 Bitmap 对象。
    // 此时，解码出的图片将被缩放到指定的 width 宽度。
    return BitmapFactory.decodeResource(res, R.drawable.avatar_girl, options)
}