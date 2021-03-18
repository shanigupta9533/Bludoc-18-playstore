package com.likesby.bludoc.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Fragment.CreatePrescription;
import com.likesby.bludoc.ModelLayer.Entities.LabTestItem;
import com.likesby.bludoc.ModelLayer.Entities.MedicinesItem;
import com.likesby.bludoc.R;

import java.util.ArrayList;

public class SearchLabTestAdapter extends RecyclerView.Adapter<SearchLabTestAdapter.ViewHolder> implements Filterable
{
    private ArrayList<LabTestItem> mArrayList = new ArrayList<>();
    private ArrayList<LabTestItem> mFilteredList = new ArrayList<>();
    private Context ctx ;
    EditText et_no_of_days;
    EditText et_additional_comments_labtest;
    RecyclerView mRecyclerViewLabTest;
    EditText searchBarMaterialLabTest;
    public SearchLabTestAdapter(ArrayList<LabTestItem> arrayList,
                                EditText et_additional_comments_labtest, RecyclerView mRecyclerViewLabTest, EditText searchBarMaterialLabTest)
    {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        this.et_additional_comments_labtest = et_additional_comments_labtest;
        this.mRecyclerViewLabTest = mRecyclerViewLabTest;
        this.searchBarMaterialLabTest = searchBarMaterialLabTest;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.cv_lab_test,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        if(mFilteredList != null) {
            if (mFilteredList.size() > 0) {
                LabTestItem medicinesItem = mFilteredList.get(i);
                viewHolder.MEDICINE_NAME.setText(medicinesItem.getLabTestName());
                viewHolder.MEDICINE_ID.setText(medicinesItem.getLabId());
            }
        }
//        freqq = freqq.split("(-)")[0];;
//
//        if(("").equalsIgnoreCase(freqq)){
//            viewHolder.MEDICINE_FREQ.setText(freqq);
//        }else {
//            viewHolder.MEDICINE_FREQ.setVisibility(View.GONE);
//        }
//       // viewHolder.MEDICINE_FREQ2.setText(medicinesItem.getInstruction());
//        if(("0").equalsIgnoreCase(medicinesItem.getNoOfDays())){
//            viewHolder.MEDICINE_NODAYS.setText(" for "+medicinesItem.getNoOfDays()+ " Days ");
//        }else {
//            viewHolder.MEDICINE_NODAYS.setVisibility(View.GONE);
//        }
//        if(("").equalsIgnoreCase(medicinesItem.getRoute())){
//            viewHolder.MEDICINE_ROUTE.setText(medicinesItem.getNoOfDays());
//        }else {
//            viewHolder.MEDICINE_ROUTE.setVisibility(View.GONE);
//        }

//        viewHolder.MEDICINE_INSTRUCTN.setText(medicinesItem.getInstruction());
//        viewHolder.MEDICINE_ADD_CMMNT.setText(medicinesItem.getAdditionaComment());
//        viewHolder.MEDICINE_CREATED.setText(medicinesItem.getCreated());
//        viewHolder.MEDICINE_MODIFIED.setText(medicinesItem.getModified());

    }



    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
       return mFilteredList.size();
    }


    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            // Perform filtering...

            String charString = charSequence.toString();
            if (charString.isEmpty()) {
                mFilteredList = mArrayList;
            } else {
                ArrayList<LabTestItem> filteredList = new ArrayList<>();

                for (LabTestItem categories : mArrayList) {

                    if (categories.getLabTestName().toLowerCase().contains(charString.toLowerCase())) {

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
            // Update and Notify adapter...
            mFilteredList = (ArrayList<LabTestItem>) filterResults.values;
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        // CORRECT: Always use the same Filter instance.
        return this.filter;
    }


   /* @Override
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

                    ArrayList<LabTestItem> filteredList = new ArrayList<>();

                    for (LabTestItem categories : mArrayList) {

                            if (categories.getLabTestName().toLowerCase().contains(charString.toLowerCase())) {

                                filteredList.add(categories);
                            }
//                            else if (categories.getFrequency().toLowerCase().contains(charString.toLowerCase())) {
//
//                                filteredList.add(categories);
//                            } else if (categories.getNoOfDays().toLowerCase().contains(charString.toLowerCase())) {
//
//                                filteredList.add(categories);
//                            } else if (categories.getInstruction().toLowerCase().contains(charString.toLowerCase())) {
//
//                                filteredList.add(categories);
//                            }
                        }


                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<LabTestItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView MEDICINE_NAME,MEDICINE_ID,MEDICINE_FREQ,MEDICINE_FREQ2,MEDICINE_NODAYS,
                MEDICINE_ROUTE,MEDICINE_INSTRUCTN,MEDICINE_ADD_CMMNT,MEDICINE_CREATED,MEDICINE_MODIFIED;
       // FrameLayout fl;
        ProgressBar pb;
        //ImageButton pic;
        LinearLayout ll_labtest_main;
        ImageView imageView;
        public ViewHolder(View view)
        {
            super(view);
            ctx = view.getContext();
            MEDICINE_NAME        = view.findViewById(R.id.tv_labtest_name);
            MEDICINE_ID        = view.findViewById(R.id.tv_labtest_id);
           MEDICINE_ADD_CMMNT        = view.findViewById(R.id.tv_labtest_comment);
            ll_labtest_main      = view.findViewById(R.id.ll_labtest_main );

            ll_labtest_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(ctx, "ll_labtest_main", Toast.LENGTH_SHORT).show();


                    //searchBarMaterialLabTest.setText("");
                    LabTestItem medicinesItem = mFilteredList.get(getAdapterPosition());
                   // mRecyclerViewMedicines.setVisibility(View.GONE);
                    searchBarMaterialLabTest.setText(medicinesItem.getLabTestName());
                    et_additional_comments_labtest.setText(medicinesItem.getLabTestComment());

                    hideKeyboard(ctx);
                    mRecyclerViewLabTest.setVisibility(View.GONE);
                    mRecyclerViewLabTest.setVisibility(View.GONE);

                }
            });
           //=============================================================================================
        }

        public  void hideKeyboard(Context ctx) {
            InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            View v = ((Activity) ctx).getCurrentFocus();
            if (v == null)
                return;
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}
