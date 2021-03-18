package com.virtual_market.virtualmarket.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.virtual_market.virtualmarket.R

class OtpValidationActivity : AppCompatActivity() {

    private lateinit var otpView: PinView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_validation)

        otpView = findViewById<PinView>(R.id.otp_view)

        findViewById<RelativeLayout>(R.id.otpSubmit).setOnClickListener {

            Toast.makeText(applicationContext,otpView.text.toString(),Toast.LENGTH_LONG).show()

            startActivity(Intent(applicationContext,MainActivity::class.java))

        }

    }
}