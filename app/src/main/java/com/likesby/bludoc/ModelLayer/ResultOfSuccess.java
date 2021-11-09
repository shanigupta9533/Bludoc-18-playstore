package com.likesby.bludoc.ModelLayer;

import java.util.ArrayList;

public class ResultOfSuccess {

    private ArrayList<AppointmentListModel> patients=new ArrayList<>();

    public ArrayList<AppointmentListModel> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<AppointmentListModel> patients) {
        this.patients = patients;
    }
}
