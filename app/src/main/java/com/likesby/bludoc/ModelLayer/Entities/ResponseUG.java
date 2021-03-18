package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseUG{

	@SerializedName("UG")
	private ArrayList<UGItem> uG;

	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

	public void setUG(ArrayList<UGItem> uG){
		this.uG = uG;
	}

	public ArrayList<UGItem> getUG(){
		return uG;
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
			"ResponseUG{" + 
			"uG = '" + uG + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}