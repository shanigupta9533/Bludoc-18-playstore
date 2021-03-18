package com.likesby.bludoc.ModelLayer.NewEntities2;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LabTestItem implements Parcelable {

	@SerializedName("lab_test_comment")
	private String labTestComment;

	@SerializedName("lab_template_id")
	private String labTemplateId;

	@SerializedName("lab_template_test_id")
	private String labTemplateTestId;

	@SerializedName("lab_test_name")
	private String labTestName;

	public void setLabTestComment(String labTestComment){
		this.labTestComment = labTestComment;
	}

	public String getLabTestComment(){
		return labTestComment;
	}

	public void setLabTemplateId(String labTemplateId){
		this.labTemplateId = labTemplateId;
	}

	public String getLabTemplateId(){
		return labTemplateId;
	}

	public void setLabTemplateTestId(String labTemplateTestId){
		this.labTemplateTestId = labTemplateTestId;
	}

	public String getLabTemplateTestId(){
		return labTemplateTestId;
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
			",lab_template_id = '" + labTemplateId + '\'' + 
			",lab_template_test_id = '" + labTemplateTestId + '\'' + 
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
		dest.writeString(this.labTemplateId);
		dest.writeString(this.labTemplateTestId);
		dest.writeString(this.labTestName);
	}

	public LabTestItem() {
	}

	protected LabTestItem(Parcel in) {
		this.labTestComment = in.readString();
		this.labTemplateId = in.readString();
		this.labTemplateTestId = in.readString();
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