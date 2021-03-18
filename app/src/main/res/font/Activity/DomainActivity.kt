package com.virtual_market.virtualmarket.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.virtual_market.virtualmarket.Adapter.DomainAdapter
import com.virtual_market.virtualmarket.R

class DomainActivity : AppCompatActivity() {

    val domainModelList:List<String> = ArrayList<String>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_domain)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val linearLayoutManager=LinearLayoutManager(applicationContext,RecyclerView.VERTICAL,false)
        val domainAdapter = DomainAdapter(applicationContext,domainModelList)
        recyclerView.layoutManager=linearLayoutManager
        recyclerView.adapter=domainAdapter

    }
}