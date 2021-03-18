package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class PatientsItem{

	@SerializedName("gender")
	private String gender;

	@SerializedName("p_email")
	private String pEmail;

	@SerializedName("patient_id")
	private String patientId;

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

	public String getPName(){
		return pName;
	}

	public void setCreated(String created){
		this.created = created;
	}

	public String getCreated(){
		return created;
	}

	public void setModified(String modified){
		this.modified = modified;
	}

	public String getModified(){
		return modified;
	}

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	public void setAge(String age){
		this.age = age;
	}

	public String getAge(){
		return age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
}