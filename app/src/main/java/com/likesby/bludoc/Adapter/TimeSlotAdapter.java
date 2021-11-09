package com.likesby.bludoc.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.TimeSlotModel;
import com.likesby.bludoc.R;

import java.util.ArrayList;


public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<TimeSlotModel> timeSlotModelArrayList;

    public TimeSlotAdapter(ArrayList<TimeSlotModel> arrayList, Context context) {
        mContext = context;
        timeSlotModelArrayList = arrayList;
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public TimeSlotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.time_slot_adapter_layout,
                viewGroup, false);
        return new TimeSlotAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final TimeSlotAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        TimeSlotModel timeSlotModel = timeSlotModelArrayList.get(position);
        viewHolder.time.setText(timeSlotModel.getTime());

    }

    @Override
    public int getItemCount() {
        return timeSlotModelArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView time;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View view) {
            super(view);

            time= view.findViewById(R.id.time);
        }
    }
}

