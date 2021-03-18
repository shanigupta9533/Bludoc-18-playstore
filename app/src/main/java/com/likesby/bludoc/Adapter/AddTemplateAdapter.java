package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Fragment.TemplateDetail;
import com.likesby.bludoc.R;

public class AddTemplateAdapter extends RecyclerView.Adapter<AddTemplateAdapter.ViewHolder> {
    Context context;

    // static Typeface face;
    static String YesFROMTAG = "NO";
    static String NameofTAG;


    public AddTemplateAdapter(Context context) {

        this.context = context;
    }

    // Create new views
    @Override
    public AddTemplateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_template_adapter, parent, false);
        return new AddTemplateAdapter.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(AddTemplateAdapter.ViewHolder viewHolder, final int position) {

          }



    @Override
    public int getItemCount() {
        return 1;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtstates;
        RelativeLayout card_view;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
        }
    }
}

