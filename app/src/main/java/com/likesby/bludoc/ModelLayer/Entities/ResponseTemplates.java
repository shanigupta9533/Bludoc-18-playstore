package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseTemplates{

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
			"ResponseTemplates{" + 
			"success = '" + success + '\'' + 
			",templates = '" + templates + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}