package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;
import com.likesby.bludoc.ModelLayer.ConsentTemplateModel;
import com.likesby.bludoc.ModelLayer.InvoicePresModel;

import java.util.ArrayList;

public class ResponseSuccess {
    @SerializedName("success")
    private String success;

    @SerializedName("message")
    private String message;

    @SerializedName("presb_patient_id")
    private String prescriptionId;

    @SerializedName("doctor_consent_id")
    private String doctorConsentId;

    @SerializedName("Invoice")
    private ArrayList<InvoicePresModel> invoice;

    @SerializedName("Consent")
    private ArrayList<ConsentTemplateModel> consent;

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    //	@SerializedName("subscription_history")
//	private ArrayList<SubscriptionHistoryItem> subscriptionHistory;

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<InvoicePresModel> getInvoice() {
        return invoice;
    }

    public void setInvoice(ArrayList<InvoicePresModel> invoice) {
        this.invoice = invoice;
    }

    public ArrayList<ConsentTemplateModel> getConsent() {
        return consent;
    }

    public void setConsent(ArrayList<ConsentTemplateModel> consent) {
        this.consent = consent;
    }

    public String getDoctorConsentId() {
        return doctorConsentId;
    }

    public void setDoctorConsentId(String doctorConsentId) {
        this.doctorConsentId = doctorConsentId;
    }

    @Override
    public String toString() {
        return "ResponseSuccess{" +
                "success='" + success + '\'' +
                ", message='" + message + '\'' +
                ", prescriptionId='" + prescriptionId + '\'' +
                '}';
    }
}

