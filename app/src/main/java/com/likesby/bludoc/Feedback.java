package com.likesby.bludoc;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.Entities.Response;
import com.likesby.bludoc.SessionManager.SessionManager;
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

public class Feedback  extends AppCompatActivity
{
    private static final String TAG = "ActiveMember_____";
    RecyclerView recyclerViewViewMembers;
    Context mContext;

    ApiViewHolder mLocationViewHolder;
    CompositeDisposable mBag = new CompositeDisposable();
  //  ArrayList<MyinvitesItem> trackingsArrayList ;
  //  MyInvitesAdapter activeMemberAdapter;
    Button actionBtn;
    SearchView searchView;
    MyDB myDB;
    CheckBox active_member_select_all_cb;
    TextView textViewSelectAll;
    LinearLayout linearLayout_action;
    SessionManager manager;
    Button btnSubmit;
    RadioButton rbSuggestion,rbFeedback,rbComplaint;
    EditText etSubject,etMessage;
    FrameLayout frameLayout;
    String type= "Feedback";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_complaints);

        mContext = Feedback.this;

        myDB = new MyDB(mContext);
        manager = new SessionManager();

        initViews();


        Calendar c2 = Calendar .getInstance();
        System.out.println("Current time => "+c2.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String date2 = df.format(c2.getTime());

        Log.e(TAG,"CurrentDateTimeString3 = "+date2);
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
        btnSubmit = findViewById(R.id.submit_feedback_suggestion);
        frameLayout = findViewById(R.id.fl_progress_bar_feedback);
        rbSuggestion = findViewById(R.id.rb_suggestion);
        rbFeedback = findViewById(R.id.rb_feedback);
        rbComplaint = findViewById(R.id.rb_complaints);
        etSubject = findViewById(R.id.et_subject);
        etMessage = findViewById(R.id.et_message);

        rbFeedback.setChecked(true);
        rbSuggestion.setChecked(false);
        rbComplaint.setChecked(false);

        rbFeedback.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    type= "Feedback";
                    rbSuggestion.setChecked(false);
                    rbComplaint.setChecked(false);
                }
            }
        });

        rbSuggestion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    type= "Suggestion";
                    rbFeedback.setChecked(false);
                    rbComplaint.setChecked(false);
                }
            }
        });


        rbComplaint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    type= "Complaint";
                    rbSuggestion.setChecked(false);
                    rbFeedback.setChecked(false);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSubject.getText().toString().trim().equalsIgnoreCase(""))
                {
                    etSubject.setError("Subject Required");
                    etSubject.setFocusable(true);
                }
                else
                {
                    if(etMessage.getText().toString().trim().equalsIgnoreCase(""))
                    {
                        etMessage.setError("Message Required");
                        etMessage.setFocusable(true);
                    }
                    else
                    {
                        if (Utils.isConnectingToInternet(mContext)) {
                            frameLayout.setVisibility(View.VISIBLE);
                            if(manager.getPreferences(mContext,"doctor_id") != null) {
                                mLocationViewHolder.Feedback(manager.getPreferences(mContext,"doctor_id") , type, etSubject.getText().toString().trim(), etMessage.getText().toString().trim())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(responseFeedback);
                            }
                        } else {
                            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }


    private  void popup()
    {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.feedback);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);




        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog_data.dismiss();

            }
        });
        dialog_data.show();
    }


    private void initViewHolder() {
        mLocationViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
    }

    SingleObserver<Response> responseFeedback = new SingleObserver<Response>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(Response response) {
            if (response != null) {
                if (response.getMessage() != null) {
                    Toast.makeText(mContext, type+" added Successfully", Toast.LENGTH_SHORT).show();
                    frameLayout.setVisibility(View.GONE);
                    popup();


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
