package com.carrie.practicecustomview.view

import android.util.Log
import android.view.View

open class MyView {
    // 公共标志位变量
    protected var mViewFlags: Int = 0

    // 私有标志位变量
    private var mPrivateFlags = 0

    // 标志位常量
    companion object {
        const val WILL_NOT_DRAW: Int = 0x00000080
        const val DRAW_MASK: Int = 0x00000080
        private const val PFLAG_SKIP_DRAW: Int = 0x00000080
    }

    /**
     * 设置标志位的方法
     *  xor 按位异或
     *  inv 按位取反
     */
    protected fun setFlags(flags: Int, mask: Int) {
        val oldFlags = mViewFlags
        mViewFlags = (mViewFlags and mask.inv()) or (flags and mask)
        Log.d("MyView", "mViewFlags: ${mViewFlags.javaClass.declaredFields}")

        val changed = mViewFlags xor oldFlags
        Log.d("MyView", "setFlags: oldFlags=$oldFlags, mViewFlags=$mViewFlags, changed=${changed} ,  ")

        if (changed and WILL_NOT_DRAW != 0) {
            if (mViewFlags and WILL_NOT_DRAW != 0) {
                // 设置私有标志位
                mPrivateFlags = mPrivateFlags or PFLAG_SKIP_DRAW
            } else {
                // 清除私有标志位
                mPrivateFlags = mPrivateFlags and PFLAG_SKIP_DRAW.inv()
            }
        }
    }

    // 绘制方法
    open fun draw() {
        if (mPrivateFlags and PFLAG_SKIP_DRAW != 0) {
            // 跳过绘制
            Log.d("MyView", "draw: 跳过绘制 ")
            return
        }
        // 执行绘制逻辑
        Log.d("MyView", "draw: 执行绘制 ")
    }

    // 对外提供的方法，用于设置是否绘制
    fun setWillNotDraw(willNotDraw: Boolean) {
        setFlags(if (willNotDraw) WILL_NOT_DRAW else 0, DRAW_MASK)
    }

    fun findMatchingConstant(value:Int){
        
    }


}
