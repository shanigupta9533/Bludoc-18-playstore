package com.likesby.bludoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.likesby.bludoc.Adapter.ViewPagerFragmentAdapter;
import com.likesby.bludoc.Fragment.ConsentListFragment;
import com.likesby.bludoc.Fragment.CreatePrescription;
import com.likesby.bludoc.Fragment.History;
import com.likesby.bludoc.Fragment.HistoryAppointmentFragment;
import com.likesby.bludoc.Fragment.InvoiceHistoryFragment;
import com.likesby.bludoc.SessionManager.SessionManager;

public class HistoryOfPatientsActivity extends AppCompatActivity {

    private TextView numbering;
    SessionManager session = new SessionManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_of_patients);

        session.setPreferences(this,"backSubs","true");

        String patient_id = getIntent().getStringExtra("patient_id");

        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        numbering = findViewById(R.id.numbering);

        InvoiceHistoryFragment invoiceHistoryFragment = new InvoiceHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("patient_id", patient_id);
        invoiceHistoryFragment.setArguments(bundle);

        HistoryAppointmentFragment historyAppointmentFragment = new HistoryAppointmentFragment();
        bundle = new Bundle();
        bundle.putString("patient_id", patient_id);
        historyAppointmentFragment.setArguments(bundle);

        ConsentListFragment consentListFragment = new ConsentListFragment();
        bundle = new Bundle();
        bundle.putString("patient_id", patient_id);
        consentListFragment.setArguments(bundle);

        ViewPagerFragmentAdapter viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), 0);
        viewPagerFragmentAdapter.setFragment(getHistoryFragment(1, patient_id), "Prescriptions");
        viewPagerFragmentAdapter.setFragment(historyAppointmentFragment, "Appointment - History");
        viewPagerFragmentAdapter.setFragment(getHistoryFragment(2, patient_id), "Certificates");
        viewPagerFragmentAdapter.setFragment(invoiceHistoryFragment, "Invoices");
        viewPagerFragmentAdapter.setFragment(consentListFragment, "Consent");

        viewPager.setAdapter(viewPagerFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(5);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                numbering.setText((position + 1) + "/5");

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

    }

    private History getHistoryFragment(int i, String patient_id) {

        History history = new History();
        Bundle bundle = new Bundle();
        bundle.putString("patient_id",patient_id);
        bundle.putInt("isCertificate",i); // for prescription
        history.setArguments(bundle);

        return history;

    }

    @Override
    protected void onResume() {
        super.onResume();

        session.setPreferences(this,"backSubs","true");

    }

    @Override
    protected void onStop() {
        super.onStop();

        session.setPreferences(this,"backSubs","true");

    }
}