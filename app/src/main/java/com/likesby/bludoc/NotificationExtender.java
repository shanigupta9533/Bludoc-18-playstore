package com.likesby.bludoc;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import java.math.BigInteger;

public class NotificationExtender extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Green on Android 5.0+ devices.
                return builder.setColor(new BigInteger("219108", 16).intValue());
            }
        };

        /* Do something with notification payload */
        String title = notification.payload.title;
        String body  = notification.payload.body;
        String additionalData = notification.payload.additionalData.toString();
        Log.e("OneSignal", "title = "+title);
        Log.e("OneSignal", "body = "+body);
        Log.e("OneSignal", "title = "+additionalData);

        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Log.e("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }
}
