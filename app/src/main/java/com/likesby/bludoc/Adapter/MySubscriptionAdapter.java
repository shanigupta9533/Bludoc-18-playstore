
package com.likesby.bludoc.Adapter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;


import com.likesby.bludoc.ModelLayer.Entities.MySubscription;
import com.likesby.bludoc.ModelLayer.Entities.User;
import com.likesby.bludoc.ModelLayer.NewEntities.SubscriptionsItem;
import com.likesby.bludoc.NetworkCheck.CheckNetwork;
import com.likesby.bludoc.PaymentGateway;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SubscriptionPackages;
import com.likesby.bludoc.db.MyDB;
import com.likesby.bludoc.utils.ScreenSize;
import com.likesby.bludoc.viewModels.ApiViewHolder;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MySubscriptionAdapter extends RecyclerView.Adapter<MySubscriptionAdapter.ViewHolder> {
    private static final String TAG = "SubscriptionAdapter__";

    private ArrayList<com.likesby.bludoc.ModelLayer.Entities.MySubscription> mFilteredList = new ArrayList<>();
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
    private onClickListener onClickListener;

    public interface onClickListener{

        void onClick(String doctorSubscriptionId);

    }

    public void setOnClickListener(onClickListener onclickListener){

        this.onClickListener = onclickListener;

    }

    public MySubscriptionAdapter(ArrayList<com.likesby.bludoc.ModelLayer.Entities.MySubscription> arrayList, Context context, ApiViewHolder mapiViewHolder, CompositeDisposable bag, AppCompatActivity activity) {
        mContext = context;
        mFilteredList = arrayList;
        apiViewHolder = mapiViewHolder;
        mBag = bag;
        myDB = new MyDB(mContext);
        this.activity = activity;

    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.my_subscriptions,
                viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        viewHolder.packagename.setText(mFilteredList.get(position).getSubscriptionName());

        if(TextUtils.isEmpty(mFilteredList.get(position).getHospitalCode())) {

            viewHolder.id_notify.setVisibility(View.GONE);
            viewHolder.validity.setText("Validity : " + mFilteredList.get(position).getDays() + " Days");
            viewHolder.dateofpurchase.setText("Purchase Date : " + mFilteredList.get(position).getDate());
            viewHolder.dateofstart.setText("Start Date : " + mFilteredList.get(position).getStart());
            viewHolder.dateofend.setText("End Date : " + mFilteredList.get(position).getEnd());
            viewHolder.txnid.setText("Transaction Id : " + mFilteredList.get(position).getTransactionId());
            viewHolder.amount.setText("Amount Paid : " + mContext.getResources().getString(R.string.Rs) + " " + mFilteredList.get(position).getAmount() + " /  USD " + String.format("%.2f", Float.parseFloat(mFilteredList.get(position).getAmount()) / 60));

            if(TextUtils.isEmpty(mFilteredList.get(position).getTransactionId())){

                viewHolder.txnid.setVisibility(View.GONE);
                viewHolder.amount.setVisibility(View.GONE);
                viewHolder.dateofpurchase.setVisibility(View.GONE);
                viewHolder.copy.setVisibility(View.GONE);

            }

        } else {

            viewHolder.itemView.setVisibility(View.GONE);

        }


        viewHolder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Text Copied", mFilteredList.get(position).getTransactionId());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext, "Text Copied "+mFilteredList.get(position).getTransactionId(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.cancel_subscriptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickListener!=null)
                    onClickListener.onClick(mFilteredList.get(position).getDoctorSubscriptionId());

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
        private TextView amount,packagename, validity,dateofpurchase, txnid,dateofstart,dateofend,id_notify,cancel_subscriptions;
        ImageView copy;
        Button btnSubscribe;
        NestedScrollView ll_subscriptions;
        LinearLayout coupon;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View view) {
            super(view);
            mContext = view.getContext();

            packagename = view.findViewById(R.id.packagename);
            validity = view.findViewById(R.id.validity);
            dateofpurchase = view.findViewById(R.id.dateofpurchase);
            txnid = view.findViewById(R.id.txnid);
            copy= view.findViewById(R.id.copy);
            amount= view.findViewById(R.id.amount);
            dateofstart= view.findViewById(R.id.dateofstart);
            dateofend= view.findViewById(R.id.dateofend);
            id_notify= view.findViewById(R.id.id_notify);
            cancel_subscriptions= view.findViewById(R.id.cancel_subscriptions);
        }
    }
}

