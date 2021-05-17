package com.likesby.bludoc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityAddAPharmacistBinding;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.AllPharmacistList;
import com.likesby.bludoc.viewModels.ResultOfApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AddAPharmacistActivity extends AppCompatActivity {

    private ActivityAddAPharmacistBinding activity;
    private RelativeLayout progressBar;
    private SessionManager manager;
    private AllPharmacistList pharmacist;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_add_a_pharmacist);

        manager = new SessionManager();

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        Intent intent = getIntent();
        if (intent != null) {

            pharmacist = intent.getParcelableExtra("pharmacist");
            isEdit = intent.getBooleanExtra("isEdit", false);

            if (isEdit && pharmacist != null) {

                activity.etName.setText(pharmacist.getPharmacist_name());
                activity.emailId.setText(pharmacist.getPharmacist_email());
                activity.whatsnumber.setText(pharmacist.getPharmacist_mobile());

            }

        }

        progressBar = findViewById(R.id.progress_bar);

        activity.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(activity.etName.getText().toString())) {

                    activity.etName.setError("Name is Required*");
                    activity.etName.requestFocus();

                } else if (TextUtils.isEmpty(activity.whatsnumber.getText().toString())) {

                    activity.whatsnumber.setError("what'sup number is Required*");
                    activity.whatsnumber.requestFocus();

                } else if (TextUtils.isEmpty(activity.emailId.getText().toString())) {

                    activity.emailId.setError("email id is Required*");
                    activity.emailId.requestFocus();

                } else {

                    if (isEdit)
                        editPharmacistDetails(pharmacist.getPharmacist_id());
                    else
                        UploadPharmicistDetails();

                }

            }
        });

    }

    private void editPharmacistDetails(String pharmacist_id) {

        if (Utils.isConnectingToInternet(this)) {

            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();
            ;
            final WebServices request = retrofit.create(WebServices.class);
            ;

            Call<ResultOfApi> call = request.editPharmacist(pharmacist_id, activity.etName.getText().toString(), activity.whatsnumber.getText().toString(), activity.emailId.getText().toString());

            call.enqueue(new Callback<ResultOfApi>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {
                    ResultOfApi jsonResponse = response.body();
                    assert jsonResponse != null;
                    progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                            jsonResponse.getMessage().equalsIgnoreCase("Pharmacist Updated")) {

                        progressBar.setVisibility(View.GONE);

                        activity.whatsnumber.setText("");
                        activity.etName.setText("");
                        activity.emailId.setText("");

                        Toast.makeText(AddAPharmacistActivity.this, "Pharmacist Added", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AddAPharmacistActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(AddAPharmacistActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void UploadPharmicistDetails() {
        if (Utils.isConnectingToInternet(this)) {

            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();
            ;
            final WebServices request = retrofit.create(WebServices.class);
            ;

            Call<ResultOfApi> call = request.addPharmacist(manager.getPreferences(AddAPharmacistActivity.this, "doctor_id"), activity.etName.getText().toString(), activity.whatsnumber.getText().toString(), activity.emailId.getText().toString());

            call.enqueue(new Callback<ResultOfApi>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {
                    ResultOfApi jsonResponse = response.body();
                    assert jsonResponse != null;
                    progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                            jsonResponse.getMessage().equalsIgnoreCase("Pharmacist Added")) {

                        progressBar.setVisibility(View.GONE);

                        activity.whatsnumber.setText("");
                        activity.etName.setText("");
                        activity.emailId.setText("");

                        Toast.makeText(AddAPharmacistActivity.this, "Pharmacist Added", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AddAPharmacistActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(AddAPharmacistActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}