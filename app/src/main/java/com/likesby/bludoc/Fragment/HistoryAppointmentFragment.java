package com.likesby.bludoc.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.NewAppointmentAdapter;
import com.likesby.bludoc.HistoryAppointmentActivity;
import com.likesby.bludoc.ModelLayer.AppointmentListModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.ResultOfSuccess;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityHistoryAppointmentBinding;
import com.likesby.bludoc.databinding.FragmentHistoryAppointmentBinding;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ResultOfApi;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class HistoryAppointmentFragment extends Fragment {

    private FragmentHistoryAppointmentBinding binding;
    private ArrayList<AppointmentListModel> showModel = new ArrayList<>();
    private SessionManager manager = new SessionManager();
    private NewAppointmentAdapter appointmentAdapter;
    private RelativeLayout no_data_found_id;
    private FragmentActivity fragmentActivity;
    private String patient_id;

    public HistoryAppointmentFragment() {
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

        binding=FragmentHistoryAppointmentBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            patient_id = getArguments().getString("patient_id", "");
        }

        View root = binding.getRoot();

        LinearLayoutManager gridLayoutManagerMorning = new LinearLayoutManager(fragmentActivity, LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(gridLayoutManagerMorning);
        appointmentAdapter = new NewAppointmentAdapter(showModel, getContext());
        binding.recyclerview.setAdapter(appointmentAdapter);

        appointmentAdapter.setAdapter(true);

        appointmentAdapter.setOnClickListener(new NewAppointmentAdapter.onClickListener() {
            @Override
            public void onClick(AppointmentListModel appointmentListModel, int position) {

                deleteAppointment(appointmentListModel,position);

            }
        });

        binding.searchPatientView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                appointmentAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        no_data_found_id = root.findViewById(R.id.no_data_found);

        root.findViewById(R.id.refresh_button_no_data).setOnClickListener(v -> AllGetAppointment());

        binding.pullToRefresh.setOnRefreshListener(() -> {

            AllGetAppointment();
            binding.pullToRefresh.setRefreshing(false);

        });

        AllGetAppointment();

        return binding.getRoot();
    }

    public void deleteAppointment(AppointmentListModel appointmentListModel, int position) {

        if (Utils.isConnectingToInternet(fragmentActivity)) {

            binding.progressBarInclude.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfApi> call = request.deleteAppointment(appointmentListModel.getApp_list_id());

            call.enqueue(new Callback<ResultOfApi>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {
                    ResultOfApi jsonResponse = response.body();
                    assert jsonResponse != null;
                    binding.progressBarInclude.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                            jsonResponse.getMessage().equalsIgnoreCase("Appointment Removed")) {

                        binding.progressBarInclude.setVisibility(View.GONE);
                        showModel.remove(position);
                        appointmentAdapter.notifyDataSetChanged();

                        if (showModel.size() == 0)
                            no_data_found_id.setVisibility(View.VISIBLE);
                        else
                            no_data_found_id.setVisibility(View.GONE);


                        Toast.makeText(fragmentActivity, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    binding.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(fragmentActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void AllGetAppointment() {

        if (Utils.isConnectingToInternet(fragmentActivity)) {

            binding.progressBarInclude.setVisibility(View.VISIBLE);
            no_data_found_id.setVisibility(View.GONE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfSuccess> call = request.getAppointmentList(manager.getPreferences(fragmentActivity, "doctor_id"),"","app_date");

            call.enqueue(new Callback<ResultOfSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfSuccess> call, @NonNull retrofit2.Response<ResultOfSuccess> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        ResultOfSuccess jsonResponse = response.body();
                        binding.progressBarInclude.setVisibility(View.GONE);
                        ArrayList<AppointmentListModel> appointmentListModels = jsonResponse.getPatients();

                        if (appointmentListModels.size() > 0) {

                            showModel.clear();

                            for (AppointmentListModel appointmentListModel : appointmentListModels) { // compare today date

                                DateTime dateTime;

                                try {
                                    dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+Utils.convertTo24Hour(appointmentListModel.getApp_time()));
                                } catch (Exception e){
                                    dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+appointmentListModel.getApp_time());
                                }

                                DateTime today = new DateTime();

                                if (today.getMillis() > dateTime.getMillis() + 60000 &&
                                        !TextUtils.isEmpty(appointmentListModel.getPatient_id()) && !TextUtils.isEmpty(patient_id) &&
                                        appointmentListModel.getPatient_id().equalsIgnoreCase(patient_id)) {
                                    showModel.add(appointmentListModel);
                                } else if(TextUtils.isEmpty(patient_id)) {
                                    showModel.add(appointmentListModel);
                                }

                            }

                            if (showModel.size() > 0) {

                                appointmentAdapter.notifyItemRangeChanged(0, showModel.size());
                                no_data_found_id.setVisibility(View.GONE);

                            } else {

                                no_data_found_id.setVisibility(View.VISIBLE);

                            }

                        } else {

                            no_data_found_id.setVisibility(View.VISIBLE);

                        }

                    } else {

                        try {
                            Toast.makeText(fragmentActivity, "" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfSuccess> call, @NonNull Throwable t) {
                    binding.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(fragmentActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (manager.getPreferences(fragmentActivity, "Appointment").equals("true")) {

            AllGetAppointment();
            manager.setPreferences(fragmentActivity, "Appointment", "false");

        }
    }
}