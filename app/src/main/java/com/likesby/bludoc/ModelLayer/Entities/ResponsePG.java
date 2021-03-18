package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponsePG{

	@SerializedName("success")
	private String success;

	@SerializedName("PG")
	private ArrayList<PGItem> pG;

	@SerializedName("message")
	private String message;

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setPG(ArrayList<PGItem> pG){
		this.pG = pG;
	}

	public ArrayList<PGItem> getPG(){
		return pG;
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
			"ResponsePG{" + 
			"success = '" + success + '\'' + 
			",pG = '" + pG + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}