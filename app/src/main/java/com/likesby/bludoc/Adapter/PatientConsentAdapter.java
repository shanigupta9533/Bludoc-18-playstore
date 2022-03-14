package com.likesby.bludoc.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.ModelLayer.ConsentDataModel;
import com.likesby.bludoc.ModelLayer.ConsentTemplateModel;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SignatureActivity;

import java.util.ArrayList;

public class PatientConsentAdapter extends RecyclerView.Adapter<PatientConsentAdapter.ViewHolder> implements Filterable {

    private ArrayList<ConsentDataModel> mArrayList;
    private ArrayList<ConsentDataModel> mFilteredList;
    private Context mContext;
    private OnClickListener onClickListener;

    public PatientConsentAdapter(ArrayList<ConsentDataModel> arrayList, Context context) {
        mContext = context;
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    public interface OnClickListener {

        void share(ConsentDataModel consentDataModel, int i);

    }

    public void setOnClickListener(OnClickListener onClickListener) {

        this.onClickListener = onClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.consent_patient_adapter_layout,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        ConsentDataModel consentDataModel = mFilteredList.get(i);

        viewHolder.title.setText(consentDataModel.getTitle());

        if (!TextUtils.isEmpty(consentDataModel.getParents_name()))
            viewHolder.patientName.setText(consentDataModel.getPatient_name() + " (Parent Name : " + consentDataModel.getParents_name() + ")");
        else
            viewHolder.patientName.setText(consentDataModel.getPatient_name());


        viewHolder.patientMobile.setText(consentDataModel.getP_mobile());
        viewHolder.description.setText(consentDataModel.getDescription());

        if (!TextUtils.isEmpty(consentDataModel.getP_email())) {
            viewHolder.patientEmail.setText(consentDataModel.getP_email());
        } else {
            viewHolder.patientEmail.setText("Patient Email Is Not Available");
        }

        if (!TextUtils.isEmpty(consentDataModel.getP_mobile()) && !consentDataModel.getP_mobile().equals("0")) {
            viewHolder.patientMobile.setText(consentDataModel.getP_mobile());
        } else {
            viewHolder.patientMobile.setText("Phone Number Is Not Available");
        }

        if (!TextUtils.isEmpty(consentDataModel.getSignature())) {
            viewHolder.status.setText("View Signature");
        } else {
            viewHolder.status.setText("Signature is not Available");
        }

        viewHolder.status.setOnClickListener(view -> {

            if (!TextUtils.isEmpty(consentDataModel.getSignature())) {
                Intent intent = new Intent(mContext, SignatureActivity.class);
                intent.putExtra("signature", consentDataModel.getSignature());
                mContext.startActivity(intent);
            }

        });

        viewHolder.patientEmail.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(consentDataModel.getP_email())) {

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{consentDataModel.getP_email()});
                email.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                email.putExtra(Intent.EXTRA_TEXT, "Message");

                //need this to prompts email client only
                email.setType("message/rfc822");


                try {
                    mContext.startActivity(Intent.createChooser(email, "Choose an Email client :"));
                } catch (Exception ex) {
                    Toast.makeText(mContext, "Error Sending Email,Try Later.", Toast.LENGTH_SHORT).show();
                }

            }

        });

        viewHolder.patientMobile.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(consentDataModel.getP_mobile()) && !consentDataModel.getP_mobile().equals("0")) {

                Uri u = Uri.parse("tel:" + consentDataModel.getP_mobile());
                Intent i1 = new Intent(Intent.ACTION_DIAL, u);

                try {
                    mContext.startActivity(i1);
                } catch (SecurityException s) {
                    Toast.makeText(mContext, "An error occurred", Toast.LENGTH_LONG)
                            .show();
                }

            }

        });

        viewHolder.share.setOnClickListener(view -> {

            if (onClickListener != null) onClickListener.share(consentDataModel, i);

        });

        viewHolder.patientMobile.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(consentDataModel.getP_mobile()) && !consentDataModel.getP_mobile().equals("0")) {

                phoneNumberOfWhatsApp(consentDataModel.getP_mobile());

            }

        });

    }

    public void phoneNumberOfWhatsApp(String p_mobile) {

        String contact = "+91" + p_mobile; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = mContext.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            mContext.startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {

            try { // business whatsapp android
                PackageManager pm = mContext.getPackageManager();
                pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                mContext.startActivity(i);
            } catch (PackageManager.NameNotFoundException exception) {
                Toast.makeText(mContext, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<ConsentDataModel> filteredList = new ArrayList<>();

                    for (ConsentDataModel categories : mArrayList) {

                        if (categories.getTitle().toLowerCase().contains(charString)) {

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
                mFilteredList = (ArrayList<ConsentDataModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, patientName, patientEmail, patientMobile, description, status;
        private final ImageView share;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            patientName = view.findViewById(R.id.patientName);
            patientEmail = view.findViewById(R.id.patientEmail);
            patientMobile = view.findViewById(R.id.patientMobile);
            description = view.findViewById(R.id.description);
            status = view.findViewById(R.id.status);
            share = view.findViewById(R.id.share);
        }
    }

}