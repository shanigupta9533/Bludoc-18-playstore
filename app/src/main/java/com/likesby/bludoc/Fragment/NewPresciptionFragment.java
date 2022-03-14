package com.likesby.bludoc.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likesby.bludoc.R;

public class NewPresciptionFragment extends Fragment {

    public NewPresciptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        inflater.inflate(R.layout.fragment_new_presciption, container, false);

        return inflater.inflate(R.layout.fragment_new_presciption, container, false);
    }
}