package com.likesby.bludoc.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.likesby.bludoc.DigitalClinicActivity;
import com.likesby.bludoc.DigitalClinicConfirmationActivity;
import com.likesby.bludoc.ModelLayer.AppointmentModel;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.FragmentClinicDetailskBinding;
import com.likesby.bludoc.utils.Utils;
import com.mynameismidori.currencypicker.CurrencyPicker;
import com.mynameismidori.currencypicker.CurrencyPickerListener;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class ClinicDetailskFragment extends Fragment {

    private FragmentClinicDetailskBinding binding;
    private SessionManager manager;
    private FragmentActivity fragmentActivity;
    private AppointmentModel appointment;
    private boolean isSave;
    private String currency = "₹";
    ToggleButton Mon, Tue, Wed, Thu, Fri, Sat, Sun;
    private String isFirstTimeDigital;
    private boolean isFirstTime = true;

    public ClinicDetailskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fragmentActivity = (FragmentActivity) context;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentClinicDetailskBinding.inflate(inflater, container, false);

//        binding.progressBar.setVisibility(View.VISIBLE);

        manager = new SessionManager();

        isFirstTimeDigital = manager.getPreferences(fragmentActivity, "isFirstTimeDigitalClinic");

        if (TextUtils.isEmpty(isFirstTimeDigital)) {
            isFirstTime = false;
        }

        binding.currency.setText(currency);

        Mon = binding.getRoot().findViewById(R.id.Mon);
        Tue = binding.getRoot().findViewById(R.id.Tue);
        Wed = binding.getRoot().findViewById(R.id.Wed);
        Thu = binding.getRoot().findViewById(R.id.Thu);
        Fri = binding.getRoot().findViewById(R.id.Fri);
        Sat = binding.getRoot().findViewById(R.id.Sat);
        Sun = binding.getRoot().findViewById(R.id.Sun);

        appointment = manager.getObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment");

        binding.clinicName.setText(manager.getPreferences(fragmentActivity.getApplicationContext(), "clinic_name"));
        binding.clinicAddress.setText(manager.getPreferences(fragmentActivity.getApplicationContext(), "clinic_address"));

        String[] visiting_hr_from_details = manager.getPreferences(fragmentActivity.getApplicationContext(), "visiting_hr_from").split(Pattern.quote("|"));

        String[] visiting_hr_to_details = manager.getPreferences(fragmentActivity.getApplicationContext(), "visiting_hr_to").split(Pattern.quote("|"));

        if (!TextUtils.isEmpty(manager.getPreferences(fragmentActivity, "working_days"))) {
            setWorkingdays(manager.getPreferences(fragmentActivity, "working_days"));
        } else {
            setWorkingdays("");
        }

        if (visiting_hr_from_details.length > 1) {
            binding.twoOne.setText(visiting_hr_to_details[0].toLowerCase());
            binding.toTwo.setText(visiting_hr_to_details[1].toLowerCase());
            binding.fromOne.setText(visiting_hr_from_details[0].toLowerCase());
            binding.fromTwo.setText(visiting_hr_from_details[1].toLowerCase());

        } else {
            binding.fromOne.setText(visiting_hr_from_details[0].toLowerCase());
            binding.twoOne.setText(visiting_hr_to_details[0].toLowerCase());
        }

        binding.clinicAddress.setOnTouchListener((v, event) -> {
            if (binding.clinicAddress.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });

        binding.submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appointment = manager.getObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment");

                if (appointment != null) {
                    UploadClinicDetails();
                    manager.setPreferences(fragmentActivity, "isFirstTimeDigitalClinic", "true"); // todo isfirsttime handle here
                } else {
                    Toast.makeText(fragmentActivity, "First, save the first tab of Doctor Details.", Toast.LENGTH_SHORT).show();
                    ((DigitalClinicActivity) fragmentActivity).moveViewPager(0);
                }

            }
        });

        binding.currencyParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CurrencyPicker picker = CurrencyPicker.newInstance("Select Currency");  // dialog title
                picker.setListener(new CurrencyPickerListener() {
                    @Override
                    public void onSelectCurrency(String name, String code, String symbol, int flagDrawableResID) {

                        currency = code;

                        if (code.equalsIgnoreCase("INR"))
                            currency = "₹";

                        binding.currency.setText(currency);
                        picker.dismiss();

                    }
                });
                picker.show(fragmentActivity.getSupportFragmentManager(), "CURRENCY_PICKER");

            }
        });

        binding.save.setOnClickListener(v -> {

            isSave = true;

            appointment = manager.getObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment");

            if (appointment != null)
                UploadClinicDetails();
            else {
                Toast.makeText(fragmentActivity, "First, save the first tab of Doctor Details.", Toast.LENGTH_SHORT).show();
                ((DigitalClinicActivity) fragmentActivity).moveViewPager(0);
            }

        });

        if (appointment != null) {

            binding.clinicName.setText(appointment.getClinic_name());
            binding.clinicAddress.setText(appointment.getClinic_add());
            binding.consultationCharges.setText(appointment.getCons_charges());

            if (isFirstTime) {
                binding.save.setVisibility(View.VISIBLE);
            }

            if (isFirstTime) {
                if (!TextUtils.isEmpty(appointment.getDays())) {
                    setWorkingdays(appointment.getDays());
                } else {
                    setWorkingdays("");
                }

            }

            if (!TextUtils.isEmpty(appointment.getCurrency()))
                binding.currency.setText(appointment.getCurrency());
            else
                binding.currency.setText(currency);

            String[] visiting_hr_from_details_1 = {"", ""};
            String[] visiting_hr_to_details_2 = {"", ""};

            if (!TextUtils.isEmpty(appointment.getVisiting_hr_from())) {
                visiting_hr_from_details_1 = appointment.getVisiting_hr_from().split(Pattern.quote("|"));
            }

            if (!TextUtils.isEmpty(appointment.getVisiting_hr_to())) {
                visiting_hr_to_details_2 = appointment.getVisiting_hr_to().split(Pattern.quote("|"));
            }

            binding.twoOne.setText("");
            binding.toTwo.setText("");
            binding.fromOne.setText("");
            binding.fromTwo.setText("");

            if (visiting_hr_from_details_1.length > 1) {

                binding.twoOne.setText(visiting_hr_to_details_2[0].toLowerCase());
                binding.toTwo.setText(visiting_hr_to_details_2[1].toLowerCase());
                binding.fromOne.setText(visiting_hr_from_details_1[0].toLowerCase());
                binding.fromTwo.setText(visiting_hr_from_details_1[1].toLowerCase());

            } else {

                if (visiting_hr_to_details_2.length > 1) {
                    binding.fromOne.setText(visiting_hr_from_details_1[0].toLowerCase());
                    binding.twoOne.setText(visiting_hr_to_details_2[0].toLowerCase());
                }

            }

        }

        return binding.getRoot();
    }

    private void setWorkingdays(String working_days) {

        if (working_days != null) {
            String[] work = working_days.split(",");

            for (int k = 0; k < work.length; k++) {
                if (work[k].equalsIgnoreCase("Mon")) {
                    Mon.setChecked(true);
                }
                if (work[k].equalsIgnoreCase("Tue")) {
                    Tue.setChecked(true);
                }
                if (work[k].equalsIgnoreCase("Wed")) {
                    Wed.setChecked(true);
                }
                if (work[k].equalsIgnoreCase("Thu")) {
                    Thu.setChecked(true);
                }
                if (work[k].equalsIgnoreCase("Fri")) {
                    Fri.setChecked(true);
                }
                if (work[k].equalsIgnoreCase("Sat")) {
                    Sat.setChecked(true);
                }
                if (work[k].equalsIgnoreCase("Sun")) {
                    Sun.setChecked(true);
                }

            }

        }

    }

    public String getCheckDays() {

        String markedButtons = "";
        String notmarked = "";

        //Check individual items.
        if (Mon.isChecked()) {
            markedButtons += ",Mon";
        } else {
            notmarked += ",Mon";
        }
        if (Tue.isChecked()) {
            markedButtons += ",Tue";
        } else {
            notmarked += ",Tue";
        }
        if (Wed.isChecked()) {
            markedButtons += ",Wed";
        } else {
            notmarked += ",Wed";
        }
        if (Thu.isChecked()) {
            markedButtons += ",Thu";
        } else {
            notmarked += ",Thu";
        }
        if (Fri.isChecked()) {
            markedButtons += ",Fri";
        } else {
            notmarked += ",Fri";
        }
        if (Sat.isChecked()) {
            markedButtons += ",Sat";
        } else {
            notmarked += ",Sat";
        }
        if (Sun.isChecked()) {
            markedButtons += ",Sun";
        } else {
            notmarked += ",Sun";
        }

        if (!markedButtons.equalsIgnoreCase("")) {
            markedButtons = markedButtons.substring(1);
        }

        if (!notmarked.equalsIgnoreCase("")) {
            notmarked = notmarked.substring(1);
        }

        return markedButtons;

    }

    public void UploadClinicDetails() {

        appointment = manager.getObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment");

        if (Utils.isConnectingToInternet(fragmentActivity.getApplicationContext())) {

            binding.progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<AppointmentModel> call = request.addClinicDetails(
                    manager.getPreferences(fragmentActivity, "doctor_id"),
                    appointment.getDoc_name(),
                    appointment.getReg_no(),
                    binding.clinicName.getText().toString(),
                    getCheckDays(),
                    binding.currency.getText().toString(),
                    binding.clinicAddress.getText().toString(),
                    binding.fromOne.getText().toString() + "|" + binding.fromTwo.getText().toString(),
                    binding.twoOne.getText().toString() + "|" + binding.toTwo.getText().toString(),
                    binding.consultationCharges.getText().toString());

            call.enqueue(new Callback<AppointmentModel>() {
                @Override
                public void onResponse(@NonNull Call<AppointmentModel> call, @NonNull retrofit2.Response<AppointmentModel> response) {
                    AppointmentModel jsonResponse = response.body();
                    assert jsonResponse != null;
                    binding.progressBar.setVisibility(View.GONE);
                    if (jsonResponse.getSuccess().equalsIgnoreCase("success")) {

                        binding.progressBar.setVisibility(View.GONE);
                        manager.setObjectAppointmentDetails(fragmentActivity.getApplicationContext(), "appointment", jsonResponse);

                        if (isSave) {
                            startActivity(new Intent(fragmentActivity, DigitalClinicConfirmationActivity.class));
                            isSave = false;
                            fragmentActivity.finish();

                        }

                        ((DigitalClinicActivity) fragmentActivity).moveViewPager(2);


                    } else {
                        Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<AppointmentModel> call, @NonNull Throwable t) {
                    binding.progressBar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(fragmentActivity, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(fragmentActivity, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


}