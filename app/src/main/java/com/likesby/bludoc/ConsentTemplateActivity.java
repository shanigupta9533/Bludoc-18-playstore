package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.ConsentTemplateAdapter;
import com.likesby.bludoc.Fragment.AddTemplate;
import com.likesby.bludoc.ModelLayer.ConsentTemplateModel;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityConsentTemplateBinding;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ConsentTemplateActivity extends AppCompatActivity {

    private ActivityConsentTemplateBinding activity;
    private final ArrayList<ConsentTemplateModel> arrayList = new ArrayList<>();
    private final SessionManager manager = new SessionManager();
    private ConsentTemplateAdapter consentTemplateAdapter;
    private PatientsItem patientDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_consent_template);

        String patient_id = getIntent().getStringExtra("patient_id");
        String patient_name = getIntent().getStringExtra("patient_name");
        patientDetails = getIntent().getParcelableExtra("patientDetails");

        activity.btnAdd.setOnClickListener(view -> {

            Intent intent = new Intent(this, AddConsentTemplateActivity.class);
            intent.putExtra("patient_id", patient_id);
            startActivity(intent);

        });

        activity.btnBackEditProfile.setOnClickListener(view -> onBackPressed());

        consentTemplateAdapter = new ConsentTemplateAdapter(arrayList, this);
        activity.recyclerview.setAdapter(consentTemplateAdapter);

        consentTemplateAdapter.setPatientId(patient_id);

        activity.recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        activity.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                consentTemplateAdapter.getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        consentTemplateAdapter.setOnClickListener(new ConsentTemplateAdapter.OnClickListener() {
            @Override
            public void onEdit(ConsentTemplateModel consentTemplateModel, int i) {

                Intent intent = new Intent(ConsentTemplateActivity.this, AddConsentTemplateActivity.class);
                intent.putExtra("patient_id", patient_id);
                intent.putExtra("consentTemplateModel", consentTemplateModel);
                intent.putExtra("isEdit", true);
                startActivity(intent);

            }

            @Override
            public void onDelete(ConsentTemplateModel consentTemplateModel, int i) {

                deleteConsentTemplate(consentTemplateModel);

            }

            @Override
            public void add(ConsentTemplateModel consentTemplateModel, int i) {

                Intent intent = new Intent(ConsentTemplateActivity.this, ConsentActivity.class);
                intent.putExtra("patient_id", patient_id);
                intent.putExtra("patient_name", patient_name);
                intent.putExtra("patientDetails", patientDetails);
                intent.putExtra("consentTemplateModel", consentTemplateModel);
                startActivity(intent);

            }
        });

        getAllConsentTemplate();

        activity.pullToRefresh.setOnRefreshListener(() -> {

            getAllConsentTemplate();

            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {

                    activity.pullToRefresh.setRefreshing(false);

                }
            }, 4000);

        });

    }

    public void deleteConsentTemplate(ConsentTemplateModel consentTemplateModel) {

        Retrofit retrofit = RetrofitClient.getInstance();

        activity.progressBarInclude.setVisibility(View.VISIBLE);

        final WebServices request = retrofit.create(WebServices.class);

        Call<ResponseSuccess> call = request.deleteConsentTemplate(consentTemplateModel.getConsent_id());

        call.enqueue(new Callback<ResponseSuccess>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseSuccess> call, @NonNull retrofit2.Response<ResponseSuccess> response) {

                if (response.isSuccessful() && response.body() != null) {

                    activity.progressBarInclude.setVisibility(View.GONE);

                    ResponseSuccess jsonResponse = response.body();
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success")) {

                        getAllConsentTemplate();

                        Toast.makeText(ConsentTemplateActivity.this, jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    } else if (response.body().getMessage().equals("No data available")) {

                        Toast.makeText(ConsentTemplateActivity.this, "No Template Found", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(ConsentTemplateActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(ConsentTemplateActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseSuccess> call, @NonNull Throwable t) {
                activity.progressBarInclude.setVisibility(View.GONE);
                Log.e("Error  ***", t.getMessage());
                Toast.makeText(ConsentTemplateActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void getAllConsentTemplate() {

        Retrofit retrofit = RetrofitClient.getInstance();

        activity.progressBarInclude.setVisibility(View.VISIBLE);

        final WebServices request = retrofit.create(WebServices.class);

        Call<ResponseSuccess> call = request.getAllConsent(manager.getPreferences(ConsentTemplateActivity.this, "doctor_id"));

        call.enqueue(new Callback<ResponseSuccess>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseSuccess> call, @NonNull retrofit2.Response<ResponseSuccess> response) {

                if (response.isSuccessful() && response.body() != null) {

                    activity.progressBarInclude.setVisibility(View.GONE);

                    ResponseSuccess jsonResponse = response.body();
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success")) {

                        ArrayList<ConsentTemplateModel> consent = response.body().getConsent();
                        arrayList.clear();
                        arrayList.addAll(consent);
                        consentTemplateAdapter.notifyDataSetChanged();

                    } else if (response.body().getMessage().equals("Data not available")) {

                        Toast.makeText(ConsentTemplateActivity.this, "No Template Found", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(ConsentTemplateActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(ConsentTemplateActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseSuccess> call, @NonNull Throwable t) {
                activity.progressBarInclude.setVisibility(View.GONE);
                Log.e("Error  ***", t.getMessage());
                Toast.makeText(ConsentTemplateActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (manager.getPreferences(this, "isUpdate").equals("true")) {

            getAllConsentTemplate();

            manager.setPreferences(ConsentTemplateActivity.this, "isUpdate", "false");

        }

    }
}