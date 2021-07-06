package com.likesby.bludoc.ModelLayer.NewEntities;

import com.google.gson.annotations.SerializedName;

public class SubcriptionsItem{

	@SerializedName("subscription_id")
	private String subscriptionId;

	@SerializedName("date")
	private String date;

	@SerializedName("name")
	private String nameHospital;

	@SerializedName("useFor")
	private String useFor;

	@SerializedName("doctor_id")
	private String doctorId;

	@SerializedName("amount")
	private String amount;

	@SerializedName("doctor_subscription_id")
	private String doctorSubscriptionId;

	@SerializedName("payment_status")
	private String paymentStatus;

	@SerializedName("start")
	private String start;

	@SerializedName("days")
	private String days;

	@SerializedName("end")
	private String end;

	@SerializedName("type")
	private String type;

	public void setSubscriptionId(String subscriptionId){
		this.subscriptionId = subscriptionId;
	}

	public String getSubscriptionId(){
		return subscriptionId;
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

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setDoctorSubscriptionId(String doctorSubscriptionId){
		this.doctorSubscriptionId = doctorSubscriptionId;
	}

	public String getDoctorSubscriptionId(){
		return doctorSubscriptionId;
	}

	public void setPaymentStatus(String paymentStatus){
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentStatus(){
		return paymentStatus;
	}

	public void setStart(String start){
		this.start = start;
	}

	public String getStart(){
		return start;
	}

	public void setDays(String days){
		this.days = days;
	}

	public String getDays(){
		return days;
	}

	public void setEnd(String end){
		this.end = end;
	}

	public String getEnd(){
		return end;
	}

	public String getUseFor() {
		return useFor;
	}

	public void setUseFor(String useFor) {
		this.useFor = useFor;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public String getNameHospital() {
		return nameHospital;
	}

	public void setNameHospital(String nameHospital) {
		this.nameHospital = nameHospital;
	}

	@Override
 	public String toString(){
		return 
			"SubcriptionsItem{" + 
			"subscription_id = '" + subscriptionId + '\'' + 
			",date = '" + date + '\'' + 
			",doctor_id = '" + doctorId + '\'' + 
			",amount = '" + amount + '\'' + 
			",doctor_subscription_id = '" + doctorSubscriptionId + '\'' + 
			",payment_status = '" + paymentStatus + '\'' + 
			",start = '" + start + '\'' + 
			",days = '" + days + '\'' + 
			",end = '" + end + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}