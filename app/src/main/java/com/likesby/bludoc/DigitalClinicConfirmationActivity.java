package com.likesby.bludoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.likesby.bludoc.ModelLayer.AppointmentModel;
import com.likesby.bludoc.ServerConnect.ServerConnect;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityDigitalClinicConfirmationBinding;

import java.io.UnsupportedEncodingException;

public class DigitalClinicConfirmationActivity extends AppCompatActivity {

    private ActivityDigitalClinicConfirmationBinding activity;
    private SessionManager manager;
    private AppointmentModel appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_digital_clinic_confirmation);
        manager = new SessionManager();
        appointment = manager.getObjectAppointmentDetails(this, "appointment");

        if (manager.getPreferences(getApplicationContext(), "name").equals(""))
            activity.name.setText("Dr. " + manager.getPreferences(getApplicationContext(), "name").trim());
        else
            activity.name.setText(manager.getPreferences(getApplicationContext(), "name").trim());

        activity.qualifications.setText(manager.getPreferences(getApplicationContext(), "addtional_qualification"));

        activity.phoneNumber.setText(manager.getPreferences(getApplicationContext(), "mobile"));

        activity.emailId.setText(manager.getPreferences(getApplicationContext(), "email"));

        activity.registrationNumber.setText(manager.getPreferences(getApplicationContext(), "registration_no"));

        Glide.with(this).load(appointment.getProfile_image()).placeholder(R.drawable.default_male).into(activity.profilePic);

        activity.back.setOnClickListener(v -> onBackPressed());

        activity.viewDigitalDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    byte[] data = appointment.getApp_id().getBytes("UTF-8");

                    String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                    String name  = appointment.getDoc_name().replace(" ", "_");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ServerConnect.BASE_URL_LINK_DIGITAL+"MyDigitalClinic/index/"+name+"/"+base64));
                    Intent chooseIntent = Intent.createChooser(intent, "Choose from below");
                    startActivity(chooseIntent);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

        activity.flShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    byte[] data = appointment.getApp_id().getBytes("UTF-8");

                    String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                    String name  = appointment.getDoc_name().replace(" ", "_");
                    String message = appointment.getDoc_name()+" has shared digital clinic page created via BluDoc "+ServerConnect.BASE_URL_LINK_DIGITAL+"MyDigitalClinic/index/"+name+"/"+base64;
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, message);

                    startActivity(Intent.createChooser(share, "Choose from below"));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

        activity.edit.setOnClickListener(v -> {

            startActivity(new Intent(DigitalClinicConfirmationActivity.this, DigitalClinicActivity.class));
            finish();

        });

        setValueFromAppointment();

    }

    private void setValueFromAppointment() {

        if (appointment != null) {

            if (!TextUtils.isEmpty(appointment.getDoc_name())) {

                if (appointment.getDoc_name().contains("Dr. "))
                    activity.name.setText(appointment.getDoc_name());
                else
                    activity.name.setText("Dr. " + appointment.getDoc_name());
            } else
                activity.name.setText("Not Available");

            if (!TextUtils.isEmpty(appointment.getCont_no()))
                activity.phoneNumber.setText(appointment.getCont_no());
            else
                activity.phoneNumber.setText("Not Available");

            if (!TextUtils.isEmpty(appointment.getMail_id()))
                activity.emailId.setText(appointment.getMail_id());
            else
                activity.emailId.setText("Not Available");

            if (!TextUtils.isEmpty(appointment.getReg_no()))
                activity.registrationNumber.setText(appointment.getReg_no());
            else
                activity.registrationNumber.setText("Not Available");

            if (!TextUtils.isEmpty(appointment.getQualification()))
                activity.qualifications.setText(appointment.getQualification());
            else
                activity.qualifications.setText("Not Available");

        }

    }

}