package com.likesby.bludoc.ModelLayer.NewEntities3;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseHistory implements Parcelable {

	@SerializedName("Prescription")
	private ArrayList<PrescriptionItem> prescription;

	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

	public void setPrescription(ArrayList<PrescriptionItem> prescription){
		this.prescription = prescription;
	}

	public ArrayList<PrescriptionItem> getPrescription(){
		return prescription;
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

	@Override
 	public String toString(){
		return 
			"ResponseHistory{" + 
			"prescription = '" + prescription + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(this.prescription);
		dest.writeString(this.success);
		dest.writeString(this.message);
	}

	public ResponseHistory() {
	}

	protected ResponseHistory(Parcel in) {
		this.prescription = new ArrayList<PrescriptionItem>();
		in.readList(this.prescription, PrescriptionItem.class.getClassLoader());
		this.success = in.readString();
		this.message = in.readString();
	}

	public static final Parcelable.Creator<ResponseHistory> CREATOR = new Parcelable.Creator<ResponseHistory>() {
		@Override
		public ResponseHistory createFromParcel(Parcel source) {
			return new ResponseHistory(source);
		}

		@Override
		public ResponseHistory[] newArray(int size) {
			return new ResponseHistory[size];
		}
	};
}