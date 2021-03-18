package com.likesby.bludoc.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.likesby.bludoc.Adapter.Pres_adapter;
import com.likesby.bludoc.Adapter.SampleAdapter;
import com.likesby.bludoc.ModelLayer.Entities.MedicinesItem;
import com.likesby.bludoc.ModelLayer.Entities.PrescriptionItem;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Sample extends Fragment {
    Button generatePDF, back;
    Context mContext;
    Dialog dialog;
    View v;
    static RecyclerView rView;
    static LinearLayoutManager lLayout;
    PrescriptionItem prescriptionItem = new PrescriptionItem();
    ImageView image_layout, signature;
    TextView sig_text, title, textView_Clinic_name, textView_DocName, textView_degree, textView_res_num, textView_des;
    TextView textView_UID, textView_pat_name, textView_age, textView_days, textView_diag;
    TextView textView_date, textView_add, textView_time, textView_contact, textView_email, textView_treatment_advice,textView_findings,textView_history,textView_chief_complaint,textView_end_note,textView_medical_cert_desc;
    SessionManager manager;
    NestedScrollView scrollview_edit_profile;
    private CompositeDisposable mBag = new CompositeDisposable();
    ImageView sample_image;
    FrameLayout fl_progress_bar;
    View bottomSheet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        manager = new SessionManager();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater.inflate(R.layout.state_frag, container, false);
        v = inflater.inflate(R.layout.generate_prescription, container, false);
        initCalls(v);
        return v;
    }


    private void initCalls(View view) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        ConstraintLayout root = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null); //RelativeLayout is root view of my UI(xml) file.
        root.setDrawingCacheEnabled(true);

        lLayout = new LinearLayoutManager(mContext);
        rView = (RecyclerView) view.findViewById(R.id.pres_recycler);
        rView.setLayoutManager(lLayout);
        bottomSheet = view.findViewById(R.id.bottom_sheet);
        bottomSheet.setVisibility(View.GONE);
        back = view.findViewById(R.id.btn_back_edit_profile);
        sample_image = view.findViewById(R.id.sample_image);
        textView_end_note = view.findViewById(R.id.textView_end_note);
        scrollview_edit_profile = view.findViewById(R.id.scrollview_edit_profile);
        generatePDF = view.findViewById(R.id.generatePDF);
        title = view.findViewById(R.id.title);
        textView_chief_complaint = view.findViewById(R.id.textView_chief_complaint);
        textView_history = view.findViewById(R.id.textView_history);
        textView_treatment_advice = view.findViewById(R.id.textView_treatment_advice);
        textView_findings = view.findViewById(R.id.textView_findings);
        textView_diag = view.findViewById(R.id.textView_diag);
        textView_end_note = view.findViewById(R.id.textView_end_note);
        textView_medical_cert_desc  = view.findViewById(R.id.textView_medical_cert_desc);
        textView_medical_cert_desc.setVisibility(View.GONE);

        generatePDF.setEnabled(false);
        title.setText("E-Letter Head Preview");
        back.setText("Back");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        textView_chief_complaint.setVisibility(View.VISIBLE);
        textView_history.setVisibility(View.VISIBLE);
        textView_treatment_advice.setVisibility(View.VISIBLE);
        textView_findings.setVisibility(View.VISIBLE);
        textView_diag.setVisibility(View.VISIBLE);
        textView_end_note.setVisibility(View.VISIBLE);

        textView_chief_complaint.setText("Chief Complaint : - ");
        textView_history.setText("History : - ");
        textView_treatment_advice.setText("Treatment/Advice : - ");
        textView_findings.setText("Findings : - ");
        textView_diag.setText("Diagnosis : - ");
        textView_end_note.setText("Note : - ");



        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "registration_no"))) {
            sample_image.setVisibility(View.GONE);
            scrollview_edit_profile.setVisibility(View.VISIBLE);
            image_layout = view.findViewById(R.id.image_layout);
            signature = view.findViewById(R.id.iv_signature);
            fl_progress_bar = view.findViewById(R.id.fl_progress_layout);
            textView_Clinic_name = view.findViewById(R.id.textView_Clinic_name);
            textView_DocName = view.findViewById(R.id.textView_DocName);
            textView_degree = view.findViewById(R.id.textView_degree);
            textView_res_num = view.findViewById(R.id.textView_res_num);
            textView_des = view.findViewById(R.id.textView_des);
            textView_UID = view.findViewById(R.id.textView_UID);
            textView_pat_name = view.findViewById(R.id.textView_pat_name);
            textView_age = view.findViewById(R.id.textView_age);
            textView_diag = view.findViewById(R.id.textView_diag);
            textView_days = view.findViewById(R.id.textView_days);
            textView_date = view.findViewById(R.id.textView_date);
            textView_add = view.findViewById(R.id.textView_add);
            textView_time = view.findViewById(R.id.textView_time);
            textView_contact = view.findViewById(R.id.textView_contact);

            //  textView_closed_day=  view.findViewById(R.id.textView_closed_day);
            textView_email = view.findViewById(R.id.textView_email);


            sig_text = view.findViewById(R.id.sig_text);
            initViews(view);
        } else {
            sample_image.setVisibility(View.VISIBLE);
            scrollview_edit_profile.setVisibility(View.GONE);
        }


    }

    private void initViews(View view) {

        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "clinic_name"))) {
            textView_Clinic_name.setText(manager.getPreferences(mContext, "clinic_name"));
        } else {
            textView_Clinic_name.setVisibility(View.GONE);
        }
        if(!("").equalsIgnoreCase(manager.getPreferences(mContext,"name").trim())) {
            textView_DocName.setText(manager.getPreferences(mContext, "name").trim());
        }
        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "ug_name"))) {
            textView_degree.setText(manager.getPreferences(mContext, "ug_name") + " " + manager.getPreferences(mContext, "pg_name") + " " + manager.getPreferences(mContext, "addtional_qualification"));

        } else {
            textView_degree.setVisibility(View.GONE);
        }
        textView_res_num.setText("Reg. No - " + manager.getPreferences(mContext, "registration_no"));
        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "designation_name"))) {
            textView_des.setText(manager.getPreferences(mContext, "designation_name"));
        } else {
            textView_des.setVisibility(View.GONE);
        }
        textView_pat_name.setText("Patient Details : Name, Gender / Age");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar c = Calendar.getInstance();
        String currentDate = sdf.format(c.getTime());
        textView_date.setText("Date : " + currentDate);
        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "working_days"))) {
            String work_days = manager.getPreferences(mContext, "working_days");
            if(work_days.contains("MONDAY"))
                work_days = work_days.replace("MONDAY"," MON");

            if(work_days.contains("TUESDAY"))
                work_days = work_days.replace("TUESDAY"," TUE");

            if(work_days.contains("WEDNESDAY"))
                work_days = work_days.replace("WEDNESDAY"," WED");

            if(work_days.contains("THURSDAY"))
                work_days = work_days.replace("THURSDAY"," THUR");

            if(work_days.contains("FRIDAY"))
                work_days = work_days.replace("FRIDAY"," FRI");

            if(work_days.contains("SATURDAY"))
                work_days = work_days.replace("SATURDAY"," SAT");

            if(work_days.contains("SUNDAY"))
                work_days = work_days.replace("SUNDAY"," SUN");

            textView_days.setText("Working : " + work_days);
        } else {
            textView_days.setVisibility(View.GONE);
        }

        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "visiting_hr_from"))) {

                String[] visiting_hr_from_details = manager.getPreferences(mContext,"visiting_hr_from").split(Pattern.quote("|"));;
                String[] visiting_hr_to_details = manager.getPreferences(mContext,"visiting_hr_to").split(Pattern.quote("|"));;
                if(visiting_hr_from_details.length>1)
                    textView_time.setText("Time : " +visiting_hr_from_details[0].toLowerCase().trim()+" - "+visiting_hr_to_details[0].toLowerCase().trim() +", "+visiting_hr_from_details[1].toLowerCase().trim()+" - "+visiting_hr_to_details[1].toLowerCase().trim() );
                else
                    textView_time.setText("Time : " +visiting_hr_from_details[0].toLowerCase().trim()+" - "+visiting_hr_to_details[0].toLowerCase().trim());


          //  textView_time.setText("Time : " + manager.getPreferences(mContext, "visiting_hr_from") + " to " + manager.getPreferences(mContext, "visiting_hr_to"));
        } else {
            textView_time.setVisibility(View.GONE);
        }
        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "mobile_letter_head"))) {
            textView_contact.setText("Contact : " + manager.getPreferences(mContext, "mobile_letter_head"));
        } else {
            textView_contact.setVisibility(View.GONE);
        }
        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "email_letter_head"))) {
            textView_email.setText("Mail Id : " + manager.getPreferences(mContext, "email_letter_head"));
        } else {
            textView_email.setVisibility(View.GONE);
        }
        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "clinic_address"))) {
            textView_add.setText("Address : " + manager.getPreferences(mContext, "clinic_address"));
        } else {
            textView_add.setVisibility(View.GONE);
        }


        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "image"))) {
            Picasso.with(mContext).
                    load(manager.getPreferences(mContext, "image"))
                    .into(image_layout, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            image_layout.setVisibility(View.INVISIBLE);
        }


        if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "signature"))) {
            Picasso.with(mContext).
                    load(manager.getPreferences(mContext, "signature"))
                    .into(signature, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            signature.setVisibility(View.GONE);
            sig_text.setVisibility(View.GONE);
        }


            SampleAdapter templateAdapter = new SampleAdapter();
                rView.setAdapter(templateAdapter);


    }

}
