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

public class InvoicesTemplateAdapter extends RecyclerView.Adapter<InvoicesTemplateAdapter.viewHolder> {

    private final Context context;
    private final ArrayList<String> invoicesDataModelList;
    private OnClickListener onClickListener;


    public InvoicesTemplateAdapter(Context context, ArrayList<String> stringList) {

        this.context = context;
        this.invoicesDataModelList = stringList;

    }

    public interface OnClickListener {

        void onEdit(int position);
        void onDelete(int position);
        void onAdd(int position);

    }

    public void setOnClickListener(OnClickListener onClickListener) {

        this.onClickListener = onClickListener;

    }

    @NonNull
    @Override
    public InvoicesTemplateAdapter.viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.invoices_template_adapter_layout,
                parent, false);
        return new InvoicesTemplateAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InvoicesTemplateAdapter.viewHolder holder, int position) {

        holder.template_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickListener!=null){
                    onClickListener.onAdd(position);
                }

            }
        });

        holder.template_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickListener!=null){
                    onClickListener.onEdit(position);
                }

            }
        });

        holder.template_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickListener!=null){
                    onClickListener.onDelete(position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class viewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_template_name;
        private final Button template_add;
        private final Button template_edit;
        private final Button template_delete;

        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_template_name = itemView.findViewById(R.id.tv_template_name);
            template_add = itemView.findViewById(R.id.template_add);
            template_edit = itemView.findViewById(R.id.template_edit);
            template_delete = itemView.findViewById(R.id.template_delete);

        }
    }

}
