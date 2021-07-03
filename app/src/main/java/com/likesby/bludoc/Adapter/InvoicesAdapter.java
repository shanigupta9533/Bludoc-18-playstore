package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.InvoicesModel.InvoicesDataModel;
import com.likesby.bludoc.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.viewHolder> {

    private final Context context;
    private final ArrayList<InvoicesDataModel> invoicesDataModelList;
    private OnClickListener onClickListener;


    public InvoicesAdapter(Context context, ArrayList<InvoicesDataModel> stringList) {

        this.context = context;
        this.invoicesDataModelList = stringList;

    }

    public interface OnClickListener {

        void onEdit(InvoicesDataModel invoicesDataModel, int position);

        void onDelete(InvoicesDataModel invoicesDataModel, int position);

    }

    public void setOnClickListener(OnClickListener onClickListener) {

        this.onClickListener = onClickListener;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.invoices_adapter_layout,
                parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {

        InvoicesDataModel invoicesDataModel = invoicesDataModelList.get(position);
        holder.title.setText(invoicesDataModel.getInvoicesTitle());
        holder.items.setText(invoicesDataModel.getItem_name());
        holder.amount.setText("Amount - "+invoicesDataModel.getAmount());

        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onClickListener != null)
                    onClickListener.onEdit(invoicesDataModel, position);

            }
        });

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onClickListener != null)
                    onClickListener.onDelete(invoicesDataModel, position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return invoicesDataModelList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView items;
        private final TextView amount;
        private final Button edit_button;
        private final Button delete_button;

        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            items = itemView.findViewById(R.id.items);
            amount = itemView.findViewById(R.id.amount);
            edit_button = itemView.findViewById(R.id.edit_button);
            delete_button = itemView.findViewById(R.id.delete_button);

        }
    }

}
