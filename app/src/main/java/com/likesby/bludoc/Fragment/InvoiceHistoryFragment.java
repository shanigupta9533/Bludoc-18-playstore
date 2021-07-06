package com.likesby.bludoc.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.likesby.bludoc.Adapter.InvoicesHistoryAdapter;
import com.likesby.bludoc.Adapter.InvoicesPresAdapter;
import com.likesby.bludoc.AllPharmacistActivity;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.InvoicePresModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.FragmentInvoiceHistoryBinding;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.AllPharmacistList;
import com.likesby.bludoc.viewModels.AllPharmacistModels;
import com.yalantis.phoenix.PullToRefreshView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class InvoiceHistoryFragment extends Fragment {

    private FragmentInvoiceHistoryBinding binding;
    private ArrayList<InvoicePresModel> invoicePresModelArrayList=new ArrayList<>();
    private InvoicesHistoryAdapter invoicesPresAdapter;
    private FragmentActivity fragmentActivity;
    private View root;
    private FrameLayout progressBar;
    private SessionManager manager;
    private PullToRefreshView pullToRefresh;
    private RelativeLayout no_data_found_id;

    public InvoiceHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        fragmentActivity= (FragmentActivity) context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentInvoiceHistoryBinding.inflate(inflater, container, false);

        manager=new SessionManager();

        root = binding.getRoot();

        pullToRefresh=root.findViewById(R.id.pullToRefresh);
        no_data_found_id=root.findViewById(R.id.no_data_found);

        progressBar=root.findViewById(R.id.fl_progress_layout);

        invoicesPresAdapter=new InvoicesHistoryAdapter(invoicePresModelArrayList,getContext());
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.recyclerview.setAdapter(invoicesPresAdapter);

        invoicesPresAdapter.setOnClickListener(new InvoicesHistoryAdapter.OnClickListener() {
            @Override
            public void onDelete(AllPharmacistList s, int position) {

            }

            @Override
            public void onSearch(int i) {

                if(i<=0)
                    no_data_found_id.setVisibility(View.VISIBLE);
                else
                    no_data_found_id.setVisibility(View.GONE);


            }
        });

        binding.toolbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                invoicesPresAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AllGetInvoicesHistory();

        pullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                AllGetInvoicesHistory();

                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        pullToRefresh.setRefreshing(false);
                    }
                },3000);

            }
        });

        return binding.getRoot();
    }

    public void AllGetInvoicesHistory() {

        if (Utils.isConnectingToInternet(fragmentActivity.getApplicationContext())) {

            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResponseSuccess> call = request.allInvoicesHistory(manager.getPreferences(fragmentActivity, "doctor_id"));

            call.enqueue(new Callback<ResponseSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResponseSuccess> call, @NonNull retrofit2.Response<ResponseSuccess> response) {
                    ResponseSuccess jsonResponse = response.body();
                    pullToRefresh.setRefreshing(false);
                    assert jsonResponse != null;
                    progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success")) {

                        ArrayList<InvoicePresModel> invoicesPresModel = jsonResponse.getInvoice();
                        invoicePresModelArrayList.clear();
                        invoicePresModelArrayList.addAll(invoicesPresModel);
                        invoicesPresAdapter.notifyDataSetChanged();
                        pullToRefresh.setRefreshing(false);
                        no_data_found_id.setVisibility(View.GONE);

                    } else {

                        pullToRefresh.setRefreshing(false);
                        no_data_found_id.setVisibility(View.VISIBLE);
                        Toast.makeText(fragmentActivity, jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseSuccess> call, @NonNull Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(fragmentActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}