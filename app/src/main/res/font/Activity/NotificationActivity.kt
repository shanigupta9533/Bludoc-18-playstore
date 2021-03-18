package com.virtual_market.virtualmarket.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.virtual_market.virtualmarket.Adapter.NotificationFrament
import com.virtual_market.virtualmarket.R

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification2)

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,NotificationFrament(),"notification").commit()

    }
}