package com.likesby.bludoc.ModelLayer.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class PrescriptionItem implements Parcelable {

	@SerializedName("date")
	private String date;

	@SerializedName("doctor_id")
	private String doctorId;

	@SerializedName("medicines")
	private ArrayList<MedicinesItem> medicines;

	@SerializedName("presb_patient_id")
	private String presbPatientId;

	@SerializedName("gender")
	private String gender;

	@SerializedName("p_email")
	private String pEmail;

	@SerializedName("patient_id")
	private String patientId;

	@SerializedName("p_name")
	private String pName;

	@SerializedName("age")
	private String age;

	@SerializedName("p_mobile")
	private String pMobile;

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	@SerializedName("diagnosis")
	private String diagnosis;

	public PrescriptionItem(Parcel in) {
		date = in.readString();
		doctorId = in.readString();
		presbPatientId = in.readString();
		gender = in.readString();
		pEmail = in.readString();
		patientId = in.readString();
		pName = in.readString();
		age = in.readString();
		pMobile = in.readString();
		diagnosis = in.readString();
	}

	public static final Creator<PrescriptionItem> CREATOR = new Creator<PrescriptionItem>() {
		@Override
		public PrescriptionItem createFromParcel(Parcel in) {
			return new PrescriptionItem(in);
		}

		@Override
		public PrescriptionItem[] newArray(int size) {
			return new PrescriptionItem[size];
		}
	};

	public PrescriptionItem() {

	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setDoctorId(String doctorId){
		this.doctorId = doctorId;
	}

	public String getDoctorId(){
		return doctorId;
	}

	public void setMedicines(ArrayList<MedicinesItem> medicines){
		this.medicines = medicines;
	}

	public ArrayList<MedicinesItem> getMedicines(){
		return medicines;
	}

	public void setPresbPatientId(String presbPatientId){
		this.presbPatientId = presbPatientId;
	}

	public String getPresbPatientId(){
		return presbPatientId;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setPEmail(String pEmail){
		this.pEmail = pEmail;
	}

	public String getPEmail(){
		return pEmail;
	}

	public void setPatientId(String patientId){
		this.patientId = patientId;
	}

	public String getPatientId(){
		return patientId;
	}

	public void setPName(String pName){
		this.pName = pName;
	}

	public String getPName(){
		return pName;
	}

	public void setAge(String age){
		this.age = age;
	}

	public String getAge(){
		return age;
	}

	public void setPMobile(String pMobile){
		this.pMobile = pMobile;
	}

	public String getPMobile(){
		return pMobile;
	}

	@Override
 	public String toString(){
		return 
			"PrescriptionItem{" + 
			"date = '" + date + '\'' + 
			",doctor_id = '" + doctorId + '\'' + 
			",medicines = '" + medicines + '\'' + 
			",presb_patient_id = '" + presbPatientId + '\'' + 
			",gender = '" + gender + '\'' + 
			",p_email = '" + pEmail + '\'' + 
			",patient_id = '" + patientId + '\'' + 
			",p_name = '" + pName + '\'' + 
			",age = '" + age + '\'' + 
			",p_mobile = '" + pMobile + '\'' + 
			",diagnosis = '" + diagnosis + '\'' +
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(date);
		dest.writeString(doctorId);
		dest.writeString(presbPatientId);
		dest.writeString(gender);
		dest.writeString(pEmail);
		dest.writeString(patientId);
		dest.writeString(pName);
		dest.writeString(age);
		dest.writeString(pMobile);
		dest.writeString(diagnosis);
	}
}