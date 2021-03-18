package com.likesby.bludoc.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.likesby.bludoc.ModelLayer.Entities.ResponseAddPatient;
import com.likesby.bludoc.ModelLayer.Entities.ResponsePatients;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities.SubcriptionsItem;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.SplashActivity;
import com.likesby.bludoc.constants.ApplicationConstant;
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
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.likesby.bludoc.Fragment.CreatePrescription.certificate_selection;

public class PatientRegistration extends Fragment {
    private static final String TAG = "PatientReg_____";

    Context mContext;
    Dialog dialog;
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-6756023122563497/5728747901";
    private UnifiedNativeAd nativeAd;
    View v;
    static RecyclerView rView;
    static LinearLayoutManager lLayout;
    TextView save_prescribe, save_later;
    private EditText address, blood_group;
    EditText et_name, et_age, et_mobile, et_email;
    ImageView info, info1;
    String gender_ = "Male";
    ApiViewHolder apiViewHolder;
    CompositeDisposable mBag = new CompositeDisposable();
    SessionManager manager;
    boolean prescribeFlag = false;
    AlertDialog alertDialog;
    String patientId;
    ImageView back;
    FrameLayout fl_progress_bar;
    private static final int PERMISSION_REQUEST_CODE = 99;
    RadioButton rb_year, rb_month, rb_male, rb_female, rb_other;
    String age_type = "yr";
    private AdView mAdView;
    Boolean showNativeAdFlag;
    boolean flag_reset_free = false;
    private boolean isYear;
    private boolean isConstant;
    private TextView date_f_birth;
    private boolean isMonth;
    private ImageView cancel_action;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        manager = new SessionManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater.inflate(R.layout.state_frag, container, false);
        v = inflater.inflate(R.layout.patient_registration, container, false);
        initCalls(v);
        mAdView = v.findViewById(R.id.adView);
        if (manager.contains(mContext, "show_banner_ad"))
            BannerAd();
        String sub_valid = "", premium_valid = "";
        boolean flag_reset_paid = false;
        Date date1 = null, date2 = null;
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
                            premium_valid = si.getDays();
                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


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
                            premium_valid = si.getDays();
                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


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

                    showNativeAdFlag = false;

