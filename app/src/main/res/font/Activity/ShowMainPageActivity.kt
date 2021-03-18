package com.virtual_market.virtualmarket.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.transition.Fade
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.virtual_market.virtualmarket.Adapter.ImageLayoutAdapter
import com.virtual_market.virtualmarket.R
import java.util.*

class ShowMainPageActivity : AppCompatActivity() {

    private val showData: ArrayList<String> = ArrayList<String>()
    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_main_page)

        val fade = Fade()
        val decor = window.decorView
        fade.excludeTarget(decor.findViewById<View>(R.id.action_bar_container), true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
        window.enterTransition = fade
        window.exitTransition = fade

        val productId = intent.getStringExtra("product_id")

        if(!TextUtils.isEmpty(productId))
            Toast.makeText(this, "" + productId, Toast.LENGTH_LONG).show()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val imageLayoutAdapter= ImageLayoutAdapter(this, showData)
        val linearLayoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.HORIZONTAL,
            false
        )
        recyclerView.layoutManager=linearLayoutManager
        recyclerView.adapter=imageLayoutAdapter

        findViewById<ImageView>(R.id.share).setOnClickListener {

            link()

        }

    }

    private fun link() {
        val dynamicLink: DynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://virtualmarket.com/product?product_id=" + rand())) // change manifest after change this link ----- E/deeplink: https://virtualmarket.com/invite?code=1234
            .setDomainUriPrefix(" https://virtualmarket.page.link/?") // Open links with this app on Android
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build()) // Open links with com.example.ios on iOS
            .buildDynamicLink()
        val dynamicLinkUri: Uri = dynamicLink.uri

        val shortLinkTask: Task<ShortDynamicLink> =
            FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLinkUri)
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
        shortLinkTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val shortLink: Uri = task.result!!.shortLink!!
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "India’s first app that helps you discover the best business in cheap price. . $shortLink"
                )
                intent.type = "text/plain"
                startActivity(intent)
            } else {
                Toast.makeText(this@ShowMainPageActivity, "fail to share", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun rand() : Int {
        return random.nextInt(100 - 10) + 10
    }

}