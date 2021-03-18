package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseDesignation{

	@SerializedName("Designation")
	private ArrayList<DesignationItem> designation;

	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

	public void setDesignation(ArrayList<DesignationItem> designation){
		this.designation = designation;
	}

	public ArrayList<DesignationItem> getDesignation(){
		return designation;
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
			"ResponseDesignation{" + 
			"designation = '" + designation + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}