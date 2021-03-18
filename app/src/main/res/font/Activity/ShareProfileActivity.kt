package com.virtual_market.virtualmarket.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.virtual_market.virtualmarket.R
import java.util.*

class ShareProfileActivity : AppCompatActivity() {

    private val random = Random()
    private lateinit var url_firebase:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_profile)

        url_firebase=findViewById(R.id.url_firebase)

        findViewById<RelativeLayout>(R.id.share_profile).setOnClickListener {

            shareProfile()

        }


    }

    private fun shareProfile() {
        val dynamicLink: DynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse("https://virtualmarket.com/profile?profile_id="+rand())) // change manifest after change this link ----- E/deeplink: https://virtualmarket.com/invite?code=1234
            .setDomainUriPrefix("https://virtualmarket.page.link/?") // Open links with this app on Android
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

                url_firebase.text=shortLink.toString()

                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Download Best App For Promotion And Buy A Business $shortLink"
                )
                intent.type = "text/plain"
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "fail to share", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun rand() : Int {
        return random.nextInt(100 - 10) + 10
    }

}