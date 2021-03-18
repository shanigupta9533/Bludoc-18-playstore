package com.likesby.bludoc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Adapter.FaqsAdapter;
import com.likesby.bludoc.ModelLayer.Entities.FaqsItem;
import com.likesby.bludoc.ModelLayer.Entities.FaqsResponse;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FAQ  extends AppCompatActivity
{
    private static final String TAG = "FaqsActivity_____";
    RecyclerView recyclerViewViewMembers;
    Context mContext;

    ApiViewHolder mLocationViewHolder;
    CompositeDisposable mBag = new CompositeDisposable();
    ArrayList<FaqsItem> trackingsArrayList ;
    FaqsAdapter faqsAdapter;
    Button actionBtn;
    MyDB myDB;
    CheckBox active_member_select_all_cb;
    TextView textViewSelectAll;
    LinearLayout linearLayout_action;
    SessionManager manager;


    FrameLayout frameLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faqs);

        mContext = FAQ.this;

        myDB = new MyDB(mContext);
        manager = new SessionManager();

        frameLayout = findViewById(R.id.fl_progress_bar_faqs);
        Calendar c2 = Calendar .getInstance();
        System.out.println("Current time => "+c2.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date2 = df.format(c2.getTime());

        Log.e(TAG,"CurrentDateTimeString3 = "+date2);

        initViews();
        initViewHolder();

        ImageView btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void initViews() {
        recyclerViewViewMembers =findViewById(R.id.rv_faqs);

        initRecyclerViewTrackingList();
    }

    private void initRecyclerViewTrackingList()
    {
        //Create new GridLayoutManager
        @SuppressLint("WrongConstant") GridLayoutManager gridLayoutManagerr = new GridLayoutManager(mContext,
                1,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager

        recyclerViewViewMembers.setLayoutManager(gridLayoutManagerr);

        //=========================================================================================================
    }


    private void initViewHolder() {
        mLocationViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);

        if (Utils.isConnectingToInternet(mContext)) {


            mLocationViewHolder.getFaqs()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseCategories);
        } else {
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    SingleObserver<FaqsResponse> responseCategories = new SingleObserver<FaqsResponse>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(FaqsResponse response) {
            if (response != null) {
                if (response.getMessage() != null) {

                    if(response.getFaqs()!=null && response.getFaqs().size()>0) {
                        trackingsArrayList = response.getFaqs();

                        faqsAdapter = new FaqsAdapter(trackingsArrayList, mContext);
                        recyclerViewViewMembers.setAdapter(faqsAdapter);
                        frameLayout.setVisibility(View.GONE);

                    }
                    else
                    {
                        Toast.makeText(mContext, "No Faqs", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    frameLayout.setVisibility(View.GONE);
                    Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            frameLayout.setVisibility(View.GONE);
            Log.e(TAG, "onError: responseCategories >> " + e.toString());

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // later when not needed
        mBag.dispose();
    }
}