package com.virtual_market.virtualmarket.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import com.virtual_market.virtualmarket.R

class CreateMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_menu)

        findViewById<RelativeLayout>(R.id.logout_button).setOnClickListener {

            startActivity(Intent(this,LoginActivity::class.java))

        }

        findViewById<ImageView>(R.id.back_button).setOnClickListener {

            onBackPressed()

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()


    }

}