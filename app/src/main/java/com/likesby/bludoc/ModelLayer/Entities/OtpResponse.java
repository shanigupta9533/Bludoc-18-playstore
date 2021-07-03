package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class OtpResponse {

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@SerializedName("status")
	private String status;

	@SerializedName("success")
	private String success;

	@SerializedName("otp")
	private int otp;

	@SerializedName("message")
	private String message;

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setOtp(int otp){
		this.otp = otp;
	}

	public int getOtp(){
		return otp;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"OtpResponse{" + 
			"success = '" + success + '\'' + 
			",otp = '" + otp + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' +
			"}";
		}
}