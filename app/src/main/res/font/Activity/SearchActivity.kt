package com.virtual_market.virtualmarket.Activity

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.virtual_market.virtualmarket.Adapter.SearchAdapter
import com.virtual_market.virtualmarket.R
import java.util.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchBar = findViewById<EditText>(R.id.search_bar)

        searchBar.requestFocus()

        findViewById<ImageView>(R.id.back).setOnClickListener {

            onBackPressed()

        }

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        val stringList: MutableList<String> = ArrayList()

        stringList.add("Amazon")
        stringList.add("Tik tok Source Code")
        stringList.add("App Sell")
        stringList.add("vahak")
        stringList.add("sahak")


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val sliderAdapter = SearchAdapter(this, stringList)
        val linearLayout = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = linearLayout
        recyclerView.adapter = sliderAdapter

    }
}