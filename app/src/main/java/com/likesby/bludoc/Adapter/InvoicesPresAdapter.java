package com.likesby.bludoc.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.InvoicesModel.InvoicesDataModel;
import com.likesby.bludoc.R;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InvoicesPresAdapter extends RecyclerView.Adapter<InvoicesPresAdapter.viewHolder> {

    private final Context context;
    private final ArrayList<InvoicesDataModel> invoicesDataModelList;
    private InvoicesAdapter.OnClickListener onClickListener;
    DecimalFormat formatter = new DecimalFormat("#,###,###");


    public InvoicesPresAdapter(Context context, ArrayList<InvoicesDataModel> stringList) {

        this.context = context;
        this.invoicesDataModelList = stringList;

    }

    public interface OnClickListener {

        void onEdit(InvoicesDataModel invoicesDataModel, int position);

        void onDelete(InvoicesDataModel invoicesDataModel, int position);

    }

    public void setOnClickListener(InvoicesAdapter.OnClickListener onClickListener) {

        this.onClickListener = onClickListener;

    }

    @NonNull
    @Override
    public InvoicesPresAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.invoices_pres_layout_adapter,
                parent, false);
        return new InvoicesPresAdapter.viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull InvoicesPresAdapter.viewHolder holder, int position) {

        InvoicesDataModel invoicesDataModel = invoicesDataModelList.get(position);
        holder.invoicesHeading.setText(invoicesDataModel.getItem_name());
        holder.price.setText(formatter.format(Long.parseLong(invoicesDataModel.getAmount())));
        holder.position.setText((position+1)+". ");

        if(position%2==0){
            Typeface typeface = ResourcesCompat.getFont(context, R.font.poppins);
            holder.invoicesHeading.setTypeface(typeface);      // for Normal Text
            holder.price.setTypeface(typeface);      // for Normal Text
            holder.position.setTypeface(typeface);      // for Normal Text
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            Typeface typeface = ResourcesCompat.getFont(context, R.font.poppinsmedium);
            holder.invoicesHeading.setTypeface(typeface);        // for Bold only
            holder.position.setTypeface(typeface);        // for Bold only
            holder.price.setTypeface(typeface);        // for Bold only
            holder.itemView.setBackgroundColor(Color.parseColor("#eceff5"));
        }

    }

    @Override
    public int getItemCount() {
        return invoicesDataModelList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        private final TextView invoicesHeading;
        private final TextView price;
        private final TextView position;

        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            invoicesHeading = itemView.findViewById(R.id.invoicesHeading);
            price = itemView.findViewById(R.id.price);
            position = itemView.findViewById(R.id.position);

        }
    }

}