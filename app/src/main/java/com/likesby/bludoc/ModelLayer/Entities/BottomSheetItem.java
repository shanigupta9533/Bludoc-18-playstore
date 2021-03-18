package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class BottomSheetItem {

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    @SerializedName("menu_id")
    private String menuId;

    @Override
    public String toString() {
        return "BottomSheetItem{" +
                "menuId='" + menuId + '\'' +
                ", menuName='" + menuName + '\'' +
                ", menuImage='" + menuImage + '\'' +
                '}';
    }

    @SerializedName("menu_name")
    private String menuName;

    @SerializedName("menu_image")
    private String menuImage;

}

