package com.likesby.bludoc.ModelLayer.NewEntities3;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PrescriptionItem implements Parcelable {

    public PrescriptionItem(){



    }

    @SerializedName("date")
    private String date;

    @SerializedName("end_note")
    private String endNote;

    @SerializedName("medicines")
    private ArrayList<MedicinesItem> medicines;

    @SerializedName("lab_test")
    private ArrayList<LabTestItem> labTest;

    @SerializedName("presb_patient_id")
    private String presbPatientId;

    @SerializedName("gender")
    private String gender;

    @SerializedName("diagnosis")
    private String diagnosis;

    @SerializedName("p_mobile")
    private String pMobile;

    @SerializedName("doctor")
    private Doctor doctor;

    @SerializedName("doctor_id")
    private String doctorId;

    @SerializedName("p_email")
    private String pEmail;

    @SerializedName("patient_id")
    private String patientId;

    @SerializedName("p_address")
    private String address;

    @SerializedName("p_blood_grp")
    private String pBloodGrp;

    @SerializedName("p_dob")
    private String pDob;

    @SerializedName("p_name")
    private String pName;

    @SerializedName("age")
    private String age;

    protected PrescriptionItem(Parcel in) {
        date = in.readString();
        endNote = in.readString();
        medicines = in.createTypedArrayList(MedicinesItem.CREATOR);
        labTest = in.createTypedArrayList(LabTestItem.CREATOR);
        presbPatientId = in.readString();
        gender = in.readString();
        diagnosis = in.readString();
        pMobile = in.readString();
        doctor = in.readParcelable(Doctor.class.getClassLoader());
        doctorId = in.readString();
        pEmail = in.readString();
        patientId = in.readString();
        address = in.readString();
        pBloodGrp = in.readString();
        pDob = in.readString();
        pName = in.readString();
        age = in.readString();
    }

    public static final Creator<PrescriptionItem> CREATOR = new Creator<PrescriptionItem>() {
        @Override
        public PrescriptionItem createFromParcel(Parcel in) {
            return new PrescriptionItem(in);
        }

        @Override
        public PrescriptionItem[] newArray(int size) {
            return new PrescriptionItem[size];
        }
    };

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setEndNote(String endNote) {
        this.endNote = endNote;
    }

    public String getEndNote() {
        return endNote;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getpBloodGrp() {
        return pBloodGrp;
    }

    public void setpBloodGrp(String pBloodGrp) {
        this.pBloodGrp = pBloodGrp;
    }

    public String getpDob() {
        return pDob;
    }

    public void setpDob(String pDob) {
        this.pDob = pDob;
    }

    public void setMedicines(ArrayList<MedicinesItem> medicines) {
        this.medicines = medicines;
    }

    public ArrayList<MedicinesItem> getMedicines() {
        return medicines;
    }

    public void setLabTest(ArrayList<LabTestItem> labTest) {
        this.labTest = labTest;
    }

    public ArrayList<LabTestItem> getLabTest() {
        return labTest;
    }

    public void setPresbPatientId(String presbPatientId) {
        this.presbPatientId = presbPatientId;
    }

    public String getPresbPatientId() {
        return presbPatientId;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setPMobile(String pMobile) {
        this.pMobile = pMobile;
    }

    public String getPMobile() {
        return pMobile;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setPEmail(String pEmail) {
        this.pEmail = pEmail;
    }

    public String getPEmail() {
        return pEmail;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public String getPName() {
        return pName;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    @Override
    public String toString() {
        return
                "PrescriptionItem{" +
                        "date = '" + date + '\'' +
                        ",end_note = '" + endNote + '\'' +
                        ",medicines = '" + medicines + '\'' +
                        ",lab_test = '" + labTest + '\'' +
                        ",presb_patient_id = '" + presbPatientId + '\'' +
                        ",gender = '" + gender + '\'' +
                        ",diagnosis = '" + diagnosis + '\'' +
                        ",p_mobile = '" + pMobile + '\'' +
                        ",doctor = '" + doctor + '\'' +
                        ",doctor_id = '" + doctorId + '\'' +
                        ",p_email = '" + pEmail + '\'' +
                        ",patient_id = '" + patientId + '\'' +
                        ",p_name = '" + pName + '\'' +
                        ",age = '" + age + '\'' +
                        ",p_blood_grp = '" + pBloodGrp + '\'' +
                        ",p_dob = '" + pDob + '\'' +
                        ",p_address = '" + address + '\'' +
                        "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(endNote);
        dest.writeTypedList(medicines);
        dest.writeTypedList(labTest);
        dest.writeString(presbPatientId);
        dest.writeString(gender);
        dest.writeString(diagnosis);
        dest.writeString(pMobile);
        dest.writeParcelable(doctor, flags);
        dest.writeString(doctorId);
        dest.writeString(pEmail);
        dest.writeString(patientId);
        dest.writeString(address);
        dest.writeString(pBloodGrp);
        dest.writeString(pDob);
        dest.writeString(pName);
        dest.writeString(age);
    }
}