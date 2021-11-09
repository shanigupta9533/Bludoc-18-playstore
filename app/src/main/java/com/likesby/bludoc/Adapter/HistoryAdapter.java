package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Fragment.GeneratePres;
import com.likesby.bludoc.ModelLayer.Entities.TemplatesItem;
import com.likesby.bludoc.ModelLayer.NewEntities3.LabTestItem;
import com.likesby.bludoc.ModelLayer.NewEntities3.PrescriptionItem;
import com.likesby.bludoc.R;
import com.likesby.bludoc.utils.DateUtils;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> implements Filterable {

    private ArrayList<PrescriptionItem> mArrayList = new ArrayList<>();
    private ArrayList<PrescriptionItem> mFilteredList = new ArrayList<>();
    private Context ctx ;
    LinearLayout ll_patients_view, ll_prescription_view;
    Button btn_create_patient;
    PrescriptionItem prescriptionItem;

    public HistoryAdapter(ArrayList<PrescriptionItem> arrayList, Context context)
    {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        ctx = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.history_adapter,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.patient_name.setText("Name : "+mFilteredList.get(i).getPName());
        String age___ = "";

        if (TextUtils.isEmpty(mFilteredList.get(i).getpDob()))

            if (TextUtils.isEmpty(mFilteredList.get(i).getAge()))
                viewHolder.tv_gender.setText(mFilteredList.get(i).getGender());
            else
                viewHolder.tv_gender.setText(mFilteredList.get(i).getAge() + " / " + mFilteredList.get(i).getGender());

        else {

            String[] split = mFilteredList.get(i).getpDob().split("-");
            String return_value = decideMonthOrYear(split);
            viewHolder.tv_gender.setText(return_value + " / " + mFilteredList.get(i).getGender());

        }

//
//   //     viewHolder.tv_patient_created.setText("Date : "+ DateUtils.outFormatset(mFilteredList.get(i).getDate()));
//        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
//        Date newDate = null;
//        try {
//            newDate = format.parse(DateUtils.outFormatset(mFilteredList.get(i).getDate()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        viewHolder.tv_patient_created.setText("Date : "+ newDate);
//
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String couponsStartDate  = mFilteredList.get(i).getDate();
//        Date date1 = null;
//        try {
//            date1 = sdf.parse(couponsStartDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat sdf1 = new SimpleDateFormat( "dd MMM yyyy");
//        viewHolder.tv_patient_created.setText(sdf1.format(date1));
//
//
//
//



        /*String sourceDate = mFilteredList.get(i).getDate();
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-YYYY");
        Date date = null;
        try {
            date = originalFormat.parse(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        System.out.println(formattedDate);*/

        try {
            viewHolder.tv_patient_created.setText(DateUtils.outFormatsetMMM(mFilteredList.get(i).getDate()));
        } catch (Exception e){
            Log.i("TAG", "onBindViewHolder: Exception"+mFilteredList.get(i).getDate());
        }

        if(!("").equalsIgnoreCase(mFilteredList.get(i).getPMobile())) {
            viewHolder.tv_mobile.setText("Mobile : " + mFilteredList.get(i).getPMobile());
        }
        if(!("0").equalsIgnoreCase(mFilteredList.get(i).getPMobile())) {
            viewHolder.tv_mobile.setText("Mobile : " + mFilteredList.get(i).getPMobile());
        }
        else {
            viewHolder.tv_mobile.setText("Mobile : -");
        }

        if(!("").equalsIgnoreCase(mFilteredList.get(i).getPEmail())) {
            viewHolder.tv_email.setText("Email : "+mFilteredList.get(i).getPEmail());
        }else {
            viewHolder.tv_email.setText("Email : -");
        }
        viewHolder.ll_main_patient_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneratePres myFragment = new GeneratePres();
                Bundle bundle = new Bundle();
                prescriptionItem = new PrescriptionItem();
                prescriptionItem = mFilteredList.get(i);
                bundle.putParcelable("defaultAttributeId", prescriptionItem);
                ArrayList<com.likesby.bludoc.ModelLayer.NewEntities.LabTestItem> labTestItemArrayList  = new ArrayList<>();
                for (LabTestItem lii: prescriptionItem.getLabTest()
                     ) {
                    com.likesby.bludoc.ModelLayer.NewEntities.LabTestItem labTestItem = new com.likesby.bludoc.ModelLayer.NewEntities.LabTestItem();
                    labTestItem.setLabTestName(lii.getLabTestName());
                    labTestItem.setLabTestComment(lii.getLabTestComment());
                    labTestItemArrayList.add(labTestItem);
                }
                bundle.putParcelableArrayList("defaultAttributeIdLabTest", labTestItemArrayList);
                bundle.putString("definer", "history");
                bundle.putString("end_note", prescriptionItem.getEndNote());
                bundle.putString("yesOrNo",prescriptionItem.getAge());
                bundle.putString("pres_id",prescriptionItem.getPresbPatientId());
                bundle.putString("isCertificate",prescriptionItem.getIsCertificate());
                myFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) ctx).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.homePageContainer, myFragment, "first").addToBackStack("Detail").commit();
            }
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
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<PrescriptionItem> filteredList = new ArrayList<>();

                    for (PrescriptionItem categories : mArrayList) {

                        if (categories.getPName().toLowerCase().contains(charString))
                        {
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
                mFilteredList = (ArrayList<PrescriptionItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView patient_name,tv_gender,tv_patient_created,tv_mobile,tv_email;
        // FrameLayout fl;
        ProgressBar pb;
        //ImageButton pic;
        LinearLayout ll_main_patient_view;
        ImageView imageView;
        public ViewHolder(View view)
        {
            super(view);
            ctx = view.getContext();

            patient_name        = view.findViewById(R.id.tv_patient_name);
            tv_gender = view.findViewById(R.id.tv_gender);

            tv_patient_created = view.findViewById(R.id.tv_patient_created);
            ll_main_patient_view =  view.findViewById(R.id.ll_main_patient_view);
            tv_mobile=  view.findViewById(R.id.tv_mobile);
            tv_email=  view.findViewById(R.id.tv_email);

            ll_main_patient_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //=============================================================================================


        }
    }
}
