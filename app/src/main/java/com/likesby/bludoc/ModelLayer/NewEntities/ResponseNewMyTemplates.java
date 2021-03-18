package com.likesby.bludoc.ModelLayer.NewEntities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseNewMyTemplates implements Parcelable {

	@SerializedName("success")
	private String success;

	@SerializedName("templates")
	private ArrayList<TemplatesItem> templates;

	@SerializedName("message")
	private String message;

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setTemplates(ArrayList<TemplatesItem> templates){
		this.templates = templates;
	}

	public ArrayList<TemplatesItem> getTemplates(){
		return templates;
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
			"ResponseNewMyTemplates{" + 
			"success = '" + success + '\'' + 
			",templates = '" + templates + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.success);
		dest.writeList(this.templates);
		dest.writeString(this.message);
	}

	public ResponseNewMyTemplates() {
	}

	protected ResponseNewMyTemplates(Parcel in) {
		this.success = in.readString();
		this.templates = new ArrayList<TemplatesItem>();
		in.readList(this.templates, TemplatesItem.class.getClassLoader());
		this.message = in.readString();
	}

	public static final Parcelable.Creator<ResponseNewMyTemplates> CREATOR = new Parcelable.Creator<ResponseNewMyTemplates>() {
		@Override
		public ResponseNewMyTemplates createFromParcel(Parcel source) {
			return new ResponseNewMyTemplates(source);
		}

		@Override
		public ResponseNewMyTemplates[] newArray(int size) {
			return new ResponseNewMyTemplates[size];
		}
	};
}