package com.likesby.bludoc.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.Entities.User;
import com.likesby.bludoc.ModelLayer.NewEntities.SubscriptionsItem;
import com.likesby.bludoc.R;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.viewModels.ApiViewHolder;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {
    private static final String TAG = "SubscriptionAdapter__";

    private ArrayList<SubscriptionsItem> mArrayList = new ArrayList<>();
    private ArrayList<SubscriptionsItem> mFilteredList = new ArrayList<>();
    private Context mContext;
    ApiViewHolder apiViewHolder;
    CompositeDisposable mBag;
    MyDB myDB;
    String amount = "";
    User user;
    public String sub_id = "";
    EditText et_promocode;

    Button btnApply, btnPayment, btnCancel, btnProceed, btnPaymentCancel, btnClose;
    LinearLayout linearLayout;
    TextView sub_amt, sub_disc, sub_total;
    String final_amt = "", offer_id = "", discount = "";
    FrameLayout fl_promo_apply;
    AppCompatActivity activity;
    LinearLayout ll_promo;
    boolean flag_renew = false;
    private OnClickListener onClickListener;


    public SubscriptionAdapter(ArrayList<SubscriptionsItem> arrayList, Context context, ApiViewHolder mapiViewHolder, CompositeDisposable bag, AppCompatActivity activity) {
        mContext = context;
        mArrayList = arrayList;
        mFilteredList = arrayList;
        apiViewHolder = mapiViewHolder;
        mBag = bag;
        myDB = new MyDB(mContext);
        this.activity = activity;

    }

    public interface OnClickListener{

        void onClick(SubscriptionsItem subscriptionsItem);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.cv_subscriptions,
                viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final SubscriptionAdapter.ViewHolder viewHolder, int i) {

        viewHolder.SUB_ID.setText(mFilteredList.get(i).getSubscriptionId());
        viewHolder.SUB_NAME.setText(mFilteredList.get(i).getSubscriptionName());
        viewHolder.SUB_DAYS.setText("No. of Days : " + mFilteredList.get(i).getDays());
        viewHolder.SUB_AMOUNT.setText(mContext.getResources().getString(R.string.Rs)+ " "+ mFilteredList.get(i).getMrp() + "/- ");
        viewHolder.sellprice.setText("New Price "+mContext.getResources().getString(R.string.Rs)+ " "+mFilteredList.get(i).getSell() + "/- "+mFilteredList.get(i).getSubscriptionDescription() +" in India \n and USD "+String.format("%.2f", (Float.parseFloat(mFilteredList.get(i).getSell()) / 60)) +" outside India \n(inclusive of all taxes)");
        viewHolder.btnSubscribe.setText("Proceed to Pay");
        viewHolder.SUB_TYPE.setText(mFilteredList.get(i).getType());
        viewHolder.mrp.setText(mContext.getResources().getString(R.string.Rs)+ " "+ mFilteredList.get(i).getMrp()+"/- "+mFilteredList.get(i).getSubscriptionDescription() +" in India \n and USD "+ String.format("%.2f",(Float.parseFloat(mFilteredList.get(i).getMrp()) / 60)) +" outside India \n(inclusive of all taxes)");
        viewHolder.total.setText("New Price "+mContext.getResources().getString(R.string.Rs)+ " "+mFilteredList.get(i).getSell() + "/- "+mFilteredList.get(i).getSubscriptionDescription() +" to pay");
//        int dim[] = ScreenSize.getDimensions(mContext);
//
//        NestedScrollView.LayoutParams parms = new NestedScrollView.LayoutParams(dim[0]-100,dim[1]);
//        viewHolder.ll_subscriptions.setLayoutParams(parms);

        viewHolder.save.setText("You’ve saved "+mContext.getResources().getString(R.string.Rs)+ " "+(Integer.parseInt(mFilteredList.get(i).getMrp())- Integer.parseInt(mFilteredList.get(i).getSell()))+ " / USD "+String.format("%.2f",(Float.parseFloat(String.valueOf((Integer.parseInt(mFilteredList.get(i).getMrp())- Integer.parseInt(mFilteredList.get(i).getSell())))) / 60)));
        viewHolder.btn_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.coupon.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickListener!=null){

                    onClickListener.onClick(mFilteredList.get(i));

                }

            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView SUB_NAME, SUB_ID,mrp, SUB_DAYS, SUB_AMOUNT,save, SUB_TYPE,sellprice,btn_coupon,total;
        Button btnSubscribe;
        NestedScrollView ll_subscriptions;
        LinearLayout coupon;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View view) {
            super(view);
            mContext = view.getContext();

            SUB_ID = view.findViewById(R.id.__subscriptions_id);
            SUB_NAME = view.findViewById(R.id.__subscriptions_name);
            SUB_DAYS = view.findViewById(R.id.__subscriptions_days);
            SUB_AMOUNT = view.findViewById(R.id.__subscriptions_amount);
            SUB_TYPE = view.findViewById(R.id.__subscriptions_type);
            btnSubscribe = view.findViewById(R.id.btn_subscribe);
            sellprice = view.findViewById(R.id.sellprice);
            ll_subscriptions = view.findViewById(R.id.ll_subscriptions);
            mrp = view.findViewById(R.id.mrp);
            btn_coupon= view.findViewById(R.id.btn_coupon);
            coupon = view.findViewById(R.id.coupon);
            save= view.findViewById(R.id.save);
            total= view.findViewById(R.id.total);

        }

    }

}
