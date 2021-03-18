package com.likesby.bludoc.ModelLayer.NewEntities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseSubscriptions{

	@SerializedName("Subscriptions")
	private ArrayList<SubscriptionsItem> subscriptions;

	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

	public void setSubscriptions(ArrayList<SubscriptionsItem> subscriptions){
		this.subscriptions = subscriptions;
	}

	public ArrayList<SubscriptionsItem> getSubscriptions(){
		return subscriptions;
	}

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ResponseSubscriptions{" + 
			"subscriptions = '" + subscriptions + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}