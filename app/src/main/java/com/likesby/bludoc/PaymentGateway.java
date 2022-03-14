package com.likesby.bludoc;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;


import com.likesby.bludoc.ModelLayer.Entities.ResponseSubscription;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PaymentGateway extends AppCompatActivity implements PaymentResultWithDataListener {
    private String amount = "", amount2, c_sub_id = "";
    private static final String TAG = "PaymentGate____";
    ApiViewHolder apiViewHolder;
    CompositeDisposable mBag = new CompositeDisposable();
    Context mContext;
    SessionManager manager;
    String subscription_id = "", transaction_id = "", country = "";
    private String razor_pay_sub_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_gateway);
        mContext = PaymentGateway.this;
        manager = new SessionManager();
        initViewHolder();
        Bundle b = getIntent().getExtras();
        razor_pay_sub_id = b.getString("razor_pay_sub_id");

        assert b != null;
        if (getIntent().getStringExtra("amount") != null) {
            amount = b.getString("amount");
            subscription_id = b.getString("subscription_id");
            country = b.getString("country");
            //amount = "100";
            Log.e(TAG, "Amount >> " + amount);

            int amt = Integer.parseInt(amount + "00");
            startPayment(String.valueOf(amt));

            // Toast.makeText(mContext, amount, Toast.LENGTH_SHORT).show();
        }

       /* amount = "100";
        Log.e(TAG, "Amount >> " + amount);

        int amt = Integer.parseInt(amount+"00");
        startPayment( String.valueOf(amt));*/

        /**
         * Preload payment resources
         */
        Checkout.preload(getApplicationContext());

    }

    private void initViewHolder() {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);

        /*if (Utils.isConnectingToInternet(mContext)) {

            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1000);
            mLocationViewHolder.pay(Integer.parseInt(amount),"INR","RC_"+randomInt,"1")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(responsePay);
        } else {
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }*/
    }


    public void startPayment(String amount_) {
        Log.e(TAG, "Amount >> " + amount);
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
//         checkout.setKeyID("rzp_test_lu8YpD3PjwGbvn");
        checkout.setKeyID("rzp_live_HENDzm8NdfptPX");

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.bludocc_loho);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();
            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "BluDoc");
            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Premium Subscription");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");

//            options.put("order_id", "#12344");//comment for test credentials ****** IMP

            JSONObject preFill = new JSONObject();


            if (manager.contains(mContext, "mobile")) {
                preFill.put("contact", manager.getPreferences(mContext, "mobile"));
            }
            if (!("").equalsIgnoreCase(country)) {
                if (country.equalsIgnoreCase("India")) {
                    options.put("currency", "INR");
                    options.put("amount", amount_);
                } else if (country.equalsIgnoreCase("OOI")) {
                    options.put("currency", "USD");
                    options.put("amount", Float.parseFloat(String.format("%.0f", (Float.parseFloat(amount_) / 60))));
                }
            } else {
                options.put("currency", "USD");
                options.put("amount", Float.parseFloat(String.format("%.0f", (Float.parseFloat(amount_) / 60))));
            }


            if (manager.contains(mContext, "email"))
                preFill.put("email", manager.getPreferences(mContext, "email"));

            options.put("prefill", preFill);
            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */


//            if (!TextUtils.isEmpty(razor_pay_sub_id)) {
//                options.put("subscription_id", razor_pay_sub_id);
//                options.put("recurring", true);
//            }

            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    public void ProfileDetails() {
        apiViewHolder.getProfileDetails(manager.getPreferences(mContext, "doctor_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseProfile);

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
                    // ProfileDetails();
                } else if (response.getMessage().equals("Profile Details")) {
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


                }

                finishAffinity();
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
            } else {
                finishAffinity();
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                //   ProfileDetails();
                Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            finishAffinity();
            Intent intent = new Intent(mContext, HomeActivity.class);
            startActivity(intent);
            //ProfileDetails();
            Log.e(TAG, "onError: profileDetails >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


    SingleObserver<ResponseSubscription> responseAddSubscription = new SingleObserver<ResponseSubscription>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseSubscription response) {
            if (response != null) {
                if (response.getMessage() != null) {

                    if (response.getMessage().equalsIgnoreCase("Subscription Added")) {
                        Date cDate = new Date();
                        String fDate = new SimpleDateFormat("dd-MM-yyyy").format(cDate);

                        Calendar c2 = Calendar.getInstance();
                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                        Date c = Calendar.getInstance().getTime();
                        Date date1 = null, date2 = null;
                        try {
                            date1 = dateFormat.parse(fDate);
                        } catch (Exception e) {

                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        sdf.format(new Date());
                        //Toast.makeText(mContext, "Subscription Added Successfully", Toast.LENGTH_SHORT).show();
                        manager.setPreferences(mContext, "purchased_new", "true");
                        manager.setPreferences(mContext, "purchased_date", "" + sdf.toString());
                        if (response.getEnd_date() != null) {
                            if (response.getEnd_date().equals(""))
                                popup("-");
                            else
                                popup(response.getEnd_date());
                        } else {
                            popup("-");
                        }

                    } else {
                        finish();
                    }
                } else {
                    finish();
                    //  Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(Throwable e) {

            finish();
            Log.e(TAG, "onError: responsePayment >> " + e.toString());

        }
    };

    private void popup(String end_date) {
        final Dialog dialog_data = new Dialog(PaymentGateway.this);
        dialog_data.setCancelable(false);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);
        dialog_data.setContentView(R.layout.popup_upgraded_to_premium);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_continue = dialog_data.findViewById(R.id.btn_continue);
        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);
        TextView tv_end_date = dialog_data.findViewById(R.id.tv_end_date);

        tv_end_date.setText("You have successfully upgraded to premium. Your subscription will end on " + end_date);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();
                ProfileDetails();

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

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

//        Log.i(TAG, "onPaymentSuccess: "+paymentData.getData().toString());

        Log.e(TAG, "onPaymentSuccess = " + s);
        transaction_id = s;

        final int min = 2001;
        final int max = 8999;
        final int random = new Random().nextInt((max - min) + 1) + min;

        apiViewHolder.AddSubscription(manager.getPreferences(mContext, "doctor_id"), subscription_id,
                "Success", transaction_id, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseAddSubscription);

        Toast.makeText(mContext, "Payment Success", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

        Log.e(TAG, "onPaymentError = " + s);
        Log.e(TAG, "onPaymentError i = " + i);
        Toast.makeText(mContext, "Payment Failure", Toast.LENGTH_LONG).show();
        finish();

    }




   /*
    SingleObserver<StatusResponse> responsePayment = new SingleObserver<StatusResponse>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(StatusResponse response) {
            if (response != null) {
                if (response.getMessage() != null) {

                    if(response.getStatus().equalsIgnoreCase("success") &&
                            response.getMessage().equalsIgnoreCase("Payment Added Successful"))
                    {
                        finish();

                    }
                    else
                    {
                        finish();
                    }
                } else {
                    finish();
                    // Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onError(Throwable e) {

            finish();
            Log.e(TAG, "onError: responsePayment >> " + e.toString());

        }
    };


*/


}

