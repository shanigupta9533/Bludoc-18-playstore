package com.virtual_market.virtualmarket.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.Fade
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.virtual_market.virtualmarket.Adapter.ViewPagerImageCarouselAdapter
import com.virtual_market.virtualmarket.R
import com.virtual_market.virtualmarket.databinding.ActivityShowScreenShotBinding

class ShowScreenShotActivity : AppCompatActivity() {

    lateinit var activity: ActivityShowScreenShotBinding
    private val showModel:ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity=DataBindingUtil.setContentView(this, R.layout.activity_show_screen_shot)

        showModel.add("")
        showModel.add("")
        showModel.add("")
        showModel.add("")
        showModel.add("")

        val fade = Fade()
        val decor = window.decorView
        fade.excludeTarget(decor.findViewById<View>(R.id.action_bar_container), true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
        window.enterTransition = fade
        window.exitTransition = fade

        activity.backButton.setOnClickListener {

            onBackPressed()

        }

        val viewPagerAdapter= ViewPagerImageCarouselAdapter(this, showModel);
        activity.viewPager.adapter=viewPagerAdapter
        activity.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                activity.toolbarText.text =
                    (position + 1).toString() + " of " + viewPagerAdapter.count

            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {


            }

            override fun onPageScrollStateChanged(state: Int) {

            }


        })

    }
}