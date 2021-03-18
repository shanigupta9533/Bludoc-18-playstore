package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class DesignationItem{

	@SerializedName("designation_name")
	private String designationName;

	@SerializedName("designation_id")
	private String designationId;

	public void setDesignationName(String designationName){
		this.designationName = designationName;
	}

	public String getDesignationName(){
		return designationName;
	}

	public void setDesignationId(String designationId){
		this.designationId = designationId;
	}

	public String getDesignationId(){
		return designationId;
	}

	@Override
 	public String toString(){
		return 
			"DesignationItem{" + 
			"designation_name = '" + designationName + '\'' + 
			",designation_id = '" + designationId + '\'' + 
			"}";
		}
}