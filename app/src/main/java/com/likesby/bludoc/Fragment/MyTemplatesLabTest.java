package com.likesby.bludoc.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.likesby.bludoc.Adapter.LabTemplateAdapter;
import com.likesby.bludoc.Adapter.TemplateAdapter;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseNewMyTemplates;
import com.likesby.bludoc.ModelLayer.NewEntities.TemplatesItem;
import com.likesby.bludoc.ModelLayer.NewEntities2.LabTemplatesItem;
import com.likesby.bludoc.ModelLayer.NewEntities2.ResponseLabTemplates;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.TemplateSelection;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyTemplatesLabTest extends Fragment {
    static Context mContext;
    Dialog dialog;
    View v;
    static RecyclerView recyclerView;
    static LinearLayoutManager lLayout;
    LinearLayout ll_patients_view;
    static FrameLayout fl_progress_bar;
    static Button btn_new_template;
    static TextView tv_no_template;
    static ImageView iv_no_template;
    private static final String TAG = "MyTemplates_____";
    private static ApiViewHolder apiViewHolder;
    private static CompositeDisposable mBag = new CompositeDisposable();
    static SessionManager manager;
    static LabTemplateAdapter templateAdapter;
    static ArrayList<LabTemplatesItem> templatesItemArrayList = new ArrayList<>();
    static EditText et_template_name;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater.inflate(R.layout.state_frag, container, false);
        v = inflater.inflate(R.layout.my_templates, container, false);
        mContext = getContext();
        manager = new SessionManager();
        initViewsnCalls(v);
        return v;
    }

    private void initViewsnCalls(View view) {
        btn_new_template = view.findViewById(R.id.btn_new_template);
        fl_progress_bar = view.findViewById(R.id.fl_progress_layout);
        tv_no_template = view.findViewById(R.id.tv_no_template);
        iv_no_template = view.findViewById(R.id.iv_no_template);
        recyclerView =  view.findViewById(R.id.rv_template);

        tv_no_template.setText(getResources().getString(R.string.create_prescribed_lab_test));

        Glide.with(mContext)
                .load(R.drawable.microscope)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(iv_no_template);



        btn_new_template.setVisibility(View.GONE);
        tv_no_template.setVisibility(View.GONE);
        iv_no_template.setVisibility(View.GONE);
        fl_progress_bar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);


        initRecyclerViews(view);
        initViewHolder();
        btn_new_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog_data = new Dialog(mContext);
                dialog_data.setCancelable(false);

                dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

                dialog_data.setContentView(R.layout.popup_template);

                WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
                Window window = dialog_data.getWindow();
                lp_number_picker.copyFrom(window.getAttributes());

                lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

                //window.setGravity(Gravity.CENTER);
                window.setAttributes(lp_number_picker);

                 et_template_name = dialog_data.findViewById(R.id.et_template_name);
                Button btn_add = dialog_data.findViewById(R.id.popup_add);
                Button btn_cancel = dialog_data.findViewById(R.id.promo_cancel);


                btn_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (et_template_name.getText().toString().trim().equalsIgnoreCase("")) {
                            et_template_name.setError("Name required");
                            et_template_name.setFocusable(true);
                        } else {
                            dialog_data.dismiss();
                            AddTemplateLabTest addTemplate = new AddTemplateLabTest();
                            Bundle bundle = new Bundle();
                            bundle.putString("name",et_template_name.getText().toString().trim());
                            addTemplate.setArguments(bundle);
                            getFragmentManager().beginTransaction().replace(android.R.id.content, addTemplate).addToBackStack(null).commit();

                        }
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_data.dismiss();
                    }
                });

                dialog_data.show();
            }







        });
    }

    //================     Main  Categories       ==============================================
    private void initRecyclerViews(View view) {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
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

    public void initViewHolder() {
        fl_progress_bar.setVisibility(View.VISIBLE);
        apiViewHolder.getLabTemplates(manager.getPreferences(mContext, "doctor_id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseTemplates);

    }

    private SingleObserver<ResponseLabTemplates> responseTemplates = new SingleObserver<ResponseLabTemplates>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseLabTemplates response) {
            if (response != null) {

                Log.e(TAG, "responseTemplates: >> " + response.getMessage());
                fl_progress_bar.setVisibility(View.GONE);
                btn_new_template.setVisibility(View.VISIBLE);
                if (response.getMessage() == null) {
                    tv_no_template.setVisibility(View.VISIBLE);
                    iv_no_template.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                } else if (response.getMessage().equals("Lab templates")) {
                    templatesItemArrayList = new ArrayList<>();
                    templatesItemArrayList = response.getLabTemplates();
                    Collections.reverse(templatesItemArrayList);
                    templateAdapter = new LabTemplateAdapter(templatesItemArrayList, TemplateSelection.patient_id,TemplateSelection.definer,mContext,mBag,apiViewHolder,fl_progress_bar);
                    recyclerView.setAdapter(templateAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    tv_no_template.setVisibility(View.GONE);
                    iv_no_template.setVisibility(View.GONE);
                }
                else
                {
                    tv_no_template.setVisibility(View.VISIBLE);
                    iv_no_template.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
            else
            {
                fl_progress_bar.setVisibility(View.GONE);
                tv_no_template.setVisibility(View.VISIBLE);
                iv_no_template.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                btn_new_template.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError(Throwable e) {
            tv_no_template.setVisibility(View.VISIBLE);
            iv_no_template.setVisibility(View.VISIBLE);
            fl_progress_bar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            btn_new_template.setVisibility(View.VISIBLE);
            Log.e(TAG, "onError: responseTemplates >> " + e.toString());
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


}