                    if (manager.contains(mContext, "purchased_new")) {
                        if (manager.contains(mContext, "show_banner_ad"))
                            manager.deletePreferences(mContext, "show_banner_ad");
                        if (manager.getPreferences(mContext, "purchased_new").equalsIgnoreCase("true")) {

                            showNativeAdFlag = false;
                        }
                    }

                } else   //Free Subscription
                {
                    if (flag_reset_free) {

                        showNativeAdFlag = false;
                    } else {

                        showNativeAdFlag = true;
                    }
                }
            } else if (!flag_reset_free) {

                showNativeAdFlag = true;
            }
        } else {

            showNativeAdFlag = true;
        }
        return v;
    }

    private void initCalls(View view) {
        hideKeyboard(mContext);
        initViews(view);
        initViewHolder();

    }

    private void BannerAd() {
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder()/*.addTestDevice("31B09DFC1F78AF28F2AFB1506F51B0BF")*/.build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //  Toast.makeText(mContext, "ErrorCode = "+errorCode, Toast.LENGTH_SHORT).show();
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }

    private void initViewHolder() {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
    }

    private void initViews(View view) {
        rb_year = view.findViewById(R.id.rb_year);
        rb_year.setChecked(true);
        rb_month = view.findViewById(R.id.rb_month);
        rb_male = view.findViewById(R.id.rb_male);
        rb_female = view.findViewById(R.id.rb_female);
        rb_other = view.findViewById(R.id.rb_other);
        date_f_birth = view.findViewById(R.id.date_of_birth);
        address = view.findViewById(R.id.address);
        blood_group = view.findViewById(R.id.blood_group);
        rb_male.setChecked(true);

        save_prescribe = view.findViewById(R.id.save_prescribe);
        save_later = view.findViewById(R.id.save_later);
        et_name = view.findViewById(R.id.et_name);
        cancel_action = view.findViewById(R.id.cancel_action);
        cancel_action.setVisibility(View.GONE);
        et_age = view.findViewById(R.id.et_age);
        et_email = view.findViewById(R.id.et_mail);
        et_mobile = view.findViewById(R.id.et_whtsapp);
        info = view.findViewById(R.id.info);
        fl_progress_bar = view.findViewById(R.id.fl_progress_layout);
        info1 = view.findViewById(R.id.info1);

       /* Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                et_age.addTextChangedListener(new TextWatcher(){
                    public void afterTextChanged(Editable s) {}

                    public void beforeTextChanged(CharSequence s, int start, int count, int after){}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String strEnteredVal = et_age.getText().toString().trim();

                        if (!strEnteredVal.equals("")) {
                            int num = Integer.parseInt(strEnteredVal);
                            if (num < 151) {
                                et_age.setText("" + num);
                            } else {
                                et_age.setText("");
                            }
                        }

                    }});
            }
        });*/

        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date_f_birth.setText("");

            }
        });

        date_f_birth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {

                    cancel_action.setVisibility(View.VISIBLE);

                } else {

                    cancel_action.setVisibility(View.GONE);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        date_f_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        isConstant = true;
                        String age = getAge(year, monthOfYear, dayOfMonth);

                        if (Integer.parseInt(age) < 0) {

                            age = "0";

                        }

                        et_age.setText(age);
                        et_age.setSelection(et_age.getText().toString().length());
                        rb_year.setChecked(true);

                        String myFormat = "yyyy-MM-dd"; // your format
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                        date_f_birth.setText(sdf.format(myCalendar.getTime()));
                    }

                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000).getTime());

                datePickerDialog.show();

            }
        });


        save_prescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prescribeFlag = true;


                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        //checkPermission();
                        if (checkPermission()) {
                            saveData();
                        } else {
                            requestPermission(); // Code for permission
                        }
                    } else {
                        saveData();
                    }
                }
            }
        });

        rb_year.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_month.setChecked(false);
                    age_type = "yr";

                    if (TextUtils.isEmpty(et_age.getText().toString())) {

                        Toast.makeText(mContext, "Enter Your Age...", Toast.LENGTH_SHORT).show();

                    } else if (!isConstant) {

                        String age = et_age.getText().toString();
                        et_age.setText(String.valueOf(Integer.parseInt(age) / 12));
                        et_age.setSelection(et_age.getText().toString().length());
                    }

                    isConstant = false;

                }
            }
        });

        rb_month.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_year.setChecked(false);
                    age_type = "months";

                    if (TextUtils.isEmpty(et_age.getText().toString())) {

                        Toast.makeText(mContext, "Enter Your Age...", Toast.LENGTH_SHORT).show();

                    } else {

                        String age = et_age.getText().toString();
                        et_age.setText(String.valueOf(Integer.parseInt(age) * 12));
                        et_age.setSelection(et_age.getText().toString().length());
                        isConstant = false;
                    }

                }
            }
        });

        rb_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_female.setChecked(false);
                    rb_other.setChecked(false);
                    gender_ = "Male";

                }
            }
        });

        rb_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_male.setChecked(false);
                    rb_other.setChecked(false);
                    gender_ = "Female";
                }
            }
        });

        rb_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb_female.setChecked(false);
                    rb_male.setChecked(false);
                    gender_ = "Other";
                }
            }
        });

        back = view.findViewById(R.id.btn_back_edit_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mContext);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        save_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prescribeFlag = false;
                //  saveData();
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        checkPermission();
                        if (checkPermission()) {
                            saveData();
                        } else {
                            requestPermission(); // Code for permission
                        }
                    } else {
                        saveData();
                    }
                }
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setMessage("On Approval, prescription will be directly sent to this number");

                alertDialogBuilder.setPositiveButton("Okay",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                alertDialog.dismiss();
                            }
                        });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setMessage("On Approval, prescription will be directly sent to this mail Id");

                alertDialogBuilder.setPositiveButton("Okay",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                alertDialog.dismiss();
                            }
                        });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }


    private void saveData() {

        if (Utils.isConnectingToInternet(mContext)) {
            if (et_name.getText().toString().trim().equalsIgnoreCase("")) {
                et_name.requestFocus();
                et_name.setError("Patient's Full Name");
            } else {

                if (gender_.equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "Select Gender", Toast.LENGTH_SHORT).show();
                } else {
                    String strEnteredVal = et_age.getText().toString().trim();
                    if (!strEnteredVal.equals("")) {
                        int num = Integer.parseInt(strEnteredVal);
                        if (age_type.equals("yr") && num < 151) {
                            fl_progress_bar.setVisibility(View.VISIBLE);
                            apiViewHolder.PatientRegister(et_name.getText().toString().trim(),
                                    et_age.getText().toString().trim() + " " + age_type, gender_, manager.getPreferences(mContext, "doctor_id"),
                                    et_mobile.getText().toString().trim(), et_email.getText().toString().trim(), address.getText().toString().trim(), blood_group.getText().toString().trim(), date_f_birth.getText().toString())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(responsePatientRegister);

                        } else if (age_type.equals("month") || age_type.equals("months")) {
                            if (age_type.equals("months"))
                                if (et_age.getText().toString().trim().equals("1"))
                                    age_type = "month";
                            fl_progress_bar.setVisibility(View.VISIBLE);
                            apiViewHolder.PatientRegister(et_name.getText().toString().trim(),
                                    et_age.getText().toString().trim() + " " + age_type, gender_, manager.getPreferences(mContext, "doctor_id"),
                                    et_mobile.getText().toString().trim(), et_email.getText().toString().trim(), address.getText().toString().trim(), blood_group.getText().toString().trim(), date_f_birth.getText().toString())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(responsePatientRegister);

                        } else {
                            et_age.requestFocus();
                            et_age.setError("Age not allowed");
                            Toast.makeText(mContext, "Age not allowed", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        fl_progress_bar.setVisibility(View.VISIBLE);
                        apiViewHolder.PatientRegister(et_name.getText().toString().trim(),
                                "UnAvailable", gender_, manager.getPreferences(mContext, "doctor_id"),
                                et_mobile.getText().toString().trim(), et_email.getText().toString().trim(), address.getText().toString().trim(), blood_group.getText().toString().trim(), date_f_birth.getText().toString())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(responsePatientRegister);

                    }
                }
            }
        } else {
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isBloodGroupValid(String bloodGroup, Context context) {
        if (bloodGroup != null) {
            String[] bloodGroups = context.getResources().getStringArray(R.array.blood_groups);
            int len = bloodGroups.length;
            for (int i = 0; i < len; i++) {
                if (bloodGroup.equalsIgnoreCase(bloodGroups[i]))
                    return true;
            }
        }
        return false;
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

//    private void checkPermission() {
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.READ_CONTACTS) + ContextCompat
//                .checkSelfPermission(getActivity(),
//                        Manifest.permission.WRITE_CONTACTS)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale
//                    (getActivity(), Manifest.permission.READ_CONTACTS) ||
//                    ActivityCompat.shouldShowRequestPermissionRationale
//                            (getActivity(), Manifest.permission.WRITE_CONTACTS)) {
//
//                requestPermissions(
//                        new String[]{Manifest.permission
//                                .READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
//                        PERMISSION_REQUEST_CODE);
//
//            } else {
//                requestPermissions(
//                        new String[]{Manifest.permission
//                                .READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
//                        PERMISSION_REQUEST_CODE);
//            }
//        } else {
//            saveData();
//            // write your logic code if permission already granted
//        }
//    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_CONTACTS);
        int result2 = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_CONTACTS);

        if (result2 == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    private void requestPermission() {
        if ((shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) && (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CONTACTS))) {
            Toast.makeText(mContext, "Contacts permission required. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS
            }, PERMISSION_REQUEST_CODE);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 &&
//                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Permission is granted. Continue the action or workflow
//                    // in your app.
//                    saveData();
//                }  else {
//                    // Explain to the user that the feature is unavailable because
//                    // the features requires a permission that the user has denied.
//                    // At the same time, respect the user's decision. Don't link to
//                    // system settings in an effort to convince the user to change
//                    // their decision.
//                }
//                return;
//        }
//        // Other 'case' lines to check for other
//        // permissions this app might request.
//    }
//


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveData();
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
        }
    }


    SingleObserver<ResponseAddPatient> responsePatientRegister = new SingleObserver<ResponseAddPatient>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseAddPatient response) {
            if (response != null) {

                Log.e(TAG, "responsePatientRegister: >> " + response.getMessage());

                if (response.getMessage() == null) {
                    fl_progress_bar.setVisibility(View.GONE);
                } else if (response.getMessage().equals("Patient Register")) {

                    if (!("").equalsIgnoreCase(response.getPMobile())) {
                        if (!contactExists(getActivity(), response.getPMobile())) {
                            createContact(response);
                        }
                    }
                    fl_progress_bar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Patient Added Successfully", Toast.LENGTH_SHORT).show();
                    clearEditTexts();
                    patientId = response.getPatientId();
                    updatecustomer();
                }
            } else {
                fl_progress_bar.setVisibility(View.GONE);
                Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            fl_progress_bar.setVisibility(View.GONE);
            Log.e(TAG, "onError: responsePatientRegister >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


    public boolean contactExists(Activity _activity, String number) {
        if (number != null) {
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
            String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
            Cursor cur = _activity.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
            try {
                if (cur.moveToFirst()) {
                    return true;
                }
            } finally {
                if (cur != null)
                    cur.close();
            }
            return false;
        } else {
            return false;
        }
    }// contactExists

    public void createContact(ResponseAddPatient response) {
        String displayName = response.getPName();
        String mobileNumber = response.getPMobile();


        ArrayList<ContentProviderOperation> contentProviderOperationArrayList = new ArrayList<ContentProviderOperation>();

        contentProviderOperationArrayList.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //Name
        if (displayName != null) {
            contentProviderOperationArrayList.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            displayName).build());
        }

        //Mobile Number
        if (mobileNumber != null) {
            contentProviderOperationArrayList.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }


        // Creating new contact
        try {
            mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, contentProviderOperationArrayList);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void updatecustomer() {
        apiViewHolder.getPatients(manager.getPreferences(mContext, "doctor_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responsePatients);
    }

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
                    SplashActivity splashActivity = new SplashActivity();
                    splashActivity.setPatients(response.getPatients());

                    if (prescribeFlag) {
                        final Dialog dialog_data = new Dialog(mContext);
                        dialog_data.setCancelable(false);

                        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);
                        }

                        dialog_data.setContentView(R.layout.popup_diagnosis);

                        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
                        Window window = dialog_data.getWindow();
                        lp_number_picker.copyFrom(window.getAttributes());

                        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

                        //window.setGravity(Gravity.CENTER);
                        window.setAttributes(lp_number_picker);

                        ProgressBar pb = dialog_data.findViewById(R.id.pb);


                        final RadioButton rb_prescription = dialog_data.findViewById(R.id.rb_prescription);
                        final RadioButton rb_certificate = dialog_data.findViewById(R.id.rb_certificate);

                        rb_prescription.setChecked(true);
                        rb_prescription.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked)
                                    rb_certificate.setChecked(false);
                            }
                        });

                        rb_certificate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked)
                                    rb_prescription.setChecked(false);
                            }
                        });
                        FrameLayout fl_layout = dialog_data.findViewById(R.id.fl_layout);
                        final Button btnProceed = dialog_data.findViewById(R.id.btn_proceed);
                        final Button btnCancel = dialog_data.findViewById(R.id.btn_cancel);
                        if (showNativeAdFlag) {
                            NativeAd(dialog_data, pb, btnProceed, btnCancel);

                        } else {
                            btnProceed.setVisibility(View.VISIBLE);
                            btnCancel.setVisibility(View.VISIBLE);
                            fl_layout.setVisibility(View.GONE);
                        }
                        CountDownTimer countDownTimer = new CountDownTimer(2000, 2000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                btnCancel.setVisibility(View.VISIBLE);
                                btnProceed.setVisibility(View.VISIBLE);
                            }
                        };
                        countDownTimer.start();
