package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.SelectPatientAdapter;
import com.likesby.bludoc.Fragment.CreatePrescription;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.Entities.ResponsePatients;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivitySelectApatientBinding;
import com.likesby.bludoc.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class SelectAPatientActivity extends AppCompatActivity {

    private ActivitySelectApatientBinding activity;
    private ArrayList<PatientsItem> arrayList = new ArrayList<>();
    private SessionManager manager = new SessionManager();
    private SelectPatientAdapter selectPatientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_select_apatient);

        RelativeLayout refresh = findViewById(R.id.refresh_button_no_data);

        selectPatientAdapter = new SelectPatientAdapter(arrayList, this);
        activity.recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activity.recyclerview.setAdapter(selectPatientAdapter);

        activity.addPatient.setOnClickListener(v -> {

            Intent intent = new Intent(this, PatientRegistrationActivity.class);
            startActivity(intent);

        });

        activity.searchPatientView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                selectPatientAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectPatientAdapter.setOnClickListener((patientsItem, position) -> {

            Intent data = new Intent();
            data.putExtra("patientItem", patientsItem);
            setResult(RESULT_OK, data);
            finish();

        });

        refresh.setOnClickListener(v -> AllGetPatients());

        AllGetPatients();

    }

    @Override
    protected void onResume() {
        super.onResume();

        AllGetPatients();

    }

    public void AllGetPatients() {

        activity.noDataFoundInclude.setVisibility(View.GONE);

        if (Utils.isConnectingToInternet(this)) {

            activity.progressBarInclude.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResponsePatients> call = request.getPatients2(manager.getPreferences(this, "doctor_id"));

            call.enqueue(new Callback<ResponsePatients>() {
                @Override
                public void onResponse(@NonNull Call<ResponsePatients> call, @NonNull retrofit2.Response<ResponsePatients> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        ResponsePatients responsePatients = response.body();

                        if (responsePatients.getMessage() != null) {
                            if (responsePatients.getMessage().equals("patients")) {

                                arrayList.clear();
                                arrayList.addAll(responsePatients.getPatients());
                                selectPatientAdapter.notifyItemRangeChanged(0, arrayList.size() - 1);
                                activity.noDataFoundInclude.setVisibility(View.GONE);

                            } else if (responsePatients.getMessage().equals("Data not available")) {

                                activity.noDataFoundInclude.setVisibility(View.VISIBLE);

                            }
                        }
                    } else {

                        try {
                            Toast.makeText(SelectAPatientActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    activity.progressBarInclude.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(@NonNull Call<ResponsePatients> call, @NonNull Throwable t) {
                    activity.progressBarInclude.setVisibility(View.VISIBLE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(SelectAPatientActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}