package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Fragment.AddTemplate;
import com.likesby.bludoc.Fragment.CreatePrescription;
import com.likesby.bludoc.HomeActivity;
import com.likesby.bludoc.ModelLayer.NewEntities3.MedicinesItem;
import com.likesby.bludoc.R;

import java.util.ArrayList;


public class AddMedicineAdapter extends RecyclerView.Adapter<AddMedicineAdapter.ViewHolder> implements Filterable {
    private ArrayList<MedicinesItem> mArrayList = new ArrayList<>();
    private ArrayList<MedicinesItem> mFilteredList = new ArrayList<>();
    private Context ctx;
    EditText et_no_of_days, et_additional_comments, medicineQty;
    ArrayList<String> frequency_list, frequency2_list, EditText, route_list, instructions_list;
    Spinner frequency_spinner, frequency2_spinner, route_spinner, instructions_spinner;
    Button btn_add, btnChooseFromTemplate, btn_prescribe;
    TextView textView3_5;
    LinearLayout ll_35;
    EditText searchBarMaterialMedicine;
    boolean count;
    RecyclerView mRecyclerViewAddedMedicines;
    FrameLayout fl_progress_bar;
    static String iden;
    int edited_ = 0;
    private int position;
    private int pos;

    public AddMedicineAdapter(Context mContext, ArrayList<MedicinesItem> arrayList, ArrayList<String> frequency_list,
                              ArrayList<String> frequency2_list, EditText et_no_of_days, android.widget.EditText medicineQty, ArrayList<String> route_list,
                              ArrayList<String> instructions_list, Spinner frequency_spinner,
                              Spinner frequency2_spinner, Spinner route_spinner,
                              Spinner instructions_spinner, EditText et_additional_comments, Button btn_add, TextView textView3_5, LinearLayout ll_35, EditText searchBarMaterialMedicine, RecyclerView mRecyclerViewAddedMedicines,
                              Button btnChooseFromTemplate, Button btn_prescribe, String iden, FrameLayout fl_progress_bar) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        this.frequency_list = frequency_list;
        this.frequency2_list = frequency2_list;
        this.et_no_of_days = et_no_of_days;
        this.route_list = route_list;
        this.medicineQty = medicineQty;
        this.instructions_list = instructions_list;
        this.frequency_spinner = frequency_spinner;
        this.frequency2_spinner = frequency2_spinner;
        this.route_spinner = route_spinner;
        this.instructions_spinner = instructions_spinner;
        this.et_additional_comments = et_additional_comments;
        this.btn_add = btn_add;
        this.textView3_5 = textView3_5;
        this.ll_35 = ll_35;
        this.searchBarMaterialMedicine = searchBarMaterialMedicine;
        this.mRecyclerViewAddedMedicines = mRecyclerViewAddedMedicines;
        this.btnChooseFromTemplate = btnChooseFromTemplate;
        this.btn_prescribe = btn_prescribe;
        this.iden = iden;
        this.fl_progress_bar = fl_progress_bar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.add_template_adapter,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        MedicinesItem medicinesItem = mFilteredList.get(i);

        String qty = "";

        if (!TextUtils.isEmpty(medicinesItem.getQty()) && !medicinesItem.getQty().equalsIgnoreCase("0")) {
            qty = ", Total Quantity : " + medicinesItem.getQty();
        }

        viewHolder.MEDICINE_NAME.setText(medicinesItem.getMedicineName() + qty);
        String details = "";
        details = details + medicinesItem.getFrequency() + " ";
        // viewHolder.MEDICINE_FREQ.setText(medicinesItem.getInstruction());
        // viewHolder.MEDICINE_FREQ2.setText(medicinesItem.getInstruction());

        //   viewHolder.MEDICINE_NODAYS.setText(medicinesItem.getNoOfDays());

        if (!("").equalsIgnoreCase(medicinesItem.getRoute())) {
            //details = details + medicinesItem.getRoute()+ " ";
            viewHolder.MEDICINE_ROUTE.setText(medicinesItem.getRoute());
        } else {
            viewHolder.MEDICINE_ROUTE.setVisibility(View.GONE);
        }

