package com.virtual_market.virtualmarket.Activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.virtual_market.virtualmarket.Adapter.UploadImagesAdapter
import com.virtual_market.virtualmarket.R
import com.virtual_market.virtualmarket.databinding.ActivityAddProductBinding
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy

class AddProductActivity : AppCompatActivity() {

    private val REQUEST_CODE_CHOOSE = 99
    private val REQUEST_CODE_CHOOSE_LOGO=100
    private val storageList: ArrayList<String> = ArrayList()
    private val beforeStorageList: ArrayList<String> = ArrayList()
    private lateinit var storageListForLogo: ArrayList<String>;
    private var openSize = 0
    lateinit var activity:ActivityAddProductBinding
    private var uploadImagesAdapter: UploadImagesAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity=DataBindingUtil.setContentView(this,R.layout.activity_add_product)

        storageListForLogo= ArrayList()

        activity.uploadLogo.setOnClickListener{

            askPermissionForLogo()

        }

        initRecyclerView()

    }

    private fun initRecyclerView() {
        storageList.add("")
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        uploadImagesAdapter = UploadImagesAdapter(this, storageList)
        val linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = uploadImagesAdapter
        uploadImagesAdapter!!.setOnClickListener(object : UploadImagesAdapter.OnClickListener {
            override fun onClick() {
                askPermissions()
            }

            override fun onRemove(size: Int, position: Int) {
                storageList.removeAt(position)
                uploadImagesAdapter!!.notifyDataSetChanged()
                if (storageList.size == 9 && !isEmptyValueAvailable()) {
                    storageList.add("")
                    uploadImagesAdapter!!.notifyDataSetChanged()
                }
            }
        })
    }

    private fun askPermissionForLogo(){

        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
        val rationale = "Please provide Storage permission so that you can Upload Images"
        val options = Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning")
        Permissions.check(
            this /*context*/,
            permissions,
            rationale,
            options,
            object : PermissionHandler() {
                override fun onGranted() {
                    openStorageForLogo()
                }

                override fun onDenied(
                    context: Context,
                    deniedPermissions: java.util.ArrayList<String>
                ) {
                    // permission denied, block the feature.
                }
            })

    }

    private fun openStorageForLogo() {
        Matisse.from(this)
            .choose(MimeType.ofImage(), false)
            .countable(true)
            .maxSelectable(1)
            .gridExpectedSize(
                resources.getDimensionPixelSize(R.dimen._100ssp)
            )
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .showSingleMediaType(true)
            .originalEnable(true)
            .maxOriginalSize(10)
            .autoHideToolbarOnSingleTap(true)
            .forResult(REQUEST_CODE_CHOOSE_LOGO)
    }

    private fun askPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
        val rationale = "Please provide Storage permission so that you can Upload Images"
        val options = Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning")
        Permissions.check(
            this /*context*/,
            permissions,
            rationale,
            options,
            object : PermissionHandler() {
                override fun onGranted() {
                    openStorage()
                }

                override fun onDenied(
                    context: Context,
                    deniedPermissions: java.util.ArrayList<String>
                ) {
                    // permission denied, block the feature.
                }
            })
    }

    private fun isEmptyValueAvailable(): Boolean {
        for (s in storageList) {
            if (TextUtils.isEmpty(s)) {
                return true
            }
        }
        return false
    }

    private fun openStorage() {
        if (storageList.size == 1) openSize = 10 else openSize = 10 - (storageList.size - 1)
        Matisse.from(this)
            .choose(MimeType.ofImage(), false)
            .countable(true)
            .maxSelectable(openSize)
            .gridExpectedSize(
                resources.getDimensionPixelSize(R.dimen._100ssp)
            )
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .showSingleMediaType(true)
            .originalEnable(true)
            .maxOriginalSize(10)
            .autoHideToolbarOnSingleTap(true)
            .forResult(REQUEST_CODE_CHOOSE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE && data != null) {
                beforeStorageList.clear()
                for (s in storageList) {
                    if (!TextUtils.isEmpty(s)) {
                        beforeStorageList.add(s)
                    }
                }
                storageList.clear()
                storageList.addAll(beforeStorageList)
                storageList.addAll(Matisse.obtainPathResult(data))
                if (storageList.size < 10) {
                    storageList.add("")
                }
                uploadImagesAdapter!!.notifyDataSetChanged()

            } else if(requestCode == REQUEST_CODE_CHOOSE_LOGO && data != null){

                storageListForLogo.addAll(Matisse.obtainPathResult(data))

                Toast.makeText(this@AddProductActivity,""+storageListForLogo.size,Toast.LENGTH_LONG).show()

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}