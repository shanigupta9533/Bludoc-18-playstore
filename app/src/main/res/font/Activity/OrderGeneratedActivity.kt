package com.virtual_market.virtualmarket.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.virtual_market.virtualmarket.R
import org.json.JSONObject

class OrderGeneratedActivity : AppCompatActivity(), PaymentResultListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_generated)
        findViewById<View>(R.id.pay_now).setOnClickListener { startPayment() }
    }

    fun startPayment() {
        /**
         * Instantiate Checkout
         */
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_dn6pzUfPO9NUMk")
        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.temple_run_after_delete)
        /**
         * Reference to current activity
         */
        val activity: Activity = this
        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            val options = JSONObject()
            options.put("name", getString(R.string.app_name))
            options.put("description", "Pay Indian Local Services with razor pay")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", R.color.colorPrimaryDark)
            options.put("currency", "USD")
            options.put("amount", (500 * 100).toString()) //pass amount in currency subunits
            checkout.open(activity, options)
        } catch (e: Exception) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e)
        }
    }

    override fun onPaymentSuccess(order_id: String) {

        val intent:Intent= Intent(this,SuccessActivity::class.java)
        intent.putExtra("key",order_id)
        startActivity(intent)

    }
    override fun onPaymentError(i: Int, s: String) {
        if (i == Checkout.NETWORK_ERROR) {
            Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show()
        } else if (i == Checkout.INVALID_OPTIONS) {
            Toast.makeText(this, "Invalid Options", Toast.LENGTH_SHORT).show()
        } else if (i == Checkout.PAYMENT_CANCELED) {
            Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show()
        } else if (i == Checkout.TLS_ERROR) Toast.makeText(
            this,
            "The device does not support TLS v1.1 or TLS v1.2.",
            Toast.LENGTH_SHORT
        ).show()
    }
}