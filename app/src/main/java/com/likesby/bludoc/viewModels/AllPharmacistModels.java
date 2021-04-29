package com.likesby.bludoc.viewModels;

import java.util.ArrayList;

public class AllPharmacistModels {

    private String success;
    private String message;
    private ArrayList<AllPharmacistList> Pharmacist;

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

    public ArrayList<AllPharmacistList> getPharmacist() {
        return Pharmacist;
    }

    public void setPharmacist(ArrayList<AllPharmacistList> pharmacist) {
        Pharmacist = pharmacist;
    }
}
