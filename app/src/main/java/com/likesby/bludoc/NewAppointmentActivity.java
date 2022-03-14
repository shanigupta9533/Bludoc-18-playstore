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
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.NewAppointmentAdapter;
import com.likesby.bludoc.ModelLayer.AppointmentListModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.ResultOfSuccess;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityNewAppointmentBinding;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ResultOfApi;
import com.yalantis.phoenix.PullToRefreshView;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class NewAppointmentActivity extends AppCompatActivity {

    private ActivityNewAppointmentBinding activity;
    private ArrayList<AppointmentListModel> showModel = new ArrayList<>();
    private SessionManager manager = new SessionManager();
    private NewAppointmentAdapter appointmentAdapter;
    private RelativeLayout no_data_found_id;
    private ArrayList<AppointmentListModel> allTimeData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_new_appointment);

        LinearLayoutManager gridLayoutManagerMorning = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activity.recyclerview.setLayoutManager(gridLayoutManagerMorning);
        appointmentAdapter = new NewAppointmentAdapter(showModel, this);
        activity.recyclerview.setAdapter(appointmentAdapter);

        activity.categoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SelectedByPosition(position);

//                if (position == 2) {
//
//                    activity.spinnerMonth.setVisibility(View.VISIBLE);
//                    DateTime today = new DateTime();
//                    activity.monthSpinner.setSelection(today.getMonthOfYear()-1);
//
//                } else {
//
//                    activity.spinnerMonth.setVisibility(View.GONE);
//
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        activity.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                no_data_found_id.setVisibility(View.GONE);

                ArrayList<AppointmentListModel> appointmentListModels = new ArrayList<>();

                for (AppointmentListModel appointmentListModel : allTimeData) {

                    DateTime dateTime = new DateTime(appointmentListModel.getApp_date() + "T" + convertTo24Hour(appointmentListModel.getApp_time()));
                    DateTime today = new DateTime();

                    if (dateTime.getMonthOfYear() == position + 1 && today.getMillis() + 60000 < dateTime.getMillis()) {

                        appointmentListModels.add(appointmentListModel);

                    }

                }

                showModel.clear();
                showModel.addAll(appointmentListModels);
                appointmentAdapter.notifyDataSetChanged();

                if (showModel.size() == 0) {
                    no_data_found_id.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        appointmentAdapter.setOnClickListener(new NewAppointmentAdapter.onClickListener() {
            @Override
            public void onClick(AppointmentListModel appointmentListModel, int position) {

                deleteAppointment(appointmentListModel, position);

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

        findViewById(R.id.refresh_button_no_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AllGetAppointment("");

            }
        });

        activity.pullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                AllGetAppointment("");

                activity.pullToRefresh.setRefreshing(false);

            }
        });

        activity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        AllGetAppointment("");

    }

    @SuppressLint("NotifyDataSetChanged")
    private void SelectedByPosition(int position) {

        no_data_found_id.setVisibility(View.GONE);

        switch (position) {


            case 0:

                AllGetAppointment("");

                break;

            case 1:

                AllGetAppointment("Ascending");

                break;

            case 2:

                AllGetAppointment("Descending");
                break;

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void AllGetStaticsDaily() {

        ArrayList<AppointmentListModel> appointmentListModels = new ArrayList<>();

        for (AppointmentListModel appointmentListModel : allTimeData) {

            DateTime dateTime;

            String time = appointmentListModel.getCreated().replace(" ", "T");

            try {
                dateTime = new DateTime(time);
            } catch (Exception e){
                dateTime = new DateTime(time);
            }

            DateTime today = new DateTime();

            if (dateTime.getYear() == today.getYear() &&
                    dateTime.getMonthOfYear() == today.getMonthOfYear() &&
                    dateTime.getDayOfMonth() == today.getDayOfMonth()) {

                appointmentListModels.add(appointmentListModel);

            }
        }

        showModel.clear();
        showModel.addAll(appointmentListModels);
        appointmentAdapter.notifyDataSetChanged();

        if (showModel.size() == 0) {

            no_data_found_id.setVisibility(View.VISIBLE);

        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private void AllGetStaticsMonthly() {

        ArrayList<AppointmentListModel> appointmentListModels = new ArrayList<>();

        for (AppointmentListModel appointmentListModel : allTimeData) {

            DateTime dateTime = new DateTime(appointmentListModel.getApp_date() + "T" + convertTo24Hour(appointmentListModel.getApp_time()));
            DateTime today = new DateTime();
            DateTime beforeOneMonth = today.minusMonths(1);

            if (dateTime.getMillis() >= beforeOneMonth.getMillis()) {

                appointmentListModels.add(appointmentListModel);

            }

        }

        showModel.clear();
        showModel.addAll(appointmentListModels);
        appointmentAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (manager.getPreferences(NewAppointmentActivity.this, "Appointment").equals("true")) {

            AllGetAppointment("");
            manager.setPreferences(NewAppointmentActivity.this, "Appointment", "false");

        }
    }

    public static String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("hh:mm a"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("HH:mm");
        String x = f2.format(d); // "23:00"

        return x;
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


                        Toast.makeText(NewAppointmentActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(NewAppointmentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    activity.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(NewAppointmentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void AllGetAppointment(String shortKeywords) {

        if (Utils.isConnectingToInternet(this)) {

            activity.progressBarInclude.setVisibility(View.VISIBLE);
            no_data_found_id.setVisibility(View.GONE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfSuccess> call = request.getAppointmentList(manager.getPreferences(NewAppointmentActivity.this, "doctor_id"),shortKeywords,"");

            call.enqueue(new Callback<ResultOfSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfSuccess> call, @NonNull retrofit2.Response<ResultOfSuccess> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        ResultOfSuccess jsonResponse = response.body();
                        activity.progressBarInclude.setVisibility(View.GONE);
                        ArrayList<AppointmentListModel> appointmentListModels = jsonResponse.getPatients();

                        allTimeData.clear();
                        showModel.clear();

                        for (AppointmentListModel appointmentListModel : appointmentListModels) { // compare today date

                            DateTime dateTime;

                            try {
                                dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+Utils.convertTo24Hour(appointmentListModel.getApp_time()));
                            } catch (Exception e){
                                dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+appointmentListModel.getApp_time());
                            }

                            DateTime today = new DateTime();

                            if (today.getMillis()  < dateTime.getMillis() + 60000) {
                                showModel.add(appointmentListModel);
                            }

                        }

                        allTimeData.addAll(showModel);

                        if (allTimeData.size() > 0) {

                            appointmentAdapter.notifyItemRangeChanged(0, showModel.size());
                            no_data_found_id.setVisibility(View.GONE);

                        } else {

                            no_data_found_id.setVisibility(View.VISIBLE);

                        }
                    } else {

                        try {
                            Toast.makeText(NewAppointmentActivity.this, "" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultOfSuccess> call, @NonNull Throwable t) {
                    activity.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(NewAppointmentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}