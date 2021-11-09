package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.StaticsAdapter;
import com.likesby.bludoc.Adapter.TimeSlotAdapter;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.StaticsModel;
import com.likesby.bludoc.ModelLayer.StaticsModelClass;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityStaticsBludocBinding;
import com.likesby.bludoc.utils.GridSpacingItemDecoration;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.AllPharmacistList;
import com.likesby.bludoc.viewModels.AllPharmacistModels;

import org.joda.time.DateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class StaticsBludocActivity extends AppCompatActivity {

    private ActivityStaticsBludocBinding activity;
    private SessionManager manager = new SessionManager();
    private ArrayList<StaticsModelClass> arrayList = new ArrayList<>();
    private StaticsAdapter staticsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_statics_bludoc);

        GridLayoutManager gridLayoutManagerMorning = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        int spanCount = 2; // 3 columns
        int spacing = 30; // 5px
        boolean includeEdge = true;
        activity.recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        activity.recyclerView.setLayoutManager(gridLayoutManagerMorning);
        staticsAdapter = new StaticsAdapter(this,arrayList);
        activity.recyclerView.setAdapter(staticsAdapter);

        activity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        activity.categoryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 1){

                    activity.spinnerMonth.setVisibility(View.VISIBLE);
                    DateTime today = new DateTime();
                    activity.monthSpinner.setSelection(today.getMonthOfYear()-1);

                }else {

                    activity.spinnerMonth.setVisibility(View.GONE);
                    SelectedByPosition(position);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        activity.monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String month_pos = "0";
                
                if(position<9)
                    month_pos ="0"+(position+1);
                else
                    month_pos = ""+(position+1);
                
                DateTime dateTime = new DateTime();
                AllGetStatics(dateTime.getYear() + "-"+month_pos);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void SelectedByPosition(int position) {

        String currentDate = "";

        switch (position) {

            case 2:

                currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                AllGetStatics(currentDate);

                break;

            case 1:

                currentDate = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
                AllGetStatics(currentDate);

                break;

            case 0:

                AllGetStatics("");

                break;

        }

    }

    public void AllGetStatics(String date) {

        if (Utils.isConnectingToInternet(this)) {

            activity.progressBarInclude.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<StaticsModel> call = request.getStaticsModel(manager.getPreferences(StaticsBludocActivity.this, "doctor_id"), date);

            call.enqueue(new Callback<StaticsModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<StaticsModel> call, @NonNull retrofit2.Response<StaticsModel> response) {

                    activity.progressBarInclude.setVisibility(View.GONE);

                    if (response.isSuccessful() && response.body() != null) {

                        StaticsModel jsonResponse = response.body();

                        AddOnArrayList(jsonResponse);


                    } else if (response.body() != null) {

                        Toast.makeText(StaticsBludocActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        try {

                            String value = response.errorBody().string();

                            Toast.makeText(StaticsBludocActivity.this, value, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<StaticsModel> call, @NonNull Throwable t) {
                    activity.progressBarInclude.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(StaticsBludocActivity.this, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void AddOnArrayList(StaticsModel jsonResponse) {

        ArrayList<StaticsModelClass> staticsModelClasses = new ArrayList<>();

        StaticsModelClass staticsModelClass = new StaticsModelClass();
        staticsModelClass.setName("Patients");
        staticsModelClass.setValue(jsonResponse.getPatients());
        staticsModelClasses.add(staticsModelClass);

        staticsModelClass = new StaticsModelClass();
        staticsModelClass.setName("Prescriptions");
        staticsModelClass.setValue(jsonResponse.getPrescription());
        staticsModelClasses.add(staticsModelClass);

        staticsModelClass = new StaticsModelClass();
        staticsModelClass.setName("Certificates");
        staticsModelClass.setValue(jsonResponse.getCertificate());
        staticsModelClasses.add(staticsModelClass);

        staticsModelClass = new StaticsModelClass();
        staticsModelClass.setName("Invoices");
        staticsModelClass.setValue(jsonResponse.getInvoice());
        staticsModelClasses.add(staticsModelClass);

        staticsModelClass = new StaticsModelClass();
        staticsModelClass.setName("Templates");
        staticsModelClass.setValue(jsonResponse.getTemplates());
        staticsModelClasses.add(staticsModelClass);

        staticsModelClass = new StaticsModelClass();
        staticsModelClass.setName("Medicines");
        staticsModelClass.setValue(jsonResponse.getMedicine());
        staticsModelClasses.add(staticsModelClass);

        staticsModelClass = new StaticsModelClass();
        staticsModelClass.setName("Lab Test/Imaging");
        staticsModelClass.setValue(jsonResponse.getLabtest());
        staticsModelClasses.add(staticsModelClass);

        staticsModelClass = new StaticsModelClass();
        staticsModelClass.setName("Appointments");
        staticsModelClass.setValue(jsonResponse.getAppointment());
        staticsModelClasses.add(staticsModelClass);

        arrayList.clear();
        arrayList.addAll(staticsModelClasses);
        staticsAdapter.notifyDataSetChanged();

    }

}