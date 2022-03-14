package com.likesby.bludoc.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.R;
import com.likesby.bludoc.utils.NoPaginate.callback.OnRepeatListener;
import com.likesby.bludoc.utils.NoPaginate.item.ErrorItem;

public class CustomErrorItem implements ErrorItem {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_error, parent, false);

       return new RecyclerView.ViewHolder(inflate) {
        };

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, OnRepeatListener onRepeatListener) {

        Button button = holder.itemView.findViewById(R.id.btnRepeat);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 onRepeatListener.onClickRepeat();

            }
        });

    }
}
