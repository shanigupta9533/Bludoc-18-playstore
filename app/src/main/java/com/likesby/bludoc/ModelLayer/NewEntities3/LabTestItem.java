package com.likesby.bludoc.ModelLayer.NewEntities3;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LabTestItem implements Parcelable {

	@SerializedName("presb_patient_id")
	private String presbPatientId;

	@SerializedName("lab_test_comment")
	private String labTestComment;

	@SerializedName("prescribe_lab_test_id")
	private String prescribeLabTestId;

	@SerializedName("lab_test_name")
	private String labTestName;

	public void setPresbPatientId(String presbPatientId){
		this.presbPatientId = presbPatientId;
	}

	public String getPresbPatientId(){
		return presbPatientId;
	}

	public void setLabTestComment(String labTestComment){
		this.labTestComment = labTestComment;
	}

	public String getLabTestComment(){
		return labTestComment;
	}

	public void setPrescribeLabTestId(String prescribeLabTestId){
		this.prescribeLabTestId = prescribeLabTestId;
	}

	public String getPrescribeLabTestId(){
		return prescribeLabTestId;
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
			"presb_patient_id = '" + presbPatientId + '\'' + 
			",lab_test_comment = '" + labTestComment + '\'' + 
			",prescribe_lab_test_id = '" + prescribeLabTestId + '\'' + 
			",lab_test_name = '" + labTestName + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.presbPatientId);
		dest.writeString(this.labTestComment);
		dest.writeString(this.prescribeLabTestId);
		dest.writeString(this.labTestName);
	}

	public LabTestItem() {
	}

	protected LabTestItem(Parcel in) {
		this.presbPatientId = in.readString();
		this.labTestComment = in.readString();
		this.prescribeLabTestId = in.readString();
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