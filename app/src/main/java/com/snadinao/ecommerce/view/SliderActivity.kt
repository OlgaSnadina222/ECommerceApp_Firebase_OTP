package com.snadinao.ecommerce.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.snadinao.ecommerce.R
import com.snadinao.ecommerce.adapter.MyViewPagerAdapter
import com.snadinao.ecommerce.databinding.ActivitySliderBinding
import com.snadinao.ecommerce.util.PrefManager
import org.w3c.dom.Text

class SliderActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySliderBinding
    private var viewPager: ViewPager? = null
    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    private var dotsLayout: LinearLayout? = null
    private lateinit var dots: Array<TextView?>
    private lateinit var layouts: IntArray
    private var prefManager: PrefManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySliderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //checking for first time launch - before calling setContentView
        prefManager = PrefManager(this)
        if (!prefManager!!.isFirstTimeLaunch){
            launchHomeScreen()
            finish()
        }

        //making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        //setContentView(R.layout.activity_slider)

        //layout of all welcome slider
        layouts = intArrayOf(
            R.layout.welcom_sl1,
            R.layout.welcom_sl2,
            R.layout.welcom_sl3,
            R.layout.welcom_sl4
        )
        //adding bottom dots
        addBottomDots(0)

        //making notification bar transparent
        changeStatusBarColor()

        //viewPager = findViewById(R.id.viewPager)
        myViewPagerAdapter = MyViewPagerAdapter(this, layouts)
        viewPager?.adapter = myViewPagerAdapter

        viewPager?.addOnPageChangeListener(viewPageChangeListener)
        binding.btSkip.setOnClickListener {
            launchHomeScreen()
        }
        binding.btNext.setOnClickListener {
            //checking last page: if home - launch
            val currentPage = getItem(+1)
            if (currentPage < layouts.size){
                //move to next screen
                viewPager?.currentItem = currentPage
            } else {
                launchHomeScreen()
            }
        }
    }

    private val viewPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener{
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            addBottomDots(position)
            //changing text button NEXT -> GOT IT
            if (position == layouts.size - 1){
                //last page - GOT IT
                binding.btNext.text = getString(R.string.start)
                binding.btSkip.visibility = View.GONE
            } else {
                binding.btNext.text = getString(R.string.next)
                binding.btSkip.visibility = View.VISIBLE
            }
        }

        override fun onPageSelected(position: Int) {}

        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem + i
    }

    private fun launchHomeScreen() {
        prefManager?.isFirstTimeLaunch = false
        startActivity(Intent(this, VerificationActivity::class.java))
        finish()
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)
        val colorActive = resources.getIntArray(R.array.array_dot_active)
        val colorInactive = resources.getIntArray(R.array.array_dot_inactive)
        dotsLayout?.removeAllViews()
        for (i in dots.indices){
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226")
            dots[i]?.textSize = 35f
            dots[i]?.setTextColor(colorInactive[currentPage])
            dotsLayout?.addView(dots[i])
        }
        if(dots.isNotEmpty()){
            dots[currentPage]?.setTextColor(colorActive[currentPage])
        }
    }
}