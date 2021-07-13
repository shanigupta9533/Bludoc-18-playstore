package com.likesby.bludoc;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.likesby.bludoc.ModelLayer.Entities.BannersItem;
import com.likesby.bludoc.ModelLayer.Entities.LabTestItem;
import com.likesby.bludoc.ModelLayer.Entities.MedicinesItem;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.Entities.ProfileDetails;
import com.likesby.bludoc.ModelLayer.Entities.ResponseBanners;
import com.likesby.bludoc.ModelLayer.Entities.ResponseLabTest;
import com.likesby.bludoc.ModelLayer.Entities.ResponseMedicines;
import com.likesby.bludoc.ModelLayer.Entities.ResponsePatients;
import com.likesby.bludoc.ModelLayer.Entities.ResponseVersion;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.utils.DateUtils;
import com.likesby.bludoc.viewModels.ApiViewHolder;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity
{ private static String TAG = "SplashActivity___";
    Uri data =null;
    SessionManager manager;
    Context mContext;
    static CompositeDisposable mBag = new CompositeDisposable();
    public static boolean deeplinkFlag = false;
    public static String[] deeplinkData = null;
    int secondsDelayed = 100;
    static ApiViewHolder apiViewHolder;
    //public  static  ArrayList<MedicinesItem> medicinesItemArrayList = new ArrayList<>();
    public  static ArrayList<BannersItem> bannerArrayList = new ArrayList<>();
    public static ArrayList<PatientsItem> patientsItemArrayList = new ArrayList<>();
  //  public static ArrayList<LabTestItem> labTestItemArrayList = new ArrayList<>();
    int start = 0,x1 =0,x2 = 0,x3 =0 ,x4 =0, x5 =0, x6 = 0,x7 = 0;

    MyDB myDB;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.splash_activity);
            mContext = SplashActivity.this;
            myDB = new MyDB(mContext);
            mBag = new CompositeDisposable();
            manager = new SessionManager();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.parseColor("#000000"));

            }
         /*   SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.format(new Date());
            Date d = new Date();

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            date2 = dateFormat.parse();
            String dateWithoutTime = d.toString().substring(0, 10);
            Log.e(TAG, "Date >> " + DateUtils.printDifference(dateFormat,)));
           // Log.e(TAG, "Date >> " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(sdf));
            //Toast.makeText(mContext, "Subscription Added Successfully", Toast.LENGTH_SHORT).show();
            manager.setPreferences(mContext,"test",""+sdf.toString());*/
         if(manager.contains(mContext,"purchased_new")){
             manager.deletePreferences(mContext,"purchased_new");
             manager.deletePreferences(mContext,"purchased_date");
         }

            checkLogin();
            apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);

            mContext.deleteDatabase("Tracksido.db");
        }


    private void checkLogin() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
            if(manager.contains(mContext,"doctor_id"))
            {
                //if(!manager.getPreferences(mContext,"mobile").equals("")) {
                    apiViewHolder.getVersion()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responseVersion);
