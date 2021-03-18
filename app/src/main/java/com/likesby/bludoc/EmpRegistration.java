//package com.likesby.bludoc;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.lifecycle.ViewModelProviders;
//
//import com.likesby.bludoc.ModelLayer.Entities.User;
//import com.likesby.bludoc.ModelLayer.Entities.UserResponse;
//import com.likesby.bludoc.constants.ApplicationConstant;
//import com.likesby.bludoc.db.MyDB;
//import com.likesby.bludoc.utils.Utils;
//import com.likesby.bludoc.viewModels.ApiViewHolder;
//
//import io.reactivex.SingleObserver;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
//
//public class EmpRegistration extends AppCompatActivity {
//    private static final String TAG = "EmpRegistration_____";
//    ApiViewHolder mLocationViewHolder;
//    Context mContext;
//    CompositeDisposable mBag = new CompositeDisposable();
//    Button mBtnSignUp;
//    EditText first_name, last_name,email;
//    MyDB myDB;
//    ProgressBar progressBarSubmit;
//    String otp_="";
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.registration);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setElevation(0);
//
//        mContext = EmpRegistration.this;
//        myDB = new MyDB(mContext);
//        hideSoftKeyboard(this);
//        initViews();
//        initViewHolder();
//
//        final User user = myDB.getUser();
//        first_name.setText(user.getCustomerName());
//        email.setText(user.getCustomerEmail());
//        if(user.getFbId().equals("") && user.getGmailId().equals("") )
//        {
//            email.setEnabled(true);
//        }
//        else
//        {
//            email.setEnabled(false);
//        }
//
//        /*pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                runtimePermissions();
//            }
//        });*/
//
//        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (TextUtils.isEmpty(first_name.getText().toString().trim())) {
//                    first_name.setError(getResources().getString(R.string.first_name));
//                    first_name.requestFocus();
//                } else {
//                    if (TextUtils.isEmpty(last_name.getText().toString().trim())) {
//                        last_name.setError(getResources().getString(R.string.last_name));
//                        last_name.requestFocus();
//                    } else {
//                        if (TextUtils.isEmpty(email.getText().toString().trim())) {
//                            email.setError(getResources().getString(R.string.email_id));
//                            email.requestFocus();
//                        } else {
//                            String first_name_ = first_name.getText().toString().trim();
//                            String  last_name_ = last_name.getText().toString().trim();
//                            String email_ = email.getText().toString().trim();
//
//                            User user11 = myDB.getUser();
//                            progressBarSubmit.setVisibility(View.VISIBLE);
//                            mBtnSignUp.setVisibility(View.GONE);
//                            setUserUpdate(user11.getCustomerId(), first_name_+" "+last_name_, email_, user11.getCustomerMobile(), "", "", "",
//                                    "", "", user.getFbId(), user.getGmailId(), user.getCustomerImage());
//                        }
//                    }
//                }
//
//            }
//        });
//    }
//
//    @Override
//    public boolean onSupportNavigateUp(){
//        finish();
//        return true;
//    }
//
//    private void initViews() {
//        mBtnSignUp = findViewById(R.id.btn_submit);
//        first_name = findViewById(R.id.first_name);
//        last_name = findViewById(R.id.last_name);
//        email = findViewById(R.id.email);
//        progressBarSubmit = findViewById(R.id.progress_bar_submit);
//
//    }
//
//    private void initViewHolder() {
//        mLocationViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
//    }
//
//
//    //region Get Product Response
//    private void setUserUpdate(String customer_id,String name, String email, String mobile,
//                                     String address, String city, String pincode,
//                               String department_id, String designation_id,String fb_id,String gmail_id,String image) {
//        //if(subscriptionList.size()!=0) {
//        if (Utils.isConnectingToInternet(mContext)) {
//            mLocationViewHolder.setUserUpdate( customer_id, name,  email,  mobile,
//                     address,  city,  pincode, department_id,  designation_id, fb_id, gmail_id, image)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(responseUserUpdate);
//        } else {
//            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
//        }
//        // }
//    }
//
//    SingleObserver<UserResponse> responseUserUpdate = new SingleObserver<UserResponse>() {
//        @Override
//        public void onSubscribe(Disposable d) {
//            mBag.add(d);
//        }
//
//        @Override
//        public void onSuccess(UserResponse response) {
//            if (response != null) {
//                 if (response.getMessage().equals("Profile updated")) {
//                    Toast.makeText(mContext, "Registration Successful", Toast.LENGTH_SHORT).show();
//                    clearEditTexts();
//
//                     User user1 = myDB.getUser();
//                     if(response.getUser().getGmailId().equals("") && response.getUser().getFbId().equals(""))
//                         myDB.updateUser(response.getUser(),response.getUser().getCustomerId(),1);
//                     else if(response.getUser().getFbId().equals(""))
//                        myDB.updateUser(response.getUser(),user1.getGmailId(),2);
//                     else if(response.getUser().getGmailId().equals(""))
//                         myDB.updateUser(response.getUser(),user1.getFbId(),3);
//
//                     finish();
//                     Intent intent = new Intent(mContext, MainActivity.class);
//                     startActivity(intent);
//                } else {
//                     progressBarSubmit.setVisibility(View.GONE);
//                     mBtnSignUp.setVisibility(View.VISIBLE);
//                     if(response.getMessage().equalsIgnoreCase("Email already exist!"))
//                     {
//                         email.setText("");
//                         email.requestFocus();
//                         email.setError("Email Already Exist");
//                     }
//                     else
//                     {
//                         Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
//
//                     }
//                }
//            }
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            progressBarSubmit.setVisibility(View.GONE);
//            mBtnSignUp.setVisibility(View.VISIBLE);
//            Log.e(TAG, "onError: responseUserRegistration >> " + e.toString());
//            //intentCall();
//            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    private void clearEditTexts() {
//        first_name.setText("");
//        last_name.setText("");
//        email.setText("");
//    }
//
//    private void showKeyboard()
//    {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
//        //Show
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
//    }
//
//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                activity.getCurrentFocus().getWindowToken(), 0);
//    }
//    }