package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserResponse{

	@SerializedName("success")
	private String success;

	@SerializedName("subscription_valid")
	private String subscriptionValid;

	@SerializedName("message")
	private String message;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@SerializedName("otp")
	private String otp;

	@SerializedName("user")
	private User user;

//	@SerializedName("subscription_history")
//	private ArrayList<SubscriptionHistoryItem> subscriptionHistory;

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setSubscriptionValid(String subscriptionValid){
		this.subscriptionValid = subscriptionValid;
	}

	public String getSubscriptionValid(){
		return subscriptionValid;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

//	public void setSubscriptionHistory(ArrayList<SubscriptionHistoryItem> subscriptionHistory){
//		this.subscriptionHistory = subscriptionHistory;
//	}
//
//	public ArrayList<SubscriptionHistoryItem> getSubscriptionHistory(){
//		return subscriptionHistory;
//	}

	@Override
	public String toString(){
		return
				"UserResponse{" +
						"success = '" + success + '\'' +
						",subscription_valid = '" + subscriptionValid + '\'' +
						",message = '" + message + '\'' +
						",user = '" + user + '\'' +
						",otp = '" + otp + '\'' +
						"}";
	}
}