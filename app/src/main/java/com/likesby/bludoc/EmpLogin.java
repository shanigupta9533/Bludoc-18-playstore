package com.likesby.bludoc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaos.view.PinView;
import com.likesby.bludoc.Adapter.CountryCodes;
import com.likesby.bludoc.ModelLayer.Entities.OtpResponse;
import com.likesby.bludoc.ModelLayer.Entities.ResponseRegister;
import com.likesby.bludoc.ModelLayer.Entities.User;
import com.likesby.bludoc.ModelLayer.Entities.UserResponse;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.sms.OtpReceivedInterface;
import com.likesby.bludoc.sms.SmsBroadcastReceiver;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.onesignal.OneSignal;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EmpLogin extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "EmpLogin_____";
    ApiViewHolder apiViewHolder;
    Context mContext;
    MyDB myDB;
    CompositeDisposable mBag = new CompositeDisposable();
    EditText  et_email;

    SessionManager manager;
    //ProgressBar progressBarCountryCode,progressBarSend;
    private int RESOLVE_HINT = 2;

    GoogleApiClient mGoogleApiClient;
    SmsBroadcastReceiver mSmsBroadcastReceiver;
    Class activity_name=null;
    TextView tv_please_enter_mob,submit,submitPhone,submit_resend,btn_clear;
    String mobile_="";
    View green_view;
    boolean otpReceivedFlag = true;
    FrameLayout frameLayoutProgressMainl;
    String country_code;
    RelativeLayout login_verifyOTPLayout;
    TextView login_buttonVerify;
    LinearLayout ll_submit;
    PinView login_editTextOTP;
    ImageView btn_back;
    TextView login_textView_otp;
    com.lamudi.phonefield.PhoneEditText inputMobileNumber_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mContext = EmpLogin.this;
        myDB = new MyDB(mContext);
        manager = new SessionManager();
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setElevation(0);
        }
        inputMobileNumber_login = findViewById(R.id.login_editTextInputMobile);
        //  inputMobileNumber_login.setHint(R.string.phone_hint);
        inputMobileNumber_login.setDefaultCountry("IN");
        login_textView_otp = findViewById(R.id.login_textView_otp);
        submit = findViewById(R.id.submit);
        submit_resend = findViewById(R.id.submit_resend);
        login_editTextOTP = findViewById(R.id.login_editTextOTP);
        submitPhone = findViewById(R.id.submit_phone);
        btn_clear = findViewById(R.id.btn_clear);
        ll_submit = findViewById(R.id.ll_submit);
   //     ll_submit_phone = findViewById(R.id.ll_submit_phone);
        login_verifyOTPLayout = findViewById(R.id.login_verifyOTPLayout);
        login_buttonVerify = findViewById(R.id.login_buttonVerify);
        //login_verifyOTPLayout.setVisibility(View.GONE);
        submit_resend.setVisibility(View.GONE);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        submitPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Objects.requireNonNull(inputMobileNumber_login.getPhoneNumber()).toString().trim().equalsIgnoreCase(""))
                {
                    Toast.makeText(mContext, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    frameLayoutProgressMainl.setVisibility(View.VISIBLE);
                    manager.setPreferences(mContext,"mobile",inputMobileNumber_login.getPhoneNumber().toString().trim());
                    intentCall(SplashActivity.class);
                    /*if(manager.contains(mContext,"verify_otp"))
                    {
                        if(Objects.requireNonNull(inputMobileNumber_login.getPhoneNumber()).toString().trim().equalsIgnoreCase(String.valueOf(manager.getPreferences(mContext,"verify_otp"))))
                        {
                            frameLayoutProgressMainl.setVisibility(View.VISIBLE);
                            manager.setPreferences(mContext,"mobile",inputMobileNumber_login.getPhoneNumber().toString().trim());
                            intentCall(SplashActivity.class);
                        }
                    }
                    else {

                    }*/
                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_editTextOTP.setText("");
            }
        });
        if(manager.contains(mContext,"verify_otp")) {
            if (!(manager.getPreferences(mContext, "verify_email").equalsIgnoreCase("")))
            {
                login_textView_otp.setVisibility(View.VISIBLE);
                login_textView_otp.setText(manager.getPreferences(mContext, "verify_email"));

            }
        }

        login_buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(Objects.requireNonNull(login_editTextOTP.getText()).toString().trim().equalsIgnoreCase(""))
            {
                Toast.makeText(mContext, "Enter OTP", Toast.LENGTH_SHORT).show();
            }
            else {
                if (manager.contains(mContext, "verify_otp")) {
                    if (Objects.requireNonNull(login_editTextOTP.getText()).toString().trim().equalsIgnoreCase(String.valueOf(manager.getPreferences(mContext, "verify_otp")))) {
                        frameLayoutProgressMainl.setVisibility(View.VISIBLE);
                   //     ll_submit.setVisibility(View.GONE);
                       // login_verifyOTPLayout.setVisibility(View.GONE);
                       // ll_submit_phone.setVisibility(View.VISIBLE);
                        try {
                            setLogin(manager.getPreferences(mContext, "verify_email"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else
                        Toast.makeText(mContext, "OTP Doesn't Match", Toast.LENGTH_SHORT).show();
                }
            }

            }
        });
        /*TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        assert manager != null;
        String countryCodeValue  = manager.getSimCountryIso().toUpperCase();
        String[] rl= getResources().getStringArray(R.array.country_code);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(countryCodeValue.trim())){
                Toast.makeText(mContext, ""+g[0], Toast.LENGTH_SHORT).show();;
                break;
            }
        }
*/
        //CountryCodes cc = new CountryCodes( this );


        TelephonyManager man = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        assert man != null;
         country_code = CountryCodes.getCode( man.getSimCountryIso() );
            //Toast.makeText(mContext,  = country_code , Toast.LENGTH_SHORT).show();
       /* if(country_code==null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            } else {
                country_code = mContext.getResources().getConfiguration().locale.getISO3Country();
            }
        }*/

       /* TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        assert tm != null;
        CountryCodes countryCodes  = tm.getNetworkCountryIso();
        Toast.makeText(mContext, countryCodeValue, Toast.LENGTH_SHORT).show();*/

        // init broadcast receiver
        mSmsBroadcastReceiver = new SmsBroadcastReceiver();
        //set google api client for hint request
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();
        mSmsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);
        // get mobile number from phone
      //  getHintPhoneNumber();

        initViews();

        initViewHolder();
        oneSignelId();

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            if(!(Objects.requireNonNull(extras.getString("email")).equalsIgnoreCase("")))
            {
               // ll_submit.setVisibility(View.GONE);
                frameLayoutProgressMainl.setVisibility(View.VISIBLE);
                hideKeyboard(mContext);
                submit_resend.setVisibility(View.VISIBLE);

                login_verifyOTPLayout.setVisibility(View.VISIBLE);
             //   ll_submit_phone.setVisibility(View.VISIBLE);

                try {
                    setLogin(extras.getString("email"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        submit.setEnabled(true);
    }

    private void oneSignelId()
    {
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {

                Log.e(TAG, "User:" + userId);
                manager.setPreferences(mContext,"App_id",userId);

                if (registrationId != null)
                    Log.e(TAG, "registrationId:" + registrationId);

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void initViews() {
        et_email = findViewById(R.id.et_email);
        frameLayoutProgressMainl = findViewById(R.id.fl_progress_main);
        green_view = findViewById(R.id.green_view);
        submit_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiViewHolder.getOtpFromBackEnd(manager.getPreferences(mContext,"verify_email"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseOTP);

                frameLayoutProgressMainl.setVisibility(View.VISIBLE);
                hideKeyboard(mContext);
                submit_resend.setVisibility(View.VISIBLE);

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_email.getText().toString().trim().equalsIgnoreCase(""))
                {
                    et_email.setError(getResources().getString(R.string.email_id) );
                }
                else
                {
                    if(Utils.emailValidator(et_email.getText().toString().trim()))
                    {
                        manager.setPreferences(mContext,"verify_email",et_email.getText().toString().trim());

                            login_textView_otp.setVisibility(View.VISIBLE);
                            login_textView_otp.setText(manager.getPreferences(mContext, "verify_email"));


                        apiViewHolder.getOtpFromBackEnd(et_email.getText().toString().trim())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(responseOTP);

                       // ll_submit.setVisibility(View.GONE);
                        frameLayoutProgressMainl.setVisibility(View.VISIBLE);
                        hideKeyboard(mContext);
                        submit_resend.setVisibility(View.VISIBLE);
                       /* try {
                            setLogin(et_email.getText().toString().trim());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                    else {
                        Toast.makeText(mContext, "Email Id is not valid", Toast.LENGTH_SHORT).show();
                    }
                }

              /*  if(!("").equalsIgnoreCase(et_email.getText().toString().trim()) ) {
                   // if(mobile.getText().length() != 10) {

                    //mobile.clearFocus();
                    //mobile.setFocusable(false);
                    try {

                        String user_no1 = inputMobileNumber_login.getRawInput().toString().trim();;
                        String user_no = inputMobileNumber_login.getPhoneNumber().toString().trim();
                       // setLogin(country_code+"-"+mobile.getText().toString().trim());
                        String country_code = user_no.replace(user_no1,"");
                        country_code = country_code.replace("+","");
                        //setLogin(user_no1);
                        setLogin(et_email.getText().toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // startSMSListener(country_code+mobile.getText().toString().trim());

            }
                else {
                    Toast.makeText(mContext, "Enter Mobile Number", Toast.LENGTH_SHORT).show();}
            }*/
              }
        });
    }



    private void initViewHolder() {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        }

    //-----------------------------------------------------------
    //region Get Product Response
    private void setLogin(String email) throws JSONException {
        //if(subscriptionList.size()!=0) {
        if (Utils.isConnectingToInternet(mContext)) {
            /*JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer_mobile","8545989999685");*/
            apiViewHolder.setUserLogin( email)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responseUserLogin);
        } else {
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
        // }
    }

    SingleObserver<ResponseRegister> responseUserLogin = new SingleObserver<ResponseRegister>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseRegister response) {
            if (response != null) {

                Log.e(TAG, "responseUserLogin: >> " + response.getMessage());

                if (response.getMessage() == null) {
                    Toast.makeText(mContext, "Login Failed", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.getMessage().equals("Register")) {
                    manager.deletePreferences(mContext,"verify_otp");
                    frameLayoutProgressMainl.setVisibility(View.GONE);
                    // manager.setPreferences(mContext,"mobile",country_code+"-"+mobile_);
                    manager.setPreferences(mContext,"doctor_id",response.getDoctorId());
                  //  manager.setPreferences(mContext,"mobile",response.getMobile());
                    manager.setPreferences(mContext,"email",response.getEmail());
                    manager.setPreferences(mContext,"name",response.getName());
                    manager.setPreferences(mContext,"designation",response.getDesignationName());
                   // getHintPhoneNumber();
                    intentCall(SplashActivity.class);
                } else  if (response.getMessage().equals("Login"))  {
                    manager.deletePreferences(mContext,"verify_otp");
                    manager.setPreferences(mContext,"doctor_id",response.getDoctorId());
                    manager.setPreferences(mContext,"mobile",response.getMobile());
                    manager.setPreferences(mContext,"email",response.getEmail());
                    manager.setPreferences(mContext,"name",response.getName());
                    manager.setPreferences(mContext,"designation",response.getDesignationName());
                    intentCall(SplashActivity.class);
//                    if(manager.getPreferences(mContext,"mobile").equals("")) {
//                        getHintPhoneNumber();
//                    }else {
//                        intentCall(SplashActivity.class);
//                    }
                 //   myDB.createRecordsUserSubscription(response.getSubscriptionHistory());
                  //  myDB.updateUserSubscription("AC",myDB.getUser().getCustomerId(),response.getSubscriptionHistory().get(0).getSubscriptionId());

                   // manager.setPreferences(mContext,"cat_id",response.getSubscriptionHistory().get(0).getCategoryId());
                 //LocationPermissionsActivity
                }
                else if(response.getMessage().equals("You are inactive contact admin")){
                    Toast.makeText(mContext, "Your account is Inactive. Contact Admin.", Toast.LENGTH_SHORT).show();
                    popuplogout();
                }
                else if(response.getMessage().equals("Your account is inactive or deleted please contact admin")){
                    Toast.makeText(mContext, "Your account is Inactive or Deleted. Contact Admin.", Toast.LENGTH_SHORT).show();
                    popuplogout();
                }
            }else  if (response.getMessage().equals("Your profile is deleted"))  {
                frameLayoutProgressMainl.setVisibility(View.GONE);
                Toast.makeText(mContext, "The account with this email id has already been deleted, try login with another email id.", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(EmpLogin.this, SocialLogin.class);
                startActivity(intent1);
                finish();
            }
            else
            {
                frameLayoutProgressMainl.setVisibility(View.GONE);
                //progressBarSend.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(Throwable e) {
            frameLayoutProgressMainl.setVisibility(View.GONE);
            Log.e(TAG, "onError: responseLogin >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
//            progressBarSend.setVisibility(View.GONE);
        }
    };


    @Override
    public void onOtpReceived(String otp) {
        Toast.makeText(mContext, "onOtpReceived = " + otp, Toast.LENGTH_SHORT).show();
        if (!(otp.equalsIgnoreCase(""))) {

            otp = otp.substring(0, 4);
            if(manager.contains(mContext,"verify_otp")) {
                if (otp.equals(String.valueOf(manager.getPreferences(mContext, "verify_otp")))) {
                    Intent intent = new Intent(mContext, RegisterDetails.class);
                    startActivity(intent);
                }
            }
            //inputOtp.setText(otp);
        }
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
        btn_no.setVisibility(View.GONE);
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

    public void intentCall(Class activity_name)
    {
        Intent intent = new Intent(mContext,activity_name);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onOtpTimeout() {
        Toast.makeText(this, "Time out, please resend", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override public void onSuccess(Void aVoid) {

              //  Toast.makeText(mContext, "SMS Retriever starts", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "SmsRetrieverClient Error", Toast.LENGTH_LONG).show();
            }
        });
    }



    SingleObserver<OtpResponse> responseOTP = new SingleObserver<OtpResponse>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(OtpResponse response) {
            if (response != null) {
                if(response.getMessage().equalsIgnoreCase("Otp Sent"))
                {
                    et_email.setText("");
                    frameLayoutProgressMainl.setVisibility(View.GONE);
                    login_verifyOTPLayout.setVisibility(View.VISIBLE);
                    manager.setPreferences(mContext,"verify_otp",String.valueOf(response.getOtp()));
                    submit.setEnabled(true);
                }
                else if(response.getMessage().equals("You are inactive contact admin")){
                    Toast.makeText(mContext, "Your account is Inactive. Contact Admin.", Toast.LENGTH_SHORT).show();
                    popuplogout();
                }
                else if(response.getMessage().equals("Your account is inactive or deleted please contact admin")){
                    Toast.makeText(mContext, "Your account is Inactive or Deleted. Contact Admin.", Toast.LENGTH_SHORT).show();
                    popuplogout();
                }


            }
        }
        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: responseLogin >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
            //progressBarSend.setVisibility(View.GONE);
        }
    };

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void getHintPhoneNumber() {
        frameLayoutProgressMainl.setVisibility(View.GONE);
     //   ll_submit_phone.setVisibility(View.VISIBLE);
        ll_submit.setVisibility(View.GONE);
        HintRequest hintRequest =
                new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();
        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(mIntent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result if we want hint number
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // <-- will need to process phone number string
                    assert credential != null;

                    String mob_ = credential.getId();
                    mob_ = mob_.substring(Math.max(0, mob_.length() - 10));
                    mobile_ = mob_;
                    frameLayoutProgressMainl.setVisibility(View.VISIBLE);
                    manager.setPreferences(mContext,"mobile",country_code+"-"+mobile_);
                   // startSMSListener(country_code+"-"+mob_);

                    frameLayoutProgressMainl.setVisibility(View.VISIBLE);
                    apiViewHolder.Register1( manager.getPreferences(mContext,"doctor_id"),
                            "",
                            manager.getPreferences(mContext,"email"),
                            manager.getPreferences(mContext,"App_id"),country_code+"-"+mobile_
                            )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responseRegister1);

                }
            }
            /*else
            {
                Toast.makeText(mContext, "Select Number from the list", Toast.LENGTH_LONG).show();
                finish();
            }*/
        }
    }

    SingleObserver<ResponseRegister> responseRegister1 = new SingleObserver<ResponseRegister>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseRegister response) {
            if (response != null) {

                Log.e(TAG, "responseRegister1: >> " + response.getMessage());
                frameLayoutProgressMainl.setVisibility(View.GONE);
                if (response.getMessage() == null) {
                    Toast.makeText(mContext, "Registration Failed", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.getMessage().equals("Profile Updated")) {
                    manager.setPreferences(mContext,"doctor_id",response.getDoctorId());
                    manager.setPreferences(mContext,"email",response.getEmail());
//                    manager.setPreferences(mContext,"name",response.getName());

                    String name_ = response.getName();

                    manager.setPreferences(mContext, "name", name_);
                    intentCall(SplashActivity.class);
                }
                else
                {
                    Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
                    intentCall(SplashActivity.class);
                }
            }
            else
            {
                frameLayoutProgressMainl.setVisibility(View.GONE);
                Toast.makeText(mContext, ""+response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            frameLayoutProgressMainl.setVisibility(View.GONE);
            Log.e(TAG, "onError: responseRegister1 >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

}