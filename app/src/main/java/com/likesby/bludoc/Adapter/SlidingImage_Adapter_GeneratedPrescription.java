package com.likesby.bludoc.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.likesby.bludoc.ModelLayer.Entities.BannersItem;
import com.likesby.bludoc.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlidingImage_Adapter_GeneratedPrescription extends PagerAdapter {

    private ArrayList<Uri> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public SlidingImage_Adapter_GeneratedPrescription(Context context, ArrayList<Uri> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout2, view, false);

        assert imageLayout != null;
        final ImageView imageView = imageLayout.findViewById(R.id.image);
       // Bitmap myBitmap = BitmapFactory.decodeFile(IMAGES.get(position).getPath());
        Picasso.with(context).
                load(IMAGES.get(position))
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                    }
                });

        imageLayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this will log the page number that was click
            }
        });

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}