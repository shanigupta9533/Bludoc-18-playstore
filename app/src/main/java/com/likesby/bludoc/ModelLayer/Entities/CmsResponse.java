package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CmsResponse{

	@SerializedName("success")
	private String success;

	@SerializedName("cms")
	private ArrayList<CmsItem> cms;

	@SerializedName("message")
	private String message;

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setCms(ArrayList<CmsItem> cms){
		this.cms = cms;
	}

	public ArrayList<CmsItem> getCms(){
		return cms;
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
			"CmsResponse{" + 
			"success = '" + success + '\'' + 
			",cms = '" + cms + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}