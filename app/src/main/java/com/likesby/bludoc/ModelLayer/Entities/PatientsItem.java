package com.likesby.bludoc.ModelLayer.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PatientsItem implements Parcelable {

	@SerializedName("gender")
	private String gender;

	@SerializedName("p_email")
	private String pEmail;

	@SerializedName("patient_id")
	private String patientId;

	@SerializedName("message")
	private String message;

	@SerializedName("p_name")
	private String pName;

	@SerializedName("p_address")
	private String address;

	@SerializedName("p_blood_grp")
	private String pBloodGrp;

	@SerializedName("p_dob")
	private String pDob;

	@SerializedName("created")
	private String created;

	@SerializedName("modified")
	private String modified;

	@SerializedName("created_by")
	private String createdBy;

	@SerializedName("age")
	private String age;

	@SerializedName("p_mobile")
	private String pMobile;

	public PatientsItem(){

	}

	protected PatientsItem(Parcel in) {
		gender = in.readString();
		pEmail = in.readString();
		patientId = in.readString();
		message = in.readString();
		pName = in.readString();
		address = in.readString();
		pBloodGrp = in.readString();
		pDob = in.readString();
		created = in.readString();
		modified = in.readString();
		createdBy = in.readString();
		age = in.readString();
		pMobile = in.readString();
	}

	public static final Creator<PatientsItem> CREATOR = new Creator<PatientsItem>() {
		@Override
		public PatientsItem createFromParcel(Parcel in) {
			return new PatientsItem(in);
		}

		@Override
		public PatientsItem[] newArray(int size) {
			return new PatientsItem[size];
		}
	};

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPEmail() {
		return pEmail;
	}

	public void setPEmail(String pEmail) {
		this.pEmail = pEmail;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPName() {
		return pName;
	}

	public void setPName(String pName) {
		this.pName = pName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getpBloodGrp() {
		return pBloodGrp;
	}

	public void setpBloodGrp(String pBloodGrp) {
		this.pBloodGrp = pBloodGrp;
	}

	public String getpDob() {
		return pDob;
	}

	public void setpDob(String pDob) {
		this.pDob = pDob;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPMobile() {
		return pMobile;
	}

	public void setPMobile(String pMobile) {
		this.pMobile = pMobile;
	}

	@Override
 	public String toString(){
		return 
			"PatientsItem{" + 
			"gender = '" + gender + '\'' + 
			",p_email = '" + pEmail + '\'' + 
			",patient_id = '" + patientId + '\'' + 
			",p_name = '" + pName + '\'' + 
			",created = '" + created + '\'' + 
			",modified = '" + modified + '\'' + 
			",created_by = '" + createdBy + '\'' + 
			",age = '" + age + '\'' + 
			",p_mobile = '" + pMobile + '\'' + 
			",p_blood_grp = '" + pBloodGrp + '\'' +
			",p_dob = '" + pDob + '\'' +
			",p_address = '" + address + '\'' +
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(gender);
		dest.writeString(pEmail);
		dest.writeString(patientId);
		dest.writeString(message);
		dest.writeString(pName);
		dest.writeString(address);
		dest.writeString(pBloodGrp);
		dest.writeString(pDob);
		dest.writeString(created);
		dest.writeString(modified);
		dest.writeString(createdBy);
		dest.writeString(age);
		dest.writeString(pMobile);
	}
}