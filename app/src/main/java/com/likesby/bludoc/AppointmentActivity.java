package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.likesby.bludoc.ModelLayer.AppointmentListModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.ResultOfSuccess;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityAppointmentBinding;
import com.likesby.bludoc.databinding.ActivityCreateAppointmentBinding;
import com.likesby.bludoc.utils.Utils;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AppointmentActivity extends AppCompatActivity {

    private ActivityAppointmentBinding activity;
    private SessionManager manager = new SessionManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this,R.layout.activity_appointment);

        activity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        activity.dotParent.setVisibility(View.GONE);

        activity.createAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AppointmentActivity.this,CreateAppointmentActivity.class));

            }
        });

        activity.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AppointmentActivity.this,InfoForAppointmentActivity.class));

            }
        });

        activity.createAppointmentOnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AppointmentActivity.this,CreateAppointmentActivity.class));

            }
        });

        activity.newAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AppointmentActivity.this,NewAppointmentActivity.class));

            }
        });

        activity.calenderview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AppointmentActivity.this,CalenderViewActivity.class));

            }
        });

        activity.todayAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AppointmentActivity.this,TodayAppointmentActivity.class));

            }
        });

        activity.historyAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AppointmentActivity.this,HistoryAppointmentActivity.class));

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        AllGetAppointment();

    }

    public void AllGetAppointment() {

        if (Utils.isConnectingToInternet(this)) {

            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfSuccess> call = request.getAppointmentList(manager.getPreferences(AppointmentActivity.this, "doctor_id"),"","");

            call.enqueue(new Callback<ResultOfSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfSuccess> call, @NonNull retrofit2.Response<ResultOfSuccess> response) {

                    int count = 0;

                    if (response.isSuccessful() && response.body() != null) {

                        ResultOfSuccess jsonResponse = response.body();
                        ArrayList<AppointmentListModel> appointmentListModels = jsonResponse.getPatients();
                        if (appointmentListModels.size() > 0) {

                            for (AppointmentListModel appointmentListModel : appointmentListModels) { // compare today date

                                DateTime dateTime;

                                try {
                                    dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+Utils.convertTo24Hour(appointmentListModel.getApp_time()));
                                } catch (Exception e){
                                    dateTime = new DateTime(appointmentListModel.getApp_date() + "T"+appointmentListModel.getApp_time());
                                }

                                DateTime today = new DateTime();

                                if (dateTime.getYear() == today.getYear() &&
                                        dateTime.getMonthOfYear() == today.getMonthOfYear() &&
                                        dateTime.getDayOfMonth() == today.getDayOfMonth() && today.getMillis()  < dateTime.getMillis() + 60000) {

                                    count++;

                                }

                                activity.dotParent.setVisibility(View.VISIBLE);

                                if(count == 0)
                                    activity.dotParent.setVisibility(View.GONE);

                            }

                        } else {

                            activity.dotParent.setVisibility(View.GONE);

                        }

                    } else {

                        activity.dotParent.setVisibility(View.GONE);

                        try {
                            Toast.makeText(AppointmentActivity.this, "" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultOfSuccess> call, @NonNull Throwable t) {

                    activity.dotParent.setVisibility(View.GONE);

                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(AppointmentActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}