package com.likesby.bludoc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Adapter.PharmacistAdapter;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.AllPharmacistList;
import com.likesby.bludoc.viewModels.AllPharmacistModels;
import com.likesby.bludoc.viewModels.ResultOfApi;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AllPharmacistActivity extends AppCompatActivity {

    private ArrayList<AllPharmacistList> showModel = new ArrayList<>();
    private FrameLayout progressBar;
    private SessionManager manager;
    private PharmacistAdapter pharmacistAdapter;
    private PullToRefreshView pullToRefresh;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pharmacist);

        manager = new SessionManager();

        EditText add_pharmacist_serachView = findViewById(R.id.add_pharmacist_serachView);

        progressBar = findViewById(R.id.parent_of_progress_bar);
        message = findViewById(R.id.message);

        pullToRefresh = findViewById(R.id.pullToRefresh);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });



        pullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                AllGetPharmacist();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        pullToRefresh.setRefreshing(false);

                    }
                }, 2000);

            }
        });

        findViewById(R.id.add_a_parmacist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AllPharmacistActivity.this, AddAPharmacistActivity.class));

            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        pharmacistAdapter = new PharmacistAdapter(showModel, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pharmacistAdapter);

        pharmacistAdapter.setOnClickListener(new PharmacistAdapter.OnClickListener() {
            @Override
            public void onClick(final AllPharmacistList s, final int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(AllPharmacistActivity.this, R.style.AlertDialog));
                builder.setMessage("Do you wish to delete the pharmacist Details?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                deletePharmacist(s.getPharmacist_id(), position);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        AllGetPharmacist();

        add_pharmacist_serachView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    pharmacistAdapter.getFilter().filter(s);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void AllGetPharmacist() {

        if (Utils.isConnectingToInternet(this)) {

            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();
            ;
            final WebServices request = retrofit.create(WebServices.class);
            ;

            Call<AllPharmacistModels> call = request.allPharmacist(manager.getPreferences(AllPharmacistActivity.this, "doctor_id"));

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
                        pharmacistAdapter.notifyDataSetChanged();
                        pullToRefresh.setRefreshing(false);
                        message.setVisibility(View.GONE);

                    } else {
                        pullToRefresh.setRefreshing(false);
                        message.setVisibility(View.VISIBLE);
                        message.setText(jsonResponse.getMessage());

                    }
                }

                @Override
                public void onFailure(@NonNull Call<AllPharmacistModels> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(AllPharmacistActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void deletePharmacist(String pharmacist_id, final int position) {

        if (Utils.isConnectingToInternet(this)) {

            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();
            ;
            final WebServices request = retrofit.create(WebServices.class);
            ;

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
                        pharmacistAdapter.notifyDataSetChanged();

                        Toast.makeText(AllPharmacistActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(AllPharmacistActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResultOfApi> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(AllPharmacistActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}