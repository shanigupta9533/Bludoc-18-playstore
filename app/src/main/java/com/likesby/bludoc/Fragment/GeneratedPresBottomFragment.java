package com.likesby.bludoc.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.likesby.bludoc.R;
import com.likesby.bludoc.databinding.FragmentGeneratedPresBottomBinding;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormatSymbols;
import java.util.Calendar;

public class GeneratedPresBottomFragment extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener {

    private FragmentGeneratedPresBottomBinding binding;
    private onClickListener onClickListener;
    private boolean isClinicFound;
    private FragmentActivity fragmentActivity;

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

                if(onClickListener!=null)
                onClickListener.onChangeResult(1,isChecked);

            }
        });

        binding.dateOfCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Calendar myCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(fragmentActivity, R.style.DialogTheme, GeneratedPresBottomFragment.this,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();
            }
        });

        binding.hostiptalName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(onClickListener!=null)
                onClickListener.onChangeResult(2,isChecked);

            }
        });

        binding.footerDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(onClickListener!=null)
                    onClickListener.onChangeResult(3,isChecked);

            }
        });

        binding.addPharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onClickListener!=null)
                    onClickListener.onChangeResult(4,false);

                dismiss();

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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if(onClickListener!=null)
            onClickListener.onChooseCalender(year+"-"+(month+1)+"-"+dayOfMonth, dayOfMonth+"-"+getMonthForInt(month)+"-"+year);

        dismiss();

    }

}