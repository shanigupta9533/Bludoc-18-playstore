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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.likesby.bludoc.ModelLayer.Entities.ProfileDetails;
import com.likesby.bludoc.ModelLayer.Entities.Response;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Profile extends AppCompatActivity {
    TextView tab_full_name,   tab_mob,   tab_mail,   tab_designation,   tab_registration,  tab_UG,  tab_PG,  tab_time,
            tab_alt_mail,   tab_days,   tab_close,   tab_logo,  tab_sign ,tab_Add , tab_alt_mob ;
    Button button_edit_profile,btn_delete;
    Context mContext;
    SessionManager manager;
    ImageButton profile_pic;
    ImageView back;
    CompositeDisposable mBag = new CompositeDisposable();
    private static String TAG = "profile";

    ApiViewHolder apiViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        mContext = Profile.this;
        manager = new SessionManager();
    }


    private void initViews() {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
        button_edit_profile = findViewById(R.id.btn_edit_profile);

        tab_full_name = findViewById(R.id.tab_full_name);
        tab_mob = findViewById(R.id.tab_mob);
        tab_mail = findViewById(R.id.tab_mail);
        tab_designation = findViewById(R.id.tab_designation);
        tab_registration = findViewById(R.id.tab_registration);
        tab_UG = findViewById(R.id.tab_UG);
        tab_PG = findViewById(R.id.tab_PG);
        tab_time = findViewById(R.id.tab_time);
        tab_alt_mail = findViewById(R.id.tab_alt_mail);
        tab_days = findViewById(R.id.tab_days);
        tab_close = findViewById(R.id.tab_close);
        tab_logo = findViewById(R.id.tab_logo);
        tab_sign = findViewById(R.id.tab_sign);
        profile_pic= findViewById(R.id.profile_pic);
        tab_Add= findViewById(R.id.tab_Add);
        tab_alt_mob= findViewById(R.id.tab_alt_mob);
        back = findViewById(R.id.btn_back_edit_profile);
        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               popuplogout();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(!(manager.getPreferences(mContext,"image").equalsIgnoreCase(""))) {
            profile_pic.setVisibility(View.VISIBLE);
            Picasso.with(mContext).
                    load(manager.getPreferences(mContext, "image"))
                    //.resize(width_custom-100 , width_custom-100)
                    // .memoryPolicy(MemoryPolicy.)
                    // .centerCrop()

                    .into(profile_pic, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            profile_pic.setVisibility(View.GONE);
                        }
                    });
        }
        else
            profile_pic.setVisibility(View.GONE);

        /*String name_ = manager.getPreferences(mContext,"name").trim();
        if(name_.contains("Dr.")) {
            name_ = name_.replace("Dr.", "");
            manager.setPreferences(mContext, "name", name_.trim());
        }
        String name_2 = manager.getPreferences(mContext,"name").trim();
        if(name_2.contains("Dr."))
        {
            name_2 = name_2.replace("Dr.","");
            manager.setPreferences(mContext,"name",name_2.trim());
        }*/
        if(!("").equalsIgnoreCase(manager.getPreferences(mContext,"name").trim())) {
            tab_full_name.setText(manager.getPreferences(mContext,"name").trim());
        }


        if(manager.contains(mContext,"mobile"))
            tab_mob.setText(manager.getPreferences(mContext,"mobile"));


        if(manager.contains(mContext,"email"))
            tab_mail.setText(manager.getPreferences(mContext,"email"));

        if(manager.contains(mContext,"designation_name"))
            tab_designation.setText(manager.getPreferences(mContext,"designation_name"));

        if(manager.contains(mContext,"registration_no"))
            tab_registration.setText(manager.getPreferences(mContext,"registration_no"));

        if(manager.contains(mContext,"ug_name"))
            tab_UG.setText(manager.getPreferences(mContext,"ug_name"));

        if(manager.contains(mContext,"pg_name"))
            tab_PG.setText(manager.getPreferences(mContext,"pg_name"));

        if(manager.contains(mContext,"visiting_hr_from")) {
            String[] visiting_hr_from_details = manager.getPreferences(mContext, "visiting_hr_from").split(Pattern.quote("|"));
            ;
            String[] visiting_hr_to_details = manager.getPreferences(mContext, "visiting_hr_to").split(Pattern.quote("|"));
            ;
            if (visiting_hr_from_details.length > 1) {
                tab_time.setText(visiting_hr_from_details[0].toLowerCase() + " - " + visiting_hr_to_details[0].toLowerCase() + ",   " + visiting_hr_from_details[1].toLowerCase() + " - " + visiting_hr_to_details[1].toLowerCase());

            } else {
                tab_time.setText(visiting_hr_from_details[0].toLowerCase() + " - " + visiting_hr_to_details[0].toLowerCase());
            }
        }


        if(manager.contains(mContext,"email_letter_head"))
            tab_alt_mail.setText(manager.getPreferences(mContext,"email_letter_head"));

        if(manager.contains(mContext,"working_days"))
            tab_days.setText(manager.getPreferences(mContext,"working_days"));

        if(manager.contains(mContext,"close_day"))
            tab_close.setText(manager.getPreferences(mContext,"close_day"));

        if(manager.contains(mContext,"addtional_qualification"))
            tab_Add.setText(manager.getPreferences(mContext,"addtional_qualification"));

        if(manager.contains(mContext,"mobile_letter_head"))
            tab_alt_mob.setText(manager.getPreferences(mContext,"mobile_letter_head"));

        if(manager.contains(mContext,"image"))
        {
            if(manager.getPreferences(mContext,"image").equalsIgnoreCase(""))
                tab_logo.setText("No");
            else
                tab_logo.setText("Yes");
        }else
            tab_logo.setText("No");


        if(manager.contains(mContext,"signature"))
        {
            if(manager.getPreferences(mContext,"signature").equalsIgnoreCase(""))
                tab_sign.setText("No");
            else
                tab_sign.setText("Yes");
        }else
            tab_sign.setText("No");



        button_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Profile.this, RegisterDetails.class);
                startActivity(intent1);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private  void popuplogout()
    {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_logout);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_no = dialog_data.findViewById(R.id.btn_no);
        Button btn_yes = dialog_data.findViewById(R.id.btn_yes);
        TextView tv_no_template = dialog_data.findViewById(R.id.tv_no_template);
        TextView tv_no_template1= dialog_data.findViewById(R.id.tv_no_template1);
        tv_no_template.setText("Would to like to delete your profile permanently?");
        tv_no_template1.setVisibility(View.VISIBLE);
        tv_no_template1.setText("All the patient records and files will be lost. You cannot login again with the same mail id hereafter.");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiViewHolder.deleteProfile(manager.getPreferences(mContext, "doctor_id"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseProfile);

            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
            }
        });
        dialog_data.show();
    }


    SingleObserver<Response> responseProfile = new SingleObserver<Response>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(Response response) {
            if (response != null) {

                Log.e(TAG, "profileDetails: >> " + response.getMessage());

                if (response.getMessage() == null) {
                    Toast.makeText(mContext, "Unable to process your request", Toast.LENGTH_SHORT).show();
                } else if (response.getMessage().equals("Doctor Deleted")) {
                    mContext.getSharedPreferences("status", 0).edit().clear().commit();
                    Intent i  = new Intent(mContext, SplashActivity.class);
                    startActivity(i);
                    finishAffinity();
                }
            }
            else
            {
               
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
           
            Log.e(TAG, "onError: profileDetails >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

}
