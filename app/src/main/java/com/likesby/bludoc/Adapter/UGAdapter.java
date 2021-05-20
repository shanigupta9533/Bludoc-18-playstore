package com.likesby.bludoc.Adapter;


        import android.annotation.SuppressLint;
        import android.app.Dialog;
        import android.content.Context;
        import android.os.Build;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import android.widget.Filter;
        import android.widget.Filterable;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.annotation.RequiresApi;
        import androidx.recyclerview.widget.RecyclerView;

        import com.likesby.bludoc.ModelLayer.Entities.UGItem;
        import com.likesby.bludoc.R;
        import com.likesby.bludoc.RegisterDetails;
        import com.likesby.bludoc.viewModels.ApiViewHolder;

        import java.util.ArrayList;

        import io.reactivex.disposables.CompositeDisposable;

        import static com.likesby.bludoc.RegisterDetails.textView__select_ug;
        import static com.likesby.bludoc.RegisterDetails.ug_id__;
        import static com.likesby.bludoc.RegisterDetails.ug_name__;


public class UGAdapter extends RecyclerView.Adapter<UGAdapter.ViewHolder> implements Filterable {

    private ArrayList<UGItem> mArryUG,mFilteredList;
    Context context;
    String ug_name;
    ApiViewHolder apiViewHolder;
    CompositeDisposable mBag;
    Dialog dialog_data;
    RegisterDetails registerDetails;
    public UGAdapter(Context context,
                     ArrayList<UGItem> objects, String ug_name, ApiViewHolder apiViewHolder, CompositeDisposable mBag, Dialog dialog_data, RegisterDetails registerDetails) {
        mArryUG = objects;
        mFilteredList = objects;
        this.context = context;
        this.ug_name = ug_name;
        this.apiViewHolder = apiViewHolder;
        this.mBag = mBag;
        this.dialog_data = dialog_data;
        this.registerDetails = registerDetails;

    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.cv_ug,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.UGID.setText(mFilteredList.get(i).getUgId());
        viewHolder.UGNAME.setText(mFilteredList.get(i).getUgName());
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArryUG;
                } else {

                    ArrayList<UGItem> filteredList = new ArrayList<>();

                    for (UGItem categories : mArryUG) {

                        if (categories.getUgName().toLowerCase().contains(charString) )
                        {
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
                mFilteredList = (ArrayList<UGItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout ll_ug_;
        TextView UGNAME, UGID;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View view)
        {
            super(view);
            context = view.getContext();

            ll_ug_        = view.findViewById(R.id.ll_ug_);
            UGNAME        = view.findViewById(R.id.tv_ug_name);
            UGID       = view.findViewById(R.id.tv_ug_id);

            ll_ug_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ug_name__ = mFilteredList.get(getAdapterPosition()).getUgName();
                    ug_id__ = mFilteredList.get(getAdapterPosition()).getUgId();
                    ug_name = ug_name__;
                    dialog_data.dismiss();
                    if(ug_name__.equals("Other")){

                        registerDetails.addUG();
                    }
                    else
                        textView__select_ug.setText(ug_name__);

                }
            });

        }
    }


}