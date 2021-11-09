package com.likesby.bludoc.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.likesby.bludoc.AddAPharmacistActivity;
import com.likesby.bludoc.DigitalClinicActivity;
import com.likesby.bludoc.DigitalClinicConfirmationActivity;
import com.likesby.bludoc.HomeActivity;
import com.likesby.bludoc.ModelLayer.AppointmentModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.FragmentDoctorDetailsBinding;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ResultOfApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class DoctorDetailsFragment extends Fragment {

    private FragmentDoctorDetailsBinding binding;
    private SessionManager manager;
    private FragmentActivity fragmentActivity;
    private AppointmentModel appointment;
    private boolean isSave;
    private String isFirstTimeDigital;
    private boolean isFirstTime=true;

    public DoctorDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fragmentActivity = (FragmentActivity) context;

    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentDoctorDetailsBinding.inflate(inflater, container, false);

        manager = new SessionManager();

        isFirstTimeDigital = manager.getPreferences(fragmentActivity, "isFirstTimeDigitalDetails");

        if(TextUtils.isEmpty(isFirstTimeDigital)){

            isFirstTime = false;
            manager.setPreferences(fragmentActivity, "isFirstTimeDigitalDetails","true");

        }

        View view = binding.getRoot();

        binding.notes.setOnTouchListener((v, event) -> {
            if (binding.notes.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });

        binding.aboutMe.setOnTouchListener((v, event) -> {
            if (binding.aboutMe.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });

        appointment = manager.getObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment");

        if (manager.getPreferences(fragmentActivity.getApplicationContext(), "name").equals(""))
            binding.doctorName.setText("Dr. " + manager.getPreferences(fragmentActivity.getApplicationContext(), "name").trim());
        else
            binding.doctorName.setText(manager.getPreferences(fragmentActivity.getApplicationContext(), "name").trim());

        binding.qualifications.setText(manager.getPreferences(fragmentActivity.getApplicationContext(), "addtional_qualification"));

        binding.contactNumber.setText(manager.getPreferences(fragmentActivity.getApplicationContext(), "mobile"));

        binding.mailId.setText(manager.getPreferences(fragmentActivity.getApplicationContext(), "email"));

        binding.registrationNumber.setText(manager.getPreferences(fragmentActivity.getApplicationContext(), "registration_no"));

        setValueFromAppointment();

        binding.submitDetails.setOnClickListener(v -> {

            if(TextUtils.isEmpty(binding.doctorName.getText().toString())){

                binding.doctorName.setError("Doctor name is required*");
                binding.doctorName.requestFocus();

            } else if(TextUtils.isEmpty(binding.registrationNumber.getText().toString())){

                binding.registrationNumber.setError("Registration number is required*");
                binding.registrationNumber.requestFocus();

            } else if(TextUtils.isEmpty(binding.qualifications.getText().toString())){

                binding.qualifications.setError("Doctor qualification is required*");
                binding.qualifications.requestFocus();

            } else if(TextUtils.isEmpty(binding.contactNumber.getText().toString())){

                binding.contactNumber.setError("contact is required*");
                binding.contactNumber.requestFocus();

            } else {

                UploadDoctorDetails();

            }

        });

        binding.save.setOnClickListener(v -> {

            isSave=true;

            if(TextUtils.isEmpty(binding.doctorName.getText().toString())){

                binding.doctorName.setError("Doctor name is required*");
                binding.doctorName.requestFocus();

            } else if(TextUtils.isEmpty(binding.registrationNumber.getText().toString())){

                binding.registrationNumber.setError("Registration number is required*");
                binding.registrationNumber.requestFocus();

            } else if(TextUtils.isEmpty(binding.qualifications.getText().toString())){

                binding.qualifications.setError("Doctor qualification is required*");
                binding.qualifications.requestFocus();

            } else if(TextUtils.isEmpty(binding.contactNumber.getText().toString())){

                binding.contactNumber.setError("contact is required*");
                binding.contactNumber.requestFocus();

            } else {

                UploadDoctorDetails();

            }

        });

        return view;
    }

    private void setValueFromAppointment() {

        if (appointment != null) {

            if (!TextUtils.isEmpty(appointment.getDoc_name())) {

                if (appointment.getDoc_name().contains("Dr. "))
                    binding.doctorName.setText(appointment.getDoc_name());
                else
                    binding.doctorName.setText("Dr. " + appointment.getDoc_name());
            }

            if(isFirstTime){
                binding.save.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(appointment.getQualification()))
                binding.qualifications.setText(appointment.getQualification());

            if (!TextUtils.isEmpty(appointment.getNote()))
                binding.notes.setText(appointment.getNote());

            if (!TextUtils.isEmpty(appointment.getCont_no()))
                binding.contactNumber.setText(appointment.getCont_no());

            if (!TextUtils.isEmpty(appointment.getMail_id()))
                binding.mailId.setText(appointment.getMail_id());

            if (!TextUtils.isEmpty(appointment.getReg_no()))
                binding.registrationNumber.setText(appointment.getReg_no());

            if (!TextUtils.isEmpty(appointment.getAbout_me()))
                binding.aboutMe.setText(appointment.getAbout_me());

        }

    }

    public void UploadDoctorDetails() {

        if (Utils.isConnectingToInternet(fragmentActivity.getApplicationContext())) {

            binding.progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<AppointmentModel> call = request.addDoctorDetails(
                    manager.getPreferences(fragmentActivity, "doctor_id"),
                    binding.doctorName.getText().toString(),
                    binding.registrationNumber.getText().toString(),
                    binding.qualifications.getText().toString(),
                    binding.notes.getText().toString(),
                    binding.contactNumber.getText().toString(),
                    binding.mailId.getText().toString(),
                    binding.aboutMe.getText().toString());

            call.enqueue(new Callback<AppointmentModel>() {
                @Override
                public void onResponse(@NonNull Call<AppointmentModel> call, @NonNull retrofit2.Response<AppointmentModel> response) {
                    AppointmentModel jsonResponse = response.body();
                    assert jsonResponse != null;
                    binding.progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("success")) {

                        binding.progressBar.setVisibility(View.GONE);

                        manager.setObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment", jsonResponse);

                        if(isSave){

                            startActivity(new Intent(fragmentActivity, DigitalClinicConfirmationActivity.class));
                            isSave=false;
                            fragmentActivity.finish();

                        }

                        ((DigitalClinicActivity) fragmentActivity).moveViewPager(1);


                    } else {
                        Toast.makeText(fragmentActivity, jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<AppointmentModel> call, @NonNull Throwable t) {
                    binding.progressBar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(fragmentActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}