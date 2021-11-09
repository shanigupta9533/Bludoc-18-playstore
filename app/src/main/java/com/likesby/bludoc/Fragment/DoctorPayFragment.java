package com.likesby.bludoc.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.likesby.bludoc.DigitalClinicActivity;
import com.likesby.bludoc.DigitalClinicConfirmationActivity;
import com.likesby.bludoc.ModelLayer.AppointmentModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.FragmentClinicDetailskBinding;
import com.likesby.bludoc.databinding.FragmentDoctorDetailsBinding;
import com.likesby.bludoc.databinding.FragmentDoctorPayBinding;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ResultOfApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class DoctorPayFragment extends Fragment {

    private FragmentDoctorPayBinding binding;
    private SessionManager manager;
    private FragmentActivity fragmentActivity;
    private AppointmentModel appointment;
    private String isFirstTimeDigital;
    private boolean isFirstTime=true;

    public DoctorPayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fragmentActivity = (FragmentActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentDoctorPayBinding.inflate(inflater, container, false);

        manager = new SessionManager();

        isFirstTimeDigital = manager.getPreferences(fragmentActivity, "isFirstTimeDigitalPay");

        appointment = manager.getObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment");

        if(appointment!=null) {
            binding.googlePay.setText(appointment.getG_pay());
            binding.phonePay.setText(appointment.getPhone_pe());
            binding.amazon.setText(appointment.getAmazon());
            binding.upiId.setText(appointment.getUpi_id());
            binding.otherPaymentLink.setText(appointment.getPmt_link());
            binding.paytm.setText(appointment.getPaytm());

        }

        binding.save.setOnClickListener(v -> {

            UploadPaymentDetails();

        });

        return binding.getRoot();
    }

    public void UploadPaymentDetails() {

        appointment = manager.getObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment");

        if (Utils.isConnectingToInternet(fragmentActivity.getApplicationContext())) {

            binding.progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<AppointmentModel> call = request.addPaymentsDetails(
                    manager.getPreferences(fragmentActivity, "doctor_id"),
                    appointment.getDoc_name(),
                    appointment.getReg_no(),
                    binding.googlePay.getText().toString(),
                    binding.paytm.getText().toString(),
                    binding.phonePay.getText().toString(),
                    binding.amazon.getText().toString(),
                    binding.upiId.getText().toString(),
                    binding.otherPaymentLink.getText().toString());

            call.enqueue(new Callback<AppointmentModel>() {
                @Override
                public void onResponse(@NonNull Call<AppointmentModel> call, @NonNull retrofit2.Response<AppointmentModel> response) {
                    AppointmentModel jsonResponse = response.body();
                    assert jsonResponse != null;
                    binding.progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("success")) {

                        binding.progressBar.setVisibility(View.GONE);
                        manager.setObjectAppointmentDetails(fragmentActivity.getApplicationContext(),"appointment",jsonResponse);
                        startActivity(new Intent(getContext(), DigitalClinicConfirmationActivity.class));

                        fragmentActivity.finish();

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