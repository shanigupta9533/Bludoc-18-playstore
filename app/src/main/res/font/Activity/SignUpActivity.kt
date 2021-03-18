package com.virtual_market.virtualmarket.Activity

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import com.virtual_market.virtualmarket.R
import com.virtual_market.virtualmarket.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity(), OSSubscriptionObserver {

    lateinit var activity: ActivitySignUpBinding;
    lateinit var mGoogleSignInClient: GoogleSignInClient;
    private val RC_SIGN_IN = 1001
    private var mAuth: FirebaseAuth? = null
    lateinit var senderId:String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity=DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        OneSignal.addSubscriptionObserver(this)

        activity.loginWithGoogle.setOnClickListener {

            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

        }

        // [START config_signin]
        // Configure Google Sign In

        // [START config_signin]
        // Configure Google Sign In
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        // [END config_signin]
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        activity.signupButton.setOnClickListener {

            if(TextUtils.isEmpty(activity.userName.text.toString())){

                activity.userName.error="Name is Required*"
                activity.userName.requestFocus()

            } else if(TextUtils.isEmpty(activity.mobile.text.toString())){

                activity.mobile.error="Mobile Number is Required*"
                activity.mobile.requestFocus()

            } else if(TextUtils.isEmpty(activity.userEmail.text.toString())){

                activity.userEmail.error="Email is Required*"
                activity.userEmail.requestFocus()

            } else if(TextUtils.isEmpty(activity.password.text.toString())){

                activity.password.error="Password is Required*"
                activity.password.requestFocus()

            } else if(TextUtils.isEmpty(activity.retypePassword.text.toString())){

                activity.retypePassword.error="Re-Type Password is Required*"
                activity.retypePassword.requestFocus()

            } else if(!activity.retypePassword.text.toString().equals(
                    activity.password.text.toString(),
                    true
                )){

                activity.retypePassword.error="Password is not matched*"
                activity.retypePassword.requestFocus()

            } else{

                Toast.makeText(this, "SignUp start", Toast.LENGTH_LONG).show()

            }

        }

    }

    // [START onactivityresult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Log.d("TAG", "firebaseAuthWithGoogle:" + account!!.id)
                account.idToken?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth!!.signInWithCredential(credential).addOnCompleteListener(
            this,
            OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user: FirebaseUser = mAuth!!.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }

                // [START_EXCLUDE]
                // [END_EXCLUDE]
            })
    }

    private fun updateUI(user: FirebaseUser?) {
        user?.let { googleOrFacebookLogin(it) }
    }

    private fun googleOrFacebookLogin(user: FirebaseUser) {

        var email_user: String? = null

        for (profile in user.providerData) {
            val email_get = profile.email
            if (!TextUtils.isEmpty(email_get) && email_get != "null") {
                email_user = email_get
            }
        }

        Toast.makeText(this, email_user, Toast.LENGTH_LONG).show()


    }

    override fun onOSSubscriptionChanged(stateChanges: OSSubscriptionStateChanges?) {

        senderId = stateChanges!!.to.userId

        Toast.makeText(this, "" +senderId,Toast.LENGTH_LONG).show()

    }

}