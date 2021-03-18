package com.virtual_market.virtualmarket.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.luseen.spacenavigation.SpaceItem
import com.luseen.spacenavigation.SpaceNavigationView
import com.luseen.spacenavigation.SpaceOnClickListener
import com.virtual_market.virtualmarket.Adapter.NotificationFrament
import com.virtual_market.virtualmarket.Fragment.*
import com.virtual_market.virtualmarket.R
import java.util.*

class MainActivity : AppCompatActivity() {

    private var tags: String? = null
    private val address = arrayOf("home", "message", "AdsAndFavorite", "profile")
    private var itemIndexGlobal:Int?=null
    private lateinit var toolbar:RelativeLayout
    private var spaceNavigationView: SpaceNavigationView?=null


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar=findViewById(R.id.toolbar);

        findViewById<ImageView>(R.id.bell_icon).setOnClickListener {

            startActivity(Intent(this,NotificationActivity::class.java))

        }

        supportFragmentManager.beginTransaction().add(R.id.frameLayout, HomeFragment(), "home").commit()

        findViewById<ImageView>(R.id.menuItem).setOnClickListener {

            startActivity(Intent(applicationContext,CreateMenuActivity::class.java))

        }

        findViewById<TextView>(R.id.search_view).setOnClickListener {

            startActivity(Intent(applicationContext,SearchActivity::class.java))

        }

        spaceNavigationView = findViewById(R.id.bottom_space)
        spaceNavigationView!!.showIconOnly()
        spaceNavigationView!!.initWithSaveInstanceState(savedInstanceState)
        spaceNavigationView!!.addSpaceItem(SpaceItem("", R.drawable.ic_home_icon))
        spaceNavigationView!!.addSpaceItem(SpaceItem("", R.drawable.ic_chat_message))
        spaceNavigationView!!.addSpaceItem(SpaceItem("", R.drawable.ic_heart_icon))
        spaceNavigationView!!.addSpaceItem(SpaceItem("", R.drawable.ic_user_icon))

        spaceNavigationView!!.setSpaceOnClickListener(object : SpaceOnClickListener {
            override fun onCentreButtonClick() {

                startActivity(Intent(applicationContext,ChooseACategoryActivity::class.java))

            }

            override fun onItemClick(itemIndex: Int, itemName: String?) {

                loadFragment(itemIndex)
                itemIndexGlobal = itemIndex


            }


            override fun onItemReselected(itemIndex: Int, itemName: String?) {

                loadFragment(itemIndex)
                itemIndexGlobal = itemIndex

            }
        })

//        Handler(Looper.getMainLooper()).postDelayed({
//            /* Create an Intent that will start the Menu-Activity. */
//
//            spaceNavigationView!!.showBadgeAtIndex(3, 12, Color.parseColor("#ff0000"));
//            spaceNavigationView!!.showBadgeAtIndex(2, 12, Color.parseColor("#ff0000"));
//
//        }, 3000)

    }

    private fun loadFragment(itemIndex: Int){

        var selectedFragment: Fragment? = null;
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()

        when (itemIndex) {

            0 -> {

                tags = "home"

                if (fm.findFragmentByTag(tags) != null) {
                    hideFragment()
                    Objects.requireNonNull(
                        supportFragmentManager.findFragmentByTag(tags)
                    )?.let {
                        ft.show(it).commit()
                    }
                }
                selectedFragment = HomeFragment()

            }
            1 -> {

                tags = "message"

                if (fm.findFragmentByTag(tags) != null) {
                    hideFragment()
                    Objects.requireNonNull(
                        supportFragmentManager.findFragmentByTag(tags)
                    )?.let {
                        ft.show(it).commit()
                    }
                }
                selectedFragment = MessageFragment()

            }
            2 -> {

                tags = "AdsAndFavorite"

                if (fm.findFragmentByTag(tags) != null) {
                    hideFragment()
                    Objects.requireNonNull(
                        supportFragmentManager.findFragmentByTag(tags)
                    )?.let {
                        ft.show(it).commit()
                    }
                }
                selectedFragment = AdsAndFavoriteFragment()

            }
            3 -> {

                tags = "profile"

                if (fm.findFragmentByTag(tags) != null) {
                    hideFragment()
                    Objects.requireNonNull(
                        supportFragmentManager.findFragmentByTag(tags)
                    )?.let {
                        ft.show(it).commit()
                    }
                }
                selectedFragment = ProfileFragment()

            }

        }

        if (selectedFragment != null && !TextUtils.isEmpty(tags) && supportFragmentManager.findFragmentByTag(tags) == null) {
            hideFragment()
            ft.add(R.id.frameLayout, selectedFragment, tags).addToBackStack(null).commit()
        }

    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount

        if (backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            spaceNavigationView!!.changeCurrentItem(0)
            val fm: FragmentManager = supportFragmentManager
            for (i in 0 until fm.backStackEntryCount) {
                fm.popBackStack()
            }
        }
    }

    private fun showFragment() {
        if (supportFragmentManager.findFragmentById(R.id.frameLayout) != null) Objects.requireNonNull(
            supportFragmentManager.findFragmentById(
                R.id.frameLayout
            )
        )?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        }
    }

    private fun hideFragment() {
        for (addres in address) {
            if (supportFragmentManager.findFragmentByTag(addres) != null) Objects.requireNonNull(supportFragmentManager.findFragmentByTag(addres))?.let {
                supportFragmentManager.beginTransaction().hide(it).commit()
            }

        }
    }

}