
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.my_subscriptions,
                viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.packagename.setText(mFilteredList.get(i).getSubscriptionName());

        if(!TextUtils.isEmpty(mFilteredList.get(i).getHospitalCode())) {
            viewHolder.id_notify.setVisibility(View.GONE);
            viewHolder.validity.setText("Validity : " + mFilteredList.get(i).getDays() + " Days");
            viewHolder.dateofpurchase.setText("Purchase Date : " + mFilteredList.get(i).getDate());
            viewHolder.dateofstart.setText("Start Date : " + mFilteredList.get(i).getStart());
            viewHolder.dateofend.setText("End Date : " + mFilteredList.get(i).getEnd());
            viewHolder.txnid.setText("Transaction Id : " + mFilteredList.get(i).getTransactionId());
            viewHolder.amount.setText("Amount Paid : " + mContext.getResources().getString(R.string.Rs) + " " + mFilteredList.get(i).getAmount() + " /  USD " + String.format("%.2f", Float.parseFloat(mFilteredList.get(i).getAmount()) / 60));
        } else {
            viewHolder.id_notify.setVisibility(View.VISIBLE);
            viewHolder.id_notify.setText("Your subscription is purchased by ‘"+mFilteredList.get(i).getName()+"’ valid till : "+mFilteredList.get(i).getDate());
        }

        viewHolder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Text Copied", mFilteredList.get(i).getTransactionId());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(mContext, "Text Copied "+mFilteredList.get(i).getTransactionId(), Toast.LENGTH_SHORT).show();
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
        private TextView amount,packagename, validity,dateofpurchase, txnid,dateofstart,dateofend,id_notify;
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
        }
    }
}

