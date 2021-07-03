package com.likesby.bludoc.ModelLayer.InvoicesModel;

import android.os.Parcel;
import android.os.Parcelable;

public class InvoicesDataModel implements Parcelable {

    private String invoicesTitle;
    private String item_name;
    private String amount;

    public InvoicesDataModel(){

    }

    protected InvoicesDataModel(Parcel in) {
        invoicesTitle = in.readString();
        item_name = in.readString();
        amount = in.readString();
    }

    public String getInvoicesTitle() {
        return invoicesTitle;
    }

    public void setInvoicesTitle(String invoicesTitle) {
        this.invoicesTitle = invoicesTitle;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static final Creator<InvoicesDataModel> CREATOR = new Creator<InvoicesDataModel>() {
        @Override
        public InvoicesDataModel createFromParcel(Parcel in) {
            return new InvoicesDataModel(in);
        }

        @Override
        public InvoicesDataModel[] newArray(int size) {
            return new InvoicesDataModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invoicesTitle);
        dest.writeString(item_name);
        dest.writeString(amount);
    }
}
