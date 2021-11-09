package com.likesby.bludoc.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.likesby.bludoc.ModelLayer.Entities.MedicinesItem;

import com.likesby.bludoc.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable
{
    private final EditText medicineQty;
    private ArrayList<MedicinesItem> mArrayList = new ArrayList<>();
    private ArrayList<MedicinesItem> mFilteredList = new ArrayList<>();
    private Context ctx ;
    EditText et_no_of_days;
    ArrayList<String> frequency_list, frequency2_list, EditText , route_list,instructions_list;
    Spinner frequency_spinner, frequency2_spinner, route_spinner,  instructions_spinner;
    EditText et_additional_comments;
    RecyclerView mRecyclerViewMedicines;
    EditText searchBarMaterialMedicine;
    public SearchAdapter(ArrayList<MedicinesItem> arrayList, ArrayList<String> frequency_list,
                         ArrayList<String> frequency2_list, EditText et_no_of_days, ArrayList<String> route_list,
                         ArrayList<String> instructions_list, Spinner frequency_spinner,
                         Spinner frequency2_spinner, Spinner route_spinner, Spinner instructions_spinner,
                         EditText et_additional_comments, RecyclerView mRecyclerViewMedicines, EditText searchBarMaterialMedicine, EditText medicineQty)
    {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        this.frequency_list = frequency_list;
        this.frequency2_list = frequency2_list;
        this.et_no_of_days = et_no_of_days;
        this.route_list = route_list;
        this.instructions_list = instructions_list;
        this.frequency_spinner = frequency_spinner;
        this.frequency2_spinner = frequency2_spinner;
        this.route_spinner = route_spinner;
        this.instructions_spinner = instructions_spinner;
        this.et_additional_comments = et_additional_comments;
        this.mRecyclerViewMedicines = mRecyclerViewMedicines;
        this.searchBarMaterialMedicine = searchBarMaterialMedicine;
        this.medicineQty = medicineQty;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.cv_medicines,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        if(mFilteredList != null) {
            if (mFilteredList.size() > 0) {
                MedicinesItem medicinesItem = mFilteredList.get(i);
                viewHolder.MEDICINE_NAME.setText(medicinesItem.getMedicineName());
                viewHolder.MEDICINE_ID.setText(medicinesItem.getMedicineId());
                String freqq = medicinesItem.getFrequency();
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

                    ArrayList<MedicinesItem> filteredList = new ArrayList<>();

                    for (MedicinesItem categories : mArrayList) {
                        if (categories.getMedicineName().toLowerCase().contains(charString.toLowerCase())) {
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
            mFilteredList = (ArrayList<MedicinesItem>) filterResults.values;
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        // CORRECT: Always use the same Filter instance.
        return this.filter;
    }

    /*@Override
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

                    ArrayList<MedicinesItem> filteredList = new ArrayList<>();

                    for (MedicinesItem categories : mArrayList) {

                            if (categories.getMedicineName().toLowerCase().contains(charString.toLowerCase())) {

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
                mFilteredList = (ArrayList<MedicinesItem>) filterResults.values;
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
        LinearLayout ll_medicine_main;
        ImageView imageView;
        public ViewHolder(View view)
        {
            super(view);
            ctx = view.getContext();
            MEDICINE_NAME        = view.findViewById(R.id.tv_medicine_name);
            MEDICINE_ID        = view.findViewById(R.id.tv_medicine_id);
            MEDICINE_FREQ        = view.findViewById(R.id.tv_frequency );
            MEDICINE_FREQ2        = view.findViewById(R.id.tv_frequency2 );
            MEDICINE_NODAYS        = view.findViewById(R.id.tv_nodays );
            MEDICINE_ROUTE        = view.findViewById(R.id.tv_route );
            MEDICINE_INSTRUCTN        = view.findViewById(R.id.tv_instructions );
            MEDICINE_ADD_CMMNT        = view.findViewById(R.id.tv_medicine_add_comments);
            MEDICINE_CREATED        = view.findViewById(R.id.tv_medicine_created );
            MEDICINE_MODIFIED        = view.findViewById(R.id.tv_medicine_modified );
            ll_medicine_main      = view.findViewById(R.id.ll_medicine_main );

            ll_medicine_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // searchBarMaterialMedicine.clearSuggestions();
                   // searchBarMaterialMedicine.setText("");
                    MedicinesItem medicinesItem = mFilteredList.get(getAdapterPosition());
                   // mRecyclerViewMedicines.setVisibility(View.GONE);
                    et_no_of_days.setText(medicinesItem.getNoOfDays());
                    searchBarMaterialMedicine.setText(medicinesItem.getMedicineName());

                    if(!TextUtils.isEmpty(medicinesItem.getQty()) && !medicinesItem.getQty().equalsIgnoreCase("0")){
                        medicineQty.setText(medicinesItem.getQty());
                    }

                    et_additional_comments.setText(medicinesItem.getAdditionaComment());

                    int pos = 0;
                        if(medicinesItem.getInstruction() != null) {
                            for (int i = 0; i < instructions_list.size(); i++) {
                                if(medicinesItem.getInstruction().equalsIgnoreCase(instructions_list.get(i))){
                                    pos = i;
                                }
                            }
                        }

                    instructions_spinner.setSelection(pos);
                    for (int i=0;i<frequency_list.size();i++ ) {
                        if(frequency_list.get(i).equalsIgnoreCase(medicinesItem.getFrequency()))
                        {

                            frequency_spinner.setSelection(i);
                            frequency2_spinner.setSelection(i);
                            break;
                        }
                        else
                        {
                            frequency_spinner.setSelection(0);
                            frequency2_spinner.setSelection(0);
                        }

                    }

                   /* for (int i=0;i<frequency2_list.size();i++ ) {
                        if(frequency2_list.get(i).equalsIgnoreCase(medicinesItem.getNoOfDays()))
                        {
                            frequency2_spinner.setSelection(i);
                            break;
                        }
                        else
                            frequency2_spinner.setSelection(1);
                    }*/


                    for (int i=0;i<route_list.size();i++ ) {
                        if(route_list.get(i).equalsIgnoreCase(medicinesItem.getRoute()))
                        {
                            route_spinner.setSelection(i);
                            break;
                        }
                        else
                            route_spinner.setSelection(0);
                    }


                    for (int i=0;i<instructions_list.size();i++ ) {
                        if(instructions_list.get(i).equalsIgnoreCase(medicinesItem.getInstruction()))
                        {
                            instructions_spinner.setSelection(i);
                            break;
                        }
                        else
                            instructions_spinner.setSelection(0);
                    }
                    mRecyclerViewMedicines.setVisibility(View.GONE);
                }
            });


           //=============================================================================================


        }
    }



}
