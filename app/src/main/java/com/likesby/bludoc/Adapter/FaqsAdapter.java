package com.likesby.bludoc.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.Entities.FaqsItem;
import com.likesby.bludoc.R;

import java.util.ArrayList;

public class FaqsAdapter extends RecyclerView.Adapter<FaqsAdapter.ViewHolder>
{
    private static final String TAG = "TrackingListAdapter__";
    private ArrayList<FaqsItem> mFilteredList = new ArrayList<>();
    private Context mContext ;

    public FaqsAdapter(ArrayList<FaqsItem> arrayList, Context context)
    {
        mContext = context;
        mFilteredList = arrayList;
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.cv_faqs,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.FAQ_ID.setText(mFilteredList.get(i).getId());
        viewHolder.FAQ_QUE.setText(""+(i+1)+". "+mFilteredList.get(i).getQuestion());
        viewHolder.FAQ_ANS.setText("Ans : "+ Html.fromHtml(mFilteredList.get(i).getAnswer()));

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView FAQ_ID, FAQ_QUE,FAQ_ANS;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View view)
        {
            super(view);
            mContext = view.getContext();

            FAQ_ID     = view.findViewById(R.id.__faq_id);
            FAQ_QUE     = view.findViewById(R.id.__faq_que);
            FAQ_ANS          = view.findViewById(R.id.__faq_ans);

        }
    }


}
