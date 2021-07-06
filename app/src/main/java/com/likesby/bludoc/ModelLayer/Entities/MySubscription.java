
package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MySubscription {

    @SerializedName("doctor_subscription_id")
    @Expose
    private String doctorSubscriptionId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("hospital_code")
    @Expose
    private String hospitalCode;

    @SerializedName("doctor_id")
    @Expose
    private String doctorId;
    @SerializedName("subscription_id")
    @Expose
    private String subscriptionId;
    @SerializedName("subscription_name")
    @Expose
    private String subscriptionName;
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("days")
    @Expose
    private String days;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;

    public String getDoctorSubscriptionId() {
        return doctorSubscriptionId;
    }

    public void setDoctorSubscriptionId(String doctorSubscriptionId) {
        this.doctorSubscriptionId = doctorSubscriptionId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
