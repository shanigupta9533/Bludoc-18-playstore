package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.ConsentTemplateModel;
import com.likesby.bludoc.R;

import java.util.ArrayList;

public class ConsentTemplateAdapter extends RecyclerView.Adapter<ConsentTemplateAdapter.ViewHolder> implements Filterable {

    private ArrayList<ConsentTemplateModel> mArrayList;
    private ArrayList<ConsentTemplateModel> mFilteredList;
    private Context mContext;
    private ConsentTemplateAdapter.OnClickListener onClickListener;
    private String patient_id="";

    public ConsentTemplateAdapter(ArrayList<ConsentTemplateModel> arrayList, Context context) {
        mContext = context;
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    public void setPatientId(String patient_id) {

        this.patient_id=patient_id;

    }

    public interface OnClickListener {

        void onEdit(ConsentTemplateModel consentTemplateModel, int i);
        void onDelete(ConsentTemplateModel consentTemplateModel, int i);
        void add(ConsentTemplateModel consentTemplateModel, int i);

    }

    public void setOnClickListener(ConsentTemplateAdapter.OnClickListener onClickListener) {

        this.onClickListener = onClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.consent_template_adapter_layout,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        ConsentTemplateModel consentTemplateModel = mFilteredList.get(i);

        viewHolder.TEMPLATE_NAME.setText(consentTemplateModel.getTitle());

        if(TextUtils.isEmpty(patient_id)){
            viewHolder.add.setVisibility(View.GONE);
        } else {
            viewHolder.add.setVisibility(View.VISIBLE);
        }

        viewHolder.add.setOnClickListener(view -> {

            if(onClickListener!=null)
                onClickListener.add(consentTemplateModel,viewHolder.getAdapterPosition());

        });

        viewHolder.edit.setOnClickListener(view -> {

            if(onClickListener!=null)
                onClickListener.onEdit(consentTemplateModel,viewHolder.getAdapterPosition());

        });

        viewHolder.delete.setOnClickListener(view -> {

            if(onClickListener!=null)
                onClickListener.onDelete(consentTemplateModel,viewHolder.getAdapterPosition());

        });

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<ConsentTemplateModel> filteredList = new ArrayList<>();

                    for (ConsentTemplateModel categories : mArrayList) {

                        if (categories.getTitle().toLowerCase().contains(charString)) {

                            filteredList.add(categories);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<ConsentTemplateModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView TEMPLATE_NAME;
        // FrameLayout fl;
        ProgressBar pb;
        //ImageButton pic;
        ImageView imageView;
        Button add, edit, delete;
        ;

        public ViewHolder(View view) {
            super(view);
            TEMPLATE_NAME = view.findViewById(R.id.tv_template_name);
            edit = view.findViewById(R.id.template_edit);
            delete = view.findViewById(R.id.template_delete);
            add = view.findViewById(R.id.template_add);
        }
    }

}