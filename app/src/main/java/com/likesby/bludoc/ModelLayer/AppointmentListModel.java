package com.likesby.bludoc.ModelLayer;

import android.os.Parcel;
import android.os.Parcelable;

public class AppointmentListModel implements Parcelable {

    private String doctor_id;
    private String app_list_id;
    private String patient_id;
    private String p_name;
    private String p_email;
    private String p_mobile;
    private String p_age;
    private String p_gender;
    private String app_date;
    private String app_time;
    private String status;
    private String created;
    private String purpose;
    private String modified;

    protected AppointmentListModel(Parcel in) {
        doctor_id = in.readString();
        app_list_id = in.readString();
        patient_id = in.readString();
        p_name = in.readString();
        p_email = in.readString();
        p_mobile = in.readString();
        p_age = in.readString();
        p_gender = in.readString();
        app_date = in.readString();
        app_time = in.readString();
        status = in.readString();
        purpose = in.readString();
        created = in.readString();
        modified = in.readString();
    }

    public AppointmentListModel(){



    }

    public static final Creator<AppointmentListModel> CREATOR = new Creator<AppointmentListModel>() {
        @Override
        public AppointmentListModel createFromParcel(Parcel in) {
            return new AppointmentListModel(in);
        }

        @Override
        public AppointmentListModel[] newArray(int size) {
            return new AppointmentListModel[size];
        }
    };

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getApp_list_id() {
        return app_list_id;
    }

    public void setApp_list_id(String app_list_id) {
        this.app_list_id = app_list_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_email() {
        return p_email;
    }

    public void setP_email(String p_email) {
        this.p_email = p_email;
    }

    public String getP_mobile() {
        return p_mobile;
    }

    public void setP_mobile(String p_mobile) {
        this.p_mobile = p_mobile;
    }

    public String getP_age() {
        return p_age;
    }

    public void setP_age(String p_age) {
        this.p_age = p_age;
    }

    public String getP_gender() {
        return p_gender;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setP_gender(String p_gender) {
        this.p_gender = p_gender;
    }

    public String getApp_date() {
        return app_date;
    }

    public void setApp_date(String app_date) {
        this.app_date = app_date;
    }

    public String getApp_time() {
        return app_time;
    }

    public void setApp_time(String app_time) {
        this.app_time = app_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(doctor_id);
        dest.writeString(app_list_id);
        dest.writeString(patient_id);
        dest.writeString(p_name);
        dest.writeString(p_email);
        dest.writeString(p_mobile);
        dest.writeString(p_age);
        dest.writeString(p_gender);
        dest.writeString(app_date);
        dest.writeString(app_time);
        dest.writeString(status);
        dest.writeString(purpose);
        dest.writeString(created);
        dest.writeString(modified);
    }
}
