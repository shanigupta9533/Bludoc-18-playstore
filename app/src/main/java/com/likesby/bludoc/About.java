package com.likesby.bludoc;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;


import com.likesby.bludoc.ModelLayer.Entities.CmsItem;
import com.likesby.bludoc.ModelLayer.Entities.CmsResponse;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class About extends AppCompatActivity
{
    private static final String TAG = "AboutUs_____";
    Context mContext;

    ApiViewHolder mLocationViewHolder;
    CompositeDisposable mBag = new CompositeDisposable();
    ArrayList<CmsItem> CmsList ;


    MyDB myDB;
    TextView tv_title,tv_desc;
    LinearLayout linearLayout_action;
    SessionManager manager;
    Button btn_type;
    ImageView imageView,back;

    FrameLayout frameLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        mContext = About.this;

        myDB = new MyDB(mContext);
        manager = new SessionManager();

        frameLayout = findViewById(R.id.fl_progress_bar_about_us);
        Calendar c2 = Calendar .getInstance();
        System.out.println("Current time => "+c2.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date2 = df.format(c2.getTime());

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
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);
        imageView = findViewById(R.id.iv_about_us);
        back = findViewById(R.id.btn_back_edit_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initViewHolder() {
        mLocationViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);

        if (Utils.isConnectingToInternet(mContext)) {


            mLocationViewHolder.Cms("about_us")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseCategories);
        } else {
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    SingleObserver<CmsResponse> responseCategories = new SingleObserver<CmsResponse>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(CmsResponse response) {
            if (response != null) {
                if (response.getMessage() != null) {

                    CmsList  =  response.getCms();
                    tv_title.setText(CmsList.get(0).getTitle());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        tv_desc.setText(Html.fromHtml(CmsList.get(0).getDescription(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        tv_desc.setText(Html.fromHtml(CmsList.get(0).getDescription()));
                    }
                    if(CmsList.get(0).getImage().equalsIgnoreCase(""))
                    {
                        Picasso.with(mContext).
                                load( R.drawable.com_facebook_profile_picture_blank_square)

                                .into(imageView, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {

                                    }
                                });
                    }
                    else {
                        Picasso.with(mContext).
                                load(CmsList.get(0).getImage())

                                .into(imageView, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso.with(mContext).
                                                load(R.drawable.com_facebook_profile_picture_blank_square)

                                                .into(imageView, new com.squareup.picasso.Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError() {

                                                    }
                                                });
                                    }
                                });
                    }
                    frameLayout.setVisibility(View.GONE);
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
