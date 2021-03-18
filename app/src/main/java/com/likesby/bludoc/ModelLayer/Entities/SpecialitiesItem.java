package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class SpecialitiesItem{

	@SerializedName("speciality_name")
	private String specialityName;

	@SerializedName("speciality_id")
	private String specialityId;

	public void setSpecialityName(String specialityName){
		this.specialityName = specialityName;
	}

	public String getSpecialityName(){
		return specialityName;
	}

	public void setSpecialityId(String specialityId){
		this.specialityId = specialityId;
	}

	public String getSpecialityId(){
		return specialityId;
	}

	@Override
 	public String toString(){
		return 
			"SpecialitiesItem{" + 
			"speciality_name = '" + specialityName + '\'' + 
			",speciality_id = '" + specialityId + '\'' + 
			"}";
		}
}