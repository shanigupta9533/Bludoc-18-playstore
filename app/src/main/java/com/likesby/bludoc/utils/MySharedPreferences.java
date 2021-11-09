package com.likesby.bludoc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class MySharedPreferences {

    private static final String KEY_PREFERENCE_NAME = "MY BLUEDOC";
    public static final String name="name";
    public static final String registrationNumber="registrationNumber";
    public static final String qualification="qualification";
    public static final String contactNumber="contactNumber";
    public static final String mailId="mailId";
    public static final String clinicName="clinicName";
    public static final String clinicAddress="clinicAddress";
    public static final String consultationCharges="consultationCharges";

    //instance field

    private static SharedPreferences mSharedPreference;
    private static MySharedPreferences mInstance = null;
    private static Context mContext;
    private final SharedPreferences.Editor editor;

    public MySharedPreferences() {
        mSharedPreference = mContext.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreference.edit();
    }

    public static void removeValueFromKey(String keyname) {
        mSharedPreference.edit().remove(keyname).apply();
    }

    public static void removeAllData() {

        SharedPreferences preferences = mContext.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();
        SharedPreferences.Editor editor = preferences.edit();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            editor.remove(entry.getKey());
        }

        editor.apply();

    }

    public static MySharedPreferences getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new MySharedPreferences();
        }
        return mInstance;
    }

    public void clearAll() {

        editor.clear();
        editor.apply();

    }

    //Method to set boolean for (AppIntro)
    public void setBooleanKey(String keyname) {
        mSharedPreference.edit().putBoolean(keyname, true).apply();
    }

    public void setBooleanKey(String keyname, boolean state) {
        mSharedPreference.edit().putBoolean(keyname, state).apply();
    }

    /*
     * Method to get boolan key
     * true = means set
     * false = not set (show app intro)
     * */
    public boolean getBooleanKey(String keyname) {
        return mSharedPreference.getBoolean(keyname, false);
    }

    public void setStringKey(String keyname, String state) {
        mSharedPreference.edit().putString(keyname,state).apply();
    }

    public void setLongKey(String keyname, long state) {
        mSharedPreference.edit().putLong(keyname,state).apply();
    }

    public long getLongKey(String keyname) {
        return mSharedPreference.getLong(keyname,0);
    }


    /*
     * Method to get boolan key
     * true = means set
     * false = not set (show app intro)
     * */
    public String getStringkey(String keyname) {
        return mSharedPreference.getString(keyname, "");
    }

    public void setIntKey(String keyname, int state) {
        mSharedPreference.edit().putInt(keyname,state).apply();
    }

    /*
     * Method to get boolan key
     * true = means set
     * false = not set (show app intro)
     * */
    public int getIntkey(String keyname) {
        return mSharedPreference.getInt(keyname, 0);
    }

}
