package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class ResponseSuccess {
        @SerializedName("success")
        private String success;


        @SerializedName("message")
        private String message;


    @SerializedName("presb_patient_id")
    private String prescriptionId;

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }


    //	@SerializedName("subscription_history")
//	private ArrayList<SubscriptionHistoryItem> subscriptionHistory;

        public void setSuccess(String success){
            this.success = success;
        }

        public String getSuccess(){
            return success;
        }


        public void setMessage(String message){
            this.message = message;
        }

        public String getMessage(){
            return message;
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

