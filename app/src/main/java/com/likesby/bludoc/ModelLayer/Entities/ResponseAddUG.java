package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class ResponseAddUG{

	@SerializedName("ug_name")
	private String ugName;

	@SerializedName("success")
	private String success;

	@SerializedName("ug_id")
	private int ugId;

	@SerializedName("message")
	private String message;

	public void setUgName(String ugName){
		this.ugName = ugName;
	}

	public String getUgName(){
		return ugName;
	}

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setUgId(int ugId){
		this.ugId = ugId;
	}

	public int getUgId(){
		return ugId;
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
			"ResponseAddUG{" + 
			"ug_name = '" + ugName + '\'' + 
			",success = '" + success + '\'' + 
			",ug_id = '" + ugId + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}