        if (("").equalsIgnoreCase(medicinesItem.getInstruction()) || ("None").equalsIgnoreCase(medicinesItem.getInstruction())) {
            viewHolder.MEDICINE_INSTRUCTN.setVisibility(View.GONE);
        } else {
            details = details + medicinesItem.getInstruction() + " ";
            // viewHolder.MEDICINE_INSTRUCTN.setText(medicinesItem.getInstruction());
        }


        if (("0").equalsIgnoreCase(medicinesItem.getNoOfDays()) || ("").equalsIgnoreCase(medicinesItem.getNoOfDays())) {
            viewHolder.MEDICINE_NODAYS.setVisibility(View.GONE);
        } else {
            details = details + " for " + medicinesItem.getNoOfDays() + " Days ";
            //  viewHolder.MEDICINE_NODAYS.setText(" for "+medicinesItem.getNoOfDays()+ " Days ");
        }


        if (!("").equalsIgnoreCase(medicinesItem.getAdditionaComment())) {
            viewHolder.MEDICINE_ADD_CMMNT.setText(medicinesItem.getAdditionaComment());
        } else {
            viewHolder.MEDICINE_ADD_CMMNT.setVisibility(View.GONE);
        }

        viewHolder.MEDICINE_FREQ.setText(details);

        viewHolder.rt.setVisibility(View.GONE);
        viewHolder.cm.setVisibility(View.GONE);
        viewHolder.MEDICINE_ROUTE.setVisibility(View.GONE);
        viewHolder.MEDICINE_ADD_CMMNT.setVisibility(View.GONE);
        count = false;
        viewHolder.view_more.setText("View More");

