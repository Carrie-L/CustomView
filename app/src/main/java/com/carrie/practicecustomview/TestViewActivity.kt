package com.carrie.practicecustomview

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.app.TaskStackBuilder

class TestViewActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.activity_main)



    }


}