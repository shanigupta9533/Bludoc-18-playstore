package com.likesby.bludoc.ModelLayer;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.likesby.bludoc.ModelLayer.InvoicesModel.InvoicesDataModel;

import java.util.ArrayList;

public class InvoicePresModel implements Parcelable {
    
    private String invoice_name ="";
    private String doctor_id ="";
    private String patient_id ="";
    private String invoice_title ="";
    private String invoice_number ="";
    private String currency="";
    private String discount_title ="";
    private String advance_amount_title ="";
    private String tax_title ="";
    private String note="";
    private String discount ="";
    private String advance_amount ="";
    private String final_amount ="";
    private String tax ="";
    private String pay_status ="";
    private String total_amount ="";
    private String discount_percentage ="";
    private String advance_amount_percentage ="";
    private String tax_percentage ="";
    private String p_mobile ="";
    private String p_email ="";
    private String p_address ="";
    private String p_name ="";
    private String hospital_code;
    private String status="Completed";
    private ArrayList<InvoicesDataModel> Items;

    public InvoicePresModel(){
        
    }


    protected InvoicePresModel(Parcel in) {
        invoice_name = in.readString();
        doctor_id = in.readString();
        patient_id = in.readString();
        invoice_title = in.readString();
        currency = in.readString();
        discount_title = in.readString();
        advance_amount_title = in.readString();
        tax_title = in.readString();
        note = in.readString();
        discount = in.readString();
        advance_amount = in.readString();
        final_amount = in.readString();
        tax = in.readString();
        pay_status = in.readString();
        total_amount = in.readString();
        discount_percentage = in.readString();
        advance_amount_percentage = in.readString();
        tax_percentage = in.readString();
        p_mobile = in.readString();
        p_email = in.readString();
        p_address = in.readString();
        p_name = in.readString();
        status = in.readString();
        invoice_number = in.readString();
        hospital_code = in.readString();
        Items = in.createTypedArrayList(InvoicesDataModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invoice_name);
        dest.writeString(doctor_id);
        dest.writeString(patient_id);
        dest.writeString(invoice_title);
        dest.writeString(currency);
        dest.writeString(discount_title);
        dest.writeString(advance_amount_title);
        dest.writeString(tax_title);
        dest.writeString(note);
        dest.writeString(discount);
        dest.writeString(advance_amount);
        dest.writeString(final_amount);
        dest.writeString(tax);
        dest.writeString(pay_status);
        dest.writeString(total_amount);
        dest.writeString(discount_percentage);
        dest.writeString(advance_amount_percentage);
        dest.writeString(tax_percentage);
        dest.writeString(p_mobile);
        dest.writeString(p_email);
        dest.writeString(p_address);
        dest.writeString(p_name);
        dest.writeString(status);
        dest.writeString(hospital_code);
        dest.writeString(invoice_number);
        dest.writeTypedList(Items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InvoicePresModel> CREATOR = new Creator<InvoicePresModel>() {
        @Override
        public InvoicePresModel createFromParcel(Parcel in) {
            return new InvoicePresModel(in);
        }

        @Override
        public InvoicePresModel[] newArray(int size) {
            return new InvoicePresModel[size];
        }
    };

    public String getHospital_code() {
        return hospital_code;
    }

    public void setHospital_code(String hospital_code) {
        this.hospital_code = hospital_code;
    }

    public String getInvoice_name() {
        return invoice_name;
    }

    public void setInvoice_name(String invoice_name) {
        this.invoice_name = invoice_name;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getInvoice_title() {
        return invoice_title;
    }

    public void setInvoice_title(String invoice_title) {
        this.invoice_title = invoice_title;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDiscount_title() {
        return discount_title;
    }

    public void setDiscount_title(String discount_title) {
        this.discount_title = discount_title;
    }

    public String getAdvance_amount_title() {
        return advance_amount_title;
    }

    public void setAdvance_amount_title(String advance_amount_title) {
        this.advance_amount_title = advance_amount_title;
    }

    public String getTax_title() {
        return tax_title;
    }

    public void setTax_title(String tax_title) {
        this.tax_title = tax_title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAdvance_amount() {
        return advance_amount;
    }

    public void setAdvance_amount(String advance_amount) {
        this.advance_amount = advance_amount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(String discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getAdvance_amount_percentage() {
        return advance_amount_percentage;
    }

    public void setAdvance_amount_percentage(String advance_amount_percentage) {
        this.advance_amount_percentage = advance_amount_percentage;
    }

    public String getTax_percentage() {
        return tax_percentage;
    }

    public void setTax_percentage(String tax_percentage) {
        this.tax_percentage = tax_percentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<InvoicesDataModel> getItems() {
        return Items;
    }

    public void setItems(ArrayList<InvoicesDataModel> items) {
        Items = items;
    }

    public String getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(String final_amount) {
        this.final_amount = final_amount;
    }

    public String getP_mobile() {
        return p_mobile;
    }

    public void setP_mobile(String p_mobile) {
        this.p_mobile = p_mobile;
    }

    public String getP_email() {
        return p_email;
    }

    public void setP_email(String p_email) {
        this.p_email = p_email;
    }

    public String getP_address() {
        return p_address;
    }

    public void setP_address(String p_address) {
        this.p_address = p_address;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }
}
