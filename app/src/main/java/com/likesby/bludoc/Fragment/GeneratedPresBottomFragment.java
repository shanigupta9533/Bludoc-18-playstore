package com.likesby.bludoc.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.likesby.bludoc.Adapter.PharmacistAdapter;
import com.likesby.bludoc.databinding.FragmentGeneratedPresBottomBinding;
import com.likesby.bludoc.databinding.GeneratePrescriptionBinding;

public class GeneratedPresBottomFragment extends BottomSheetDialogFragment {

    private FragmentGeneratedPresBottomBinding binding;
    private onClickListener onClickListener;

    public GeneratedPresBottomFragment() {
        // Required empty public constructor
    }

    public interface onClickListener{

        void onChangeResult(int ids, boolean value);

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

        }

        binding.doctorDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                onClickListener.onChangeResult(1,isChecked);

            }
        });

        binding.hostiptalName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                onClickListener.onChangeResult(2,isChecked);

            }
        });

        binding.footerDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                onClickListener.onChangeResult(3,isChecked);

            }
        });

        binding.addPharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickListener.onChangeResult(4,false);

                dismiss();

            }
        });

        binding.sendToPharmacist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickListener.onChangeResult(5,false);

                dismiss();

            }
        });

        binding.language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickListener.onChangeResult(6,false);

                dismiss();

            }
        });


        return binding.getRoot();
    }
}