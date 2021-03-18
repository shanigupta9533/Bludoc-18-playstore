package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class MedicinesItem{

	@SerializedName("medicine_id ")
	private String medicineId;

	@SerializedName("route")
	private String route;

	@SerializedName("instruction")
	private String instruction;

	@SerializedName("created")
	private String created;

	@SerializedName("no_of_days")
	private String noOfDays;

	@SerializedName("additiona_comment")
	private String additionaComment;

	@SerializedName("modified")
	private String modified;

	@SerializedName("medicine_name")
	private String medicineName;

	@SerializedName("created_by")
	private String createdBy;

	@SerializedName("frequency")
	private String frequency;

	public void setMedicineId(String medicineId){
		this.medicineId = medicineId;
	}

	public String getMedicineId(){
		return medicineId;
	}

	public void setRoute(String route){
		this.route = route;
	}

	public String getRoute(){
		return route;
	}

	public void setInstruction(String instruction){
		this.instruction = instruction;
	}

	public String getInstruction(){
		return instruction;
	}

	public void setCreated(String created){
		this.created = created;
	}

	public String getCreated(){
		return created;
	}

	public void setNoOfDays(String noOfDays){
		this.noOfDays = noOfDays;
	}

	public String getNoOfDays(){
		return noOfDays;
	}

	public void setAdditionaComment(String additionaComment){
		this.additionaComment = additionaComment;
	}

	public String getAdditionaComment(){
		return additionaComment;
	}

	public void setModified(String modified){
		this.modified = modified;
	}

	public String getModified(){
		return modified;
	}

	public void setMedicineName(String medicineName){
		this.medicineName = medicineName;
	}

	public String getMedicineName(){
		return medicineName;
	}

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public void setFrequency(String frequency){
		this.frequency = frequency;
	}

	public String getFrequency(){
		return frequency;
	}

	@Override
 	public String toString(){
		return 
			"MedicinesItem{" + 
			"medicine_id  = '" + medicineId + '\'' + 
			",route = '" + route + '\'' + 
			",instruction = '" + instruction + '\'' + 
			",created = '" + created + '\'' + 
			",no_of_days = '" + noOfDays + '\'' + 
			",additiona_comment = '" + additionaComment + '\'' + 
			",modified = '" + modified + '\'' + 
			",medicine_name = '" + medicineName + '\'' + 
			",created_by = '" + createdBy + '\'' + 
			",frequency = '" + frequency + '\'' + 
			"}";
		}
}