package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.ReferralAdapter;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.AllPharmacistList;
import com.likesby.bludoc.viewModels.AllPharmacistModels;
import com.likesby.bludoc.viewModels.ResultOfApi;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class RefrelActivity extends AppCompatActivity {

    private final ArrayList<AllPharmacistList> showModel = new ArrayList<>();
    private FrameLayout progressBar;
    private SessionManager manager;
    private ReferralAdapter referralAdapter;
    private PullToRefreshView pullToRefresh;
    private RelativeLayout no_data_found_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrel);

        manager = new SessionManager();

        EditText add_pharmacist_serachView = findViewById(R.id.add_pharmacist_serachView);

        progressBar = findViewById(R.id.parent_of_progress_bar);
        no_data_found_id = findViewById(R.id.no_data_found_id);

        findViewById(R.id.info).setOnClickListener(v -> startActivity(new Intent(RefrelActivity.this, AboutPharmacyActivity.class)));

        pullToRefresh = findViewById(R.id.pullToRefresh);

        findViewById(R.id.refresh_button_no_data).setOnClickListener(v -> {

            Intent intent = new Intent(RefrelActivity.this, AddAReferralActivity.class);
            intent.putExtra("isSelectAdd", true);
            startActivity(intent);

        });

        findViewById(R.id.back).setOnClickListener(v -> onBackPressed());

        pullToRefresh.setOnRefreshListener(() -> {

            AllGetReferral();

            new Handler(Looper.getMainLooper()).postDelayed(() -> pullToRefresh.setRefreshing(false), 2000);

        });

        findViewById(R.id.add_a_parmacist).setOnClickListener(v -> startActivity(new Intent(RefrelActivity.this, AddAReferralActivity.class)));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        referralAdapter = new ReferralAdapter(showModel, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(referralAdapter);

        referralAdapter.setOnClickListener(new ReferralAdapter.OnClickListener() {
            @Override
            public void onDelete(final AllPharmacistList s, final int position) {

           AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(RefrelActivity.this, R.style.AlertDialog));
                builder.setMessage("Do you wish to delete the Referral Centre?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> deleteReferral(s.getPharmacist_id(), position))
                        .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();

            }

            @Override
            public void onChecked(AllPharmacistList s, int position, boolean isChecked) {

                sendIdsOnServer(s, isChecked);

            }
        });

                AllGetReferral();

        add_pharmacist_serachView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                referralAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void sendIdsOnServer(AllPharmacistList s, boolean isChecked) {

        if (Utils.isConnectingToInternet(this)) {

            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            String str;
            if (isChecked) str = "yes";
            else str = "false";

            Call<ResultOfApi> call = request.sendPharmacistById(manager.getPreferences(RefrelActivity.this, "doctor_id"), s.getPharmacist_id(), str);

            call.enqueue(new Callback<ResultOfApi>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {
                    ResultOfApi jsonResponse = response.body();
                    assert jsonResponse != null;

                    if (jsonResponse.getSuccess().equals("success")) {

                        if (isChecked) {
                            Toast.makeText(RefrelActivity.this, "Added to send list", Toast.LENGTH_SHORT).show();
                            s.setIs_check("yes");
                        }  else {
                            Toast.makeText(RefrelActivity.this, "Removed from send list", Toast.LENGTH_SHORT).show();
                            s.setIs_check("no");
                        }

                    } else {

                        Toast.makeText(RefrelActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(RefrelActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void AllGetReferral() {

        if (Utils.isConnectingToInternet(this)) {

            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<AllPharmacistModels> call = request.allPharmacist(manager.getPreferences(RefrelActivity.this, "doctor_id"));

            call.enqueue(new Callback<AllPharmacistModels>() {
                @Override
                public void onResponse(@NonNull Call<AllPharmacistModels> call, @NonNull retrofit2.Response<AllPharmacistModels> response) {
                    AllPharmacistModels jsonResponse = response.body();
                    assert jsonResponse != null;
                    progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success")) {

                        ArrayList<AllPharmacistList> pharmacist = jsonResponse.getPharmacist();
                        showModel.clear();
                        showModel.addAll(pharmacist);
                        referralAdapter.notifyDataSetChanged();
                        pullToRefresh.setRefreshing(false);
                        no_data_found_id.setVisibility(View.GONE);

                    } else {

                        pullToRefresh.setRefreshing(false);
                        no_data_found_id.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(@NonNull Call<AllPharmacistModels> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(RefrelActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteReferral(String pharmacist_id, final int position) {

        if (Utils.isConnectingToInternet(this)) {

            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResultOfApi> call = request.deletePharmacist(pharmacist_id);

            call.enqueue(new Callback<ResultOfApi>() {
                @Override
                public void onResponse(@NonNull Call<ResultOfApi> call, @NonNull retrofit2.Response<ResultOfApi> response) {
                    ResultOfApi jsonResponse = response.body();
                    assert jsonResponse != null;
                    progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success") &&
                            jsonResponse.getMessage().equalsIgnoreCase("Pharmacist Deleted")) {

                        progressBar.setVisibility(View.GONE);
                        showModel.remove(position);
                        referralAdapter.notifyDataSetChanged();

                        if (showModel.size() == 0)
                            no_data_found_id.setVisibility(View.VISIBLE);
                        else
                            no_data_found_id.setVisibility(View.GONE);


                        Toast.makeText(RefrelActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(RefrelActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(RefrelActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (manager.getPreferences(RefrelActivity.this, "uploadPharmacist").equals("true")) {

            AllGetReferral();
            manager.setPreferences(RefrelActivity.this, "uploadPharmacist", "false");

        }

    }

}