package com.likesby.bludoc.ModelLayer;

import android.os.Parcel;
import android.os.Parcelable;

public class ConsentTemplateModel implements Parcelable {

    private String consent_id = "";
    private String doctor_id = "";
    private String title = "";
    private String description = "";

    protected ConsentTemplateModel(Parcel in) {
        consent_id = in.readString();
        doctor_id = in.readString();
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<ConsentTemplateModel> CREATOR = new Creator<ConsentTemplateModel>() {
        @Override
        public ConsentTemplateModel createFromParcel(Parcel in) {
            return new ConsentTemplateModel(in);
        }

        @Override
        public ConsentTemplateModel[] newArray(int size) {
            return new ConsentTemplateModel[size];
        }
    };

    public String getConsent_id() {
        return consent_id;
    }

    public void setConsent_id(String consent_id) {
        this.consent_id = consent_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(consent_id);
        parcel.writeString(doctor_id);
        parcel.writeString(title);
        parcel.writeString(description);
    }
}
