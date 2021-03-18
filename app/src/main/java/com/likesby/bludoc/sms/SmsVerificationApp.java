package com.likesby.bludoc.sms;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.likesby.bludoc.NotificationOpenedHandler;
import com.facebook.FacebookSdk;
import com.onesignal.OneSignal;

public class SmsVerificationApp extends Application {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            FacebookSdk.sdkInitialize(getApplicationContext());
        }catch (Exception e)
        {
            Log.e("MyApplication Exception",""+e);
        }

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures();
    }
}
