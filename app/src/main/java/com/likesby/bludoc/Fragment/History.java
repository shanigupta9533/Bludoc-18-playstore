package com.likesby.bludoc.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.likesby.bludoc.Adapter.HistoryAdapter;
import com.likesby.bludoc.Adapter.TemplateAdapter;
import com.likesby.bludoc.ModelLayer.Entities.ResponseTemplates;
import com.likesby.bludoc.ModelLayer.Entities.TemplatesItem;
import com.likesby.bludoc.ModelLayer.NewEntities3.PrescriptionItem;
import com.likesby.bludoc.ModelLayer.NewEntities3.ResponseHistory;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.utils.CustomErrorItem;
import com.likesby.bludoc.utils.NoPaginate.callback.OnLoadMoreListener;
import com.likesby.bludoc.utils.NoPaginate.paginate.NoPaginate;
import com.likesby.bludoc.viewModels.ApiViewHolder;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class History extends Fragment {
    Context mContext;
    Dialog dialog;
    View v;
    RecyclerView recyclerView;
    LinearLayoutManager lLayout;
    LinearLayout ll_patients_view;
    FrameLayout fl_progress_bar;
    int page = 0;
    // Button btn_new_template;
    private static final String TAG = "History_____";
    private ApiViewHolder apiViewHolder;
    private CompositeDisposable mBag = new CompositeDisposable();
    SessionManager manager;
    HistoryAdapter historyAdapter;
    ArrayList<PrescriptionItem> prescriptionList = new ArrayList<>();
    EditText et_template_name, searchBarMaterialMedicine;
    ImageView back;
    private AdView mAdView, mAdViewNative;
    private String patient_id;
    private int isCertificate;
    String keywords = "";
    private NoPaginate noPaginate;
    private View no_data_found_id;
    private final int limit = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater.inflate(R.layout.state_frag, container, false);
        v = inflater.inflate(R.layout.history, container, false);

        no_data_found_id = v.findViewById(R.id.no_data_found);

        v.findViewById(R.id.refresh_button_no_data).setOnClickListener(view -> {

            no_data_found_id.setVisibility(View.GONE);
            page = 0;
            prescriptionList.clear();
            historyAdapter.notifyDataSetChanged();
            fl_progress_bar.setVisibility(View.VISIBLE);

            noPaginate.showLoading(false);
            noPaginate.showError(false);
            noPaginate.setNoMoreItems(false);

        });

        Bundle arguments = getArguments();

        if (arguments != null) {

            patient_id = arguments.getString("patient_id");
            isCertificate = arguments.getInt("isCertificate", 0);

            if(isCertificate==1){
                keywords = "Prescription";
            } else if(isCertificate==2) {
                keywords = "Certificate";
            }

        }

        mContext = getContext();
        manager = new SessionManager();
        initViewsnCalls(v);
        return v;
    }

    private void initViewsnCalls(View view) {
        //   btn_new_template = view.findViewById(R.id.btn_new_template);
        fl_progress_bar = view.findViewById(R.id.fl_progress_layout);
        recyclerView = view.findViewById(R.id.rv_template);
        searchBarMaterialMedicine = view.findViewById(R.id.medicine_searchview);
        searchBarMaterialMedicine.setHint("Type here to search");
        //    btn_new_template.setVisibility(View.GONE);

        back = view.findViewById(R.id.btn_back_edit_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {

                page = 0;
                prescriptionList.clear();
                historyAdapter.notifyDataSetChanged();
                fl_progress_bar.setVisibility(View.VISIBLE);

                noPaginate.showLoading(false);
                noPaginate.showError(false);
                noPaginate.setNoMoreItems(false)      ;

            }
        });

        initRecyclerViews(view);
        initViewHolder();
        mAdView = view.findViewById(R.id.adView);
    }


    //================     Main  Categories       ==============================================
    private void initRecyclerViews(View view) {
        recyclerView = view.findViewById(R.id.rv_template);
        //Create new GridLayoutManager
        @SuppressLint("WrongConstant") GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,
                1,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        historyAdapter = new HistoryAdapter(prescriptionList, mContext, isCertificate);
        recyclerView.setAdapter(historyAdapter);

    }

    private void initViewHolder() {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);

        if (prescriptionList.size() > 0) {
            //Collections.reverse(prescriptionList);
            recyclerView.setVisibility(View.VISIBLE);
        } else {


            if (page == 0) fl_progress_bar.setVisibility(View.VISIBLE);

            noPaginate = NoPaginate.with(recyclerView).setLoadingTriggerThreshold(0)
                    .setCustomErrorItem(new CustomErrorItem())
                    .setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {

                            noPaginate.showLoading(true);
                            noPaginate.showError(false);

                            int offset = 0;
                            if(page>0)
                                offset = (page*limit);

                            apiViewHolder.getAllPrescription(manager.getPreferences(mContext, "doctor_id"),patient_id,String.valueOf(offset), String.valueOf(limit),
                                    searchBarMaterialMedicine.getText().toString(),keywords)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(responseHistory);

                        }
                    }).build();

        }
    }

    private SingleObserver<ResponseHistory> responseHistory = new SingleObserver<ResponseHistory>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseHistory response) {
            if (response != null) {

                fl_progress_bar.setVisibility(View.GONE);
                no_data_found_id.setVisibility(View.GONE);
                if (response.getMessage() == null) {
                    noPaginate.showError(true);
                    noPaginate.showLoading(false);


                } else if (response.getMessage().equals("Prescription")) {

                    int size=prescriptionList.size();
                    ArrayList<PrescriptionItem> prescriptionListModify = response.getPrescription();
                    prescriptionList.addAll(prescriptionListModify);
                    recyclerView.setVisibility(View.VISIBLE);

                    if (prescriptionListModify.isEmpty()) noPaginate.setNoMoreItems(true);

                    if(page == 0 && prescriptionList.isEmpty()){
                        no_data_found_id.setVisibility(View.VISIBLE);

                    }

                    historyAdapter.notifyDataSetChanged();

                    page++;
                    noPaginate.showLoading(false);
                    fl_progress_bar.setVisibility(View.GONE);

                } else if(page ==0 && response.getMessage().equals("Data not available")) {
                    noPaginate.showError(true);
                    noPaginate.showLoading(false);
                    no_data_found_id.setVisibility(View.VISIBLE);

                } else {

                    no_data_found_id.setVisibility(View.GONE);
                    noPaginate.setNoMoreItems(true);

                }

               /* else if (response.getMessage().equals("Data not available")) {
                    tv_no_template.setVisibility(View.VISIBLE);
                    iv_no_template.setVisibility(View.VISIBLE);


                }*/
            } else {
                fl_progress_bar.setVisibility(View.GONE);
                noPaginate.showError(true);
                noPaginate.showLoading(false);
                no_data_found_id.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(Throwable e) {
            fl_progress_bar.setVisibility(View.GONE);
            noPaginate.showError(true);
            noPaginate.showLoading(false);


            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


}