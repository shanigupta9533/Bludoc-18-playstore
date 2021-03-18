package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class TemplatesItem{

	@SerializedName("template_name")
	private String templateName;

	@SerializedName("template_id")
	private String templateId;

	public void setTemplateName(String templateName){
		this.templateName = templateName;
	}

	public String getTemplateName(){
		return templateName;
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
			",template_id = '" + templateId + '\'' + 
			"}";
		}
}