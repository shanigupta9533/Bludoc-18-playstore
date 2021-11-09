package com.likesby.bludoc.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities.SubcriptionsItem;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.FragmentGeneratedPresBottomBinding;
import com.likesby.bludoc.utils.DateUtils;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class GeneratedPresBottomFragment extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener {

    private FragmentGeneratedPresBottomBinding binding;
    private onClickListener onClickListener;
    private boolean isClinicFound;
    private FragmentActivity fragmentActivity;
    SessionManager manager = new SessionManager();

    public GeneratedPresBottomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        fragmentActivity= (FragmentActivity) context;

    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    public interface onClickListener{

        void onChangeResult(int ids, boolean value);
        void onChooseCalender(String dataValue, String date_with_name);

    }

    public void SetOnClickListener(onClickListener onClickListener){

        this.onClickListener=onClickListener;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentGeneratedPresBottomBinding.inflate(inflater, container, false);

        Bundle arguments = getArguments();

        if(arguments!=null){

            binding.doctorDetails.setChecked(arguments.getBoolean("isDoctorDetails",false));
            binding.hostiptalName.setChecked(arguments.getBoolean("isClinic",false));
            binding.footerDetails.setChecked(arguments.getBoolean("isFooter",false));
            isClinicFound = arguments.getBoolean("isClinicFound", false);

            if(!isClinicFound){

                binding.hostiptalName.setEnabled(false);
                binding.hostiptalName.setChecked(false);

            }

        }

        binding.doctorDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!("").equalsIgnoreCase(manager.getPreferences(fragmentActivity, "registration_no"))) {
                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
                    if (("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                        String sub_valid = "", premium_valid = "";
                        boolean flag_reset_paid = false;
                        Date date1 = null, date2 = null;
                        int days_left_free = 0, days_left_paid = 0;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
                        if (profileDetails.getSubcriptions() != null) {
                            if (profileDetails.getSubcriptions().size() != 0) {
                                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                                    if (!si.getHospital_code().equals("")) {
                                        boolean flag_reset_free;
                                        try {
                                            Calendar c2 = Calendar.getInstance();
                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                            Date dateEnd = dateFormat.parse(si.getEnd());
                                            Date c = Calendar.getInstance().getTime();
                                            assert dateEnd != null;

                                            //premium_valid = si.getDays();
                                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                            try {
                                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                                date2 = dateFormat.parse(si.getEnd());
                                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                                flag_reset_free = true;

                                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                                // break;
                                                sub_valid = si.getEnd();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                                        } catch (ParseException pe) {
                                            // handle the failure
                                            flag_reset_free = false;
                                        }

                                    }
                                }

                            } else {
                                popupPaused();
                                binding.doctorDetails.setChecked(true);
                                return;

                            }
                        } else {
                            popupPaused();
                            binding.doctorDetails.setChecked(true);
                            return;
                        }

                        if (days_left_free < 0) {
                            popupPaused();
                            binding.doctorDetails.setChecked(true);
                        } else {

                            if(onClickListener!=null)
                                onClickListener.onChangeResult(1,isChecked);
                        }

                    }
                } else {
                    popup();
                }

            }
        });

        binding.dateOfCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!("").equalsIgnoreCase(manager.getPreferences(fragmentActivity, "registration_no"))) {
                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
                    if (("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                        String sub_valid = "", premium_valid = "";
                        boolean flag_reset_paid = false;
                        Date date1 = null, date2 = null;
                        int days_left_free = 0, days_left_paid = 0;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
                        if (profileDetails.getSubcriptions() != null) {
                            if (profileDetails.getSubcriptions().size() != 0) {
                                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                                    if (!si.getHospital_code().equals("")) {
                                        boolean flag_reset_free;
                                        try {
                                            Calendar c2 = Calendar.getInstance();
                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                            Date dateEnd = dateFormat.parse(si.getEnd());
                                            Date c = Calendar.getInstance().getTime();
                                            assert dateEnd != null;

                                            //premium_valid = si.getDays();
                                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                            try {
                                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                                date2 = dateFormat.parse(si.getEnd());
                                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                                flag_reset_free = true;

                                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                                // break;
                                                sub_valid = si.getEnd();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                                        } catch (ParseException pe) {
                                            // handle the failure
                                            flag_reset_free = false;
                                        }

                                    }
                                }

                            } else {
                                popupPaused();
                                return;

                            }
                        } else {
                            popupPaused();
                            return;
                        }

                        if (days_left_free < 0) {
                            popupPaused();
                        } else {

                            Calendar myCalendar = Calendar.getInstance();

                            DatePickerDialog datePickerDialog = new DatePickerDialog(fragmentActivity, R.style.DialogTheme, GeneratedPresBottomFragment.this,
                                    myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH));

                            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                            datePickerDialog.show();
                        }

                    }
                } else {
                    popup();
                }

            }
        });

        binding.hostiptalName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!("").equalsIgnoreCase(manager.getPreferences(fragmentActivity, "registration_no"))) {
                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
                    if (("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                        String sub_valid = "", premium_valid = "";
                        boolean flag_reset_paid = false;
                        Date date1 = null, date2 = null;
                        int days_left_free = 0, days_left_paid = 0;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
                        if (profileDetails.getSubcriptions() != null) {
                            if (profileDetails.getSubcriptions().size() != 0) {
                                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                                    if (!si.getHospital_code().equals("")) {
                                        boolean flag_reset_free;
                                        try {
                                            Calendar c2 = Calendar.getInstance();
                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                            Date dateEnd = dateFormat.parse(si.getEnd());
                                            Date c = Calendar.getInstance().getTime();
                                            assert dateEnd != null;

                                            //premium_valid = si.getDays();
                                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                            try {
                                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                                date2 = dateFormat.parse(si.getEnd());
                                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                                flag_reset_free = true;

                                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                                // break;
                                                sub_valid = si.getEnd();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                                        } catch (ParseException pe) {
                                            // handle the failure
                                            flag_reset_free = false;
                                        }

                                    }
                                }

                            } else {
                                popupPaused();
                                binding.hostiptalName.setChecked(true);
                                return;

                            }
                        } else {
                            popupPaused();
                            binding.hostiptalName.setChecked(true);
                            return;
                        }

                        if (days_left_free < 0) {
                            popupPaused();
                            binding.hostiptalName.setChecked(true);
                        } else {

                            if(onClickListener!=null)
                                onClickListener.onChangeResult(2,isChecked);
                        }

                    }
                } else {
                    popup();
                }

            }

        });

        binding.footerDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!("").equalsIgnoreCase(manager.getPreferences(fragmentActivity, "registration_no"))) {
                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
                    if (("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                        String sub_valid = "", premium_valid = "";
                        boolean flag_reset_paid = false;
                        Date date1 = null, date2 = null;
                        int days_left_free = 0, days_left_paid = 0;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
                        if (profileDetails.getSubcriptions() != null) {
                            if (profileDetails.getSubcriptions().size() != 0) {
                                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                                    if (!si.getHospital_code().equals("")) {
                                        boolean flag_reset_free;
                                        try {
                                            Calendar c2 = Calendar.getInstance();
                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                            Date dateEnd = dateFormat.parse(si.getEnd());
                                            Date c = Calendar.getInstance().getTime();
                                            assert dateEnd != null;

                                            //premium_valid = si.getDays();
                                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                            try {
                                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                                date2 = dateFormat.parse(si.getEnd());
                                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                                flag_reset_free = true;

                                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                                // break;
                                                sub_valid = si.getEnd();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                                        } catch (ParseException pe) {
                                            // handle the failure
                                            flag_reset_free = false;
                                        }

                                    }
                                }

                            } else {
                                popupPaused();
                                binding.footerDetails.setChecked(true);
                                return;

                            }
                        } else {
                            popupPaused();
                            binding.footerDetails.setChecked(true);
                            return;
                        }

                        if (days_left_free < 0) {
                            popupPaused();
                            binding.footerDetails.setChecked(true);
                        } else {

                            if(onClickListener!=null)
                                onClickListener.onChangeResult(3,isChecked);
                        }

                    }
                } else {
                    popup();
                }

            }

        });

        binding.addPharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!("").equalsIgnoreCase(manager.getPreferences(fragmentActivity, "registration_no"))) {
                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
                    if (("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                        String sub_valid = "", premium_valid = "";
                        boolean flag_reset_paid = false;
                        Date date1 = null, date2 = null;
                        int days_left_free = 0, days_left_paid = 0;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
                        if (profileDetails.getSubcriptions() != null) {
                            if (profileDetails.getSubcriptions().size() != 0) {
                                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                                    if (!si.getHospital_code().equals("")) {
                                        boolean flag_reset_free;
                                        try {
                                            Calendar c2 = Calendar.getInstance();
                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                            Date dateEnd = dateFormat.parse(si.getEnd());
                                            Date c = Calendar.getInstance().getTime();
                                            assert dateEnd != null;

                                            //premium_valid = si.getDays();
                                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                            try {
                                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                                date2 = dateFormat.parse(si.getEnd());
                                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                                flag_reset_free = true;

                                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                                // break;
                                                sub_valid = si.getEnd();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                                        } catch (ParseException pe) {
                                            // handle the failure
                                            flag_reset_free = false;
                                        }

                                    }
                                }

                            } else {
                                popupPaused();
                                return;

                            }
                        } else {
                            popupPaused();
                            return;
                        }

                        if (days_left_free < 0) {
                            popupPaused();
                        } else {

                            if(onClickListener!=null)
                                onClickListener.onChangeResult(4,false);

                            dismiss();
                        }

                    }
                } else {
                    popup();
                }

            }
        });

        binding.sendToPharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickListener!=null)
                    onClickListener.onChangeResult(5,false);

                dismiss();

            }
        });

        binding.language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickListener!=null)
                    onClickListener.onChangeResult(6,false);

                dismiss();

            }
        });


        return binding.getRoot();
    }

    private void popupPaused() {

        PopUpSubscriptionDialogFragment popUpSubscriptionDialogFragment= new PopUpSubscriptionDialogFragment();
        popUpSubscriptionDialogFragment.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putString("keywords", "Change Date");
        popUpSubscriptionDialogFragment.setArguments(bundle);
        popUpSubscriptionDialogFragment.show(fragmentActivity.getSupportFragmentManager(),"");

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if(onClickListener!=null)
            onClickListener.onChooseCalender(year+"-"+(month+1)+"-"+dayOfMonth, dayOfMonth+"-"+getMonthForInt(month)+"-"+year);

        dismiss();

    }

    private  void popup()
    {
        final Dialog dialog_data = new Dialog(fragmentActivity);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.feedback);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_data.dismiss();

            }
        });
        dialog_data.show();
    }

}