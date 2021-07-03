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

import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityAddAreferralBinding;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.AllPharmacistList;
import com.likesby.bludoc.viewModels.ResultOfApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AddAReferralActivity extends AppCompatActivity {

    private ActivityAddAreferralBinding activity;
    private RelativeLayout progressBar;
    private SessionManager manager;
    private AllPharmacistList pharmacist;
    private boolean isEdit;
    private boolean isSelectAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_add_areferral);

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

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddAReferralActivity.this, R.style.AlertDialog);
                alertDialogBuilder.setCancelable(false);

                alertDialogBuilder.setMessage("Copy of prescription to pharmacy will be sent on this mail id.");

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

                activity.titleOnToolbar.setText("Edit Pharmacy");
                activity.submit.setText("Update Pharmacy");

                if (!TextUtils.isEmpty(pharmacist.getPharmacist_mobile()) && !pharmacist.getPharmacist_mobile().equals("0"))
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

                } else if (TextUtils.isEmpty(activity.emailId.getText().toString())) {

                    activity.emailId.setError("Email is Required*");
                    activity.emailId.requestFocus();

                } else {

                    if (isEdit) {
//   todo                     editPharmacistDetails(pharmacist.getPharmacist_id());
                    } else {
//         todo               UploadPharmicistDetails();
                    }

                }

            }
        });
    }

}