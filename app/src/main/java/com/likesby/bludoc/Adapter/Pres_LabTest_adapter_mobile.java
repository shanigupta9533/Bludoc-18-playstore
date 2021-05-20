package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.NewEntities.LabTestItem;
import com.likesby.bludoc.R;

import java.util.ArrayList;

public class Pres_LabTest_adapter_mobile extends RecyclerView.Adapter<Pres_LabTest_adapter_mobile.ViewHolder> {
    Context context;

    // static Typeface face;
    static String YesFROMTAG = "NO";
    static String NameofTAG;
    ArrayList<LabTestItem> medicines;


    public Pres_LabTest_adapter_mobile(ArrayList<LabTestItem> medicines, Context context) {
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
        viewHolder.tab_name.setText(medicines.get(position).getLabTestName());
        viewHolder.med.setVisibility(View.GONE);


        if(!("").equalsIgnoreCase(medicines.get(position).getLabTestComment())) {
            viewHolder.tab_comments.setVisibility(View.VISIBLE);
            viewHolder.tab_comments.setText(medicines.get(position).getLabTestComment());
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