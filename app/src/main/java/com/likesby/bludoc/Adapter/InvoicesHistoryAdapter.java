package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.InvoiceActivity;
import com.likesby.bludoc.InvoicesPresActivity;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.InvoicePresModel;
import com.likesby.bludoc.R;
import com.likesby.bludoc.viewModels.AllPharmacistList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class InvoicesHistoryAdapter extends RecyclerView.Adapter<InvoicesHistoryAdapter.viewHolder> implements Filterable {

    private final Context context;
    private final ArrayList<InvoicePresModel> invoicePresModels;
    private ArrayList<InvoicePresModel> listFilter;
    private InvoicesHistoryAdapter.OnClickListener onClickListener;

    public InvoicesHistoryAdapter(ArrayList<InvoicePresModel> invoicePresModels, Context context) {
        this.invoicePresModels = invoicePresModels;
        this.context = context;
        this.listFilter = invoicePresModels;
    }

    // Create new views
    @NotNull
    @Override
    public InvoicesHistoryAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoices_history_adapter_layout, parent, false);
        return new InvoicesHistoryAdapter.viewHolder(view);
    }

    public interface OnClickListener {

        void onDelete(AllPharmacistList s, int position);

    }

    public void setOnClickListener(InvoicesHistoryAdapter.OnClickListener onClickListener) {

        this.onClickListener = onClickListener;

    }

    @Override
    public void onBindViewHolder(@NotNull InvoicesHistoryAdapter.viewHolder holder, final int position) {

        final InvoicePresModel invoicePresModel = listFilter.get(position);

        holder.invoice_title.setText(invoicePresModel.getInvoice_title());
        holder.patient_name.setText("Name : "+checkValue(invoicePresModel.getP_name()));
        holder.tv_email.setText("Email : "+checkValue(invoicePresModel.getP_email()));
        holder.tv_mobile.setText("Mobile Number : "+checkValue(invoicePresModel.getP_mobile()));
        holder.invoice_number.setText("#"+invoicePresModel.getInvoice_number());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PatientsItem patientsItem=new PatientsItem();
                patientsItem.setPatientId(invoicePresModel.getPatient_id());
                patientsItem.setPEmail(invoicePresModel.getP_email());
                patientsItem.setPMobile(invoicePresModel.getP_mobile());
                patientsItem.setPName(invoicePresModel.getP_name());
                patientsItem.setAddress(invoicePresModel.getP_address());

                Intent intent = new Intent(context, InvoicesPresActivity.class);
                intent.putExtra("patientName", invoicePresModel.getP_name());
                intent.putExtra("patientObject", patientsItem);
                intent.putExtra("invoicePewModel", invoicePresModel);
                context.startActivity(intent);

            }
        });

    }

    private String checkValue(String p_name) {

        if(!TextUtils.isEmpty(p_name)){
            return  p_name;
        }

        return "____";
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
                    listFilter = invoicePresModels;
                } else {
                    ArrayList<InvoicePresModel> filteredList = new ArrayList<>();
                    for (InvoicePresModel row : invoicePresModels) {
                        if (row.getInvoice_title().toLowerCase().contains(charString.toLowerCase())) {
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

                listFilter = (ArrayList<InvoicePresModel>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private final TextView invoice_title;
        private final TextView patient_name;
        private final TextView tv_mobile;
        private final TextView tv_email;
        private final TextView invoice_number;
        ProgressBar pb;
        LinearLayout ll_main_patient_view;
        ImageView imageView;

        public viewHolder(View view) {
            super(view);

            invoice_title = view.findViewById(R.id.invoice_title);
            patient_name = view.findViewById(R.id.patient_name);
            ll_main_patient_view = view.findViewById(R.id.ll_main_patient_view);
            tv_mobile = view.findViewById(R.id.tv_mobile);
            tv_email = view.findViewById(R.id.tv_email);
            invoice_number = view.findViewById(R.id.invoice_number);

        }
    }
}
