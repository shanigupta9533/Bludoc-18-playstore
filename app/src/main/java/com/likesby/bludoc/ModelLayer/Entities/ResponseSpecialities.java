package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseSpecialities{

	@SerializedName("success")
	private String success;

	@SerializedName("Specialities")
	private ArrayList<SpecialitiesItem> specialities;

	@SerializedName("message")
	private String message;

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setSpecialities(ArrayList<SpecialitiesItem> specialities){
		this.specialities = specialities;
	}

	public ArrayList<SpecialitiesItem> getSpecialities(){
		return specialities;
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
			"ResponseSpecialities{" + 
			"success = '" + success + '\'' + 
			",specialities = '" + specialities + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}