package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.Entities.MedicinesItem;
import com.likesby.bludoc.R;

import java.util.ArrayList;

public class Pres_adapter_mobile extends RecyclerView.Adapter<Pres_adapter_mobile.ViewHolder> {
    Context context;

    // static Typeface face;
    static String YesFROMTAG = "NO";
    static String NameofTAG;
    ArrayList<MedicinesItem> medicines;


    public Pres_adapter_mobile(ArrayList<MedicinesItem> medicines, Context context) {
        this.medicines = medicines;
        this.context = context;
    }

    // Create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pres_adapter_copy, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        String qty="";

        if(!TextUtils.isEmpty(medicines.get(position).getQty()) && !medicines.get(position).getQty().equalsIgnoreCase("0")){

            qty=", Total Quantity : "+medicines.get(position).getQty();

        }

        viewHolder.tab_name.setText(medicines.get(position).getMedicineName()+qty);
        String temp = "";
        if(!("").equalsIgnoreCase(medicines.get(position).getFrequency())){
            temp += medicines.get(position).getFrequency();
        }
        if(!("None").equalsIgnoreCase(medicines.get(position).getInstruction())){
            temp += " "+medicines.get(position).getInstruction();
        }
        if(("0").equalsIgnoreCase(medicines.get(position).getNoOfDays()) || ("").equalsIgnoreCase(medicines.get(position).getNoOfDays())){

        }else {
            temp += " for " + medicines.get(position).getNoOfDays() +" Days";
        }
        if(!("").equalsIgnoreCase(medicines.get(position).getRoute())){
            temp += " , (Route/Form - " +medicines.get(position).getRoute()+")";
        }
        if(!("").equalsIgnoreCase(temp)){
            viewHolder.med.setText(temp);
        }else {
            viewHolder.med.setVisibility(View.GONE);
        }


        if(!("").equalsIgnoreCase(medicines.get(position).getAdditionaComment())) {
            viewHolder.tab_comments.setVisibility(View.VISIBLE);
            viewHolder.tab_comments.setText(medicines.get(position).getAdditionaComment());
        }else {
            viewHolder.tab_comments.setVisibility(View.GONE);
        }
    }



    @Override
    public int getItemCount() {
        return medicines.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tab_name,med,tab_comments;
        RelativeLayout card_view;

        public ViewHolder(View view) {
            super(view);
            tab_name        = view.findViewById(R.id.tab_name);
            med        = view.findViewById(R.id.med);
            tab_comments= view.findViewById(R.id.tab_comments);
        }
    }
}