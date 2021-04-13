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

	@SerializedName("qty")
	private String qty;

	@SerializedName("medicine_name")
	private String medicineName;

	@SerializedName("frequency")
	private String frequency;

	protected MedicinesItem(Parcel in) {
		presbMedicineId = in.readString();
		route = in.readString();
		presbPatientId = in.readString();
		instruction = in.readString();
		noOfDays = in.readString();
		additionaComment = in.readString();
		qty = in.readString();
		medicineName = in.readString();
		frequency = in.readString();
	}

	public static final Creator<MedicinesItem> CREATOR = new Creator<MedicinesItem>() {
		@Override
		public MedicinesItem createFromParcel(Parcel in) {
			return new MedicinesItem(in);
		}

		@Override
		public MedicinesItem[] newArray(int size) {
			return new MedicinesItem[size];
		}
	};

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

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
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
			",qty = '" + qty + '\'' +
			"}";
		}

	public MedicinesItem() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(presbMedicineId);
		dest.writeString(route);
		dest.writeString(presbPatientId);
		dest.writeString(instruction);
		dest.writeString(noOfDays);
		dest.writeString(additionaComment);
		dest.writeString(qty);
		dest.writeString(medicineName);
		dest.writeString(frequency);
	}
}