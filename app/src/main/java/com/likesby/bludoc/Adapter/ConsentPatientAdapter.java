package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.ConsentDataModel;
import com.likesby.bludoc.ModelLayer.NewEntities3.PrescriptionItem;
import com.likesby.bludoc.R;
import com.likesby.bludoc.utils.DateUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class ConsentPatientAdapter extends RecyclerView.Adapter<ConsentPatientAdapter.ViewHolder> implements Filterable {

    private ArrayList<ConsentDataModel> mArrayList;
    private ArrayList<ConsentDataModel> mFilteredList;
    private Context ctx;
    LinearLayout ll_patients_view, ll_prescription_view;
    Button btn_create_patient;
    PrescriptionItem prescriptionItem;

    public ConsentPatientAdapter(ArrayList<ConsentDataModel> arrayList, Context context) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        ctx = context;
    }

    @NonNull
    @Override
    public ConsentPatientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.history_adapter,
                viewGroup, false);
        return new ConsentPatientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ConsentPatientAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.patient_name.setText("Name : " + mFilteredList.get(i).getPatient_name());
        String age___ = "";

        if (TextUtils.isEmpty(mFilteredList.get(i).getP_dob()))

            if (TextUtils.isEmpty(mFilteredList.get(i).getAge()))
                viewHolder.tv_gender.setText(mFilteredList.get(i).getGender());
            else
                viewHolder.tv_gender.setText(mFilteredList.get(i).getAge() + " / " + mFilteredList.get(i).getGender());

        else {

            String[] split = mFilteredList.get(i).getP_dob().split("-");
            String return_value = decideMonthOrYear(split);
            viewHolder.tv_gender.setText(return_value + " / " + mFilteredList.get(i).getGender());

        }

        try {
            viewHolder.tv_patient_created.setText(DateUtils.outFormatsetMMM(mFilteredList.get(i).getDate()));
        } catch (Exception e) {
            Log.i("TAG", "onBindViewHolder: Exception" + mFilteredList.get(i).getDate());
        }

        if (!("").equalsIgnoreCase(mFilteredList.get(i).getP_mobile())) {
            viewHolder.tv_mobile.setText("Mobile : " + mFilteredList.get(i).getP_mobile());
        }
        if (!("0").equalsIgnoreCase(mFilteredList.get(i).getP_mobile())) {
            viewHolder.tv_mobile.setText("Mobile : " + mFilteredList.get(i).getP_mobile());
        } else {
            viewHolder.tv_mobile.setText("Mobile : -");
        }

        if (!("").equalsIgnoreCase(mFilteredList.get(i).getP_email())) {
            viewHolder.tv_email.setText("Email : " + mFilteredList.get(i).getP_email());
        } else {
            viewHolder.tv_email.setText("Email : -");
        }
        viewHolder.ll_main_patient_view.setOnClickListener(v -> {



        });


    }

    private String decideMonthOrYear(String[] split) {

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

                    ArrayList<ConsentDataModel> filteredList = new ArrayList<>();

                    for (ConsentDataModel categories : mArrayList) {

                        if (categories.getPatient_name().toLowerCase().contains(charString)) {
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
                mFilteredList = (ArrayList<ConsentDataModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView patient_name, tv_gender, tv_patient_created, tv_mobile, tv_email;
        // FrameLayout fl;
        ProgressBar pb;
        //ImageButton pic;
        LinearLayout ll_main_patient_view;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            ctx = view.getContext();

            patient_name = view.findViewById(R.id.tv_patient_name);
            tv_gender = view.findViewById(R.id.tv_gender);

            tv_patient_created = view.findViewById(R.id.tv_patient_created);
            ll_main_patient_view = view.findViewById(R.id.ll_main_patient_view);
            tv_mobile = view.findViewById(R.id.tv_mobile);
            tv_email = view.findViewById(R.id.tv_email);

            ll_main_patient_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

}
