package com.virtual_market.virtualmarket.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.virtual_market.virtualmarket.Adapter.ChooseACategoryAdapter
import com.virtual_market.virtualmarket.R
import com.virtual_market.virtualmarket.databinding.ActivityChooseACategoryBinding

class ChooseACategoryActivity : AppCompatActivity() {

    lateinit var activity: ActivityChooseACategoryBinding
    lateinit var chooseACategoryAdapter: ChooseACategoryAdapter
    lateinit var showModel: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = DataBindingUtil.setContentView(this, R.layout.activity_choose_a_category)

        showModel = ArrayList()

        for (i in 0..9) {

            showModel.add("")

        }

        chooseACategoryAdapter = ChooseACategoryAdapter(showModel, this)
        val linearLayoutManager:LinearLayoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        activity.recyclerview.layoutManager=linearLayoutManager
        activity.recyclerview.adapter = chooseACategoryAdapter

    }
}