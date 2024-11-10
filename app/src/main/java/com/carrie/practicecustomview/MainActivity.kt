package com.carrie.practicecustomview

import CircleDashView
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.TaskStackBuilder
import androidx.viewpager2.widget.ViewPager2
import com.carrie.practicecustomview.view.DashBoardView
import com.carrie.practicecustomview.view.MyCustomView
import com.carrie.practicecustomview.view.MyTextView
import com.carrie.practicecustomview.view.PieView
import com.carrie.practicecustomview.view.ScalableImageView
import com.carrie.practicecustomview.view.DragView
import com.carrie.practicecustomview.view.IconChangeAnimateView
import com.carrie.practicecustomview.view.LetterOverlayView
import com.carrie.practicecustomview.view.ProgressCircleAnimate
import com.carrie.practicecustomview.view.QQStepAnimate
import com.carrie.practicecustomview.view.RatingBar
import com.carrie.practicecustomview.view.SideBarLetter
import com.carrie.practicecustomview.view.TextChangeView
import com.carrie.practicecustomview.view.View01
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var frameLayout: FrameLayout
    private lateinit var tvPath: TextView
    private lateinit var tvDashBoard: TextView
    private lateinit var tvPie: TextView
    private lateinit var tvScalable: TextView
    private lateinit var tvScrollList: TextView
    private lateinit var tvTextView: TextView
    private lateinit var tvScrollView: TextView
    private lateinit var qqStepAnimate: QQStepAnimate
    private var textChangeView: TextChangeView? = null
    private lateinit var tabLayout: TabLayout
    private lateinit var progressCircleAnimate: ProgressCircleAnimate
    private lateinit var iconChangeAnimateView: IconChangeAnimateView
    private val list = listOf("摘星", "云雀", "暗影", "星辰", "风起")
    private var progressCircleValue = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskBuilder = TaskStackBuilder.create(this)
        taskBuilder.addParentStack(this)
