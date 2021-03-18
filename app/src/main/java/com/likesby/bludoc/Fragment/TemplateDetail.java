package com.likesby.bludoc.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.likesby.bludoc.Adapter.AddTemplateAdapter;
import com.likesby.bludoc.Adapter.TemplateAdapter;
import com.likesby.bludoc.Adapter.TemplateDetailAdapter;
import com.likesby.bludoc.R;

public class TemplateDetail  extends Fragment {
    Context mContext;
    Dialog dialog;
    View v;
    static RecyclerView rView;
    static LinearLayoutManager lLayout;
    ImageView back;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater.inflate(R.layout.state_frag, container, false);
        v = inflater.inflate(R.layout.template_detail, container, false);
        initCalls(v);
        return v;
    }

    private void initCalls(View view) {

        lLayout = new LinearLayoutManager(mContext);
        rView = (RecyclerView) view.findViewById(R.id.template_detail);
        rView.setLayoutManager(lLayout);
        initViews(view);

        back = view.findViewById(R.id.btn_back_edit_profile);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
    }

    private void initViews(View view) {
        TemplateDetailAdapter templateDetailAdapter = new TemplateDetailAdapter(mContext);
        rView.setAdapter(templateDetailAdapter);
    }
}
