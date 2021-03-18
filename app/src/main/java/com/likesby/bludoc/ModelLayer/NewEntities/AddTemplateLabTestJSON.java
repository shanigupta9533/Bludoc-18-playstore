package com.likesby.bludoc.ModelLayer.NewEntities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AddTemplateLabTestJSON{

	@SerializedName("lab_template_name")
	private String labTemplateName;

	@SerializedName("lab_test")
	private ArrayList<LabTestItem> labTest;

	@SerializedName("created_by")
	private String createdBy;

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

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

	@Override
 	public String toString(){
		return 
			"AddTemplateLabTestJSON{" + 
			"lab_template_name = '" + labTemplateName + '\'' + 
			",lab_test = '" + labTest + '\'' + 
			",created_by = '" + createdBy + '\'' + 
			"}";
		}
}