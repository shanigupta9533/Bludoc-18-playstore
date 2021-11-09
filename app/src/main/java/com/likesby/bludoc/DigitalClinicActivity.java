package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.likesby.bludoc.Adapter.ViewPagerFragmentAdapter;
import com.likesby.bludoc.Fragment.ClinicDetailskFragment;
import com.likesby.bludoc.Fragment.DoctorDetailsFragment;
import com.likesby.bludoc.Fragment.DoctorPayFragment;
import com.likesby.bludoc.Fragment.ProfileAndLogoFragment;
import com.likesby.bludoc.ModelLayer.AppointmentModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ResultOfApi;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DigitalClinicActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView numbering;
    private SessionManager manager;
    private RelativeLayout progressbar;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_clinic);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);
        numbering = findViewById(R.id.numbering);
        manager = new SessionManager();
        progressbar = findViewById(R.id.progress_bar);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        getAllFetchData();

    }

    public void getAllFetchData() {

        if (Utils.isConnectingToInternet(getApplicationContext())) {

            progressbar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfApi> call = request.getAppointment(
                    manager.getPreferences(this, "doctor_id"));

            call.enqueue(new Callback<ResultOfApi>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull Response<ResultOfApi> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        ResultOfApi jsonResponse = response.body();
                        progressbar.setVisibility(View.GONE);

                        if (jsonResponse.getSuccess().equalsIgnoreCase("success")) {

                            AppointmentModel appointment = jsonResponse.getPatients().get(0);
                            manager.setObjectAppointmentDetails(DigitalClinicActivity.this, "appointment", appointment);
                            progressbar.setVisibility(View.GONE);

                        } else if (jsonResponse.getMessage().equalsIgnoreCase("Data not available")) {

                            popupPaused("Lets create your digital clinic page to let your patients know more about your services and take appointments.");
                            //    Toast.makeText(DigitalClinicActivity.this, jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(DigitalClinicActivity.this, jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                        setTabLayoutAdapterWithViewPager();

                    } else {

                        try {
                            Toast.makeText(DigitalClinicActivity.this, "" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    progressbar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(DigitalClinicActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    setTabLayoutAdapterWithViewPager();


                }
            });
        } else {
            Toast.makeText(DigitalClinicActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTabLayoutAdapterWithViewPager() {

        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), 0);
        viewPagerFragmentAdapter.setFragment(new DoctorDetailsFragment(), "Doctor Details");
        viewPagerFragmentAdapter.setFragment(new ClinicDetailskFragment(), "Clinic Details");
        viewPagerFragmentAdapter.setFragment(new ProfileAndLogoFragment(), "Logo & Profile");
        viewPagerFragmentAdapter.setFragment(new DoctorPayFragment(), "Payment Details");
        viewPager.setAdapter(viewPagerFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                numbering.setText((position + 1) + "/4");

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void popupPaused(String s) {
        final Dialog dialog_data = new Dialog(this);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_access_digital_clinic);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_add = dialog_data.findViewById(R.id.btn_register);
        TextView tv_no_template = dialog_data.findViewById(R.id.tv_no_template);
        tv_no_template.setText(s);
        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

            }
        });
        try {
            dialog_data.show();
        } catch (WindowManager.BadTokenException e) {
            //use a log message
        }
    }

    public void moveViewPager(int i) {

        viewPager.setCurrentItem(i);

    }
}