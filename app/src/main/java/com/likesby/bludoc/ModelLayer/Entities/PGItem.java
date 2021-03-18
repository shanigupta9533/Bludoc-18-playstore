package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class PGItem{

	@SerializedName("pg_name")
	private String pgName;

	@SerializedName("pg_id")
	private String pgId;

	public void setPgName(String pgName){
		this.pgName = pgName;
	}

	public String getPgName(){
		return pgName;
	}

	public void setPgId(String pgId){
		this.pgId = pgId;
	}

	public String getPgId(){
		return pgId;
	}

	@Override
 	public String toString(){
		return 
			"PGItem{" + 
			"pg_name = '" + pgName + '\'' + 
			",pg_id = '" + pgId + '\'' + 
			"}";
		}
}