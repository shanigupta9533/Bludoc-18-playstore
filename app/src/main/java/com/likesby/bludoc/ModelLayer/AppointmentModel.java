package com.likesby.bludoc.ModelLayer;

public class AppointmentModel {

    private String app_id;
    private String doctor_id;
    private String doc_name="";
    private String reg_no="";
    private String qualification;
    private String cont_no;
    private String mail_id;
    private String about_me;
    private String clinic_name;
    private String clinic_add;
    private String cons_charges;
    private String visiting_hr_from;
    private String visiting_hr_to;
    private String profile_image;
    private String clinic_logo;
    private String qr_code;
    private String g_pay;
    private String phone_pe;
    private String amazon;
    private String upi_id;
    private String pmt_link;
    private String paytm;
    private String note;
    private String success;
    private String message;
    private String currency;
    private String days;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getApp_id() {
        return app_id;
    }

    public String getPaytm() {
        return paytm;
    }

    public void setPaytm(String paytm) {
        this.paytm = paytm;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getSuccess() {
        return success;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getDoctor_id() {
        return doctor_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getDoc_name() {
        return doc_name;
    }

    public void setDoc_name(String doc_name) {
        this.doc_name = doc_name;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getCont_no() {
        return cont_no;
    }

    public void setCont_no(String cont_no) {
        this.cont_no = cont_no;
    }

    public String getMail_id() {
        return mail_id;
    }

    public void setMail_id(String mail_id) {
        this.mail_id = mail_id;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getClinic_add() {
        return clinic_add;
    }

    public void setClinic_add(String clinic_add) {
        this.clinic_add = clinic_add;
    }

    public String getCons_charges() {
        return cons_charges;
    }

    public void setCons_charges(String cons_charges) {
        this.cons_charges = cons_charges;
    }

    public String getVisiting_hr_from() {
        return visiting_hr_from;
    }

    public void setVisiting_hr_from(String visiting_hr_from) {
        this.visiting_hr_from = visiting_hr_from;
    }

    public String getVisiting_hr_to() {
        return visiting_hr_to;
    }

    public void setVisiting_hr_to(String visiting_hr_to) {
        this.visiting_hr_to = visiting_hr_to;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getClinic_logo() {
        return clinic_logo;
    }

    public void setClinic_logo(String clinic_logo) {
        this.clinic_logo = clinic_logo;
    }

    public String getQr_code() {
        return qr_code;
    }

    public void setQr_code(String qr_code) {
        this.qr_code = qr_code;
    }

    public String getG_pay() {
        return g_pay;
    }

    public void setG_pay(String g_pay) {
        this.g_pay = g_pay;
    }

    public String getPhone_pe() {
        return phone_pe;
    }

    public void setPhone_pe(String phone_pe) {
        this.phone_pe = phone_pe;
    }

    public String getAmazon() {
        return amazon;
    }

    public void setAmazon(String amazon) {
        this.amazon = amazon;
    }

    public String getUpi_id() {
        return upi_id;
    }

    public void setUpi_id(String upi_id) {
        this.upi_id = upi_id;
    }

    public String getPmt_link() {
        return pmt_link;
    }

    public void setPmt_link(String pmt_link) {
        this.pmt_link = pmt_link;
    }

    @Override
    public String toString() {
        return "AppointmentModel{" +
                "app_id='" + app_id + '\'' +
                ", doctor_id='" + doctor_id + '\'' +
                ", doc_name='" + doc_name + '\'' +
                ", reg_no='" + reg_no + '\'' +
                ", qualification='" + qualification + '\'' +
                ", cont_no='" + cont_no + '\'' +
                ", mail_id='" + mail_id + '\'' +
                ", about_me='" + about_me + '\'' +
                ", clinic_name='" + clinic_name + '\'' +
                ", clinic_add='" + clinic_add + '\'' +
                ", cons_charges='" + cons_charges + '\'' +
                ", visiting_hr_from='" + visiting_hr_from + '\'' +
                ", visiting_hr_to='" + visiting_hr_to + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", clinic_logo='" + clinic_logo + '\'' +
                ", qr_code='" + qr_code + '\'' +
                ", g_pay='" + g_pay + '\'' +
                ", phone_pe='" + phone_pe + '\'' +
                ", amazon='" + amazon + '\'' +
                ", upi_id='" + upi_id + '\'' +
                ", pmt_link='" + pmt_link + '\'' +
                ", success='" + success + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
