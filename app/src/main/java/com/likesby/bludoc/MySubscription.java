package com.likesby.bludoc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Adapter.MySubscriptionAdapter;
import com.likesby.bludoc.ModelLayer.Entities.Subscription;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities.SubcriptionsItem;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.utils.DateUtils;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MySubscription extends AppCompatActivity
{
    private static final String TAG = "SubPackages___";
    ApiViewHolder mLocationViewHolder;
    Context mContext;
    CompositeDisposable mBag = new CompositeDisposable();
    ArrayList<com.likesby.bludoc.ModelLayer.Entities.MySubscription> SubscriptionsArrayList;

    private RadioButton rb_Family,rb_Business;
    RecyclerView recyclerView;
    MySubscriptionAdapter mySubscriptionAdapter;

    FrameLayout fl_subscription;
    SessionManager manager;
    MyDB myDB;
    AppCompatActivity activity;
    TextView tv_msg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_subscription);
        mContext = MySubscription.this;
        manager = new SessionManager();
        myDB = new MyDB(mContext);
        initViews();
        activity = MySubscription.this;


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
        tv_msg = findViewById(R.id.tv_msg);

        String sub_valid = "", premium_valid = "";
        boolean flag_reset_free = false, flag_reset_paid = false;
        int days_left_free = 0, days_left_paid = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(mContext, "profile");
        if (profileDetails.getSubcriptions() != null) {
            if (profileDetails.getSubcriptions().size() != 0) {
                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                    if (si.getType().equals("Free")) {
                        try {
                            Calendar c2 = Calendar.getInstance();
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                            Date dateEnd = dateFormat.parse(si.getEnd());
                            Date c = Calendar.getInstance().getTime();
                            assert dateEnd != null;
                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));

                            premium_valid = si.getDays();
                            Date date1 = null, date2 = null;
                            try {
                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                date2 = dateFormat.parse(si.getEnd());
                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                flag_reset_free = true;

                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                // break;
                                sub_valid = si.getEnd();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                        } catch (ParseException pe) {
                            // handle the failure
                            flag_reset_free = false;
                        }

                    } else if (si.getType().equals("Paid")) {
                        try {
                            Calendar c2 = Calendar.getInstance();
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                            Date dateEnd = dateFormat.parse(si.getEnd());
                            Date c = Calendar.getInstance().getTime();
                            assert dateEnd != null;
                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));
                            premium_valid = si.getDays();
                            Date date1 = null, date2 = null;

                            date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                            date2 = dateFormat.parse(si.getEnd());
                            // date2 = dateFormat.parse("12-12-2020");
                            Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                            flag_reset_paid = true;

                            String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                            days_left_paid = days_left_paid + Integer.parseInt(splited[0]);
                            sub_valid = si.getEnd();
                            // break;

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (flag_reset_paid) {   //Premium Subscription
                    if (days_left_paid < 1) {

                        tv_msg.setText("Expired!! Upgrade to premium for an ad free experience.");

                    } else {
                        //  popupFreeSubscription("",false);
                        //   Toast.makeText(mContext, ""+days_left_paid, Toast.LENGTH_SHORT).show();
                        if (days_left_paid < 11) {
                            tv_msg.setText("Your subscription is Valid till " + sub_valid + ".");

                        } else {

                            tv_msg.setText("Your subscription is Valid till " + sub_valid + ".");


                            /*ll_premium.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_gradient));
                            ll_premium.setPadding(20,30,20,30);

                            tv_days_left.setTextColor(getResources().getColor(R.color.colorWhite));*/
                        }


                    }
                } else   //Free Subscription
                {
                    if (flag_reset_free) {

                        tv_msg.setText("Congratulations!! \n You have been offered "+ premium_valid+" days free trial.\nValid Till " + sub_valid + "\nUpgrade to premium for an ad free experience.");

                    } else {

                        tv_msg.setText("Your Subscription has Ended, Kindly upgrade to Premium!");


                    }

                }

            } else {
                tv_msg.setText("Your Subscription has Ended, \n Kindly upgrade to Premium!");
            }
        }else {
            tv_msg.setText("Your Subscription has Ended, \n Kindly upgrade to Premium!");
        }
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
            mLocationViewHolder.mysubscription(manager.getPreferences(mContext, "doctor_id"))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseSubscriptions);
        } else {

            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    SingleObserver<Subscription> responseSubscriptions = new SingleObserver<Subscription>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(Subscription response) {
            if (response != null) {
                fl_subscription.setVisibility(View.GONE);
                if (response.getMessage() != null) {
                    if(response.getMessage().equals("My Subscriptions")){
                        SubscriptionsArrayList = new ArrayList<>();
                        SubscriptionsArrayList = response.getMySubscriptions();

                        mySubscriptionAdapter = new MySubscriptionAdapter(SubscriptionsArrayList,mContext,mLocationViewHolder,mBag,activity);
                        recyclerView.setAdapter(mySubscriptionAdapter);
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
