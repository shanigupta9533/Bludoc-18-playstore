package com.virtual_market.virtualmarket.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.virtual_market.virtualmarket.R


class SuccessActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        val stringExtra = intent.getStringExtra("key")

        val orderId=findViewById<TextView>(R.id.order_id)
        orderId.text= "Order Id : $stringExtra"

        val mp: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.sucess_sound)
        mp.start()

        findViewById<RelativeLayout>(R.id.continue_button).setOnClickListener{

            startActivity(Intent(applicationContext, ShowMainPageActivity::class.java))

            finishAffinity()

        }

    }

    override fun onBackPressed() {

        startActivity(Intent(applicationContext, ShowMainPageActivity::class.java))
        finishAffinity()

    }

}