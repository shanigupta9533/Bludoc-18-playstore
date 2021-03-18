package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class UGItem{

	@SerializedName("ug_name")
	private String ugName;

	@SerializedName("ug_id")
	private String ugId;

	public void setUgName(String ugName){
		this.ugName = ugName;
	}

	public String getUgName(){
		return ugName;
	}

	public void setUgId(String ugId){
		this.ugId = ugId;
	}

	public String getUgId(){
		return ugId;
	}

	@Override
 	public String toString(){
		return 
			"UGItem{" + 
			"ug_name = '" + ugName + '\'' + 
			",ug_id = '" + ugId + '\'' + 
			"}";
		}
}