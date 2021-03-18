package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AddTemplateJSON{

	@SerializedName("template_name")
	private String templateName;

	@SerializedName("medicines")
	private ArrayList<MedicinesItem> medicines;

	@SerializedName("created_by")
	private String createdBy;

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

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	@Override
 	public String toString(){
		return 
			"AddTemplateJSON{" + 
			"template_name = '" + templateName + '\'' + 
			",medicines = '" + medicines + '\'' + 
			",created_by = '" + createdBy + '\'' + 
			"}";
		}
}