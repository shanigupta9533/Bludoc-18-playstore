package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class CmsItem{

	@SerializedName("image")
	private String image;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("description ")
	private String description;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	@Override
 	public String toString(){
		return 
			"CmsItem{" + 
			"image = '" + image + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",description  = '" + description + '\'' + 
			"}";
		}
}