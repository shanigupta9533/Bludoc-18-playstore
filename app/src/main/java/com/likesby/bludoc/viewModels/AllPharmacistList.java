package com.likesby.bludoc.viewModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class AllPharmacistList implements Parcelable {
    private String pharmacist_id;
    private String pharmacist_name;
    private String pharmacist_mobile;
    private String pharmacist_email;

    protected AllPharmacistList(Parcel in) {
        pharmacist_id = in.readString();
        pharmacist_name = in.readString();
        pharmacist_mobile = in.readString();
        pharmacist_email = in.readString();
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

    public void setPharmacist_name(String pharmacist_name) {
        this.pharmacist_name = pharmacist_name;
    }

    public String getPharmacist_mobile() {
        return pharmacist_mobile;
    }

    public void setPharmacist_mobile(String pharmacist_mobile) {
        this.pharmacist_mobile = pharmacist_mobile;
    }

    public String getPharmacist_email() {
        return pharmacist_email;
    }

    public void setPharmacist_email(String pharmacist_email) {
        this.pharmacist_email = pharmacist_email;
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
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(pharmacist_id);
    }

}
