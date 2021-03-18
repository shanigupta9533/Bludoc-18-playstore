package com.likesby.bludoc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Adapter.SubscriptionAdapter;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseSubscriptions;
import com.likesby.bludoc.ModelLayer.NewEntities.SubscriptionsItem;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
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

public class SubscriptionPackages extends AppCompatActivity
{
    private static final String TAG = "SubPackages___";
    ApiViewHolder mLocationViewHolder;
    Context mContext;
    CompositeDisposable mBag = new CompositeDisposable();
    ArrayList<SubscriptionsItem> SubscriptionsArrayList;

    private RadioButton rb_Family,rb_Business;
    RecyclerView recyclerView;
    SubscriptionAdapter subscriptionAdapter;

    FrameLayout fl_subscription;
    SessionManager manager;
    MyDB myDB;
    AppCompatActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_packages);
        mContext = SubscriptionPackages.this;
        manager = new SessionManager();
        myDB = new MyDB(mContext);
        initViews();
        activity = SubscriptionPackages.this;


        Calendar c2 = Calendar .getInstance();
        System.out.println("Current time => "+c2.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date2 = df.format(c2.getTime());


        initRecyclerViewCategories();
        initViewHolder();

        rb_Family.setChecked(true);
        rb_Business.setChecked(false);

        ImageView btn_back = findViewById(R.id.btn_back);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initViews() {
        rb_Family = findViewById(R.id.rb_family);
        rb_Business = findViewById(R.id.rb_business);
        recyclerView = findViewById(R.id.recycler_packages);
        fl_subscription = findViewById(R.id.fl_subscription);

    }


    //================     Main  Categories       ==============================================
    private void initRecyclerViewCategories()
    {
        recyclerView = findViewById(R.id.recycler_packages);
        //Create new GridLayoutManager
        @SuppressLint("WrongConstant") GridLayoutManager gridLayoutManagerr = new GridLayoutManager(this,
                1,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager

        recyclerView.setLayoutManager(gridLayoutManagerr);

        //=========================================================================================================
    }

    private void initViewHolder() {
        mLocationViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);

        if (Utils.isConnectingToInternet(mContext)) {
            mLocationViewHolder.getSubscriptions()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseSubscriptions);
        } else {

            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    SingleObserver<ResponseSubscriptions> responseSubscriptions = new SingleObserver<ResponseSubscriptions>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseSubscriptions response) {
            if (response != null) {
                fl_subscription.setVisibility(View.GONE);
                if (response.getMessage() != null) {

                    if(response.getMessage().equals("All Subscriptions")){
                        SubscriptionsArrayList = new ArrayList<>();
                        SubscriptionsArrayList = response.getSubscriptions();
                        subscriptionAdapter = new SubscriptionAdapter(SubscriptionsArrayList,mContext,mLocationViewHolder,mBag,activity);

                        recyclerView.setAdapter(subscriptionAdapter);
                    }



                } else {
                    Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            else
                fl_subscription.setVisibility(View.GONE);
        }


        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: responseCategories >> " + e.toString());
            //intentCall();
            fl_subscription.setVisibility(View.GONE);
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // later when not needed
        mBag.dispose();
    }
}
