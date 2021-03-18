package com.likesby.bludoc.ModelLayer.NewEntities2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LabTemplatesItem implements Parcelable {

	@SerializedName("lab_template_name")
	private String labTemplateName;

	@SerializedName("lab_test")
	private ArrayList<LabTestItem> labTest;

	@SerializedName("lab_template_id")
	private String labTemplateId;

	public void setLabTemplateName(String labTemplateName){
		this.labTemplateName = labTemplateName;
	}

	public String getLabTemplateName(){
		return labTemplateName;
	}

	public void setLabTest(ArrayList<LabTestItem> labTest){
		this.labTest = labTest;
	}

	public ArrayList<LabTestItem> getLabTest(){
		return labTest;
	}

	public void setLabTemplateId(String labTemplateId){
		this.labTemplateId = labTemplateId;
	}

	public String getLabTemplateId(){
		return labTemplateId;
	}

	@Override
 	public String toString(){
		return 
			"LabTemplatesItem{" + 
			"lab_template_name = '" + labTemplateName + '\'' + 
			",lab_test = '" + labTest + '\'' + 
			",lab_template_id = '" + labTemplateId + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.labTemplateName);
		dest.writeList(this.labTest);
		dest.writeString(this.labTemplateId);
	}

	public LabTemplatesItem() {
	}

	protected LabTemplatesItem(Parcel in) {
		this.labTemplateName = in.readString();
		this.labTest = new ArrayList<LabTestItem>();
		in.readList(this.labTest, LabTestItem.class.getClassLoader());
		this.labTemplateId = in.readString();
	}

	public static final Parcelable.Creator<LabTemplatesItem> CREATOR = new Parcelable.Creator<LabTemplatesItem>() {
		@Override
		public LabTemplatesItem createFromParcel(Parcel source) {
			return new LabTemplatesItem(source);
		}

		@Override
		public LabTemplatesItem[] newArray(int size) {
			return new LabTemplatesItem[size];
		}
	};
}