//
//
//
//
//                        final Dialog dialog_data = new Dialog(mContext);
//                        dialog_data.setCancelable(false);
//
//                        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                            Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);
//                        }
//
//                        dialog_data.setContentView(R.layout.popup_diagnosis);
//
//                        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
//                        Window window = dialog_data.getWindow();
//                        lp_number_picker.copyFrom(window.getAttributes());
//
//                        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
//                        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//                        //window.setGravity(Gravity.CENTER);
//                        window.setAttributes(lp_number_picker);
//
//                        final RadioButton rb_prescription = dialog_data.findViewById(R.id.rb_prescription);
//                        final RadioButton rb_certificate = dialog_data.findViewById(R.id.rb_certificate);
//
//                        rb_prescription.setChecked(true);
//                        rb_prescription.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                if(isChecked)
//                                    rb_certificate.setChecked(false);
//                            }
//                        });
//
//                        rb_certificate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                            @Override
//                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                if(isChecked)
//                                    rb_prescription.setChecked(false);
//                            }
//                        });
//                        Button btnProceed = dialog_data.findViewById(R.id.btn_proceed);
//                        Button btnCancel = dialog_data.findViewById(R.id.btn_cancel);

                        btnProceed.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                               /* if(rb_prescription.isChecked())
                                {
                                    patient_id = PATIENT_ID.getText().toString().trim();

                                    btn_create_patient.setVisibility(View.GONE);

                                    ll_patients_view.setVisibility(View.GONE);
                                    ll_prescription_view.setVisibility(View.VISIBLE);

                                    ll_certificate.setVisibility(View.GONE);
                                    ll_main_certificate_details.setVisibility(View.GONE);

                                    ll_medicinal_lab.setVisibility(View.VISIBLE);
                                    ll_main_lab_test_details.setVisibility(View.GONE);

                                    ll_medicine_product.setVisibility(View.VISIBLE);
                                    ll_main_medicine_details.setVisibility(View.GONE);

                                    ll_end_note.setVisibility(View.VISIBLE);
                                    ll_main_end_note_details.setVisibility(View.VISIBLE);

                                    certificate_selection = false;
                                    patientdetails.setText(mFilteredList.get(pos).getPName() + " - " + mFilteredList.get(pos).getGender() + " / " + mFilteredList.get(pos).getAge());
                                }
                                else  {
                                    patient_id = PATIENT_ID.getText().toString().trim();
                                    pos = getAdapterPosition();
                                    btn_create_patient.setVisibility(View.GONE);
                                    ll_patients_view.setVisibility(View.GONE);
                                    ll_prescription_view.setVisibility(View.VISIBLE);
                                    ll_certificate.setVisibility(View.VISIBLE);
                                    ll_main_certificate_details.setVisibility(View.VISIBLE);
                                    ll_medicinal_lab.setVisibility(View.GONE);
                                    ll_main_lab_test_details.setVisibility(View.GONE);
                                    ll_medicine_product.setVisibility(View.GONE);
                                    ll_main_medicine_details.setVisibility(View.GONE);
                                    ll_end_note.setVisibility(View.GONE);
                                    ll_main_end_note_details.setVisibility(View.GONE);
                                    certificate_selection = true;
                                    patientdetails.setText(mFilteredList.get(pos).getPName() + " - " + mFilteredList.get(pos).getGender() + " / " + mFilteredList.get(pos).getAge());
                                }*/

                                if (rb_prescription.isChecked()) {

                                    CreatePrescription myFragment = new CreatePrescription();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("patient_id", patientId);
                                    CreatePrescription.is_on_case_history = true;
                                    CreatePrescription.is_on_medicines = false;
                                    CreatePrescription.is_on_lab_test = false;
                                    certificate_selection = false;

                                    myFragment.setArguments(bundle);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();


                                } else if (rb_certificate.isChecked()) {
                                    CreatePrescription.is_on_case_history = false;
                                    CreatePrescription.is_on_medicines = false;
                                    CreatePrescription.is_on_lab_test = false;
                                    certificate_selection = true;

                                    CreatePrescription myFragment = new CreatePrescription();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("patient_id", patientId);
                                    myFragment.setArguments(bundle);
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack(null).commit();
                                }


                                dialog_data.dismiss();
                            }
                        });


                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_data.dismiss();
                            }
                        });

                        dialog_data.show();


                    } else {
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialog));
                        builder.setMessage("Patient added successfully. Would you like to add another patient?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //manager.getPreferences(Registration_.this, "service_provider");
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        hideKeyboard(mContext);
                                        FragmentManager fm = getActivity().getSupportFragmentManager();
                                        fm.popBackStack();
                                        dialog.cancel();
                                    }
                                });
                        androidx.appcompat.app.AlertDialog alert = builder.create();
                        alert.show();

                    }
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: responseMedicines >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    private void clearEditTexts() {
        et_age.setText("");
        et_name.setText("");
        et_email.setText("");
        et_mobile.setText("");
        date_f_birth.setText("");
        address.setText("");
        blood_group.setText("");

    }


    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void NativeAd(Dialog v, ProgressBar pb, Button btnProceed, Button btnCancel) {
        refreshAd(v, pb, btnProceed, btnCancel);
        //  "ca-app-pub-3940256099942544/1044960115"
    }

    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView   the view to be populated
     */
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {

            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {


            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.

                    super.onVideoEnd();
                }
            });
        } else {

        }
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     * @param
     */
    private void refreshAd(final Dialog dialog_view, final ProgressBar pb, final Button btnProceed, final Button btnCancel) {

        AdLoader.Builder builder = new AdLoader.Builder(mContext, ADMOB_AD_UNIT_ID);

        builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    // OnUnifiedNativeAdLoadedListener implementation.
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        boolean isDestroyed = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = isRemoving();
                        }
                        if (isDestroyed) {
                            unifiedNativeAd.destroy();
                            return;
                        }
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        nativeAd = unifiedNativeAd;
                        FrameLayout frameLayout = dialog_view.findViewById(R.id.fl_adplaceholder);
                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        UnifiedNativeAdView adView = (UnifiedNativeAdView) inflater.inflate(R.layout.my_ad_layout, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);

                        pb.setVisibility(View.GONE);
                        btnProceed.setVisibility(View.VISIBLE);
                        btnCancel.setVisibility(View.VISIBLE);
                    }
                });

           /* VideoOptions videoOptions =
                    new VideoOptions.Builder().build();*/

        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder
                        .withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        String error =
                                                String.format(
                                                        "domain: %s, code: %d, message: %s",
                                                        loadAdError.getDomain(),
                                                        loadAdError.getCode(),
                                                        loadAdError.getMessage());
//                                        Toast.makeText(
//                                                mContext,
//                                                "Failed to load native ad with error " + error,
//                                                Toast.LENGTH_SHORT)
//                                                .show();
                                    }
                                })
                        .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }


}
