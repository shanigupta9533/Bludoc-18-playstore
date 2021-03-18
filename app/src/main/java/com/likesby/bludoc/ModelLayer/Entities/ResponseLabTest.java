package com.likesby.bludoc.ModelLayer.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseLabTest implements Parcelable {

	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

	@SerializedName("Lab Test")
	private ArrayList<LabTestItem> labTest;

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

	public void setLabTest(ArrayList<LabTestItem> labTest){
		this.labTest = labTest;
	}

	public ArrayList<LabTestItem> getLabTest(){
		return labTest;
	}

	@Override
 	public String toString(){
		return 
			"ResponseLabTest{" + 
			"success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			",lab Test = '" + labTest + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.success);
		dest.writeString(this.message);
		dest.writeList(this.labTest);
	}

	public ResponseLabTest() {
	}

	protected ResponseLabTest(Parcel in) {
		this.success = in.readString();
		this.message = in.readString();
		this.labTest = new ArrayList<LabTestItem>();
		in.readList(this.labTest, LabTestItem.class.getClassLoader());
	}

	public static final Parcelable.Creator<ResponseLabTest> CREATOR = new Parcelable.Creator<ResponseLabTest>() {
		@Override
		public ResponseLabTest createFromParcel(Parcel source) {
			return new ResponseLabTest(source);
		}

		@Override
		public ResponseLabTest[] newArray(int size) {
			return new ResponseLabTest[size];
		}
	};
}