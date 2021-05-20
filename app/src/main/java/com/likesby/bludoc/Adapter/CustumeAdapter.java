package com.likesby.bludoc.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.likesby.bludoc.R;
import com.likesby.bludoc.viewModels.AllPharmacistList;

import java.util.ArrayList;

//CustumeAdapter
public class CustumeAdapter extends ArrayAdapter<AllPharmacistList> {

    private ArrayList<AllPharmacistList> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView text;
        CheckBox checkBox;
    }

    public CustumeAdapter(ArrayList<AllPharmacistList> data, Context context) {
        super(context, R.layout.choose_connection_list_layout_adapter, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        AllPharmacistList dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.choose_connection_list_layout_adapter, parent, false);
            viewHolder.text = convertView.findViewById(R.id.text);
            viewHolder.checkBox = convertView.findViewById(R.id.checkbox);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(!TextUtils.isEmpty(dataModel.getPharmacist_mobile()) && !dataModel.getPharmacist_mobile().equals("0") ) {
            viewHolder.text.setText(dataModel.getPharmacist_name() + " (" + dataModel.getPharmacist_mobile() + ")");
            viewHolder.checkBox.setVisibility(View.VISIBLE);
        }  else {
            viewHolder.text.setText(dataModel.getPharmacist_name() + " (Mobile Not Entered)");
            viewHolder.checkBox.setVisibility(View.GONE);
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
