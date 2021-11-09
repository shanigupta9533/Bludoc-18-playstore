package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.NewAppointmentAdapter;
import com.likesby.bludoc.ModelLayer.AppointmentListModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.ResultOfSuccess;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityHistoryAppointmentBinding;
import com.likesby.bludoc.databinding.ActivityHistoryBinding;
import com.likesby.bludoc.databinding.ActivityTodayAppointmentBinding;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ResultOfApi;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class HistoryAppointmentActivity extends AppCompatActivity {

    private ActivityHistoryAppointmentBinding activity;
    private ArrayList<AppointmentListModel> showModel = new ArrayList<>();
    private SessionManager manager = new SessionManager();
    private NewAppointmentAdapter appointmentAdapter;
    private RelativeLayout no_data_found_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_history_appointment);

        LinearLayoutManager gridLayoutManagerMorning = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activity.recyclerview.setLayoutManager(gridLayoutManagerMorning);
        appointmentAdapter = new NewAppointmentAdapter(showModel, this);
        activity.recyclerview.setAdapter(appointmentAdapter);

        appointmentAdapter.setAdapter(true);

        appointmentAdapter.setOnClickListener(new NewAppointmentAdapter.onClickListener() {
            @Override
            public void onClick(AppointmentListModel appointmentListModel, int position) {

                deleteAppointment(appointmentListModel,position);

            }
        });

        activity.searchPatientView.addTextChangedListener(new TextWatcher() {
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

        no_data_found_id = findViewById(R.id.no_data_found);

        findViewById(R.id.refresh_button_no_data).setOnClickListener(v -> AllGetAppointment());

        activity.pullToRefresh.setOnRefreshListener(() -> {

            AllGetAppointment();
            activity.pullToRefresh.setRefreshing(false);

        });

        activity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        AllGetAppointment();

    }

    public void deleteAppointment(AppointmentListModel appointmentListModel, int position) {

        if (Utils.isConnectingToInternet(this)) {

            activity.progressBarInclude.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfApi> call = request.deleteAppointment(appointmentListModel.getApp_list_id());

            call.enqueue(new Callback<ResultOfApi>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {
                    ResultOfApi jsonResponse = response.body();
                    assert jsonResponse != null;
                    activity.progressBarInclude.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                            jsonResponse.getMessage().equalsIgnoreCase("Appointment Removed")) {

                        activity.progressBarInclude.setVisibility(View.GONE);
                        showModel.remove(position);
                        appointmentAdapter.notifyDataSetChanged();

                        if (showModel.size() == 0)
                            no_data_found_id.setVisibility(View.VISIBLE);
                        else
                            no_data_found_id.setVisibility(View.GONE);


                        Toast.makeText(HistoryAppointmentActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(HistoryAppointmentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    activity.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(HistoryAppointmentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void AllGetAppointment() {

        if (Utils.isConnectingToInternet(this)) {

            activity.progressBarInclude.setVisibility(View.VISIBLE);
            no_data_found_id.setVisibility(View.GONE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfSuccess> call = request.getAppointmentList(manager.getPreferences(HistoryAppointmentActivity.this, "doctor_id"),"");

            call.enqueue(new Callback<ResultOfSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfSuccess> call, @NonNull retrofit2.Response<ResultOfSuccess> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        ResultOfSuccess jsonResponse = response.body();
                        activity.progressBarInclude.setVisibility(View.GONE);
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

                                if (today.getMillis() > dateTime.getMillis() + 60000) {
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
                            Toast.makeText(HistoryAppointmentActivity.this, "" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfSuccess> call, @NonNull Throwable t) {
                    activity.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(HistoryAppointmentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (manager.getPreferences(this, "Appointment").equals("true")) {

            AllGetAppointment();
            manager.setPreferences(this, "Appointment", "false");

        }
    }

}