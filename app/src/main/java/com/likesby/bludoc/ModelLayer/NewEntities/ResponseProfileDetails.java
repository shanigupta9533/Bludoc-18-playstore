package com.likesby.bludoc.ModelLayer.NewEntities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseProfileDetails{

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	@SerializedName("access")
	private String access;



	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	@SerializedName("hospital_code")
	private String hospitalCode;

	@SerializedName("email_letter_head")
	private String emailLetterHead;

	@SerializedName("designation_name")
	private String designationName;

	@SerializedName("signature")
	private String signature;

	@SerializedName("speciality_id")
	private String specialityId;

	@SerializedName("subcriptions")
	private ArrayList<SubcriptionsItem> subcriptions;

	public String getDesignationId() {
		return designationId;
	}

	public void setDesignationId(String designationId) {
		this.designationId = designationId;
	}

	@SerializedName("designation_id")
	private String designationId;

	@SerializedName("ug_id")
	private String ugId;

	@SerializedName("clinic_name")
	private String clinicName;

	@SerializedName("doctor_id")
	private String doctorId;

	@SerializedName("visiting_hr_to")
	private String visitingHrTo;

	@SerializedName("close_day")
	private String closeDay;

	@SerializedName("logo")
	private String logo;

	@SerializedName("email")
	private String email;

	@SerializedName("speciality_name")
	private String specialityName;

	@SerializedName("image")
	private String image;

	@SerializedName("working_days")
	private String workingDays;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("message")
	private String message;

	@SerializedName("mobile_letter_head")
	private String mobileLetterHead;

	@SerializedName("registration_no")
	private String registrationNo;

	@SerializedName("addtional_qualification")
	private String addtionalQualification;

	@SerializedName("ug_name")
	private String ugName;

	@SerializedName("success")
	private String success;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@SerializedName("status")
	private String status;

	@SerializedName("clinic_address")
	private String clinicAddress;

	@SerializedName("name")
	private String name;

	@SerializedName("pg_name")
	private String pgName;

	@SerializedName("pg_id")
	private String pgId;

	@SerializedName("visiting_hr_from")
	private String visitingHrFrom;

	public void setEmailLetterHead(String emailLetterHead){
		this.emailLetterHead = emailLetterHead;
	}

	public String getEmailLetterHead(){
		return emailLetterHead;
	}

	public void setDesignationName(String designationName){
		this.designationName = designationName;
	}

	public String getDesignationName(){
		return designationName;
	}

	public void setSignature(String signature){
		this.signature = signature;
	}

	public String getSignature(){
		return signature;
	}

	public void setSpecialityId(String specialityId){
		this.specialityId = specialityId;
	}

	public String getSpecialityId(){
		return specialityId;
	}

	public void setSubcriptions(ArrayList<SubcriptionsItem> subcriptions){
		this.subcriptions = subcriptions;
	}

	public ArrayList<SubcriptionsItem> getSubcriptions(){
		return subcriptions;
	}

	public void setUgId(String ugId){
		this.ugId = ugId;
	}

	public String getUgId(){
		return ugId;
	}

	public void setClinicName(String clinicName){
		this.clinicName = clinicName;
	}

	public String getClinicName(){
		return clinicName;
	}

	public void setDoctorId(String doctorId){
		this.doctorId = doctorId;
	}

	public String getDoctorId(){
		return doctorId;
	}

	public void setVisitingHrTo(String visitingHrTo){
		this.visitingHrTo = visitingHrTo;
	}

	public String getVisitingHrTo(){
		return visitingHrTo;
	}

	public void setCloseDay(String closeDay){
		this.closeDay = closeDay;
	}

	public String getCloseDay(){
		return closeDay;
	}

	public void setLogo(String logo){
		this.logo = logo;
	}

	public String getLogo(){
		return logo;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setSpecialityName(String specialityName){
		this.specialityName = specialityName;
	}

	public String getSpecialityName(){
		return specialityName;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setWorkingDays(String workingDays){
		this.workingDays = workingDays;
	}

	public String getWorkingDays(){
		return workingDays;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setMobileLetterHead(String mobileLetterHead){
		this.mobileLetterHead = mobileLetterHead;
	}

	public String getMobileLetterHead(){
		return mobileLetterHead;
	}

	public void setRegistrationNo(String registrationNo){
		this.registrationNo = registrationNo;
	}

	public String getRegistrationNo(){
		return registrationNo;
	}

	public void setAddtionalQualification(String addtionalQualification){
		this.addtionalQualification = addtionalQualification;
	}

	public String getAddtionalQualification(){
		return addtionalQualification;
	}

	public void setUgName(String ugName){
		this.ugName = ugName;
	}

	public String getUgName(){
		return ugName;
	}

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setClinicAddress(String clinicAddress){
		this.clinicAddress = clinicAddress;
	}

	public String getClinicAddress(){
		return clinicAddress;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPgName(String pgName){
		this.pgName = pgName;
	}

	public String getPgName(){
		return pgName;
	}

	public void setPgId(String pgId){
		this.pgId = pgId;
	}

	public String getPgId(){
		return pgId;
	}

	public void setVisitingHrFrom(String visitingHrFrom){
		this.visitingHrFrom = visitingHrFrom;
	}

	public String getVisitingHrFrom(){
		return visitingHrFrom;
	}

	@Override
 	public String toString(){
		return 
			"ResponseProfileDetails{" + 
			"email_letter_head = '" + emailLetterHead + '\'' + 
			",designation_name = '" + designationName + '\'' + 
			",signature = '" + signature + '\'' + 
			",speciality_id = '" + specialityId + '\'' + 
			",subcriptions = '" + subcriptions + '\'' + 
			",ug_id = '" + ugId + '\'' + 
			",clinic_name = '" + clinicName + '\'' + 
			",doctor_id = '" + doctorId + '\'' + 
			",visiting_hr_to = '" + visitingHrTo + '\'' + 
			",close_day = '" + closeDay + '\'' + 
			",logo = '" + logo + '\'' + 
			",email = '" + email + '\'' + 
			",speciality_name = '" + specialityName + '\'' + 
			",image = '" + image + '\'' + 
			",working_days = '" + workingDays + '\'' + 
			",mobile = '" + mobile + '\'' + 
			",message = '" + message + '\'' + 
			",mobile_letter_head = '" + mobileLetterHead + '\'' + 
			",registration_no = '" + registrationNo + '\'' + 
			",addtional_qualification = '" + addtionalQualification + '\'' + 
			",ug_name = '" + ugName + '\'' + 
			",success = '" + success + '\'' + 
			",clinic_address = '" + clinicAddress + '\'' + 
			",name = '" + name + '\'' + 
			",pg_name = '" + pgName + '\'' + 
			",pg_id = '" + pgId + '\'' + 
			",visiting_hr_from = '" + visitingHrFrom + '\'' + 
			",designation_id = '" + designationId + '\'' +
			",access = '" + access + '\'' +
			",hospitalCode = '" + hospitalCode + '\'' +
			"}";
		}
}