package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class VersionItem{

	@SerializedName("min_version")
	private String minVersion;

	@SerializedName("update_version")
	private String updateVersion;

	@SerializedName("version_id")
	private String versionId;


	public void setMinVersion(String minVersion){
		this.minVersion = minVersion;
	}

	public String getMinVersion(){
		return minVersion;
	}

	public void setUpdateVersion(String updateVersion){
		this.updateVersion = updateVersion;
	}

	public String getUpdateVersion(){
		return updateVersion;
	}

	public void setVersionId(String versionId){
		this.versionId = versionId;
	}

	public String getVersionId(){
		return versionId;
	}

	@Override
 	public String toString(){
		return 
			"VersionItem{" + 
			"min_version = '" + minVersion + '\'' + 
			",update_version  = '" + updateVersion + '\'' + 
			",version_id = '" + versionId + '\'' + 
			"}";
		}
}