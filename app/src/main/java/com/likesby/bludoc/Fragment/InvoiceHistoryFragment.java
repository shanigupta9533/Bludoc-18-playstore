package com.likesby.bludoc.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
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
import com.likesby.bludoc.utils.CustomErrorItem;
import com.likesby.bludoc.utils.NoPaginate.callback.OnLoadMoreListener;
import com.likesby.bludoc.utils.NoPaginate.paginate.NoPaginate;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.AllPharmacistList;
import com.likesby.bludoc.viewModels.AllPharmacistModels;
import com.yalantis.phoenix.PullToRefreshView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class InvoiceHistoryFragment extends Fragment {

    private FragmentInvoiceHistoryBinding binding;
    private ArrayList<InvoicePresModel> invoicePresModelArrayList = new ArrayList<>();
    private InvoicesHistoryAdapter invoicesPresAdapter;
    private FragmentActivity fragmentActivity;
    private View root;
    private FrameLayout progressBar;
    int page = 0;
    private SessionManager manager;
    private PullToRefreshView pullToRefresh;
    private RelativeLayout no_data_found_id;
    private String patient_id;
    private final int limit = 10;
    private NoPaginate noPaginate;
    private WebServices apiViewHolder;

    public InvoiceHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        fragmentActivity = (FragmentActivity) context;

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInvoiceHistoryBinding.inflate(inflater, container, false);

        manager = new SessionManager();

        if (getArguments() != null) {
            patient_id = getArguments().getString("patient_id", "");
        }

        root = binding.getRoot();

        pullToRefresh = root.findViewById(R.id.pullToRefresh);
        no_data_found_id = root.findViewById(R.id.no_data_found);

        progressBar = root.findViewById(R.id.fl_progress_layout);

        invoicesPresAdapter = new InvoicesHistoryAdapter(invoicePresModelArrayList, getContext());
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerview.setAdapter(invoicesPresAdapter);

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                page = 0;
                invoicePresModelArrayList.clear();
                invoicesPresAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.VISIBLE);

                noPaginate.showLoading(false);
                noPaginate.showError(false);
                noPaginate.setNoMoreItems(false);
                new Handler(Looper.myLooper()).postDelayed(() -> pullToRefresh.setRefreshing(false), 3000);

            }
        });

        binding.getRoot().findViewById(R.id.refresh_button_no_data).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {

                no_data_found_id.setVisibility(View.GONE);
                page = 0;
                invoicePresModelArrayList.clear();
                invoicesPresAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.VISIBLE);

                noPaginate.showLoading(false);
                noPaginate.showError(false);
                noPaginate.setNoMoreItems(false);

            }
        });

        invoicesPresAdapter.setOnClickListener(new InvoicesHistoryAdapter.OnClickListener() {
            @Override
            public void onDelete(AllPharmacistList s, int position) {

            }

            @Override
            public void onSearch(int i) {

                if (i <= 0)
                    no_data_found_id.setVisibility(View.VISIBLE);
                else
                    no_data_found_id.setVisibility(View.GONE);


            }
        });

        if (page == 0) progressBar.setVisibility(View.VISIBLE);

        noPaginate = NoPaginate.with(binding.recyclerview).setLoadingTriggerThreshold(0)
                .setCustomErrorItem(new CustomErrorItem())
                .setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        noPaginate.showLoading(true);
                        noPaginate.showError(false);
                        AllGetInvoicesHistory();

                    }
                }).build();

        pullToRefresh.setOnRefreshListener(() -> {
            page = 0;
            invoicePresModelArrayList.clear();
            invoicesPresAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.VISIBLE);

            noPaginate.showLoading(false);
            noPaginate.showError(false);
            noPaginate.setNoMoreItems(false);
            new Handler(Looper.myLooper()).postDelayed(() -> pullToRefresh.setRefreshing(false), 3000);

        });

        return binding.getRoot();
    }

    public void AllGetInvoicesHistory() {

        Retrofit retrofit = RetrofitClient.getInstance();

        final WebServices request = retrofit.create(WebServices.class);

        int offset = 0;
        if (page > 0)
            offset = (page * limit);

        Call<ResponseSuccess> call = request.allInvoicesHistory(manager.getPreferences(fragmentActivity, "doctor_id"), patient_id, offset, String.valueOf(limit),
                binding.toolbar.getText().toString());

        call.enqueue(new Callback<ResponseSuccess>() {
            @Override
            public void onResponse(@NonNull Call<ResponseSuccess> call, @NonNull retrofit2.Response<ResponseSuccess> response) {

                if (response.isSuccessful() && response.body() != null) {

                    ResponseSuccess jsonResponse = response.body();
                    pullToRefresh.setRefreshing(false);
                    assert jsonResponse != null;
                    progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("Success")) {

                        int size = invoicePresModelArrayList.size() - 1;
                        ArrayList<InvoicePresModel> invoicesPresModel = jsonResponse.getInvoice();
                        invoicePresModelArrayList.addAll(invoicesPresModel);

                        if (invoicesPresModel.isEmpty())
                            noPaginate.setNoMoreItems(true);

                        if (page == 0 && invoicePresModelArrayList.isEmpty()) {
                            no_data_found_id.setVisibility(View.VISIBLE);
                        }

                        invoicesPresAdapter.notifyItemRangeChanged(size, invoicePresModelArrayList.size());
                        pullToRefresh.setRefreshing(false);

                        page++;
                        noPaginate.showLoading(false);
                        progressBar.setVisibility(View.GONE);

                    } else if (page == 0 && response.body().getMessage().equals("No data available")) {

                        pullToRefresh.setRefreshing(false);
                        noPaginate.showError(true);
                        noPaginate.showLoading(false);
                        no_data_found_id.setVisibility(View.VISIBLE);

                    } else {

                        no_data_found_id.setVisibility(View.GONE);
                        noPaginate.setNoMoreItems(true);
                    }

                } else {

                    Toast.makeText(fragmentActivity, "Something went wrong...", Toast.LENGTH_SHORT).show();

                    noPaginate.showError(true);
                    noPaginate.showLoading(false);

                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseSuccess> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("Error  ***", t.getMessage());
                noPaginate.showError(true);
                noPaginate.showLoading(false);
                Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}