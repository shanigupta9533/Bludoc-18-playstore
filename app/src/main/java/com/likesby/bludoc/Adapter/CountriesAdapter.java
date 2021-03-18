//package com.likesby.bludoc.Adapter;
//
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Build;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.ensivosolutions.tracksido.ModelLayer.Entities.CountriesItem;
//import com.ensivosolutions.tracksido.R;
//
//import java.util.ArrayList;
//
//
//public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> implements Filterable {
//
//    private ArrayList<CountriesItem> mArryCountries,mFilteredList;
//    private Context context;
//    Dialog dialog;
//    public CountriesAdapter(Context context,
//                            ArrayList<CountriesItem> objects, Dialog dialog_data) {
//        mArryCountries = objects;
//        mFilteredList = mArryCountries;
//        this.context = context;
//        dialog = dialog_data;
//    }
//
//        @SuppressLint("NewApi")
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
//        {
//            View view = LayoutInflater.from(
//                    viewGroup.getContext()).inflate(R.layout.country_spinner,
//                    viewGroup, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
//
//            viewHolder.id.setText(mFilteredList.get(i).getCountryId());
//            viewHolder.countryName.setText(mFilteredList.get(i).getCountryName());
//            viewHolder.countryCode.setText(mFilteredList.get(i).getCountryCode());
//        }
//
//
//        @Override
//        public int getItemViewType(int position) {
//            return super.getItemViewType(position);
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return mFilteredList.size();
//        }
//
//
//        @Override
//        public Filter getFilter()
//        {
//            return new Filter()
//            {
//                @Override
//                protected FilterResults performFiltering(CharSequence charSequence)
//                {
//                    String charString = charSequence.toString();
//
//                    if (charString.isEmpty()) {
//
//                        mFilteredList = mArryCountries;
//                    } else {
//
//                        ArrayList<CountriesItem> filteredList = new ArrayList<>();
//
//                        for (CountriesItem categories : mArryCountries) {
//
//                            if (categories.getCountryName().toLowerCase().contains(charString))
//                            {
//                                filteredList.add(categories);
//                            }
//                        }
//                        mFilteredList = filteredList;
//                    }
//
//                    FilterResults filterResults = new FilterResults();
//                    filterResults.values = mFilteredList;
//                    return filterResults;
//                }
//
//                @Override
//                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                    mFilteredList = (ArrayList<CountriesItem>) filterResults.values;
//                    notifyDataSetChanged();
//                }
//            };
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder
//        {
//         TextView id, countryName, countryCode;
//         FrameLayout fl_country;
//
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            public ViewHolder(View view)
//            {
//                super(view);
//                context = view.getContext();
//
//                id        = view.findViewById(R.id.country_id);
//                countryName        = view.findViewById(R.id.country_name);
//                countryCode        = view.findViewById(R.id.country_code);
//                fl_country        = view.findViewById(R.id.fl_country);
//
//                //=============================================================================================
//
//                fl_country.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                       /* tv_country_code.setText(countryCode.getText().toString().trim());
//                        tv_country_name.setText("("+countryName.getText().toString().trim()+")");*/
//                        dialog.dismiss();
//                    }
//                });
//            }
//
//    }
//
//}
