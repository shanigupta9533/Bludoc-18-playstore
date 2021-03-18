package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class BannersItem{

	@SerializedName("banner_id")
	private String bannerId;

	@SerializedName("banner_url")
	private String bannerUrl;

	@SerializedName("banner_image")
	private String bannerImage;

	public void setBannerId(String bannerId){
		this.bannerId = bannerId;
	}

	public String getBannerId(){
		return bannerId;
	}

	public void setBannerUrl(String bannerUrl){
		this.bannerUrl = bannerUrl;
	}

	public String getBannerUrl(){
		return bannerUrl;
	}

	public void setBannerImage(String bannerImage){
		this.bannerImage = bannerImage;
	}

	public String getBannerImage(){
		return bannerImage;
	}

	@Override
 	public String toString(){
		return 
			"BannersItem{" + 
			"banner_id = '" + bannerId + '\'' + 
			",banner_url = '" + bannerUrl + '\'' + 
			",banner_image = '" + bannerImage + '\'' + 
			"}";
		}
}