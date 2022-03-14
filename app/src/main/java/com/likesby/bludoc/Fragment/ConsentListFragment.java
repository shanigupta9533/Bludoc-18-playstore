package com.likesby.bludoc.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.NewAppointmentAdapter;
import com.likesby.bludoc.Adapter.PatientConsentAdapter;
import com.likesby.bludoc.ConsentTemplateActivity;
import com.likesby.bludoc.ModelLayer.AppointmentListModel;
import com.likesby.bludoc.ModelLayer.ConsentDataModel;
import com.likesby.bludoc.ModelLayer.ConsentTemplateModel;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.ResultOfSuccess;
import com.likesby.bludoc.R;
import com.likesby.bludoc.ServerConnect.ServerConnect;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.FragmentConsentListBinding;
import com.likesby.bludoc.databinding.FragmentHistoryAppointmentBinding;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ConsentListFragment extends Fragment {

    private FragmentConsentListBinding binding;
    private ArrayList<ConsentDataModel> showModel = new ArrayList<>();
    private SessionManager manager = new SessionManager();
    private PatientConsentAdapter appointmentAdapter;
    private RelativeLayout no_data_found_id;
    private FragmentActivity fragmentActivity;
    private String patient_id;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fragmentActivity = (FragmentActivity) context;

    }

    public ConsentListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding= FragmentConsentListBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            patient_id = getArguments().getString("patient_id", "");
        }

        View root = binding.getRoot();

        LinearLayoutManager gridLayoutManagerMorning = new LinearLayoutManager(fragmentActivity, LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(gridLayoutManagerMorning);
        appointmentAdapter = new PatientConsentAdapter(showModel, getContext());
        binding.recyclerview.setAdapter(appointmentAdapter);

        appointmentAdapter.setOnClickListener(new PatientConsentAdapter.OnClickListener() {

            @Override
            public void share(ConsentDataModel consentDataModel, int i) {

                try {

                    byte[] data = consentDataModel.getDoctor_consent_id().getBytes("UTF-8");
                    String base64Ids = Base64.encodeToString(data, Base64.DEFAULT);

                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    String shareBody = ServerConnect.ShareSignature+base64Ids;
                    intent.setType("text/plain");
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(intent, "Share using"));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

        binding.searchPatientView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                appointmentAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        no_data_found_id = root.findViewById(R.id.no_data_found);

        root.findViewById(R.id.refresh_button_no_data).setOnClickListener(v -> AllGetConsent());

        binding.pullToRefresh.setOnRefreshListener(() -> {

            AllGetConsent();
            binding.pullToRefresh.setRefreshing(false);

        });

        AllGetConsent();

        return binding.getRoot();
    }

    private void AllGetConsent() {

        Retrofit retrofit = RetrofitClient.getInstance();

        binding.progressBarInclude.setVisibility(View.VISIBLE);

        final WebServices request = retrofit.create(WebServices.class);

        Call<ResultOfSuccess> call = request.getMyConsent(patient_id);

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
                        appointmentAdapter.notifyDataSetChanged();

                    } else if (response.body().getMessage().equals("Data not available")) {

                        Toast.makeText(fragmentActivity, "No Template Found", Toast.LENGTH_SHORT).show();

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