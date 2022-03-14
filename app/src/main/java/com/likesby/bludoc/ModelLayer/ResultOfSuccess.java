package com.likesby.bludoc.ModelLayer;

import java.util.ArrayList;

public class ResultOfSuccess {

    private ArrayList<AppointmentListModel> patients=new ArrayList<>();
    private ArrayList<ConsentDataModel> Consent=new ArrayList<>();

    private String success;
    private String message;

    public ArrayList<AppointmentListModel> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<AppointmentListModel> patients) {
        this.patients = patients;
    }

    public ArrayList<ConsentDataModel> getConsent() {
        return Consent;
    }

    public void setConsent(ArrayList<ConsentDataModel> consent) {
        Consent = consent;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
