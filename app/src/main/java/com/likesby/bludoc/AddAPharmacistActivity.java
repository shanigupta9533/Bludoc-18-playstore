package com.likesby.bludoc;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.likesby.bludoc.Fragment.EmailNotifyDialogFragment;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityAddAPharmacistBinding;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.AllPharmacistList;
import com.likesby.bludoc.viewModels.ResultOfApi;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AddAPharmacistActivity extends AppCompatActivity {

    private ActivityAddAPharmacistBinding activity;
    private RelativeLayout progressBar;
    private SessionManager manager;
    private AllPharmacistList pharmacist;
    private boolean isEdit;
    private boolean isSelectAdd;

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

        findViewById(R.id.notify_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddAPharmacistActivity.this, R.style.AlertDialog);
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setMessage("Copy of prescription will be sent on this email id.");

                alertDialogBuilder.setPositiveButton("Okay",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                alertDialogBuilder.show();

            }
        });

        Intent intent = getIntent();
        if (intent != null) {

            pharmacist = intent.getParcelableExtra("pharmacist");
            isEdit = intent.getBooleanExtra("isEdit", false);
            isSelectAdd = intent.getBooleanExtra("isSelectAdd", false);

            if (isEdit && pharmacist != null) {

                activity.etName.setText(pharmacist.getPharmacist_name());
                activity.emailId.setText(pharmacist.getPharmacist_email());
                activity.categoryType.setSelection(getSelectedValue(pharmacist.getType()));

                activity.titleOnToolbar.setText("Edit Referral Centre");
                activity.submit.setText("Update Referral Centre");

                if(!TextUtils.isEmpty(pharmacist.getPharmacist_mobile()) && !pharmacist.getPharmacist_mobile().equals("0") )
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

                } else if(TextUtils.isEmpty(activity.emailId.getText().toString())){

                    activity.emailId.setError("Email is Required*");
                    activity.emailId.requestFocus();

                } else if(activity.categoryType.getSelectedItemPosition()==0){

                    Toast.makeText(AddAPharmacistActivity.this, "Please Select a Category!", Toast.LENGTH_SHORT).show();

                } else {

                    if (isEdit) {
                        editPharmacistDetails(pharmacist.getPharmacist_id());
                    } else
                        UploadPharmicistDetails();

                }

            }
        });

    }

    private int getSelectedValue(String type) {

        int pos = 0;
        List<String> strings = Arrays.asList(getResources().getStringArray(R.array.select_categories));

        for (int i = 0; i < strings.size(); i++) {

              if(!TextUtils.isEmpty(strings.get(i)) && strings.get(i).equalsIgnoreCase(type)) {
                  pos = i;
                  break;
              }

        }

        return pos;
    }

    private void editPharmacistDetails(String pharmacist_id) {

        if (Utils.isConnectingToInternet(this)) {

            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();
            final WebServices request = retrofit.create(WebServices.class);

            ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(this, "profile");

            Call<ResultOfApi> call = request.editPharmacist(pharmacist_id, activity.etName.getText().toString(), activity.whatsnumber.getText().toString(), activity.emailId.getText().toString(),activity.categoryType.getSelectedItem().toString(),profileDetails.getHospitalCode());

            call.enqueue(new Callback<ResultOfApi>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {
                    ResultOfApi jsonResponse = response.body();
                    assert jsonResponse != null;
                    progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                            jsonResponse.getMessage().equalsIgnoreCase("Pharmacist Updated")) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(AddAPharmacistActivity.this, activity.categoryType.getSelectedItem().toString()+" Updated Successfully", Toast.LENGTH_SHORT).show();

                        activity.whatsnumber.setText("");
                        activity.etName.setText("");
                        activity.emailId.setText("");

                        manager.setPreferences(AddAPharmacistActivity.this,"uploadPharmacist","true");
                        onBackPressed();

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

            final WebServices request = retrofit.create(WebServices.class);

            ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(this, "profile");
            Call<ResultOfApi> call = request.addPharmacist(manager.getPreferences(AddAPharmacistActivity.this, "doctor_id"), activity.etName.getText().toString(), activity.whatsnumber.getText().toString(), activity.emailId.getText().toString(),activity.categoryType.getSelectedItem().toString(),responseProfileDetails.getHospitalCode());

            call.enqueue(new Callback<ResultOfApi>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {
                    ResultOfApi jsonResponse = response.body();
                    assert jsonResponse != null;
                    progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                            jsonResponse.getMessage().equalsIgnoreCase("Pharmacist Added")) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(AddAPharmacistActivity.this, activity.categoryType.getSelectedItem().toString()+" Added Successfully", Toast.LENGTH_SHORT).show();

                        activity.whatsnumber.setText("");
                        activity.etName.setText("");
                        activity.emailId.setText("");

                        manager.setPreferences(AddAPharmacistActivity.this,"uploadPharmacist","true");
                        onBackPressed();

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