        viewHolder.view_more.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        MedicinesItem medicinesItem = mFilteredList.get(i);
                                                        if (count) {
                                                            viewHolder.rt.setVisibility(View.GONE);
                                                            viewHolder.cm.setVisibility(View.GONE);
                                                            viewHolder.MEDICINE_ROUTE.setVisibility(View.GONE);
                                                            viewHolder.MEDICINE_ADD_CMMNT.setVisibility(View.GONE);
                                                            count = false;
                                                            viewHolder.view_more.setText("View More");

                                                        } else {

                                                            if (!("").equalsIgnoreCase(medicinesItem.getRoute())) {
                                                                viewHolder.MEDICINE_ROUTE.setVisibility(View.VISIBLE);
                                                                viewHolder.rt.setVisibility(View.VISIBLE);
                                                            } else {
                                                                viewHolder.MEDICINE_ROUTE.setVisibility(View.GONE);
                                                                viewHolder.rt.setVisibility(View.GONE);
                                                            }
                                                            if (!("").equalsIgnoreCase(medicinesItem.getAdditionaComment())) {
                                                                viewHolder.MEDICINE_ADD_CMMNT.setVisibility(View.VISIBLE);
                                                                viewHolder.cm.setVisibility(View.VISIBLE);
                                                            } else {
                                                                viewHolder.MEDICINE_ADD_CMMNT.setVisibility(View.GONE);
                                                                viewHolder.cm.setVisibility(View.GONE);
                                                            }
                                                            count = true;
                                                            viewHolder.view_more.setText("View Less");
                                                        }
                                                    }
                                                }
        );
        //viewHolder.MEDICINE_CREATED.setText(medicinesItem.getCreated());
        //viewHolder.MEDICINE_MODIFIED.setText(medicinesItem.getModified());
        // textView3_5.setText(i+"/"+mFilteredList.size());
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

                    ArrayList<MedicinesItem> filteredList = new ArrayList<>();

                    for (MedicinesItem categories : mArrayList) {

                        if (categories.getMedicineName().toLowerCase().contains(charString)) {

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
    }

    public void refreshDataOnset(int position) {

        this.pos = position;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rt, cm, view_more, MEDICINE_NAME, MEDICINE_ID, MEDICINE_FREQ, MEDICINE_FREQ2, MEDICINE_NODAYS,
                MEDICINE_ROUTE, MEDICINE_INSTRUCTN, MEDICINE_ADD_CMMNT, MEDICINE_CREATED, MEDICINE_MODIFIED;

        // FrameLayout fl;
        ProgressBar pb;
        //ImageButton pic;
        LinearLayout ll_medicine_main;
        ImageView imageView;
        Button btnEdit, btnDelete;

        public ViewHolder(View view) {
            super(view);
            ctx = view.getContext();

            MEDICINE_NAME = view.findViewById(R.id.tab_name);
            MEDICINE_FREQ = view.findViewById(R.id.tab_freq1);
            MEDICINE_FREQ2 = view.findViewById(R.id.tv_frequency2);
            MEDICINE_NODAYS = view.findViewById(R.id.tab_freq2);
            MEDICINE_ROUTE = view.findViewById(R.id.tab_route);
            MEDICINE_INSTRUCTN = view.findViewById(R.id.tab_instruction);
            MEDICINE_ADD_CMMNT = view.findViewById(R.id.tab_comments);
            view_more = view.findViewById(R.id.view_more);
            rt = view.findViewById(R.id.rt);
            cm = view.findViewById(R.id.cm);
            btnEdit = view.findViewById(R.id.edit);
            btnDelete = view.findViewById(R.id.delete);
            final CreatePrescription createPrescription = new CreatePrescription();
            final AddTemplate addTemplate = new AddTemplate();

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edited_ = getAdapterPosition();
                    MedicinesItem medicinesItem = mFilteredList.get(getAdapterPosition());
                    et_no_of_days.setText(medicinesItem.getNoOfDays());

                    if(!TextUtils.isEmpty(medicinesItem.getQty()) && !medicinesItem.getQty().equalsIgnoreCase("0")){
                        medicineQty.setText(medicinesItem.getQty());
                    }

                    et_additional_comments.setText(medicinesItem.getAdditionaComment());
                    searchBarMaterialMedicine.setText(medicinesItem.getMedicineName());
                    for (int i = 0; i < frequency_list.size(); i++) {
                        if (frequency_list.get(i).equalsIgnoreCase(medicinesItem.getFrequency())) {
                            frequency_spinner.setSelection(i);
                            break;
                        } else
                            frequency_spinner.setSelection(0);
                    }

                    for (int i = 0; i < frequency2_list.size(); i++) {
                        if (frequency2_list.get(i).equalsIgnoreCase(medicinesItem.getNoOfDays())) {
                            frequency2_spinner.setSelection(i);
                            break;
                        } else
                            frequency2_spinner.setSelection(1);
                    }


                    for (int i = 0; i < route_list.size(); i++) {
                        if (route_list.get(i).equalsIgnoreCase(medicinesItem.getRoute())) {
                            route_spinner.setSelection(i);
                            break;
                        } else
                            route_spinner.setSelection(0);
                    }


                    for (int i = 0; i < instructions_list.size(); i++) {
                        if (instructions_list.get(i).equalsIgnoreCase(medicinesItem.getInstruction())) {
                            instructions_spinner.setSelection(i);
                            break;
                        } else
                            instructions_spinner.setSelection(0);
                    }

                    btn_add.setText("Save");

                    HomeActivity.poss__ = getAdapterPosition();
                    mRecyclerViewAddedMedicines.setVisibility(View.GONE);
                    btn_prescribe.setVisibility(View.GONE);
                    if (!("template").equalsIgnoreCase(iden)) {
                        btnChooseFromTemplate.setVisibility(View.GONE);
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fl_progress_bar.setVisibility(View.VISIBLE);
                    mFilteredList.remove(getAdapterPosition());

                    if (CreatePrescription.NEWaddMedicinesArrayList != null)
                        if (CreatePrescription.NEWaddMedicinesArrayList.size() != 0)
                            CreatePrescription.NEWaddMedicinesArrayList.remove(getAdapterPosition());
                    if (mFilteredList.size() == 0) {
                        textView3_5.setVisibility(View.GONE);
                        ll_35.setVisibility(View.GONE);
                    } else {
                        textView3_5.setText("1/" + mFilteredList.size());
                    }
                    if (edited_ == getAdapterPosition())
                        btn_add.setText("Save and Add next");
                    // HomeActivity.poss__ = getAdapterPosition();
                    notifyDataSetChanged();
                    btn_prescribe.setVisibility(View.VISIBLE);
                    if (!("template").equalsIgnoreCase(iden)) {
                        if (!("temp").equalsIgnoreCase(iden)) {
                            btnChooseFromTemplate.setVisibility(View.VISIBLE);
                        } else {
                            btnChooseFromTemplate.setVisibility(View.GONE);
                        }
                    }
                    fl_progress_bar.setVisibility(View.GONE);

                }
            });
            //=============================================================================================
        }
    }


}
