package com.likesby.bludoc.ModelLayer.NewEntities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MedicinesItem implements Parcelable {

	@SerializedName("route")
	private String route;

	@SerializedName("instruction")
	private String instruction;

	@SerializedName("no_of_days")
	private String noOfDays;

	@SerializedName("additiona_comment")
	private String additionaComment;

	@SerializedName("template_id")
	private String templateId;

	@SerializedName("medicine_name")
	private String medicineName;

	@SerializedName("temp_medicine_id")
	private String tempMedicineId;

	@SerializedName("frequency")
	private String frequency;

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

	public void setTemplateId(String templateId){
		this.templateId = templateId;
	}

	public String getTemplateId(){
		return templateId;
	}

	public void setMedicineName(String medicineName){
		this.medicineName = medicineName;
	}

	public String getMedicineName(){
		return medicineName;
	}

	public void setTempMedicineId(String tempMedicineId){
		this.tempMedicineId = tempMedicineId;
	}

	public String getTempMedicineId(){
		return tempMedicineId;
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
			"route = '" + route + '\'' + 
			",instruction = '" + instruction + '\'' + 
			",no_of_days = '" + noOfDays + '\'' + 
			",additiona_comment = '" + additionaComment + '\'' + 
			",template_id = '" + templateId + '\'' + 
			",medicine_name = '" + medicineName + '\'' + 
			",temp_medicine_id = '" + tempMedicineId + '\'' + 
			",frequency = '" + frequency + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.route);
		dest.writeString(this.instruction);
		dest.writeString(this.noOfDays);
		dest.writeString(this.additionaComment);
		dest.writeString(this.templateId);
		dest.writeString(this.medicineName);
		dest.writeString(this.tempMedicineId);
		dest.writeString(this.frequency);
	}

	public MedicinesItem() {
	}

	protected MedicinesItem(Parcel in) {
		this.route = in.readString();
		this.instruction = in.readString();
		this.noOfDays = in.readString();
		this.additionaComment = in.readString();
		this.templateId = in.readString();
		this.medicineName = in.readString();
		this.tempMedicineId = in.readString();
		this.frequency = in.readString();
	}

	public static final Parcelable.Creator<MedicinesItem> CREATOR = new Parcelable.Creator<MedicinesItem>() {
		@Override
		public MedicinesItem createFromParcel(Parcel source) {
			return new MedicinesItem(source);
		}

		@Override
		public MedicinesItem[] newArray(int size) {
			return new MedicinesItem[size];
		}
	};
}