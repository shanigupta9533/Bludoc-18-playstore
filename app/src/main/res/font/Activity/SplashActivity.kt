package com.virtual_market.virtualmarket.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.virtual_market.virtualmarket.R
import kotlin.math.log2

class SplashActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var product_id:String
    private lateinit var profile_id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler= Handler(Looper.myLooper()!!)

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData -> // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null

                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    product_id = deepLink!!.getQueryParameter("product_id").toString()
                    profile_id = deepLink.getQueryParameter("profile_id").toString()

                }
            }
            .addOnFailureListener(this) { e -> Log.w("SIDTAG ", "getDynamicLink:onFailure", e) }

        handler.post(Runnable {

            if (!TextUtils.isEmpty(product_id) && product_id != "null") {

                val intent = Intent(this, ShowMainPageActivity::class.java)
                intent.putExtra("product_id", product_id)
                startActivity(intent)
                finish()

            } else if (!TextUtils.isEmpty(profile_id) && profile_id != "null") {

                val intent = Intent(this, MyProfileShareActivity::class.java)
                intent.putExtra("profile_id", profile_id)
                startActivity(intent)
                finish()

            } else {

                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }

        })

    }
}