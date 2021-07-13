
package com.likesby.bludoc.ModelLayer.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscription implements Parcelable {

    @SerializedName("My Subscriptions")
    @Expose
    private ArrayList<MySubscription> mySubscriptions = null;
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;

    protected Subscription(Parcel in) {
        success = in.readString();
        message = in.readString();
    }

    public static final Creator<Subscription> CREATOR = new Creator<Subscription>() {
        @Override
        public Subscription createFromParcel(Parcel in) {
            return new Subscription(in);
        }

        @Override
        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };

    public ArrayList<MySubscription> getMySubscriptions() {
        return mySubscriptions;
    }

    public void setMySubscriptions(ArrayList<MySubscription> mySubscriptions) {
        this.mySubscriptions = mySubscriptions;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(success);
        dest.writeString(message);
    }
}
