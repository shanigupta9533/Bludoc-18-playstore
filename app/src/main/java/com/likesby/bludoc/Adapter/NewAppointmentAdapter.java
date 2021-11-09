package com.likesby.bludoc.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.CreateAppointmentActivity;
import com.likesby.bludoc.ModelLayer.AppointmentListModel;
import com.likesby.bludoc.R;
import com.likesby.bludoc.utils.Utils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NewAppointmentAdapter extends RecyclerView.Adapter<NewAppointmentAdapter.ViewHolder> implements Filterable {

    private final Context mContext;
    private final ArrayList<AppointmentListModel> arrayList;
    private ArrayList<AppointmentListModel> listFilter;
    private onClickListener onClickListener;
    private String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"
    };
    private boolean history = false;

    public NewAppointmentAdapter(ArrayList<AppointmentListModel> arrayList, Context context) {
        mContext = context;
        this.arrayList = arrayList;
        this.listFilter = arrayList;
    }

    @Override // filter of patient list android
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()) {

                    listFilter = arrayList;

                } else {

                    ArrayList<AppointmentListModel> filteredList = new ArrayList<>();
                    for (AppointmentListModel row : arrayList) {
                        if (row.getP_name().toLowerCase().contains(charString.toLowerCase())
                                || row.getP_email().toLowerCase().contains(charString.toLowerCase())
                                || row.getP_mobile().toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row);
                        }
                    }

                    listFilter = filteredList;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                listFilter = (ArrayList<AppointmentListModel>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public void setAdapter(boolean b) {

        this.history = b;

    }

    public interface onClickListener {

        void onClick(AppointmentListModel appointmentListModel, int position);

    }

    public void setOnClickListener(onClickListener onclickListener) {

        this.onClickListener = onclickListener;

    }

    @SuppressLint("NewApi")
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.new_appointment_adapter_layout,
                viewGroup, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") int position) {

        AppointmentListModel appointmentListModel = listFilter.get(position);

        DateTime dateTime;

        try {
            dateTime = new DateTime(appointmentListModel.getApp_date() + "T" + Utils.convertTo24Hour(appointmentListModel.getApp_time()));
        } catch (Exception e) {
            dateTime = new DateTime(appointmentListModel.getApp_date() + "T" + appointmentListModel.getApp_time());
        }

        String dateDefined = dateTime.getDayOfMonth() + " " + monthNames[dateTime.getMonthOfYear() - 1] + " " + dateTime.getYear();
        DateTime today = new DateTime();

        if (today.getMillis() > dateTime.getMillis() + 60000) {
            viewHolder.patient_edit.setVisibility(View.GONE);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("EEE");
        String dayOfWeek = sd.format(dateTime.getMillis());

        viewHolder.time.setText(appointmentListModel.getApp_time());
        viewHolder.date.setText(dateDefined + ", " + dayOfWeek);

        if (appointmentListModel.getCreated().equalsIgnoreCase(appointmentListModel.getModified())) {

            DateTime dateTimeCreated = new DateTime(appointmentListModel.getCreated().replace(" ", "T"));
            String dateTimeDateDefined = dateTimeCreated.getDayOfMonth() + " " + monthNames[dateTimeCreated.getMonthOfYear() - 1] + " " + dateTimeCreated.getYear();
            String dateTimeOnCreated = convertInto12Hour(dateTimeCreated.getHourOfDay(), dateTimeCreated.getMinuteOfHour());

            if (TextUtils.isEmpty(appointmentListModel.getPatient_id()))
                viewHolder.status.setText("Created by patient on " + dateTimeDateDefined + " @ " + dateTimeOnCreated);
            else
                viewHolder.status.setText("Created by you on " + dateTimeDateDefined + " @ " + dateTimeOnCreated);

        } else {

            DateTime dateTimeCreated = new DateTime(appointmentListModel.getModified().replace(" ", "T"));
            String dateTimeDateDefined = dateTimeCreated.getDayOfMonth() + " " + monthNames[dateTimeCreated.getMonthOfYear() - 1] + " " + dateTimeCreated.getYear();
            String dateTimeOnCreated = convertInto12Hour(dateTimeCreated.getHourOfDay(), dateTimeCreated.getMinuteOfHour());

            if (TextUtils.isEmpty(appointmentListModel.getPatient_id()))
                viewHolder.status.setText("Updated by patient on " + dateTimeDateDefined + " @ " + dateTimeOnCreated);
            else
                viewHolder.status.setText("Updated by you on " + dateTimeDateDefined + " @ " + dateTimeOnCreated);

        }

        if (!TextUtils.isEmpty(appointmentListModel.getPurpose()))
            viewHolder.purpose.setText("Purpose : " + appointmentListModel.getPurpose());
        else {
            viewHolder.purpose.setText("Purpose : ____");
        }

        String p_gender = appointmentListModel.getP_gender();

        String age = "";

        if (!TextUtils.isEmpty(appointmentListModel.getP_age()) && !appointmentListModel.getP_age().trim().equalsIgnoreCase("0 yr")
                && !appointmentListModel.getP_age().trim().equalsIgnoreCase("yr")
        ) {
            age = " / " + appointmentListModel.getP_age();
        }

        viewHolder.patientName.setText(appointmentListModel.getP_name() + ", " + p_gender + "" + age);

        if (TextUtils.isEmpty(appointmentListModel.getP_email())) {
            viewHolder.patientEmail.setText("____");
        } else {
            viewHolder.patientEmail.setText(appointmentListModel.getP_email());
        }

        viewHolder.patientMobile.setOnClickListener(v -> {

            if (!appointmentListModel.getP_mobile().equalsIgnoreCase("0")) {


                Uri u = Uri.parse("tel:" + appointmentListModel.getP_mobile());
                Intent i = new Intent(Intent.ACTION_DIAL, u);

                try {
                    mContext.startActivity(i);
                } catch (SecurityException s) {
                    Toast.makeText(mContext, "An error occurred", Toast.LENGTH_LONG)
                            .show();
                }

            }

        });

        viewHolder.patientEmail.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(appointmentListModel.getP_email())) {

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{appointmentListModel.getP_email()});
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

        viewHolder.patient_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Are you sure you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (onClickListener != null) {
                                    onClickListener.onClick(appointmentListModel, position);
                                }

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        viewHolder.email_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(appointmentListModel.getP_email())) {

                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{appointmentListModel.getP_email()});
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

            }
        });

        viewHolder.phone_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri u = Uri.parse("tel:" + appointmentListModel.getP_mobile());
                Intent i = new Intent(Intent.ACTION_DIAL, u);

                try {
                    mContext.startActivity(i);
                } catch (SecurityException s) {
                    Toast.makeText(mContext, "An error occurred", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        viewHolder.whatsnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNumberOfWhatsApp(appointmentListModel.getP_mobile());

            }
        });

        if (history)
            viewHolder.patient_edit.setVisibility(View.GONE);

        viewHolder.patient_edit.setOnClickListener(v -> {

            Intent intent = new Intent(mContext, CreateAppointmentActivity.class);
            intent.putExtra("isUpdate", true);
            intent.putExtra("appointment", appointmentListModel);
            mContext.startActivity(intent);
        });

        if (TextUtils.isEmpty(appointmentListModel.getP_mobile()) || appointmentListModel.getP_mobile().equalsIgnoreCase("0")) {
            viewHolder.patientMobile.setText("___");
        } else {
            viewHolder.patientMobile.setText(appointmentListModel.getP_mobile());
        }

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
    public int getItemCount() {
        return listFilter.size();
    }

    public String convertInto12Hour(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        return String.valueOf(hours) + ':' +
                minutes + " " + timeSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView time;
        private final TextView date;
        private final TextView patientName;
        private final TextView status;
        private final TextView patientEmail;
        private final TextView patientMobile;
        private final ImageView patient_edit;
        private final ImageView patient_delete;
        private final TextView email_icon;
        private final TextView phone_icon;
        private final TextView purpose;
        private final ImageView whatsnumber;

        public ViewHolder(View view) {
            super(view);

            time = view.findViewById(R.id.time);
            date = view.findViewById(R.id.date);
            status = view.findViewById(R.id.status);
            patientName = view.findViewById(R.id.patientName);
            patientEmail = view.findViewById(R.id.patientEmail);
            patientMobile = view.findViewById(R.id.patientMobile);
            patient_delete = view.findViewById(R.id.patient_delete);
            patient_edit = view.findViewById(R.id.patient_edit);
            email_icon = view.findViewById(R.id.email_icon);
            phone_icon = view.findViewById(R.id.phone_icon);
            purpose = view.findViewById(R.id.purpose);
            whatsnumber = view.findViewById(R.id.whatsnumber);

        }
    }
}