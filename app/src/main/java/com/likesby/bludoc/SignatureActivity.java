package com.likesby.bludoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class SignatureActivity extends AppCompatActivity {

    private FrameLayout progressBarInclude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        progressBarInclude = findViewById(R.id.fl_progress_layout);

        progressBarInclude.setVisibility(View.VISIBLE);

        String signature = getIntent().getStringExtra("signature");
        ImageView imageView = findViewById(R.id.image);
        Glide.with(this).asBitmap().load(signature).addListener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                progressBarInclude.setVisibility(View.GONE);

                return false;
            }
        }).into(imageView);

    }
}