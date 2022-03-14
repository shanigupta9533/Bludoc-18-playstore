package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.likesby.bludoc.ModelLayer.ConsentTemplateModel;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityAddConsentTemplateBinding;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AddConsentTemplateActivity extends AppCompatActivity {

    private ActivityAddConsentTemplateBinding activity;
    private final SessionManager manager = new SessionManager();
    private String patient_id;
    private ConsentTemplateModel consentTemplateModel;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this,R.layout.activity_add_consent_template);

        consentTemplateModel = getIntent().getParcelableExtra("consentTemplateModel");
        patient_id = getIntent().getStringExtra("patient_id");
        isEdit = getIntent().getBooleanExtra("isEdit",false);

        activity.headingVoice.setOnClickListener(v -> {

            Intent intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
            try {
                startActivityForResult(intentSpeech, 500);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(
                        this,
                        "Oops! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT
                ).show();
            }

        });

        activity.descriptionVoice.setOnClickListener(v -> {

            Intent intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
            try {
                startActivityForResult(intentSpeech, 1000);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(
                        this,
                        "Oops! Your device doesn't support Speech to Text",
                        Toast.LENGTH_SHORT
                ).show();
            }

        });

        if(isEdit){

            activity.header.setText("Edit Template");
            activity.submit.setText("Update Template");
            activity.nameOfConsent.setText(consentTemplateModel.getTitle());
            activity.consentDescription.setText(consentTemplateModel.getDescription());

        }

        activity.btnBackEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        activity.submit.setOnClickListener(view -> {

            activity.nameOfConsent.setError(null);
            activity.consentDescription.setError(null);

            if(TextUtils.isEmpty(activity.nameOfConsent.getText().toString())){

                activity.nameOfConsent.setError("Name of consent is required*");
                activity.nameOfConsent.requestFocus();

            } else if(TextUtils.isEmpty(activity.consentDescription.getText().toString())){

                activity.consentDescription.setError("Consent description is required*");
                activity.consentDescription.requestFocus();

            } else {

                addConsentTemplate();

            }

        });

    }

    public void addConsentTemplate() {

        Retrofit retrofit = RetrofitClient.getInstance();

        activity.progressBarInclude.setVisibility(View.VISIBLE);

        final WebServices request = retrofit.create(WebServices.class);

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("title",activity.nameOfConsent.getText().toString());
        hashMap.put("description",activity.consentDescription.getText().toString());

        Call<ResponseSuccess> call;

        if(isEdit){
            hashMap.put("consent_id",consentTemplateModel.getConsent_id());
            call = request.editConstTemplate(hashMap);
        } else {
            hashMap.put("doctor_id",manager.getPreferences(this,"doctor_id"));
            call = request.addConsentTemplate(hashMap);
        }

        call.enqueue(new Callback<ResponseSuccess>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResponseSuccess> call, @NonNull retrofit2.Response<ResponseSuccess> response) {

                if (response.isSuccessful() && response.body() != null) {

                    activity.progressBarInclude.setVisibility(View.GONE);

                    ResponseSuccess jsonResponse = response.body();
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success")) {

                        activity.nameOfConsent.setText("");
                        activity.consentDescription.setText("");

                        manager.setPreferences(AddConsentTemplateActivity.this,"isUpdate","true");
                        Toast.makeText(AddConsentTemplateActivity.this, jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(isEdit)
                            finish();

                    } else if (response.body().getMessage().equals("No data available")) {

                        Toast.makeText(AddConsentTemplateActivity.this, "No Template Found", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(AddConsentTemplateActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(AddConsentTemplateActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseSuccess> call, @NonNull Throwable t) {
                activity.progressBarInclude.setVisibility(View.GONE);
                Log.e("Error  ***", t.getMessage());
                Toast.makeText(AddConsentTemplateActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 500 && data != null) {

            ArrayList<String> stringArrayListExtra =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            activity.nameOfConsent.setText(stringArrayListExtra.get(0));

            activity.nameOfConsent.post(() -> activity.nameOfConsent.setSelection(activity.nameOfConsent.getText().length()));

        } else if (requestCode == 1000 && data != null) {

            ArrayList<String> stringArrayListExtra =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            activity.consentDescription.setText(stringArrayListExtra.get(0));

            activity.consentDescription.post(() -> activity.consentDescription.setSelection(activity.consentDescription.getText().length()));

        }

    }
}