//                }else {
//                    Intent intent1 = new Intent(SplashActivity.this, SocialLogin.class);
//                    startActivity(intent1);
//                    finish();
//                }
            }
            else
            {
                Intent intent1 = new Intent(SplashActivity.this, SocialLogin.class);
                startActivity(intent1);
                finish();
            }
            }
        }, 1000);
    }

    public void Banners() {
        apiViewHolder.getBanners()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBanners);
    }
    public void Medicines() {
        apiViewHolder.getMedicines(manager.getPreferences(mContext, "doctor_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseMedicines);
    }
    public void ProfileDetails() {
        apiViewHolder.getProfileDetails(manager.getPreferences(mContext, "doctor_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseProfile);

    }
    public void Patients() {
        apiViewHolder.getPatients(manager.getPreferences(mContext, "doctor_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responsePatients);
    }

    public void LabTests() {
        apiViewHolder.getLabTests(manager.getPreferences(mContext, "doctor_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseLabTests);
    }



    private void finalMethod() {
        Log.e(TAG, "finalMethod: >> " + x1+", "+ x3+", "+ x4+", "+ x5+", ");

        if(x1==1&&x3==1 &&x4==1 && x5==1){
//set the new Content of your activity
            if(start == 0) {
                start =1;


                Intent intent1 = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent1);
                finish();

            }
        }
    }

    SingleObserver<ResponseVersion> responseVersion = new SingleObserver<ResponseVersion>() {
        @Override
        public void onSubscribe(Disposable d)
        {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseVersion response) {
            if (response != null) {

                Log.e(TAG, "responseVersion: >> " + response.getMessage());

                if (response.getMessage() == null) {
                } else if (response.getMessage().equals("Version")) {
                    //BuildConfig has critical issue sometimes when clean and rebuild project. It should act as core class not not any import library
                   /* public final class BuildConfig {
                        public static final boolean DEBUG = Boolean.parseBoolean("true");
                        public static final String APPLICATION_ID = "com.likesby.bludoc";
                        public static final String BUILD_TYPE = "debug";
                        public static final String FLAVOR = "";
                        public static final int VERSION_CODE = 12;
                        public static final String VERSION_NAME = "12.0";
                    }*/  // like this
                    float version_code = BuildConfig.VERSION_CODE;


                    Log.e(TAG, "version_code >> " + version_code);
//                    Medicines();
//
//                    Banners();
//                    ProfileDetails();
//                    Patients();
//                    LabTests();


                    if (response.getVersion() != null) {
                        if (response.getVersion().get(0).getUpdateVersion() != null && response.getVersion().get(0).getMinVersion() != null) {
                            float fetched_update_version = Float.parseFloat(response.getVersion().get(0).getUpdateVersion());
                            float fetched_min_version = Float.parseFloat(response.getVersion().get(0).getMinVersion());
                            //8.0>=8.0 && 8.0<9.0
                            if (version_code >= fetched_min_version && version_code < fetched_update_version) {
                                popup(false);
                            } else if (version_code == fetched_update_version) {
                                Medicines();
                                ;
                                Banners();
                                ProfileDetails();
                                Patients();
                                LabTests();

                            } else if (version_code <= fetched_min_version) {
                                popup(true);
                            } else if (version_code > fetched_update_version) {
                                Medicines();
                                ;
                                Banners();
                                ProfileDetails();
                                Patients();
                                LabTests();
                            }

                        }
                    }

                }
            }
            else
            {
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: responseVersion >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    private  void popup(boolean flagger)
    {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(false);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_no_app_update);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_update = dialog_data.findViewById(R.id.btn_update_play_store);
        Button btn_later = dialog_data.findViewById(R.id.btn_later);
        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);

        if(flagger)
        {
         btn_later.setVisibility(View.GONE);
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                }
                catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });

        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
                Medicines();;
                Banners();
                ProfileDetails();
                Patients();
                LabTests();
            }
        });


        dialog_data.show();
    }


    SingleObserver<ResponseProfileDetails> responseProfile = new SingleObserver<ResponseProfileDetails>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseProfileDetails response) {
            if (response != null) {

                Log.e(TAG, "profileDetails: >> " + response.getMessage());

                if (response.getMessage() == null) {
                    ProfileDetails();
                } else if (response.getMessage().equals("Profile Details")) {
                    if(response.getStatus() != null) {
                        if(response.getStatus().equalsIgnoreCase("Active")) {
                            manager.setPreferences(mContext, "doctor_id", response.getDoctorId());
                            manager.setPreferences(mContext, "name", response.getName());
                            manager.setPreferences(mContext, "email", response.getEmail());
                            if (!(response.getMobile().equalsIgnoreCase("")))
                                manager.setPreferences(mContext, "mobile", response.getMobile());
                            manager.setPreferences(mContext, "registration_no", response.getRegistrationNo());
                            manager.setPreferences(mContext, "speciality_id", response.getSpecialityId());
                            manager.setPreferences(mContext, "ug_id", response.getUgId());
                            manager.setPreferences(mContext, "pg_id", response.getPgId());
                            manager.setPreferences(mContext, "designation_id", response.getDesignationName());
                            manager.setPreferences(mContext, "addtional_qualification", response.getAddtionalQualification());
                            manager.setPreferences(mContext, "mobile_letter_head", response.getMobileLetterHead());
                            manager.setPreferences(mContext, "email_letter_head", response.getEmailLetterHead());
                            manager.setPreferences(mContext, "working_days", response.getWorkingDays());
                            manager.setPreferences(mContext, "visiting_hr_from", response.getVisitingHrFrom());
                            manager.setPreferences(mContext, "visiting_hr_to", response.getVisitingHrTo());
                            manager.setPreferences(mContext, "close_day", response.getCloseDay());
                            manager.setPreferences(mContext, "speciality_name", response.getSpecialityName());
                            manager.setPreferences(mContext, "ug_name", response.getUgName());
                            manager.setPreferences(mContext, "pg_name", response.getPgName());
                            manager.setPreferences(mContext, "designation_name", response.getDesignationName());
                            manager.setPreferences(mContext, "signature", response.getSignature());
                            manager.setPreferences(mContext, "logo", response.getLogo());
                            manager.setPreferences(mContext, "image", response.getImage());
                            manager.setPreferences(mContext, "clinic_name", response.getClinicName());
                            manager.setPreferences(mContext, "clinic_address", response.getClinicAddress());
                            manager.setObjectProfileDetails(mContext, "profile", response);
                            x1 = 1;
                            finalMethod();
                        }else {
                           // Toast.makeText(mContext, "Your account is Inactive or Deleted. Contact Admin at bludocapp@gmail.com", Toast.LENGTH_LONG).show();
                            popuplogout();
                        }
                    }
                }
            }
            else
            {
                ProfileDetails();
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            ProfileDetails();
            Log.e(TAG, "onError: profileDetails >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


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
        btn_no.setVisibility(View.GONE);
        dialog_data.setCanceledOnTouchOutside(false);
        Button btn_yes = dialog_data.findViewById(R.id.btn_yes);
        TextView text1 = dialog_data.findViewById(R.id.tv_no_template);
        ImageView img= dialog_data.findViewById(R.id.iv_no_template);
        img.setVisibility(View.GONE);
        btn_yes.setText("Okay");
        text1.setText("Your account is Inactive or Deleted. Please contact Admin at bludocapp@gmail.com");
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.getSharedPreferences("status", 0).edit().clear().commit();
                myDB.deleteAllMedicines();
                myDB.deleteAllLabTests();
                SplashActivity splashActivity = new SplashActivity();
                splashActivity.setPatientsnull();
                Intent i  = new Intent(mContext, SplashActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });
        if(dialog_data!=null && !dialog_data.isShowing())
            dialog_data.show();
    }



    SingleObserver<ResponseMedicines> responseMedicines = new SingleObserver<ResponseMedicines>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseMedicines response) {
            if (response != null) {

                Log.e(TAG, "ResponseMedicines: >> " + response.getMessage());
                if (response.getMessage() == null) {
                } else if (response.getMessage().equals("medicines")) {
                    //medicinesItemArrayList = new ArrayList<>();
                   // medicinesItemArrayList = response.getMedicines();
                    if(myDB.deleteAllMedicines())
                   myDB.createRecordsMedicine(response.getMedicines());
                }
            }
            else
            {
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            Medicines();
            Log.e(TAG, "onError: responseMedicines >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    SingleObserver<ResponseBanners> responseBanners = new SingleObserver<ResponseBanners>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseBanners response) {

            if (response != null) {
                Log.e(TAG, "responseBanners: >> " + response.getMessage());

                if (response.getMessage() == null) {
                } else if (response.getMessage().equals("Banners")) {
                    bannerArrayList = new ArrayList<>();
                    bannerArrayList = response.getBanners();

                }
                x3 = 1;
                finalMethod();
            }
            else
            {
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: responseBanners >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    private SingleObserver<ResponsePatients> responsePatients = new SingleObserver<ResponsePatients>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponsePatients response) {
            if (response != null) {
                Log.e(TAG, "ResponseMedicines: >> " + response.getMessage());
                if (response.getMessage() == null) {

                } else if (response.getMessage().equals("patients")) {
                    patientsItemArrayList = new ArrayList<>();
                    patientsItemArrayList = response.getPatients();
                   // Collections.reverse(patientsItemArrayList);
                }
                x4=1;
                finalMethod();
            }else {
                Patients();
            }


        }

        @Override
        public void onError(Throwable e) {
            Patients();
            Log.e(TAG, "onError: responseMedicines >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


    public ArrayList<PatientsItem> getpatients(){
        return patientsItemArrayList;
    }

    public void setPatients(ArrayList<PatientsItem> patients){
        patientsItemArrayList = new ArrayList<>();
        if(patients!=null)
        {
           // Collections.reverse(patients);
            patientsItemArrayList= patients;
        }

    }

    public void setPatientsnull(){
        patientsItemArrayList = new ArrayList<>();

    }



    private SingleObserver<ResponseLabTest> responseLabTests = new SingleObserver<ResponseLabTest>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseLabTest response) {
            if (response != null) {

                Log.e(TAG, "ResponseMedicines: >> " + response.getMessage());

                if (response.getMessage() == null) {

                } else if (response.getMessage().equals("Lab Test")) {

                    if(myDB.deleteAllLabTests())
                    myDB.createRecordsLabTest(response.getLabTest());

                }
                x5=1;
                finalMethod();

            }else {
                LabTests();
            }


        }

        @Override
        public void onError(Throwable e) {
            LabTests();
            Log.e(TAG, "onError: responseMedicines >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


    }
