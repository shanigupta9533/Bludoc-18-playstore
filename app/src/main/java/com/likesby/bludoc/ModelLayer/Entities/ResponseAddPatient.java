package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class ResponseAddPatient{

	@SerializedName("gender")
	private String gender;

	@SerializedName("p_email")
	private String pEmail;

	@SerializedName("patient_id")
	private String patientId;

	@SerializedName("p_name")
	private String pName;

	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

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

	public String getPName(){
		return pName;
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

	public void setPMobile(String pMobile){
		this.pMobile = pMobile;
	}

	public String getPMobile(){
		return pMobile;
	}

	@Override
 	public String toString(){
		return 
			"ResponseAddPatient{" + 
			"gender = '" + gender + '\'' + 
			",p_email = '" + pEmail + '\'' + 
			",patient_id = '" + patientId + '\'' + 
			",p_name = '" + pName + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",created_by = '" + createdBy + '\'' + 
			",age = '" + age + '\'' + 
			",p_mobile = '" + pMobile + '\'' + 
			"}";
		}
}