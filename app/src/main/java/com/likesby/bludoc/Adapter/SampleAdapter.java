package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.Entities.MedicinesItem;
import com.likesby.bludoc.R;

import java.util.ArrayList;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {
    Context context;

    public SampleAdapter() {

    }

    // Create new views
    @Override
    public SampleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pres_adapter_sample, parent, false);
        return new SampleAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SampleAdapter.ViewHolder viewHolder, final int position) {

    }



    @Override
    public int getItemCount() {
        return 3;
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