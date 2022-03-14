package com.likesby.bludoc.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likesby.bludoc.Adapter.ViewPagerFragmentAdapter;
import com.likesby.bludoc.R;
import com.likesby.bludoc.databinding.FragmentHistorySubParentBinding;

public class HistorySubParentFragment extends Fragment {

    private FragmentHistorySubParentBinding binding;

    public HistorySubParentFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHistorySubParentBinding.inflate(inflater, container, false);

        ViewPagerFragmentAdapter viewPagerFragmentAdapter=new ViewPagerFragmentAdapter(getChildFragmentManager(),0);
        viewPagerFragmentAdapter.setFragment(new History(),"History");
        viewPagerFragmentAdapter.setFragment(new InvoiceHistoryFragment(),"Invoice History");
        viewPagerFragmentAdapter.setFragment(new ConsentPatientHistoryFragment(),"Consent History");
        binding.viewPager.setAdapter(viewPagerFragmentAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }
}