//        taskBuilder.addNextIntent()


        frameLayout = this.findViewById(R.id.frame_view)
        tvPath = this.findViewById(R.id.tv_path)
        tvDashBoard = this.findViewById(R.id.tv_dash_board)
        tvPie = this.findViewById(R.id.tv_pie)
        tvScalable = this.findViewById(R.id.tv_scalable)
        tvScrollList = this.findViewById(R.id.tv_scroll_listview)
        tvTextView = this.findViewById(R.id.tv_text_view)
        tvScrollView = this.findViewById(R.id.tv_scroll_view)


        val view01 = View01(this)
        val dashBoardView = DashBoardView(this)
        val circleDashView = CircleDashView(this)
        val pieView = PieView(this)
        val scalableView = ScalableImageView(this)
        val textView = MyTextView(this, null).apply {
            setText("My favorite food is strawberry")
            setTextSize(64f)
//            setBackgroundColor(Color.GRAY)
            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            params.gravity = Gravity.CENTER
            layoutParams = params  // 设置 MyTextView 居中
        }
        val customView = MyCustomView()
        val scrollView = DragView(this)



        tvPath.setOnClickListener {
            frameLayout.removeAllViews()
            frameLayout.addView(view01)
        }
        tvDashBoard.setOnClickListener {
            frameLayout.removeAllViews()
            frameLayout.addView(dashBoardView)
        }
        tvPie.setOnClickListener {
            frameLayout.removeAllViews()
            frameLayout.addView(pieView)
        }

        tvScalable.setOnClickListener {
            frameLayout.removeAllViews()
            frameLayout.addView(scalableView)
        }

        tvTextView.setOnClickListener {
            frameLayout.removeAllViews()
            frameLayout.addView(textView)
        }

        tvScrollList.setOnClickListener {
            val intent = Intent(this, ScrollingActivity::class.java)
            this.startActivity(intent)
        }

        tvScrollView.setOnClickListener {
            frameLayout.removeAllViews()
            frameLayout.addView(scrollView)
        }

        qqStepAnimate = QQStepAnimate(this).apply {
            var stepValue = 2000
            setBorderWidth(20f.dp)
            setStepValue(stepValue)
            setStepTextColor(Color.parseColor("#c26493"))
            setStepTextSize(150f)
            setStepMaxValue(4000f)
        }
        this.findViewById<TextView?>(R.id.tv_qq_step_view).setOnClickListener {
            frameLayout.removeAllViews()
            frameLayout.addView(qqStepAnimate)
            setQQStepAnimator()
        }

        this.findViewById<TextView?>(R.id.tv_text_change).setOnClickListener {
            frameLayout.removeAllViews()
            setTextChangeViewPager()
        }


        this.findViewById<TextView>(R.id.tv_progress_circle_animate).setOnClickListener {
            frameLayout.removeAllViews()
            progressCircleAnimate = ProgressCircleAnimate(this)
            frameLayout.addView(progressCircleAnimate)
            setProgressCircleAnimator()
        }

        iconChangeAnimateView = IconChangeAnimateView(this).apply {
            val colorArray = resources.obtainTypedArray(R.array.icon_change_colors)
            val colors = List<Int>(colorArray.length()) { i ->
                colorArray.getColor(i, Color.BLACK)
            }
            colorArray.recycle()
            setColors(colors)
        }
        this.findViewById<TextView>(R.id.tv_icon_change_animate).setOnClickListener {
            frameLayout.removeAllViews()
            frameLayout.addView(iconChangeAnimateView)
            setIconChangeAnimator()
        }

        this.findViewById<TextView>(R.id.tv_rating_bar).setOnClickListener {
            frameLayout.removeAllViews()
            val ratingBar = RatingBar(this)
            frameLayout.addView(ratingBar)
        }

        val letterOverlay = findViewById<LetterOverlayView>(R.id.letterOverlay)
        this.findViewById<TextView>(R.id.tv_side_letter).setOnClickListener {
//            frameLayout.removeAllViews()



//            val textView = TextView(this@MainActivity)
//            val params = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
//            params.gravity = Gravity.CENTER
//            textView.layoutParams = params
//            frameLayout.addView(textView)

            frameLayout.addView(SideBarLetter(this).apply {
                onLetterSelectedListener = { letter ->
//                    Toast.makeText(this@MainActivity, letter, Toast.LENGTH_SHORT).show()
                    letterOverlay.show(letter)
                }
            })
        }


    }

    /**
     * QQ步数进度条
     */
    private fun setQQStepAnimator() {
        ValueAnimator.ofInt(0, 2000).apply {
            duration = 1500
            addUpdateListener {
                val value = it.animatedValue as Int
                qqStepAnimate.setStepValue(value)
                interpolator = DecelerateInterpolator()
            }
            start()
        }
    }

    /**
     * Tab文字滑动变色
     */
    private fun setTextChangeViewPager() {
        val tabViewPagerLayout = LayoutInflater.from(this).inflate(R.layout.view_pager_text_change, frameLayout, false)
        tabViewPagerLayout.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        frameLayout.addView(tabViewPagerLayout)
        val viewPager = tabViewPagerLayout.findViewById<ViewPager2>(R.id.view_pager_text_change)
        tabLayout = tabViewPagerLayout.findViewById<TabLayout>(R.id.tab_text_change)
        viewPager.isUserInputEnabled = true

        val adapter = TextViewPagerAdapter(this, list)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            // 创建自定义视图
            textChangeView = TextChangeView(this).apply {
                setTabText(list[pos])
                textSize = 20f
            }
            tab.setCustomView(textChangeView)
        }.attach()
        viewPager.registerOnPageChangeCallback(pageChangeCallback)
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            val currTabView = tabLayout.getTabAt(position)?.customView as TextChangeView
            currTabView.apply {
                setTabText(list[position])
                setFlipProgress(1 - positionOffset)
                setDirection(TextChangeView.Direction.TEXT_CHANGE_TO_RIGHT)
            }

            if (position + 1 < list.size) {
                val nextTabView = tabLayout.getTabAt(position + 1)?.customView as TextChangeView
                nextTabView.apply {
                    setTabText(list[position + 1])
                    setFlipProgress(positionOffset)
                    setDirection(TextChangeView.Direction.TEXT_CHANGE_TO_LEFT)
                }
            }
        }
    }

    /**
     * 进度条动画 圆环
     */
    private fun setProgressCircleAnimator() {
        val valueAnimator = ValueAnimator.ofInt(0, progressCircleValue)
        valueAnimator.apply {
            duration = 1500
            addUpdateListener {
                val value = valueAnimator.animatedValue as Int
                progressCircleAnimate.setProgressValue(value)
                interpolator = DecelerateInterpolator()
            }
            start()
        }
    }

    /**
     * icon 每隔1s变一次
     */
    private fun setIconChangeAnimator() {
        ValueAnimator.ofInt(0, 18).apply {
            setDuration(18 * 1000)
            addUpdateListener {
                iconChangeAnimateView.setAnimateInternal(animatedValue as Int)
                interpolator = LinearInterpolator()
            }
            start()
        }

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("MainActivity", "Received event: " + event?.action);
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("MainActivity", "DOWN received ")
            }

            MotionEvent.ACTION_MOVE -> {
                Log.d("MainActivity", "MOVE received")
            }

            MotionEvent.ACTION_UP -> {
                Log.d("MainActivity", "UP received")
                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onTouchEvent(event)
    }
}