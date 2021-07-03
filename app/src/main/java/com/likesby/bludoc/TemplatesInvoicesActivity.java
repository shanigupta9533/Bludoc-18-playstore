package com.likesby.bludoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.likesby.bludoc.Adapter.InvoicesAdapter;
import com.likesby.bludoc.Adapter.InvoicesTemplateAdapter;
import com.likesby.bludoc.databinding.ActivityTemplatesInvoicesBinding;

import java.util.ArrayList;

public class TemplatesInvoicesActivity extends AppCompatActivity {

    private ActivityTemplatesInvoicesBinding activity;
    private LinearLayoutManager linearLayoutManager;
    private InvoicesTemplateAdapter invoicesTemplateAdapter;
    private ArrayList<String> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity= DataBindingUtil.setContentView(this,R.layout.activity_templates_invoices);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activity.recyclerview.setLayoutManager(linearLayoutManager);
        invoicesTemplateAdapter=new InvoicesTemplateAdapter(this, arrayList);
        activity.recyclerview.setAdapter(invoicesTemplateAdapter);

        invoicesTemplateAdapter.setOnClickListener(new InvoicesTemplateAdapter.OnClickListener() {
            @Override
            public void onEdit(int position) {

                Intent intent=new Intent(TemplatesInvoicesActivity.this,InvoiceActivity.class);
                startActivity(intent);

            }

            @Override
            public void onDelete(int position) {

            }

            @Override
            public void onAdd(int position) {

            }
        });

    }
}