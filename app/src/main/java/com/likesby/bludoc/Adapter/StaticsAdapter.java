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

import com.likesby.bludoc.ModelLayer.NewEntities.SubscriptionsItem;
import com.likesby.bludoc.ModelLayer.StaticsModelClass;
import com.likesby.bludoc.R;

import java.util.ArrayList;

public class StaticsAdapter extends RecyclerView.Adapter<StaticsAdapter.ViewHolder> {
    private static final String TAG = "SubscriptionAdapter__";
    private final Context mContext;
    private OnClickListener onClickListener;
    private ArrayList<StaticsModelClass> staticsModelClasses;

    public StaticsAdapter(Context context, ArrayList<StaticsModelClass> arrayList) {
        mContext = context;
        staticsModelClasses = arrayList;

    }

    public interface OnClickListener {

        void onClick(SubscriptionsItem subscriptionsItem);
    }

    public void setOnClickListener(StaticsAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public StaticsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.statics_adapter_layout,
                viewGroup, false);
        return new StaticsAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final StaticsAdapter.ViewHolder viewHolder, int i) {

        StaticsModelClass staticsModelClass = staticsModelClasses.get(i);
        viewHolder.topic.setText(staticsModelClass.getName());
        viewHolder.name.setText(staticsModelClass.getValue());

    }


    @Override
    public int getItemCount() {
        return staticsModelClasses.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView topic, name;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View view) {
            super(view);

            topic = view.findViewById(R.id.topic);
            name = view.findViewById(R.id.name);

        }

    }

}
