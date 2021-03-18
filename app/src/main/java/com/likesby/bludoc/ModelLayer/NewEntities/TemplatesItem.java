package com.likesby.bludoc.ModelLayer.NewEntities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TemplatesItem implements Parcelable {

	@SerializedName("template_name")
	private String templateName;

	@SerializedName("medicines")
	private ArrayList<MedicinesItem> medicines;

	@SerializedName("template_id")
	private String templateId;

	public void setTemplateName(String templateName){
		this.templateName = templateName;
	}

	public String getTemplateName(){
		return templateName;
	}

	public void setMedicines(ArrayList<MedicinesItem> medicines){
		this.medicines = medicines;
	}

	public ArrayList<MedicinesItem> getMedicines(){
		return medicines;
	}

	public void setTemplateId(String templateId){
		this.templateId = templateId;
	}

	public String getTemplateId(){
		return templateId;
	}

	@Override
 	public String toString(){
		return 
			"TemplatesItem{" + 
			"template_name = '" + templateName + '\'' + 
			",medicines = '" + medicines + '\'' + 
			",template_id = '" + templateId + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.templateName);
		dest.writeList(this.medicines);
		dest.writeString(this.templateId);
	}

	public TemplatesItem() {
	}

	protected TemplatesItem(Parcel in) {
		this.templateName = in.readString();
		this.medicines = new ArrayList<MedicinesItem>();
		in.readList(this.medicines, MedicinesItem.class.getClassLoader());
		this.templateId = in.readString();
	}

	public static final Parcelable.Creator<TemplatesItem> CREATOR = new Parcelable.Creator<TemplatesItem>() {
		@Override
		public TemplatesItem createFromParcel(Parcel source) {
			return new TemplatesItem(source);
		}

		@Override
		public TemplatesItem[] newArray(int size) {
			return new TemplatesItem[size];
		}
	};
}