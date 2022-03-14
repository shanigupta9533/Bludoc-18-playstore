package com.likesby.bludoc.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.ConsentPatientAdapter;
import com.likesby.bludoc.Adapter.HistoryAdapter;
import com.likesby.bludoc.ModelLayer.ConsentDataModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.NewEntities3.PrescriptionItem;
import com.likesby.bludoc.ModelLayer.ResultOfSuccess;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.FragmentConsentPatientHistoryBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ConsentPatientHistoryFragment extends Fragment {

    private FragmentConsentPatientHistoryBinding binding;
    private ArrayList<ConsentDataModel> showModel = new ArrayList<>();
    private ConsentPatientAdapter consentPatientAdapter;
    private FragmentActivity fragmentActivity;
    private SessionManager manager = new SessionManager();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fragmentActivity = (FragmentActivity) context;

    }

    public ConsentPatientHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentConsentPatientHistoryBinding.inflate(inflater, container, false);

        //Create new GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(fragmentActivity,
                1,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        binding.rvTemplate.setLayoutManager(gridLayoutManager);
        binding.rvTemplate.setNestedScrollingEnabled(false);
        consentPatientAdapter = new ConsentPatientAdapter(showModel, fragmentActivity);
        binding.rvTemplate.setAdapter(consentPatientAdapter);

        AllGetConsent();

        return binding.getRoot();
    }

    private void AllGetConsent() {

        Retrofit retrofit = RetrofitClient.getInstance();

        binding.progressBarInclude.setVisibility(View.VISIBLE);

        final WebServices request = retrofit.create(WebServices.class);

        Call<ResultOfSuccess> call = request.getMyConsentHistory(manager.getPreferences(fragmentActivity,"doctor_id"));

        call.enqueue(new Callback<ResultOfSuccess>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<ResultOfSuccess> call, @NonNull retrofit2.Response<ResultOfSuccess> response) {

                if (response.isSuccessful() && response.body() != null) {

                    binding.progressBarInclude.setVisibility(View.GONE);

                    ResultOfSuccess jsonResponse = response.body();
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success")) {

                        ArrayList<ConsentDataModel> consent = response.body().getConsent();
                        showModel.clear();
                        showModel.addAll(consent);
                        consentPatientAdapter.notifyDataSetChanged();

                    } else if (response.body().getMessage().equals("Data not available")) {

                        Toast.makeText(fragmentActivity, "No Data Found", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(fragmentActivity, "Something went wrong...", Toast.LENGTH_SHORT).show();

                    }

                } else {

                    Toast.makeText(fragmentActivity, "Something went wrong...", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(@NonNull Call<ResultOfSuccess> call, @NonNull Throwable t) {
                binding.progressBarInclude.setVisibility(View.GONE);
                Log.e("Error  ***", t.getMessage());
                Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

            }
        });

    }

}