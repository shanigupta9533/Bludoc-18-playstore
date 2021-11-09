package com.likesby.bludoc.ModelLayer.NewEntities;

import com.google.gson.annotations.SerializedName;

public class SubscriptionsItem{

	@SerializedName("subscription_id")
	private String subscriptionId;

	@SerializedName("sell")
	private String sell;

	@SerializedName("subscription_description")
	private String subscriptionDescription;

	@SerializedName("subscription_name")
	private String subscriptionName;

	@SerializedName("days")
	private String days;

	@SerializedName("mrp")
	private String mrp;

	@SerializedName("type")
	private String type;

	@SerializedName("payment_type")
	private String payment_type;

	public void setSubscriptionId(String subscriptionId){
		this.subscriptionId = subscriptionId;
	}

	public String getSubscriptionId(){
		return subscriptionId;
	}

	public void setSell(String sell){
		this.sell = sell;
	}

	public String getSell(){
		return sell;
	}

	public void setSubscriptionDescription(String subscriptionDescription){
		this.subscriptionDescription = subscriptionDescription;
	}

	public String getSubscriptionDescription(){
		return subscriptionDescription;
	}

	public void setSubscriptionName(String subscriptionName){
		this.subscriptionName = subscriptionName;
	}

	public String getSubscriptionName(){
		return subscriptionName;
	}

	public void setDays(String days){
		this.days = days;
	}

	public String getDays(){
		return days;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public void setMrp(String mrp){
		this.mrp = mrp;
	}

	public String getMrp(){
		return mrp;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	@Override
	public String toString(){
		return
				"SubscriptionsItem{" +
						"subscription_id = '" + subscriptionId + '\'' +
						",sell = '" + sell + '\'' +
						",subscription_description = '" + subscriptionDescription + '\'' +
						",subscription_name = '" + subscriptionName + '\'' +
						",days = '" + days + '\'' +
						",mrp = '" + mrp + '\'' +
						",type = '" + type + '\'' +
						",paymentType = '" + payment_type + '\'' +
						"}";
	}
}