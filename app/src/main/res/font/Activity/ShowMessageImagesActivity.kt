package com.virtual_market.virtualmarket.Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.virtual_market.virtualmarket.Adapter.ViewPagerAdapter
import com.virtual_market.virtualmarket.Adapter.ViewPagerImageCarouselAdapter
import com.virtual_market.virtualmarket.R
import com.virtual_market.virtualmarket.databinding.ActivityShowMessageImagesBinding

class ShowMessageImagesActivity : AppCompatActivity() {

    private val showModel:ArrayList<String> = ArrayList()
    private var activity: ActivityShowMessageImagesBinding?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity=DataBindingUtil.setContentView(this,R.layout.activity_show_message_images)

        showModel.add("")
        showModel.add("")
        showModel.add("")
        showModel.add("")
        showModel.add("")

        activity!!.backButton.setOnClickListener {

            onBackPressed()

        }

        val viewPagerAdapter= ViewPagerImageCarouselAdapter(this,showModel);
        activity!!.viewPager.adapter=viewPagerAdapter

        activity!!.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            @SuppressLint("SetTextI18n")
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                activity!!.toolbarText.text =(position+1).toString()+ " of " + viewPagerAdapter.count

            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {


            }

            override fun onPageScrollStateChanged(state: Int) {

            }


        })

    }
}