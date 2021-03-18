package com.likesby.bludoc;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.likesby.bludoc.ModelLayer.Entities.OtpResponse;
import com.likesby.bludoc.ModelLayer.Entities.User;
import com.likesby.bludoc.NetworkCheck.CheckNetwork;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SocialLogin extends AppCompatActivity {
    private static final String TAG = "SocialLogin_____";
    private static final int RC_SIGN_IN = 111;
    boolean loginGmailFlag = false,loginFBFlag = false;
    ApiViewHolder apiViewHolder;
    Context mContext;
    MyDB myDB;
    CompositeDisposable mBag = new CompositeDisposable();
    Button btnMobNo;
    ImageButton ib_fb;
    public  static String name_str="",account_id_str="",email_str="",image_str="";

    //GMAIL
    GoogleSignInClient mGoogleSignInClient;
    //SignInButton signInButton;
    GoogleSignInOptions gso;

    //FACEBOOK
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginButton loginButton ;

 //   private static ViewPagerCustomDuration mPager;

    private static int currentPage = 0  ;
    private static int NUM_PAGES = 0;

    ImageButton google,facebook;
    TextView termsConditions;
    SessionManager manager;
    String emaill = "";

    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_login);

        mContext = SocialLogin.this;
        myDB = new MyDB(mContext);
        manager  = new SessionManager();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        initViews();
        initViewHolder();
       // initViewpager();

        /*User user = myDB.getUser();
            if(user.getCustomerId()!=null && !user.getCustomerId().equalsIgnoreCase(""))
            {
                mBag.dispose();
                Intent intent = new Intent(mContext, MainActivity.class); //TrialSubscriptionActivity
                startActivity(intent);
                finish();
            }
*/

        //---------- Facebook ---------------------------------------

        callbackManager = CallbackManager.Factory.create();

        loginButton =  findViewById(R.id.login_button);
        ib_fb =  findViewById(R.id.sign_in_button_fb);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
       // Toast.makeText(mContext, "Facebook  isLoggedIn = "+isLoggedIn, Toast.LENGTH_SHORT).show();

        // If you are using in a fragment, call loginButton.setFragment(this);

        ib_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // App code
                Toast.makeText(mContext, "onSuccess", Toast.LENGTH_SHORT).show();

                if(AccessToken.getCurrentAccessToken() != null) {
                    updateUI(loginResult);
                }
            }

            @Override
            public void onCancel() {
                Log.e(TAG,"onCancel = ");

                // App code

                Toast.makeText(mContext, "onCancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e(TAG,"FacebookException = "+exception);
                Toast.makeText(mContext, "onError", Toast.LENGTH_SHORT).show();

            }
        });

        //--------- Gmail -------------------------------------------

        // Set the dimensions of the sign-in button.
        //signInButton = findViewById(R.id.sign_in_button);
        google = findViewById(R.id.sign_in_button2);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(SocialLogin.this, gso);

        //-----------------------------------------------------------

        oneSignelId();
       printHashKey(mContext);

