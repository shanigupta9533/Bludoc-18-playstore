package com.likesby.bludoc.viewModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class AllPharmacistList implements Parcelable {
    private String pharmacist_id;
    private String pharmacist_name;
    private String pharmacist_mobile;
    private String pharmacist_email;
    private String type;
    private String is_check="";

    protected AllPharmacistList(Parcel in) {
        pharmacist_id = in.readString();
        pharmacist_name = in.readString();
        pharmacist_mobile = in.readString();
        pharmacist_email = in.readString();
        is_check = in.readString();
        type = in.readString();
    }

    public static final Creator<AllPharmacistList> CREATOR = new Creator<AllPharmacistList>() {
        @Override
        public AllPharmacistList createFromParcel(Parcel in) {
            return new AllPharmacistList(in);
        }

        @Override
        public AllPharmacistList[] newArray(int size) {
            return new AllPharmacistList[size];
        }
    };

    public String getPharmacist_id() {
        return pharmacist_id;
    }

    public void setPharmacist_id(String pharmacist_id) {
        this.pharmacist_id = pharmacist_id;
    }

    public String getPharmacist_name() {
        return pharmacist_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPharmacist_name(String pharmacist_name) {
        this.pharmacist_name = pharmacist_name;
    }

    public String getPharmacist_mobile() {
        return pharmacist_mobile;
    }

    public void setPharmacist_mobile(String pharmacist_mobile) {
        this.pharmacist_mobile = pharmacist_mobile;
    }

    public void setIs_check(String is_check) {
        this.is_check = is_check;
    }

    public String getPharmacist_email() {
        return pharmacist_email;
    }

    public void setPharmacist_email(String pharmacist_email) {
        this.pharmacist_email = pharmacist_email;
    }

    public String getIs_check() {
        return is_check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pharmacist_id);
        dest.writeString(pharmacist_name);
        dest.writeString(pharmacist_mobile);
        dest.writeString(pharmacist_email);
        dest.writeString(is_check);
        dest.writeString(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        AllPharmacistList that = (AllPharmacistList) o;
        return Objects.equals(pharmacist_id, that.pharmacist_id);
    }

    @Override
    public String toString() {
        return "AllPharmacistList{" +
                "pharmacist_id='" + pharmacist_id + '\'' +
                ", pharmacist_name='" + pharmacist_name + '\'' +
                ", pharmacist_mobile='" + pharmacist_mobile + '\'' +
                ", pharmacist_email='" + pharmacist_email + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(pharmacist_id);
    }

}
