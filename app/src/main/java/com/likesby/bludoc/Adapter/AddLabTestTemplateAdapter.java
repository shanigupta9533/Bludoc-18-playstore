package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Fragment.AddTemplate;
import com.likesby.bludoc.Fragment.CreatePrescription;
import com.likesby.bludoc.HomeActivity;
import com.likesby.bludoc.ModelLayer.NewEntities.LabTestItem;
import com.likesby.bludoc.R;

import java.util.ArrayList;


public class AddLabTestTemplateAdapter extends RecyclerView.Adapter<AddLabTestTemplateAdapter.ViewHolder> implements Filterable {
    private ArrayList<LabTestItem> mArrayList = new ArrayList<>();
    private ArrayList<LabTestItem> mFilteredList = new ArrayList<>();
    private Context ctx;
    EditText et_no_of_days, et_additional_comments;
    ArrayList<String> frequency_list, frequency2_list, EditText, route_list, instructions_list;
    Spinner frequency_spinner, frequency2_spinner, route_spinner, instructions_spinner;
    Button btn_add, btnChooseFromTemplate, btn_prescribe;
    TextView textView3_5;
    EditText searchBarMaterialMedicine;
    boolean count;
    LinearLayout ll_35;
    RecyclerView mRecyclerViewAddedMedicines;
    static String iden;

    public AddLabTestTemplateAdapter(ArrayList<LabTestItem> arrayList, EditText et_additional_comments, Button btn_add, TextView textView3_5, LinearLayout ll_35, EditText searchBarMaterialMedicine, RecyclerView mRecyclerViewAddedMedicines,
                                     Button btnChooseFromTemplate, Button btn_prescribe, String iden) {
        mArrayList = arrayList;
        mFilteredList = arrayList;

        this.et_additional_comments = et_additional_comments;
        this.btn_add = btn_add;
        this.textView3_5 = textView3_5;
        this.ll_35 = ll_35;
        this.searchBarMaterialMedicine = searchBarMaterialMedicine;
        this.mRecyclerViewAddedMedicines = mRecyclerViewAddedMedicines;
        this.btnChooseFromTemplate = btnChooseFromTemplate;
        this.btn_prescribe = btn_prescribe;
        this.iden = iden;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.add_template_adapter_lab_test,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        LabTestItem medicinesItem = mFilteredList.get(i);
        viewHolder.MEDICINE_NAME.setText(medicinesItem.getLabTestName());

        viewHolder.parent_of_button.setVisibility(View.VISIBLE);

        if (!("").equalsIgnoreCase(medicinesItem.getLabTestComment())) {
            viewHolder.MEDICINE_ADD_CMMNT.setText(medicinesItem.getLabTestComment());
            viewHolder.view_more.setEnabled(true);
            viewHolder.view_more.setBackgroundResource(R.drawable.white_back_gradient);
        } else {
            viewHolder.MEDICINE_ADD_CMMNT.setVisibility(View.GONE);
            viewHolder.view_more.setEnabled(false);
            viewHolder.view_more.setBackgroundResource(R.drawable.disabled_button);
        }

        viewHolder.MEDICINE_ADD_CMMNT.setVisibility(View.GONE);
        count = false;
        viewHolder.view_more.setText("View More");

        viewHolder.view_more.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        LabTestItem medicinesItem = mFilteredList.get(i);
                                                        if (count) {
                                                            count = false;
                                                            viewHolder.view_more.setText("View More");
                                                            viewHolder.MEDICINE_ADD_CMMNT.setVisibility(View.GONE);

                                                        } else {

                                                            if (!("").equalsIgnoreCase(medicinesItem.getLabTestComment())) {
                                                                viewHolder.MEDICINE_ADD_CMMNT.setVisibility(View.VISIBLE);

                                                            } else {
                                                                viewHolder.MEDICINE_ADD_CMMNT.setVisibility(View.GONE);
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

                    ArrayList<LabTestItem> filteredList = new ArrayList<>();

                    for (LabTestItem categories : mArrayList) {

                        if (categories.getLabTestName().toLowerCase().contains(charString)) {

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
                mFilteredList = (ArrayList<LabTestItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView view_more, MEDICINE_NAME, MEDICINE_ID, MEDICINE_FREQ, MEDICINE_FREQ2, MEDICINE_NODAYS,
                MEDICINE_ROUTE, MEDICINE_INSTRUCTN, MEDICINE_ADD_CMMNT, MEDICINE_CREATED, MEDICINE_MODIFIED;
        // FrameLayout fl;
        ProgressBar pb;
        //ImageButton pic;
        LinearLayout ll_medicine_main, parent_of_button;
        ImageView imageView;
        Button btnEdit, btnDelete;

        public ViewHolder(View view) {
            super(view);
            ctx = view.getContext();

            MEDICINE_NAME = view.findViewById(R.id.test_name);
            parent_of_button = view.findViewById(R.id.parent_of_button);
            MEDICINE_ADD_CMMNT = view.findViewById(R.id.test_comment);
            view_more = view.findViewById(R.id.labtest_view_more);
            btnEdit = view.findViewById(R.id.labtest_edit);
            btnDelete = view.findViewById(R.id.labtest_delete);
            final CreatePrescription createPrescription = new CreatePrescription();
            final AddTemplate addTemplate = new AddTemplate();


            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LabTestItem medicinesItem = mFilteredList.get(getAdapterPosition());
                    et_additional_comments.setText(medicinesItem.getLabTestComment());
                    searchBarMaterialMedicine.setText(medicinesItem.getLabTestName());

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

                    mFilteredList.remove(getAdapterPosition());
                    if (mFilteredList.size() == 0) {
                        ll_35.setVisibility(View.GONE);
                    } else {
                        ll_35.setVisibility(View.VISIBLE);
                        textView3_5.setText("1/" + mFilteredList.size());
                    }
                    notifyDataSetChanged();
                    btn_prescribe.setVisibility(View.VISIBLE);
                    if (!("template").equalsIgnoreCase(iden)) {
                        if (!("temp").equalsIgnoreCase(iden)) {
                            btnChooseFromTemplate.setVisibility(View.VISIBLE);
                        } else {
                            btnChooseFromTemplate.setVisibility(View.GONE);
                        }
                    }
                }
            });
            //=============================================================================================
        }
    }


}
