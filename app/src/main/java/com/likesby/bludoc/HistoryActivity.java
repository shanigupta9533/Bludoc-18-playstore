package com.likesby.bludoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.likesby.bludoc.Fragment.HistorySubParentFragment;
import com.likesby.bludoc.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity {

    private ActivityHistoryBinding activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=DataBindingUtil.setContentView(this,R.layout.activity_history);

        activity.btnBackEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.homePageContainer,new HistorySubParentFragment(),"historySubParentFragment").commit();

    }
}