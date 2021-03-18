package com.likesby.bludoc.ModelLayer.NewEntities2;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseLabTemplates implements Parcelable {

	@SerializedName("lab_templates")
	private ArrayList<LabTemplatesItem> labTemplates;

	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

	public void setLabTemplates(ArrayList<LabTemplatesItem> labTemplates){
		this.labTemplates = labTemplates;
	}

	public ArrayList<LabTemplatesItem> getLabTemplates(){
		return labTemplates;
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
			"ResponseLabTemplates{" + 
			"lab_templates = '" + labTemplates + '\'' + 
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
		dest.writeList(this.labTemplates);
		dest.writeString(this.success);
		dest.writeString(this.message);
	}

	public ResponseLabTemplates() {
	}

	protected ResponseLabTemplates(Parcel in) {
		this.labTemplates = new ArrayList<LabTemplatesItem>();
		in.readList(this.labTemplates, LabTemplatesItem.class.getClassLoader());
		this.success = in.readString();
		this.message = in.readString();
	}

	public static final Parcelable.Creator<ResponseLabTemplates> CREATOR = new Parcelable.Creator<ResponseLabTemplates>() {
		@Override
		public ResponseLabTemplates createFromParcel(Parcel source) {
			return new ResponseLabTemplates(source);
		}

		@Override
		public ResponseLabTemplates[] newArray(int size) {
			return new ResponseLabTemplates[size];
		}
	};
}