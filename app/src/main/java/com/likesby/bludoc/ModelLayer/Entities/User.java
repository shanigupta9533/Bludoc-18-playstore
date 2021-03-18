package com.likesby.bludoc.ModelLayer.Entities;

import com.google.gson.annotations.SerializedName;

public class User{

    @SerializedName("pincode")
    private String pincode;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @SerializedName("country_id")
    private String countryId;

    @SerializedName("customer_mobile")
    private String customerMobile;

    @SerializedName("address")
    private String address;

    @SerializedName("role")
    private String role;

    @SerializedName("department_id")
    private String departmentId;

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @SerializedName("invite_code")
    private String inviteCode;

    @SerializedName("city")
    private String city;

    @SerializedName("designation_id")
    private String designationId;

    @SerializedName("fb_id")
    private String fbId;

    @SerializedName("parent_id")
    private String parentId;

    @SerializedName("customer_email")
    private String customerEmail;

    @SerializedName("customer_name")
    private String customerName;

    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("gmail_id")
    private String gmailId;

    @SerializedName("customer_image")
    private String customerImage;

    @SerializedName("status")
    private String status;

    public void setPincode(String pincode){
        this.pincode = pincode;
    }

    public String getPincode(){
        return pincode;
    }

    public void setCustomerMobile(String customerMobile){
        this.customerMobile = customerMobile;
    }

    public String getCustomerMobile(){
        return customerMobile;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return address;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }

    public void setDepartmentId(String departmentId){
        this.departmentId = departmentId;
    }

    public String getDepartmentId(){
        return departmentId;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getCity(){
        return city;
    }

    public void setDesignationId(String designationId){
        this.designationId = designationId;
    }

    public String getDesignationId(){
        return designationId;
    }

    public void setFbId(String fbId){
        this.fbId = fbId;
    }

    public String getFbId(){
        return fbId;
    }

    public void setParentId(String parentId){
        this.parentId = parentId;
    }

    public String getParentId(){
        return parentId;
    }

    public void setCustomerEmail(String customerEmail){
        this.customerEmail = customerEmail;
    }

    public String getCustomerEmail(){
        return customerEmail;
    }

    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }

    public String getCustomerName(){
        return customerName;
    }

    public void setCustomerId(String customerId){
        this.customerId = customerId;
    }

    public String getCustomerId(){
        return customerId;
    }

    public void setGmailId(String gmailId){
        this.gmailId = gmailId;
    }

    public String getGmailId(){
        return gmailId;
    }

    public void setCustomerImage(String customerImage){
        this.customerImage = customerImage;
    }

    public String getCustomerImage(){
        return customerImage;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    @Override
    public String toString(){
        return
                "User{" +
                        "pincode = '" + pincode + '\'' +
                        ",customer_mobile = '" + customerMobile + '\'' +
                        ",address = '" + address + '\'' +
                        ",role = '" + role + '\'' +
                        ",department_id = '" + departmentId + '\'' +
                        ",city = '" + city + '\'' +
                        ",designation_id = '" + designationId + '\'' +
                        ",fb_id = '" + fbId + '\'' +
                        ",invite_code = '" + inviteCode + '\'' +
                        ",parent_id = '" + parentId + '\'' +
                        ",customer_email = '" + customerEmail + '\'' +
                        ",customer_name = '" + customerName + '\'' +
                        ",customer_id = '" + customerId + '\'' +
                        ",gmail_id = '" + gmailId + '\'' +
                        ",country_id = '" + countryId + '\'' +
                        ",customer_image = '" + customerImage + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}