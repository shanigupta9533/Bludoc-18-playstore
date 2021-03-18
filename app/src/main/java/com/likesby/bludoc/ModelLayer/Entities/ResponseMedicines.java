package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseMedicines{

	@SerializedName("medicines")
	private ArrayList<MedicinesItem> medicines;

	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

	public void setMedicines(ArrayList<MedicinesItem> medicines){
		this.medicines = medicines;
	}

	public ArrayList<MedicinesItem> getMedicines(){
		return medicines;
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
			"ResponseMedicines{" + 
			"medicines = '" + medicines + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}