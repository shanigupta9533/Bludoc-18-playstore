package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseBanners{

	@SerializedName("Banners")
	private ArrayList<BannersItem> banners;

	@SerializedName("success")
	private String success;

	@SerializedName("message")
	private String message;

	public void setBanners(ArrayList<BannersItem> banners){
		this.banners = banners;
	}

	public ArrayList<BannersItem> getBanners(){
		return banners;
	}

	public void setSuccess(String success){
		this.success = success;
	}

	public String getSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ResponseBanners{" + 
			"banners = '" + banners + '\'' + 
			",success = '" + success + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}