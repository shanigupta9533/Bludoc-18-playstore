package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
import com.likesby.bludoc.Adapter.CustumeAdapter;
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
    private final ArrayList<AllPharmacistList> arrayAdapterList=new ArrayList<>();
    private final ArrayList<String> SearchList=new ArrayList<>();
    private final ArrayList<AllPharmacistList> arrayList =new ArrayList<>();
    private ListView listView;
    private CustumeAdapter arrayAdapter;
    private ActivityMultiplePharmacistBinding activity;
    private SearchView searchView;
    private RelativeLayout no_data_found_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=DataBindingUtil.setContentView(this,R.layout.activity_multiple_pharmacist);

        listView =findViewById(R.id.list_item);
        arrayAdapter=new CustumeAdapter(arrayAdapterList,this);
        listView.setAdapter(arrayAdapter);
        no_data_found_id = findViewById(R.id.no_data_found_id);

        activity.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        findViewById(R.id.refresh_button_no_data).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MultiplePharmacistActivity.this, AddAPharmacistActivity.class);
                intent.putExtra("isSelectAdd",true);
                startActivity(intent);

            }
        });

        activity.submitAllResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent data = new Intent();
                data.putExtra("arraylist_of_details",arrayList);
                setResult(RESULT_OK,data);
                finish();

                Toast.makeText(MultiplePharmacistActivity.this, "Prescription sent successfully !", Toast.LENGTH_SHORT).show();

            }
        });

        searchView=findViewById(R.id.search_view);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AllPharmacistList allPharmacistList = arrayAdapterList.get(position);
                CheckBox checkBox=view.findViewById(R.id.checkbox);

                if(!allPharmacistList.getPharmacist_mobile().equals("0")) {

                    if (arrayList.contains(allPharmacistList)) {
                        view.setBackgroundColor(Color.parseColor("#ffffff"));
                        arrayList.remove(allPharmacistList);
                        checkBox.setChecked(false);
                    } else {
                        arrayList.add(allPharmacistList);
                        view.setBackgroundColor(Color.parseColor("#eeeeee"));
                        checkBox.setChecked(true);
                    }

                    if (!arrayList.isEmpty()) {
                        activity.submitAllResult.setVisibility(View.VISIBLE);
                    } else {
                        activity.submitAllResult.setVisibility(View.GONE);
                    }

                } else {

                    Toast.makeText(MultiplePharmacistActivity.this, "Mobile Number not found!", Toast.LENGTH_SHORT).show();

                }

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
                            SearchList.add(allPharmacistList.getPharmacist_name());
                        }

                        arrayAdapterList.addAll(pharmacist);
                        arrayAdapter.notifyDataSetChanged();

                        if(SearchList.size()>0)
                            no_data_found_id.setVisibility(View.GONE);
                        else
                            no_data_found_id.setVisibility(View.VISIBLE);


                    } else {
                        no_data_found_id.setVisibility(View.VISIBLE);
                        message.setText(jsonResponse.getMessage());

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

        if(manager.getPreferences(MultiplePharmacistActivity.this,"uploadPharmacist").equals("true")){

            AllGetPharmacist();
            manager.setPreferences(MultiplePharmacistActivity.this,"uploadPharmacist","false");

        }

    }
}