//        ib_fb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loginButton.performClick();
//            }
//        });
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

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void initViews() {
        btnMobNo = findViewById(R.id.btn_mob_no);
        progressBar = findViewById(R.id.progress_bar_social);

        termsConditions = findViewById(R.id.termsconditions);
        termsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PrivacyPolicy.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBag.dispose();
    }

    private void initViewHolder()  {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);

        btnMobNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetwork.isInternetAvailable(mContext))
                {
                    manager.setPreferences(mContext,"Flow","1");
                   // popup();

                    Intent intent = new Intent(mContext,EmpLogin.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        }


    private  void popup()
    {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_email);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());
        dialog_data.setCanceledOnTouchOutside(false);
        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        final EditText et_email_id = dialog_data.findViewById(R.id.et_email_id);
        Button btn_add = dialog_data.findViewById(R.id.btn_register);
        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_email_id.getText().toString().trim().equalsIgnoreCase(""))
                {
                    et_email_id.setError(getResources().getString(R.string.email_id) );
                }
                else
                {
                    if(Utils.emailValidator(et_email_id.getText().toString().trim()))
                    {
                        dialog_data.dismiss();
                        apiViewHolder.getOtpFromBackEnd(et_email_id.getText().toString().trim())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(responseOTP);



                    }
                    else {
                        Toast.makeText(mContext, "Email Id is not valid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

            }
        });
        dialog_data.show();
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
    // }

    //--------- Gmail -------------------------------------------
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mGoogleSignInClient.signOut();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(mContext, "Gmail Login Successful", Toast.LENGTH_SHORT).show();

            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(mContext, "Gmail Login Failed", Toast.LENGTH_SHORT).show();

        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account!=null)
        {
            /*name_str = account.getDisplayName();
            email_str =  account.getEmail() ;
            account_id_str = account.getId();
            image_str = Objects.requireNonNull(account.getPhotoUrl()).toString();*/

           /* User user = new User();
            user.setCustomerId( ""    );
            user.setParentId(   ""   );
            user.setDepartmentId( ""  );
            user.setDesignationId( "" );
            user.setCustomerName( account.getDisplayName()  );
            user.setCustomerEmail( account.getEmail() );
            user.setCustomerMobile("" );
            user.setCustomerImage( Objects.requireNonNull(account.getPhotoUrl()).toString());
            user.setAddress(    ""   );
            user.setCity(     ""      );
            user.setPincode(   ""     );
            user.setStatus(      ""  );
            user.setFbId(     ""   );
            user.setGmailId(account.getId());
            user.setRole(      ""  );

            if(myDB.getUser().getCustomerName()!=null)
                myDB.deleteUser();

            myDB.createRecordsUser(user);*/
            String email = "";
            loginGmailFlag = true;
            manager.setPreferences(mContext,"Flow","2");
            if(account.getEmail()!=null)
            {
                email = account.getEmail();
                manager.setPreferences(mContext,"email",account.getEmail());
            }
            else
                email = "";
            if(account.getPhotoUrl()!=null)
            manager.setPreferences(mContext,"profile_pic",Objects.requireNonNull(account.getPhotoUrl()).toString());
            if(account.getDisplayName()!=null)
            manager.setPreferences(mContext,"name",account.getDisplayName());

            Intent intent = new Intent(mContext,EmpLogin.class);
            intent.putExtra("email",email);
            startActivity(intent);
        }
    }

    private void updateUI(LoginResult account) {

        if(account!=null)
        {
            GraphRequest request = GraphRequest.newMeRequest(
                    account.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            String email = "",firstName="",lastName="",id="",link="";

                            // Application code
                            try {
                                Log.i("" +
                                        "Response",response.toString());
                                if(response.getJSONObject()!=null)
                                {
                                if(!(response.getJSONObject().getString("email").equalsIgnoreCase("")))
                                {
                                    email = response.getJSONObject().getString("email");
                                    emaill = email;
                                }

                                 firstName = response.getJSONObject().getString("first_name");
                                 lastName = response.getJSONObject().getString("last_name");

                                if (Profile.getCurrentProfile()!=null)
                                {
                                    link = Profile.getCurrentProfile().getProfilePictureUri(200, 200).toString();
                                    Log.e("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                                }

                                manager.setPreferences(mContext,"email",email);
                                manager.setPreferences(mContext,"profile_pic",link);
                                manager.setPreferences(mContext,"name",firstName.trim()+" "+lastName.trim());
                                Log.e("Login" + "Email", email);
                                Log.e("Login"+ "FirstName", firstName);
                                Log.e("Login" + "LastName", lastName);

                                    loginFBFlag = true;
                                    manager.setPreferences(mContext,"Flow","3");
                                    Intent intent = new Intent(mContext,EmpLogin.class);
                                    intent.putExtra("email",email);
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                Log.e("FB","JSONException = " +e);
                            }
                        }
                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email,first_name,last_name,gender");
            request.setParameters(parameters);
            request.executeAsync();

        }
    }



        //===============================================================================

   /* public void method() {
        TextView t1, t2, t4;
        ImageView e1, e2, e4;
        final EditText edtFirstName1, edLastName, edtAddress1;
        TextView txt_FirstName, txt_LastName, txt_Address_Line_1;

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.googleplus);
        t1 = (TextView) dialog.findViewById(R.id.txt1);
        t2 = (TextView) dialog.findViewById(R.id.txt2);
        t4 = (TextView) dialog.findViewById(R.id.txt4);
        e1 = (ImageView) dialog.findViewById(R.id.e1);
        e2 = (ImageView) dialog.findViewById(R.id.e2);
        e4 = (ImageView) dialog.findViewById(R.id.e4);
        e31 = (EditText) findViewById(R.id.e31);
        e32 = (EditText) findViewById(R.id.e32);
        e33 = (EditText) findViewById(R.id.e33);
        e34 = (EditText) findViewById(R.id.e34);
        e35 = (EditText) findViewById(R.id.e35);
        EnterOtp1 = (TextView) findViewById(R.id.EnterOtp);
        resend1 = (TextView) findViewById(R.id.resend);
        relativeData = (RelativeLayout) findViewById(R.id.relativeData);
        relativeDataOTP = (RelativeLayout) findViewById(R.id.relativeDataOTP);
        edtFirstName1 = (EditText) dialog.findViewById(R.id.edt_FirstName);
        edLastName = (EditText) dialog.findViewById(R.id.edt_LastName);
        //  edtCompany = (TextView) dialog.findViewById(R.id.edt_Company);
        edtAddress1 = (EditText) dialog.findViewById(R.id.edt_Address_Line_1);
        txt_FirstName = (TextView) dialog.findViewById(R.id.txt_FirstName);
        txt_LastName = (TextView) dialog.findViewById(R.id.txt_LastName);
        //  txt_Company = (TextView) dialog.findViewById(R.id.txt_Company);
        txt_Address_Line_1 = (TextView) dialog.findViewById(R.id.txt_Address_Line_1);

        relativeData.setVisibility(View.VISIBLE);
        relativeDataOTP.setVisibility(View.GONE);


        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strFirstName, strLastName, strAddress1;
                strFirstName = edtFirstName1.getText().toString();
                strLastName = edLastName.getText().toString();
                strAddress1 = edtAddress1.getText().toString();

                String namePattern = "[a-zA-Z ]+";
                int x = 0;

                if (strFirstName.isEmpty()) {
                    edtFirstName1.setError("First Name Cannot be Empty!");
                    x = 1;
                } else if ((!strFirstName.matches(namePattern))) {
                    edtFirstName1.setError("First Name cannot contain digits");
                    x = 1;
                }
                if (strLastName.isEmpty()) {
                    edLastName.setError("Last Name Cannot be Empty!");
                    x = 1;
                } else if ((!strLastName.matches(namePattern))) {
                    edLastName.setError("Last Name cannot contain digits");
                    x = 1;
                }
                if (strAddress1.isEmpty()) {
                    edtAddress1.setError("Mobile Number Cannot be Empty!");
                    x = 1;
                } else if (strAddress1.length() != 10) {
                    edtAddress1.setError("Mobile number should of 10 digits!");
                    x = 1;
                }

                if (x == 0) {
                    ChkOTPSocial(strFirstName,strLastName,strAddress1);


                }
            }


        });


    }



    public void ChkOTPSocial(final String strFirstName, final String strLastName, final String customerMobile){
        if(randomNumber == 0) {

            int range = 9;  // to generate a single number with this range, by default its 0..9
            int length = 5;
            SecureRandom secureRandom = new SecureRandom();
            String s = "";
            for (int i = 0; i < length; i++) {
                int number = secureRandom.nextInt(range);
                if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                    i = -1;
                    continue;
                }
                s = s + number;
            }

            randomNumber = Integer.parseInt(s);
        }
//        Toast.makeText(activity1, randomNumber, Toast.LENGTH_SHORT).show();
//        otptext1.setText(randomNumber);
        relativeData.setVisibility(View.VISIBLE);
        relativeDataOTP.setVisibility(View.GONE);

        final String key = "LEitKOEZapo-znpKQX9HQntgzWkPE06kKQecVQTMps";
//Multiple mobiles numbers separated by comma
        final String mobiles = customerMobile;
//Sender ID,While using route4 sender id should be 6 characters long.
        final String senderId = "EXBSKT";
//Your message to send, Add URL encoding here.
        final String messages = "Your OTP for Express Basket is "+randomNumber+". Thank you.";
//define route
        Thread thread = new Thread() {
            public void run() {
                try {
                    // Construct data
                    String apiKey = "apikey=" + URLEncoder.encode(key, "UTF-8");
                    String message = "&message=" + URLEncoder.encode(messages, "UTF-8");
                    String sender = "&sender=" + URLEncoder.encode(senderId, "UTF-8");
                    String numbers = "&numbers=" + URLEncoder.encode(mobiles, "UTF-8");

                    // Send data
                    String data = "https://api.textlocal.in/send/?" + apiKey + numbers + message + sender;
                    URL url = new URL(data);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);

                    // Get the response
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    String sResult = "";
                    while ((line = rd.readLine()) != null) {
                        // Process line...
                        sResult = sResult + line + " ";
                    }
                    rd.close();


                } catch (Exception e) {
                    System.out.println("Error SMS " + e);

                }
            }
        };
        thread.start();

        e31.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(e31.getText().toString().length()==1)     //size as per your requirement
                {
                    e32.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        e32.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(e32.getText().toString().length()==1)     //size as per your requirement
                {
                    e33.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        e33.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(e33.getText().toString().length()==1)     //size as per your requirement
                {
                    e34.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        e34.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(e34.getText().toString().length()==1)     //size as per your requirement
                {
                    e35.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(coordLogin, "OTP has been sent again!", Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(RegistrationFragment.this, R.color.Snackbar));
                snackbar.show();
                ChkOTPSocial(strFirstName, strLastName, customerMobile);
            }
        });

        EnterOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x1,x2,x3,x4,x5;
                x1 = e31.getText().toString();
                x2 = e32.getText().toString();
                x3 = e33.getText().toString()
                x4 = e34.getText().toString();
                x5 = e35.getText().toString();
                int a = 0;

                if (x1.isEmpty() || x2.isEmpty() || x3.isEmpty() || x4.isEmpty() || x5.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(coordLogin, "Please Enter 5 Digit OTP", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(RegistrationFragment.this, R.color.Snackbar));
                    snackbar.show();
                }else {
                    String finalOTP = x1+x2+x3+x4+x5;
                    if(finalOTP.equals(String.valueOf(randomNumber))){
                        social(strFirstName,strLastName,customerMobile);
                    }else {
                        Snackbar snackbar = Snackbar.make(coordLogin, "OTP is Incorrect", Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(ContextCompat.getColor(RegistrationFragment.this, R.color.Snackbar));
                        snackbar.show();
                    }
                }
            }
        });

    }

*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}