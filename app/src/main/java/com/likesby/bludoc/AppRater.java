package com.likesby.bludoc;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppRater {
    private final static String APP_TITLE = "BluDoc";// App Name
    private final static String APP_PNAME = "com.likesby.bludoc";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    public static void app_launched(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        // editor.commit();
        editor.apply();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(30, 20, 30, 20);
        TextView tv = new TextView(mContext);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        tv.setWidth(720);
        tv.setTextSize(18.0f);
        tv.setPadding(15, 15, 15, 15);
        tv.setLayoutParams(params);
        tv.setTextAppearance(mContext,R.style.text_20);
        ll.addView(tv);



        Button b1 = new Button(mContext);
        b1.setText("Rate " + APP_TITLE);
        b1.setTextColor(mContext.getResources().getColor(R.color.white));
        b1.setBackground(mContext.getResources().getDrawable(R.drawable.blue_btn_gradient_new));
        b1.setPadding(10, 10, 15, 15);
        b1.setGravity(Gravity.CENTER);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });
        b1.setLayoutParams(params);
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setTextColor(mContext.getResources().getColor(R.color.white));
        b2.setBackground(mContext.getResources().getDrawable(R.drawable.blue_btn_gradient_new));
        b2.setPadding(10, 10, 15, 15);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        b2.setLayoutParams(params);
        b2.setGravity(Gravity.CENTER);
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setTextColor(mContext.getResources().getColor(R.color.white));
        b3.setBackground(mContext.getResources().getDrawable(R.drawable.blue_btn_gradient_new));
        b3.setPadding(10, 10, 15, 15);
        b3.setLayoutParams(params);
        b3.setGravity(Gravity.CENTER);
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);
        ll.setLayoutParams(params);


        dialog.setContentView(ll);
        dialog.show();
    }
}
