package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.AddAPharmacistActivity;
import com.likesby.bludoc.R;
import com.likesby.bludoc.viewModels.AllPharmacistList;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ReferralAdapter  extends RecyclerView.Adapter<ReferralAdapter.viewHolder> implements Filterable {

    private final Context context;
    private final ArrayList<AllPharmacistList> pharmacistList;
    private ArrayList<AllPharmacistList> listFilter;
    private ArrayList<String> switch_ids=new ArrayList<>();
    private OnClickListener onClickListener;

    public ReferralAdapter(ArrayList<AllPharmacistList> pharmacistList, Context context) {
        this.pharmacistList = pharmacistList;
        this.context = context;
        this.listFilter=pharmacistList;
    }

    // Create new views
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacist_adapter_layout, parent, false);
        return new viewHolder(view);
    }

    public String getAllIds() {

        return TextUtils.join(",",switch_ids);

    }

    public interface OnClickListener{

        void onDelete(AllPharmacistList s, int position);
        void onChecked(AllPharmacistList s, int position,boolean isChecked);

    }

    public void setOnClickListener(OnClickListener onClickListener){

        this.onClickListener=onClickListener;

    }

    @Override
    public void onBindViewHolder(viewHolder viewHolder, final int position) {

        final AllPharmacistList s = listFilter.get(position);

        viewHolder.email_id.setText(s.getPharmacist_email());
        viewHolder.mobile_number.setText(s.getPharmacist_mobile());
        viewHolder.tv_pharmacist_name.setText(s.getPharmacist_name());

        if (s.getIs_check().equalsIgnoreCase("yes"))
            viewHolder.switch_ids.setChecked(true);
        else
            viewHolder.switch_ids.setChecked(false);

        viewHolder.switch_ids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickListener.onChecked(s, position, viewHolder.switch_ids.isChecked());

            }
        });

//            viewHolder.switch_ids.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    onClickListener.onChecked(s, position, isChecked);
//
//                }
//            });


        if(TextUtils.isEmpty(s.getPharmacist_email()))
            viewHolder.email_id.setText("____");

        if(s.getPharmacist_mobile().equals("0"))
            viewHolder.mobile_number.setText("____");

        viewHolder.patient_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, AddAPharmacistActivity.class);
                intent.putExtra("pharmacist",s);
                intent.putExtra("isEdit",true);
                context.startActivity(intent);

            }
        });

        viewHolder.patient_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickListener.onDelete(s,position);

            }
        });

    }



    @Override
    public int getItemCount() {
        return listFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {


                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFilter = pharmacistList;
                } else {
                    ArrayList<AllPharmacistList> filteredList = new ArrayList<>();
                    for (AllPharmacistList row : pharmacistList) {
                        if (row.getPharmacist_name().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d(TAG, "performFiltering: " + charString + " == " + row);
                            filteredList.add(row);
                        }
                    }

                    listFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                listFilter= (ArrayList<AllPharmacistList>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView tv_pharmacist_name,email_id,mobile_number;
        private ImageView patient_edit,patient_delete;
        private CheckBox switch_ids;

        public viewHolder(View view) {
            super(view);

            tv_pharmacist_name = itemView.findViewById(R.id.tv_pharmacist_name);
            email_id = itemView.findViewById(R.id.email_id);
            mobile_number = itemView.findViewById(R.id.mobile_number);
            patient_edit = itemView.findViewById(R.id.patient_edit);
            patient_delete = itemView.findViewById(R.id.patient_delete);
            switch_ids=itemView.findViewById(R.id.switch_ids);

        }
    }

}
