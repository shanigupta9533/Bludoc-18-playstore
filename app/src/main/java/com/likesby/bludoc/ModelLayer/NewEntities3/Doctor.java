package com.likesby.bludoc.ModelLayer.NewEntities3;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Doctor implements Parcelable {

	@SerializedName("email_letter_head")
	private String emailLetterHead;

	@SerializedName("image")
	private String image;

	@SerializedName("designation_name")
	private String designationName;

	@SerializedName("working_days")
	private String workingDays;

	@SerializedName("signature")
	private String signature;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("clinic_name")
	private String clinicName;

	@SerializedName("mobile_letter_head")
	private String mobileLetterHead;

	@SerializedName("registration_no")
	private String registrationNo;

	@SerializedName("addtional_qualification")
	private String addtionalQualification;

	@SerializedName("visiting_hr_to")
	private String visitingHrTo;

	@SerializedName("close_day")
	private String closeDay;

	@SerializedName("clinic_address")
	private String clinicAddress;

	@SerializedName("name")
	private String name;

	@SerializedName("pg_name")
	private String pgName;

	public String getUgName() {
		return ugName;
	}

	public void setUgName(String ugName) {
		this.ugName = ugName;
	}

	public static Creator<Doctor> getCREATOR() {
		return CREATOR;
	}

	@SerializedName("ug_name")
	private String ugName;

	@SerializedName("logo")
	private String logo;

	@SerializedName("email")
	private String email;

	@SerializedName("visiting_hr_from")
	private String visitingHrFrom;

	public void setEmailLetterHead(String emailLetterHead){
		this.emailLetterHead = emailLetterHead;
	}

	public String getEmailLetterHead(){
		return emailLetterHead;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setDesignationName(String designationName){
		this.designationName = designationName;
	}

	public String getDesignationName(){
		return designationName;
	}

	public void setWorkingDays(String workingDays){
		this.workingDays = workingDays;
	}

	public String getWorkingDays(){
		return workingDays;
	}

	public void setSignature(String signature){
		this.signature = signature;
	}

	public String getSignature(){
		return signature;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getMobile(){
		return mobile;
	}

	public void setClinicName(String clinicName){
		this.clinicName = clinicName;
	}

	public String getClinicName(){
		return clinicName;
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

	public void setVisitingHrFrom(String visitingHrFrom){
		this.visitingHrFrom = visitingHrFrom;
	}

	public String getVisitingHrFrom(){
		return visitingHrFrom;
	}

	@Override
 	public String toString(){
		return 
			"Doctor{" + 
			"email_letter_head = '" + emailLetterHead + '\'' + 
			",image = '" + image + '\'' + 
			",designation_name = '" + designationName + '\'' + 
			",working_days = '" + workingDays + '\'' + 
			",signature = '" + signature + '\'' + 
			",mobile = '" + mobile + '\'' + 
			",clinic_name = '" + clinicName + '\'' + 
			",mobile_letter_head = '" + mobileLetterHead + '\'' + 
			",registration_no = '" + registrationNo + '\'' + 
			",addtional_qualification = '" + addtionalQualification + '\'' + 
			",visiting_hr_to = '" + visitingHrTo + '\'' + 
			",close_day = '" + closeDay + '\'' + 
			",clinic_address = '" + clinicAddress + '\'' + 
			",name = '" + name + '\'' + 
			",pg_name = '" + pgName + '\'' + 
			",ug_name = '" + ugName + '\'' +
			",logo = '" + logo + '\'' +
			",email = '" + email + '\'' + 
			",visiting_hr_from = '" + visitingHrFrom + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.emailLetterHead);
		dest.writeString(this.image);
		dest.writeString(this.designationName);
		dest.writeString(this.workingDays);
		dest.writeString(this.signature);
		dest.writeString(this.mobile);
		dest.writeString(this.clinicName);
		dest.writeString(this.mobileLetterHead);
		dest.writeString(this.registrationNo);
		dest.writeString(this.addtionalQualification);
		dest.writeString(this.visitingHrTo);
		dest.writeString(this.closeDay);
		dest.writeString(this.clinicAddress);
		dest.writeString(this.name);
		dest.writeString(this.pgName);
		dest.writeString(this.ugName);
		dest.writeString(this.logo);
		dest.writeString(this.email);
		dest.writeString(this.visitingHrFrom);
	}

	public Doctor() {
	}

	protected Doctor(Parcel in) {
		this.emailLetterHead = in.readString();
		this.image = in.readString();
		this.designationName = in.readString();
		this.workingDays = in.readString();
		this.signature = in.readString();
		this.mobile = in.readString();
		this.clinicName = in.readString();
		this.mobileLetterHead = in.readString();
		this.registrationNo = in.readString();
		this.addtionalQualification = in.readString();
		this.visitingHrTo = in.readString();
		this.closeDay = in.readString();
		this.clinicAddress = in.readString();
		this.name = in.readString();
		this.pgName = in.readString();
		this.ugName = in.readString();
		this.logo = in.readString();
		this.email = in.readString();
		this.visitingHrFrom = in.readString();
	}

	public static final Parcelable.Creator<Doctor> CREATOR = new Parcelable.Creator<Doctor>() {
		@Override
		public Doctor createFromParcel(Parcel source) {
			return new Doctor(source);
		}

		@Override
		public Doctor[] newArray(int size) {
			return new Doctor[size];
		}
	};
}