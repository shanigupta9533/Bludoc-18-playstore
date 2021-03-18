package com.likesby.bludoc.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by RajivLL on 06-Apr-18.
 */
public class MyProgressDialog {

    private static ProgressDialog progressDialog;

    public static void show(Context context, int messageResourceId) {
        if (progressDialog != null && progressDialog.isShowing() ) {
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(messageResourceId));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


    public static void show(Context context, int messageResourceId,boolean cancelable) {
        if (progressDialog != null && progressDialog.isShowing() ) {
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(messageResourceId));
        progressDialog.setCancelable(cancelable);
        progressDialog.show();

    }

    public static void dismiss() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}