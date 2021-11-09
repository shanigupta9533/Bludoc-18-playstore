package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.likesby.bludoc.ModelLayer.Entities.MedicinesItem;
import com.likesby.bludoc.R;

import java.util.ArrayList;
import java.util.List;

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {
    private final List<Integer> stringList;
    Context context;

    public SampleAdapter(Context context , List<Integer> stringList) {

        this.context = context;
        this.stringList=stringList;

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

        Glide.with(context).asBitmap().load(stringList.get(position)).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.imageView);

    }



    @Override
    public int getItemCount() {
        return 3;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        private PhotoView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageOfProduct);

        }
    }
}