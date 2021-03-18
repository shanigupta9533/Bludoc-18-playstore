package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FaqsResponse{

	@SerializedName("faqs")
	private ArrayList<FaqsItem> faqs;

	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

	public void setFaqs(ArrayList<FaqsItem> faqs){
		this.faqs = faqs;
	}

	public ArrayList<FaqsItem> getFaqs(){
		return faqs;
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
			"FaqsResponse{" + 
			"faqs = '" + faqs + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}