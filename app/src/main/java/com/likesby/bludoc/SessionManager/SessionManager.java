package com.likesby.bludoc.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities.SubscriptionsItem;

import java.util.ArrayList;


public class SessionManager
{

    public void setPreferences(Context context, String key, String value)
    {

        SharedPreferences.Editor editor = context.getSharedPreferences("status", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public  String getPreferences(Context context, String key)
    {

        SharedPreferences prefs = context.getSharedPreferences("status",	Context.MODE_PRIVATE);
        return prefs.getString(key, "");



    }


    public void setObjectProfileDetails(Context context, String key, ResponseProfileDetails profileDetails)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("status", Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(profileDetails);
        editor.putString(key, json);
        editor.apply();
    }


    public ResponseProfileDetails getObjectProfileDetails(Context context, String key)
    {

        SharedPreferences prefs = context.getSharedPreferences("status",	Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, "");
        return gson.fromJson(json, ResponseProfileDetails.class);
    }



    public  void deletePreferences(Context context, String key)
    {

        SharedPreferences prefs = context.getSharedPreferences("status",	Context.MODE_PRIVATE);
        prefs.edit().remove(key).apply();

    }

    public boolean contains(Context context, String key)
    {
        SharedPreferences prefs = context.getSharedPreferences("status",	Context.MODE_PRIVATE);
        return prefs.contains(key);
    }
}