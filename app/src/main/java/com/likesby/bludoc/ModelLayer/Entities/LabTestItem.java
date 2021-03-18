package com.likesby.bludoc.ModelLayer.Entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LabTestItem implements Parcelable {

	@SerializedName("lab_test_comment")
	private String labTestComment;

	@SerializedName("lab_id ")
	private String labId;

	@SerializedName("lab_test_name")
	private String labTestName;

	public void setLabTestComment(String labTestComment){
		this.labTestComment = labTestComment;
	}

	public String getLabTestComment(){
		return labTestComment;
	}

	public void setLabId(String labId){
		this.labId = labId;
	}

	public String getLabId(){
		return labId;
	}

	public void setLabTestName(String labTestName){
		this.labTestName = labTestName;
	}

	public String getLabTestName(){
		return labTestName;
	}

	@Override
 	public String toString(){
		return 
			"LabTestItem{" + 
			"lab_test_comment = '" + labTestComment + '\'' + 
			",lab_id  = '" + labId + '\'' + 
			",lab_test_name = '" + labTestName + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.labTestComment);
		dest.writeString(this.labId);
		dest.writeString(this.labTestName);
	}

	public LabTestItem() {
	}

	protected LabTestItem(Parcel in) {
		this.labTestComment = in.readString();
		this.labId = in.readString();
		this.labTestName = in.readString();
	}

	public static final Parcelable.Creator<LabTestItem> CREATOR = new Parcelable.Creator<LabTestItem>() {
		@Override
		public LabTestItem createFromParcel(Parcel source) {
			return new LabTestItem(source);
		}

		@Override
		public LabTestItem[] newArray(int size) {
			return new LabTestItem[size];
		}
	};
}