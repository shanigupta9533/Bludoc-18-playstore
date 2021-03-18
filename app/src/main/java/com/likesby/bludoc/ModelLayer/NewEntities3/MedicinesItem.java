package com.likesby.bludoc.ModelLayer.NewEntities3;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MedicinesItem implements Parcelable {

	@SerializedName("presb_medicine_id")
	private String presbMedicineId;

	@SerializedName("route")
	private String route;

	@SerializedName("presb_patient_id")
	private String presbPatientId;

	@SerializedName("instruction")
	private String instruction;

	@SerializedName("no_of_days")
	private String noOfDays;

	@SerializedName("additiona_comment")
	private String additionaComment;

	@SerializedName("medicine_name")
	private String medicineName;

	@SerializedName("frequency")
	private String frequency;

	public void setPresbMedicineId(String presbMedicineId){
		this.presbMedicineId = presbMedicineId;
	}

	public String getPresbMedicineId(){
		return presbMedicineId;
	}

	public void setRoute(String route){
		this.route = route;
	}

	public String getRoute(){
		return route;
	}

	public void setPresbPatientId(String presbPatientId){
		this.presbPatientId = presbPatientId;
	}

	public String getPresbPatientId(){
		return presbPatientId;
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

	public void setMedicineName(String medicineName){
		this.medicineName = medicineName;
	}

	public String getMedicineName(){
		return medicineName;
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
			"presb_medicine_id = '" + presbMedicineId + '\'' + 
			",route = '" + route + '\'' + 
			",presb_patient_id = '" + presbPatientId + '\'' + 
			",instruction = '" + instruction + '\'' + 
			",no_of_days = '" + noOfDays + '\'' + 
			",additiona_comment = '" + additionaComment + '\'' + 
			",medicine_name = '" + medicineName + '\'' + 
			",frequency = '" + frequency + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.presbMedicineId);
		dest.writeString(this.route);
		dest.writeString(this.presbPatientId);
		dest.writeString(this.instruction);
		dest.writeString(this.noOfDays);
		dest.writeString(this.additionaComment);
		dest.writeString(this.medicineName);
		dest.writeString(this.frequency);
	}

	public MedicinesItem() {
	}

	protected MedicinesItem(Parcel in) {
		this.presbMedicineId = in.readString();
		this.route = in.readString();
		this.presbPatientId = in.readString();
		this.instruction = in.readString();
		this.noOfDays = in.readString();
		this.additionaComment = in.readString();
		this.medicineName = in.readString();
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