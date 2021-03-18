package com.virtual_market.virtualmarket.Activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commit451.modalbottomsheetdialogfragment.ModalBottomSheetDialogFragment
import com.commit451.modalbottomsheetdialogfragment.Option
import com.commit451.modalbottomsheetdialogfragment.OptionRequest
import com.virtual_market.virtualmarket.Adapter.ShowAdapter
import com.virtual_market.virtualmarket.R
import java.util.*

class MyProfileShareActivity : AppCompatActivity(), ModalBottomSheetDialogFragment.Listener {

    private val showData: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile_share)

        findViewById<ImageView>(R.id.sort_game).setOnClickListener {

            ModalBottomSheetDialogFragment.Builder() //custom option, without needing menu XML
                .add(OptionRequest(1, "Relevance", R.drawable.ic_action_sort))
                .add(OptionRequest(2, "Popularity", R.drawable.ic_action_sort))
                .add(OptionRequest(3, "Price -- Low to High", R.drawable.ic_action_sort))
                .add(OptionRequest(4, "Price -- High to Low", R.drawable.ic_action_sort))
                .layout(R.layout.item_customs)
                .show(supportFragmentManager, "Custom")


        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview);
        val showAdapter = ShowAdapter(applicationContext, showData)
        val linearLayoutManager = LinearLayoutManager(
            applicationContext,
            RecyclerView.VERTICAL,
            false
        )
        recyclerView.adapter = showAdapter
        recyclerView.layoutManager = linearLayoutManager

    }

    override fun onModalOptionSelected(tag: String?, option: Option) {

        Toast.makeText(applicationContext, "ID is " + option.id, Toast.LENGTH_SHORT).show()
    }


}