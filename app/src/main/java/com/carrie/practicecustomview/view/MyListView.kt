package com.carrie.practicecustomview.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView
import android.widget.ScrollView

class MyListView(context: Context?, attrs: AttributeSet?) : ListView(context, attrs) {





    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)  // 0 UNSPECIFIED
        println("onMeasure: heightMode=$heightMode")
        // DragView 传递给 MyListView 的高度模式为 UNSPECIFIED ， 所以会执行这段代码
        // if (heightMode == MeasureSpec.UNSPECIFIED) {
        //            heightSize = mListPadding.top + mListPadding.bottom + childHeight +
        //                    getVerticalFadingEdgeLength() * 2;
        //        }
        // 在这段代码里，childHeight 也就是ListView的高度，它只有一个item高，所以显示不全.
        // 重写onMeasure并传递 AT_MOST 过去后，会测量子View的高度

        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(Int.MAX_VALUE shr 2 , MeasureSpec.AT_MOST)
        println("onMeasure: Int.MAX_VALUE=${Int.MAX_VALUE.toInt()}") // 2147483647
        println("onMeasure: Int.MAX_VALUE >> 2 =${Int.MAX_VALUE shr 1}") // 1073741823
        println("onMeasure: Int.MAX_VALUE >> 1 =${Int.MAX_VALUE shr 0}") // 2147483647
        println("onMeasure: 8 >> 1 =${8 shr 1}") //
        println("onMeasure: 8 >> 2 =${8 shr 2}") //
        println("onMeasure: 8 >> 3 =${8 shr 3}") //



        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}