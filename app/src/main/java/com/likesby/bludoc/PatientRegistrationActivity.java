package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.likesby.bludoc.Fragment.PatientRegistration;

public class PatientRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

        PatientRegistration patientRegistration = new PatientRegistration();

        Bundle bundle = new Bundle();
        bundle.putBoolean("fromAppointment",true);
        patientRegistration.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer,patientRegistration,"patientRegistration")
                .commit();

    }
}