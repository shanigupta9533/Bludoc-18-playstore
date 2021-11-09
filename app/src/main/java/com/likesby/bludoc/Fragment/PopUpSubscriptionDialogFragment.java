package com.likesby.bludoc.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likesby.bludoc.R;
import com.likesby.bludoc.SubscriptionPackages;
import com.likesby.bludoc.databinding.FragmentPopUpSubscriptionBinding;

public class PopUpSubscriptionDialogFragment extends DialogFragment {

    private FragmentPopUpSubscriptionBinding binding;
    private String keywords;

    public PopUpSubscriptionDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPopUpSubscriptionBinding.inflate(inflater, container, false);

        Bundle arguments = getArguments();

        if(arguments!=null){

            keywords = arguments.getString("keywords", "");
            binding.nameOfProduct.setText(keywords);

        }

        binding.btnUpgradePremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), SubscriptionPackages.class));

            }
        });

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

            }
        });

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.round_dialog_min);
        }

        return binding.getRoot();
    }
}