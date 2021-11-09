package com.likesby.bludoc.Adapter;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.R;
import com.likesby.bludoc.viewModels.AllPharmacistList;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class SelectPatientAdapter extends RecyclerView.Adapter<SelectPatientAdapter.ViewHolder> implements Filterable {

    private final Context mContext;
    private final ArrayList<PatientsItem> patientsItemArrayList;
    private onClickListener onClickListener;
    private ArrayList<PatientsItem> listFilter;


    public SelectPatientAdapter(ArrayList<PatientsItem> arrayList, Context context) {
        mContext = context;
        patientsItemArrayList = arrayList;
        this.listFilter = arrayList;
    }

    public interface onClickListener {

        void onClick(PatientsItem patientsItem, int position);

    }

    public void setOnClickListener(onClickListener onClickListener) {

        this.onClickListener = onClickListener;

    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.select_patient_adapter_layout,
                viewGroup, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        PatientsItem patientsItem = listFilter.get(position);

        if (TextUtils.isEmpty(patientsItem.getPEmail())) // email
            viewHolder.email_id.setText("____");
        else
            viewHolder.email_id.setText(patientsItem.getPEmail());

        if (TextUtils.isEmpty(patientsItem.getPMobile()) || patientsItem.getPMobile().equalsIgnoreCase("0")) // mobile number
            viewHolder.mobile_number.setText("____");
        else
            viewHolder.mobile_number.setText(patientsItem.getPMobile());

        viewHolder.tv_pharmacist_name.setText(patientsItem.getPName());

        if (TextUtils.isEmpty(patientsItem.getpDob())) // set age by dob

            if (TextUtils.isEmpty(patientsItem.getAge()) ||
                    patientsItem.getAge().trim().equalsIgnoreCase("0 yr") ||
                    patientsItem.getAge().trim().equals("yr"))

                viewHolder.PATIENT_AGE.setText(patientsItem.getGender());
            else
                viewHolder.PATIENT_AGE.setText(patientsItem.getAge() + " / " + patientsItem.getGender());

        else {

            String[] split = patientsItem.getpDob().split("-");
            String return_value = decideMonthOrYear(split);
            viewHolder.PATIENT_AGE.setText(return_value + " / " + patientsItem.getGender());

        }

        if (("").equalsIgnoreCase(patientsItem.getpBloodGrp())) { // blood group
            viewHolder.blood_group.setText("____");
        } else {
            viewHolder.blood_group.setText(patientsItem.getpBloodGrp());
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // whole parent click
            @Override
            public void onClick(View v) {

                if (onClickListener != null) {

                    onClickListener.onClick(patientsItem, position);

                }

            }
        });

    }

    @Override // filter of patient list android
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFilter = patientsItemArrayList;
                } else {
                    ArrayList<PatientsItem> filteredList = new ArrayList<>();
                    for (PatientsItem row : patientsItemArrayList) {
                        if (row.getPName().toLowerCase().contains(charString.toLowerCase())
                                || row.getPEmail().toLowerCase().contains(charString.toLowerCase())
                                || row.getPMobile().toLowerCase().contains(charString.toLowerCase())
                        ) {
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

                listFilter = (ArrayList<PatientsItem>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    private String decideMonthOrYear(String[] split) { // decide month or year by dob

        DateTime dateTime = new DateTime();
        int year = Integer.parseInt(split[2]);
        int month = Integer.parseInt(split[1]);

        if (dateTime.getYear() - year > 0) {

            return dateTime.getYear() - year + " yr";

        } else {

            if (dateTime.getMonthOfYear() - (month) == 0)
                return "1 month";
            else
                return dateTime.getMonthOfYear() - (month) + " month";
        }

    }

    @Override
    public int getItemCount() {
        return listFilter.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_pharmacist_name;
        private final TextView email_id;
        private final TextView mobile_number;
        private final TextView blood_group;
        private final TextView PATIENT_AGE;

        public ViewHolder(View view) {
            super(view);

            tv_pharmacist_name = view.findViewById(R.id.tv_pharmacist_name);
            email_id = view.findViewById(R.id.email_id);
            PATIENT_AGE = view.findViewById(R.id.tv_age);
            blood_group = view.findViewById(R.id.blood_group);
            mobile_number = view.findViewById(R.id.mobile_number);

        }
    }
}