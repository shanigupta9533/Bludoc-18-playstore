package com.virtual_market.virtualmarket.Activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.matrimonialApp.matrimonial.Adapter.ChatPersonAdapter
import com.virtual_market.virtualmarket.Model.MessagesModelClass
import com.virtual_market.virtualmarket.R
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private var messagesModelClassList: ArrayList<MessagesModelClass>? = null
    private var recyclerView: RecyclerView? = null
    private var emojiEditText: EditText? = null
    private var speechVoice: ImageView? = null
    private var send: ImageView? = null
    private var chatPersonAdapter: ChatPersonAdapter? = null
    private var rootView: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messagesModelClassList = ArrayList()

        // This line needs to be executed before any usage of EmojiTextView, EmojiEditText or EmojiButton.
        rootView = findViewById<RelativeLayout>(R.id.rootView)
        emojiEditText = findViewById<EditText>(R.id.commentsBox)
        speechVoice = findViewById<ImageView>(R.id.speechVoice)
        send = findViewById<ImageView>(R.id.send)


        // init recycler view and adapter here

        findViewById<ImageView>(R.id.buy_here).setOnClickListener {

            startActivity(Intent(this,OrderGeneratedActivity::class.java))

        }

        // init recycler view and adapter here
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val linearLayoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.setLayoutManager(linearLayoutManager)
        chatPersonAdapter = ChatPersonAdapter(applicationContext, messagesModelClassList!!)
        recyclerView!!.setAdapter(chatPersonAdapter)

        // end here


        // end here
        send!!.setOnClickListener(View.OnClickListener {
            val messagesModelClass = MessagesModelClass()
            messagesModelClass.isMe = true
            messagesModelClass.message = emojiEditText!!.getText().toString()
            messagesModelClassList!!.add(messagesModelClass)
            chatPersonAdapter!!.notifyDataSetChanged()
            recyclerView!!.scrollToPosition(messagesModelClassList!!.size - 1)
            emojiEditText!!.setText("")
        })

        // speech voice reconization here

        // speech voice reconization here
        speechVoice!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US")
            try {
                startActivityForResult(intent, 500)
            } catch (a: ActivityNotFoundException) {
                Toast.makeText(
                    applicationContext,
                    "Oops! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        emojiEditText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val comment: String = emojiEditText!!.getText().toString().trim { it <= ' ' }
                if (comment.isEmpty()) {
                    speechVoice!!.setVisibility(View.VISIBLE)
                    DrawableCompat.setTint(
                        send!!.getDrawable(),
                        ContextCompat.getColor(applicationContext, R.color.quantum_grey700)
                    )
                    send!!.setEnabled(false)
                    send!!.setVisibility(View.GONE)
                    return
                }
                speechVoice!!.setVisibility(View.GONE)
                send!!.setVisibility(View.VISIBLE)
                send!!.setEnabled(true)
                DrawableCompat.setTint(
                    send!!.getDrawable(),
                    ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark)
                )
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        initChatingView()


    }

    private fun initChatingView() {
        for (i in 0..19) {
            val messagesModelClass = MessagesModelClass()
            when {
                i % 3 == 0 -> {
                    messagesModelClass.isMe = true
                    messagesModelClass.isImage=false
                    messagesModelClass.message = "Where does it come from?\n" +
                            "Contrary to popular belief,"
                }
                i%2==0 -> {

                    messagesModelClass.isMe = true
                    messagesModelClass.isImage=true
                    messagesModelClass.message = "Where does it come from?\n" +
                            "Contrary to popular belief, "
                }
                i%5==0 -> {

                    messagesModelClass.isYou = true
                    messagesModelClass.isImage=true
                    messagesModelClass.message = "Where does it come from?\n" +
                            "Contrary to popular belief, "

                }
                else -> {
                    messagesModelClass.isYou = true
                    messagesModelClass.isImage=false
                    messagesModelClass.message = (" " + "Where does it come from?\\n\" +\n" +
                            "                        \"Contrary to popular belief, ")
                }
            }

            messagesModelClassList!!.add(messagesModelClass)
        }
        recyclerView!!.scrollToPosition(messagesModelClassList!!.size - 1)
    }

}