
package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscription {

    @SerializedName("My Subscriptions")
    @Expose
    private ArrayList<MySubscription> mySubscriptions = null;
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;

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

}
