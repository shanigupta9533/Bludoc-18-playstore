package com.likesby.bludoc.viewModels;

import com.likesby.bludoc.ModelLayer.AppointmentListModel;
import com.likesby.bludoc.ModelLayer.AppointmentModel;

import java.util.ArrayList;

public class ResultOfApi {

    private String success;
    private String message;
    private ArrayList<AppointmentModel> patients=new ArrayList<>();

    public String getSuccess() {
        return success;
    }

    public ArrayList<AppointmentModel> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<AppointmentModel> patients) {
        this.patients = patients;
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
