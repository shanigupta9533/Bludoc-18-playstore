package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class ResponseSubscription {
        @SerializedName("success")
        private String success;


        @SerializedName("message")
        private String message;


    public String getEnd_date() {
        return end;
    }

    public void setEnd_date(String end) {
        this.end = end;
    }

    @SerializedName("end")
    private String end;

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
        public String toString(){
            return
                    "ResponseSuccess{" +
                            "success = '" + success + '\'' +
                            ",message = '" + message + '\'' +
                            ",end = '" + end + '\'' +
                            "}";
        }
}

