package com.likesby.bludoc.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
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
import com.likesby.bludoc.viewModels.ApiViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class History  extends Fragment {
    Context mContext;
    Dialog dialog;
    View v;
    static RecyclerView recyclerView;
    static LinearLayoutManager lLayout;
    LinearLayout ll_patients_view;
    FrameLayout fl_progress_bar;
   // Button btn_new_template;
    TextView tv_no_template;
    ImageView iv_no_template;
    private static final String TAG = "History_____";
    private ApiViewHolder apiViewHolder;
    private CompositeDisposable mBag = new CompositeDisposable();
    SessionManager manager;
    HistoryAdapter historyAdapter;
    ArrayList<PrescriptionItem> prescriptionList = new ArrayList<>();
    EditText et_template_name,searchBarMaterialMedicine;
    ImageView back;
    private AdView mAdView,mAdViewNative;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater.inflate(R.layout.state_frag, container, false);
        v = inflater.inflate(R.layout.history, container, false);
        mContext = getContext();
        manager = new SessionManager();
        initViewsnCalls(v);
        return v;
    }

    private void initViewsnCalls(View view) {
     //   btn_new_template = view.findViewById(R.id.btn_new_template);
        fl_progress_bar = view.findViewById(R.id.fl_progress_layout);
        tv_no_template = view.findViewById(R.id.tv_no_template);
        iv_no_template = view.findViewById(R.id.iv_no_template);
        recyclerView =  view.findViewById(R.id.rv_template);
        searchBarMaterialMedicine = view.findViewById(R.id.medicine_searchview);
        searchBarMaterialMedicine.setHint("Type here to search");
    //    btn_new_template.setVisibility(View.GONE);
        tv_no_template.setVisibility(View.GONE);
        iv_no_template.setVisibility(View.GONE);

        recyclerView.setVisibility(View.GONE);
        back = view.findViewById(R.id.btn_back_edit_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        initRecyclerViews(view);
        initViewHolder();
        mAdView = view.findViewById(R.id.adView);
        if(manager.contains(mContext,"show_banner_ad"))
        BannerAd();


    }


    private  void BannerAd(){
        mAdView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder()/*addTestDevice("31B09DFC1F78AF28F2AFB1506F51B0BF")*/.build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //Toast.makeText(mContext, "ErrorCode = "+errorCode, Toast.LENGTH_SHORT).show();
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

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
    }

    private void initViewHolder() {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);

            if (prescriptionList.size() > 0) {
                //Collections.reverse(prescriptionList);
                historyAdapter = new HistoryAdapter(prescriptionList, mContext);
                recyclerView.setAdapter(historyAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                tv_no_template.setVisibility(View.GONE);
                iv_no_template.setVisibility(View.GONE);
                searchBarMaterialMedicine.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence newText, int start, int before, int count) {
                        if (newText.length() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            historyAdapter.getFilter().filter(newText);
                        } else {
                            historyAdapter.getFilter().filter(newText);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
            } else {
                apiViewHolder.getAllPrescription(manager.getPreferences(mContext, "doctor_id"))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(responseHistory);
                fl_progress_bar.setVisibility(View.VISIBLE);


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

                Log.e(TAG, "responseTemplates: >> " + response.getMessage());
                fl_progress_bar.setVisibility(View.GONE);
                if (response.getMessage() == null) {
                    tv_no_template.setVisibility(View.VISIBLE);
                    iv_no_template.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                } else if (response.getMessage().equals("Prescription")) {
                    prescriptionList = new ArrayList<>();
                    prescriptionList = response.getPrescription();
                    Collections.reverse(prescriptionList);
                    historyAdapter = new HistoryAdapter(prescriptionList,mContext);
                    recyclerView.setAdapter(historyAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_no_template.setVisibility(View.GONE);
                    iv_no_template.setVisibility(View.GONE);
                    searchBarMaterialMedicine.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence newText, int start, int before, int count) {
                            if (newText.length() > 0) {
                                recyclerView.setVisibility(View.VISIBLE);
                                historyAdapter.getFilter().filter(newText);
                            } else {
                                historyAdapter.getFilter().filter(newText);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                }
               /* else if (response.getMessage().equals("Data not available")) {
                    tv_no_template.setVisibility(View.VISIBLE);
                    iv_no_template.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    searchBarMaterialMedicine.setVisibility(View.GONE);
                }*/
                else
                {
                    tv_no_template.setVisibility(View.VISIBLE);
                    iv_no_template.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    searchBarMaterialMedicine.setVisibility(View.GONE);
                }
            }
            else
            {
                fl_progress_bar.setVisibility(View.GONE);
                tv_no_template.setVisibility(View.VISIBLE);
                iv_no_template.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                searchBarMaterialMedicine.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(Throwable e) {
            tv_no_template.setVisibility(View.VISIBLE);
            iv_no_template.setVisibility(View.VISIBLE);
            fl_progress_bar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            searchBarMaterialMedicine.setVisibility(View.GONE);

            Log.e(TAG, "onError: responseTemplates >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


}