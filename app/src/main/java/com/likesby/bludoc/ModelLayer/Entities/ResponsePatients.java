package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponsePatients{

	@SerializedName("success")
	private String success;

	@SerializedName("patients")
	private ArrayList<PatientsItem> patients;

	@SerializedName("message")
	private String message;

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setPatients(ArrayList<PatientsItem> patients){
		this.patients = patients;
	}

	public ArrayList<PatientsItem> getPatients(){
		return patients;
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
			"ResponsePatients{" + 
			"success = '" + success + '\'' + 
			",patients = '" + patients + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}