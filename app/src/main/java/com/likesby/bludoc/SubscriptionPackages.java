package com.likesby.bludoc;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Adapter.SubscriptionAdapter;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseSubscriptions;
import com.likesby.bludoc.ModelLayer.NewEntities.SubscriptionsItem;
import com.likesby.bludoc.ModelLayer.SubscriptionModels;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class SubscriptionPackages extends AppCompatActivity {
    private static final String TAG = "SubPackages___";
    ApiViewHolder mLocationViewHolder;
    Context mContext;
    CompositeDisposable mBag = new CompositeDisposable();
    ArrayList<SubscriptionsItem> SubscriptionsArrayList;
    private RadioButton rb_Family, rb_Business;
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


        Calendar c2 = Calendar.getInstance();
        System.out.println("Current time => " + c2.getTime());
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
    private void initRecyclerViewCategories() {
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

                    if (response.getMessage().equals("All Subscriptions")) {
                        SubscriptionsArrayList = new ArrayList<>();
                        SubscriptionsArrayList = response.getSubscriptions();
                        subscriptionAdapter = new SubscriptionAdapter(SubscriptionsArrayList, mContext, mLocationViewHolder, mBag, activity);
                        recyclerView.setAdapter(subscriptionAdapter);

                        subscriptionAdapter.setOnClickListener(new SubscriptionAdapter.OnClickListener() {
                            @Override
                            public void onClick(SubscriptionsItem subscriptionsItem) {

                                if (subscriptionsItem.getPayment_type() != null && subscriptionsItem.getPayment_type().equalsIgnoreCase("RecurringPayment"))
                                    getRazorPaySubscriptionId(subscriptionsItem.getSubscriptionId(), subscriptionsItem.getSell());
                                else
                                    popuplogout(subscriptionsItem.getSubscriptionId(), subscriptionsItem.getSell(), "", "single");

                            }
                        });

                    }


                } else {
                    Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else
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

    private void getRazorPaySubscriptionId(String subscriptionId, String sell) {

        if (Utils.isConnectingToInternet(this)) {

            fl_subscription.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();
            final WebServices request = retrofit.create(WebServices.class);

            String doctor_id = manager.getPreferences(SubscriptionPackages.this, "doctor_id");

            Call<SubscriptionModels> call = request.RazorSubscriptionId(subscriptionId, doctor_id);

            call.enqueue(new Callback<SubscriptionModels>() {
                @Override
                public void onResponse(@NonNull Call<SubscriptionModels> call, @NonNull retrofit2.Response<SubscriptionModels> response) {
                    SubscriptionModels jsonResponse = response.body();
                    assert jsonResponse != null;
                    fl_subscription.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success")) {

                        fl_subscription.setVisibility(View.GONE);

                        popuplogout(subscriptionId, sell, jsonResponse.getSub_id(), "recurring");

                    } else {
                        Toast.makeText(SubscriptionPackages.this, jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<SubscriptionModels> call, @NonNull Throwable t) {
                    fl_subscription.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(SubscriptionPackages.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void popuplogout(final String trim, final String sell, String sub_id, String keywords) {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_country);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_no = dialog_data.findViewById(R.id.btn_no);
        TextView tv_no_template = dialog_data.findViewById(R.id.tv_no_template);
        TextView heading = dialog_data.findViewById(R.id.heading);
        heading.setVisibility(View.GONE);
        Button btn_yes = dialog_data.findViewById(R.id.btn_yes);

        if (keywords.equalsIgnoreCase("recurring")) {
            btn_yes.setVisibility(View.GONE); // todo hide out of india recurring payment
            btn_no.setText("Click here to continue");// todo hide out of india recurring payment
            heading.setVisibility(View.VISIBLE); // todo hide out of india recurring payment
            tv_no_template.setVisibility(View.GONE);
            btn_no.setTextSize(13);
        }

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PaymentGateway.class);
                intent.putExtra("subscription_id", trim);
                intent.putExtra("amount", sell);
                intent.putExtra("country", "OOI");
                intent.putExtra("razor_pay_sub_id", sub_id);
                mContext.startActivity(intent);
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, PaymentGateway.class);
                intent.putExtra("subscription_id", trim);
                intent.putExtra("amount", sell);
                intent.putExtra("country", "India");
                intent.putExtra("razor_pay_sub_id", sub_id);
                mContext.startActivity(intent);
            }
        });
        dialog_data.show();
    }

}
