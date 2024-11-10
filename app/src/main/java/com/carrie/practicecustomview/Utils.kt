package com.carrie.practicecustomview

import android.content.res.Resources
import android.util.TypedValue

//fun dp2px(value: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().displayMetrics)

/**
 * dp 转 px
 * usage: [100.px]  即把 100dp 转成 px
 */
val Float.px1
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)