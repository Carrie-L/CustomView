package com.carrie.practicecustomview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView2 extends ListView {
    public MyListView2(Context context) {
        super(context);
    }

    public MyListView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyListView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxValue = Integer.MAX_VALUE;
        int size1 = Integer.MAX_VALUE >> 1;
        int size2 = Integer.MAX_VALUE >> 2;

        System.out.println("onMeasure maxValue = " + maxValue); // 2147483647
        System.out.println("onMeasure size1 = " + size1); // 1073741823
        System.out.println("onMeasure size2 = " + size2); // 536870911


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
