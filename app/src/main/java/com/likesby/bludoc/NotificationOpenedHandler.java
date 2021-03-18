package com.likesby.bludoc;

import android.content.Intent;
import android.util.Log;

import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
    // This fires when a notification is opened by tapping on it.
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        String bodyy="";
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;

        bodyy = result.notification.payload.body;

        String customKey;
        Log.e("OSNotificationPayload", "result.notification.payload.toJSONObject().toString(): " + result.notification.payload.toJSONObject().toString());

        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.e("OneSignalExample", "customkey set with value: " + customKey);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.e("OneSignalExample", "Button pressed with id: " + result.action.actionID);



        if (actionType == OSNotificationAction.ActionType.ActionTaken)
            Log.e("OneSignalExample", "Button pressed with id: " + result.action.actionID);
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK   );

        /*intent.putExtra("OneSignal",""+bodyy);
        assert data != null;
        Log.e("OneSignal Tracksido", "Type: " + data.get("n_type"));
        intent.putExtra("tracking_id",""+data.get("tracking_id"));
        intent.putExtra("customer_id",""+data.get("customer_id"));
        intent.putExtra("parent_id",""+data.get("parent_id"));
        intent.putExtra("category_id",""+data.get("category_id"));*/

        getApplicationContext().startActivity(intent);

              /*  Picasso.with(getApplicationContext()).
                        load(R.drawable.ic_km_notification_icon_active)
                        .placeholder(R.drawable.ic_km_notification_icon_active)
                        .into(btn_order_count, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });*/

        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.

        /*try {if(data.get("n_type").equals("Invite"))
        {

         Intent intent = new Intent(getApplicationContext(), MainActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK   );

            intent.putExtra("OneSignal",""+bodyy);
            assert data != null;
            Log.e("OneSignal Tracksido", "Type: " + data.get("n_type"));
            intent.putExtra("tracking_id",""+data.get("tracking_id"));
            intent.putExtra("customer_id",""+data.get("customer_id"));
            intent.putExtra("parent_id",""+data.get("parent_id"));
            intent.putExtra("category_id",""+data.get("category_id"));

            getApplicationContext().startActivity(intent);

        }
        else
        {

        }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }


//        // The following can be used to open an Activity of your choice.
//        // Replace - getApplicationContext() - with any Android Context.
//
//        /*try {if(data.get("n_type").equals("Invite"))
//        {
//
//         Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//         intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TASK   );
//
//            intent.putExtra("OneSignal",""+bodyy);
//            assert data != null;
//            Log.e("OneSignal Tracksido", "Type: " + data.get("n_type"));
//            intent.putExtra("tracking_id",""+data.get("tracking_id"));
//            intent.putExtra("customer_id",""+data.get("customer_id"));
//            intent.putExtra("parent_id",""+data.get("parent_id"));
//            intent.putExtra("category_id",""+data.get("category_id"));
//
//            getApplicationContext().startActivity(intent);
//
//        }
//        else
//        {
//
//        }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }*/
//
}