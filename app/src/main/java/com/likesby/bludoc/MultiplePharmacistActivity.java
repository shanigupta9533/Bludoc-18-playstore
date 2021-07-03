package com.likesby.bludoc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Adapter.PharmacistAdapter;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityMultiplePharmacistBinding;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.AllPharmacistList;
import com.likesby.bludoc.viewModels.AllPharmacistModels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class MultiplePharmacistActivity extends AppCompatActivity {

    private FrameLayout progressBar;
    private TextView message;
    private SessionManager manager;
    private final ArrayList<AllPharmacistList> arrayAdapterList = new ArrayList<>();
    private final ArrayList<String> SearchList = new ArrayList<>();
    private final ArrayList<AllPharmacistList> arrayList = new ArrayList<>();
    private RecyclerView recyclerview;
    private PharmacistAdapter arrayAdapter;
    private ActivityMultiplePharmacistBinding activity;
    private SearchView searchView;
    private TextView no_data_found_id;
    private String keywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_multiple_pharmacist);

        Intent intent = getIntent();
        if (intent != null) {
            keywords = intent.getStringExtra("keywords");
        }

        recyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(linearLayoutManager);
        arrayAdapter = new PharmacistAdapter(arrayAdapterList, this);
        arrayAdapter.setMultipleSelection(true);
        recyclerview.setAdapter(arrayAdapter);
        no_data_found_id = findViewById(R.id.no_data_found_id);

        if (!TextUtils.isEmpty(keywords))
            activity.appName.setText("Select " + keywords);
        else
            activity.appName.setText("Select...");

        activity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        activity.submitAllResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (arrayAdapter.getAllIds().isEmpty()) {

                    Toast.makeText(MultiplePharmacistActivity.this, "No Data Selected...", Toast.LENGTH_SHORT).show();

                } else {

                    Intent data = new Intent();
                    data.putExtra("arraylist_of_details", arrayAdapter.getAllIds());
                    data.putExtra("keywordsMultiple", keywords);
                    setResult(RESULT_OK, data);
                    finish();

                }

            }
        });

        searchView = findViewById(R.id.search_view);
        progressBar = findViewById(R.id.parent_of_progress_bar);
        message = findViewById(R.id.message);
        manager = new SessionManager();
        AllGetPharmacist();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.back.setVisibility(View.GONE);
                activity.appName.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                activity.back.setVisibility(View.VISIBLE);
                activity.appName.setVisibility(View.VISIBLE);
                return false;
            }
        });

    }

    public void AllGetPharmacist() {

        if (Utils.isConnectingToInternet(this)) {

            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<AllPharmacistModels> call = request.allPharmacist(manager.getPreferences(MultiplePharmacistActivity.this, "doctor_id"));

            call.enqueue(new Callback<AllPharmacistModels>() {
                @Override
                public void onResponse(@NonNull Call<AllPharmacistModels> call, @NonNull retrofit2.Response<AllPharmacistModels> response) {
                    AllPharmacistModels jsonResponse = response.body();
                    assert jsonResponse != null;
                    progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success")) {

                        ArrayList<AllPharmacistList> pharmacist = jsonResponse.getPharmacist();

                        arrayAdapterList.clear();
                        for (AllPharmacistList allPharmacistList : pharmacist) {
                            if (!TextUtils.isEmpty(allPharmacistList.getType()) && allPharmacistList.getType().equalsIgnoreCase(keywords))
                                arrayAdapterList.add(allPharmacistList);
                        }

                        arrayAdapter.notifyDataSetChanged();

                        if (arrayAdapterList.size() > 0)
                            no_data_found_id.setVisibility(View.GONE);
                        else
                            no_data_found_id.setVisibility(View.VISIBLE);

                    } else {

                        no_data_found_id.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(@NonNull Call<AllPharmacistModels> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(MultiplePharmacistActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (manager.getPreferences(MultiplePharmacistActivity.this, "uploadPharmacist").equals("true")) {

            AllGetPharmacist();
            manager.setPreferences(MultiplePharmacistActivity.this, "uploadPharmacist", "false");

        }

    }
}