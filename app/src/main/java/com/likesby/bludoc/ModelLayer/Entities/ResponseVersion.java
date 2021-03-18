package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseVersion{

	@SerializedName("Version")
	private ArrayList<VersionItem> version;

	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

	public void setVersion(ArrayList<VersionItem> version){
		this.version = version;
	}

	public ArrayList<VersionItem> getVersion(){
		return version;
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
			"ResponseVersion{" + 
			"version = '" + version + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}