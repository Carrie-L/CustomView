package com.carrie.practicecustomview.view

import android.util.Log

class MyCustomView: MyView() {

    init {
        setWillNotDraw(true)
    }

    override fun draw() {
        super.draw()

        if(mViewFlags and WILL_NOT_DRAW == 0 ){
            Log.d("MyCustomView", "draw: 跳过绘制")
        }

        setFlags(WILL_NOT_DRAW, DRAW_MASK)


    }

}