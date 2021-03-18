package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.likesby.bludoc.ModelLayer.NewEntities2.LabTestItem;

public class History{

	@SerializedName("Prescription")
	private ArrayList<PrescriptionItem> prescription;


	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

	public void setPrescription(ArrayList<PrescriptionItem> prescription){
		this.prescription = prescription;
	}

	public ArrayList<PrescriptionItem> getPrescription(){
		return prescription;
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
			"History{" + 
			"prescription = '" + prescription + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}