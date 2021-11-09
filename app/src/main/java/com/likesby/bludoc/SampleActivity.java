package com.likesby.bludoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.likesby.bludoc.Adapter.SampleAdapter;
import com.likesby.bludoc.Adapter.ViewPagerImageCarouselAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class SampleActivity extends AppCompatActivity {

    private RecyclerView viewPager;
    private ArrayList<Integer> imageLists=new ArrayList<>();
    private ArrayList<String> nameList=new ArrayList<>();
    private TextView header;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        imageLists.add(R.drawable.pres_image_screenshot);
        imageLists.add(R.drawable.medical_certificates);
        imageLists.add(R.drawable.invoice_screenshots);

        nameList.add("Prescription");
        nameList.add("Certificates");
        nameList.add("Invoice");

        findViewById(R.id.btn_back_edit_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        viewPager=findViewById(R.id.viewpager);
        header =findViewById(R.id.header);
        SampleAdapter viewPagerImageCarouselAdapter=new SampleAdapter(this,imageLists);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        viewPager.setLayoutManager(linearLayoutManager);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(viewPager);
        viewPager.setAdapter(viewPagerImageCarouselAdapter);

        ScrollingPagerIndicator scrollingPagerIndicator=findViewById(R.id.indicatorCondition);
        scrollingPagerIndicator.attachToRecyclerView(viewPager);
        scrollingPagerIndicator.setDotCount(0);
        scrollingPagerIndicator.setDotColor(Color.parseColor("#dcdcdc"));
        scrollingPagerIndicator.setSelectedDotColor(ContextCompat.getColor(this,R.color.dark_blue_invoice));
        scrollingPagerIndicator.setVisibleDotCount(7);

        viewPager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {

                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                String name = nameList.get(firstVisibleItemPosition);
                header.setText(name);

            }
        });

    }

}