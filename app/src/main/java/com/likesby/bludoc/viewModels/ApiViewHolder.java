package com.likesby.bludoc.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.likesby.bludoc.ModelLayer.Entities.CmsResponse;
import com.likesby.bludoc.ModelLayer.Entities.FaqsResponse;
import com.likesby.bludoc.ModelLayer.Entities.History;
import com.likesby.bludoc.ModelLayer.Entities.OtpResponse;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.Entities.ProfileDetails;
import com.likesby.bludoc.ModelLayer.Entities.Response;
import com.likesby.bludoc.ModelLayer.Entities.ResponseAddPatient;
import com.likesby.bludoc.ModelLayer.Entities.ResponseAddUG;
import com.likesby.bludoc.ModelLayer.Entities.ResponseBanners;
import com.likesby.bludoc.ModelLayer.Entities.ResponseDesignation;
import com.likesby.bludoc.ModelLayer.Entities.ResponseLabTest;
import com.likesby.bludoc.ModelLayer.Entities.ResponseMedicines;
import com.likesby.bludoc.ModelLayer.Entities.ResponsePG;
import com.likesby.bludoc.ModelLayer.Entities.ResponsePatients;
import com.likesby.bludoc.ModelLayer.Entities.ResponseRegister;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSpecialities;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSubscription;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.Entities.ResponseTemplates;
import com.likesby.bludoc.ModelLayer.Entities.ResponseUG;
import com.likesby.bludoc.ModelLayer.Entities.ResponseVersion;
import com.likesby.bludoc.ModelLayer.Entities.Subscription;
import com.likesby.bludoc.ModelLayer.Entities.UserResponse;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.ApiClient;
import com.likesby.bludoc.ModelLayer.NetworkLayer.NetworkLayer;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseNewMyTemplates;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseSubscriptions;
import com.likesby.bludoc.ModelLayer.NewEntities2.ResponseLabTemplates;
import com.likesby.bludoc.ModelLayer.NewEntities3.ResponseHistory;

import io.reactivex.Single;

public class ApiViewHolder extends AndroidViewModel {
    private NetworkLayer networkLayer;

    public ApiViewHolder(@NonNull Application application) {
        super(application);
        this.networkLayer = NetworkLayer.getInstance(application.getApplicationContext());
    }



    //region CabTabDetails API Call
    public Single<ResponseRegister> setUserLogin(String email,String hospitalCode) {
        return networkLayer.userLogin(email, hospitalCode);
    }
    //endregion


    public Single<ResponseProfileDetails> updateHospitalCode(String doctor_id,String hospital_code   ){
        return networkLayer.updateHospitalCode( doctor_id,hospital_code);
    }
    //endregion


    //region CabTabDetails API Call
    public Single<OtpResponse> getOtpFromBackEnd(String email){
        return networkLayer.getOtpFromBackEnd( email);
    }
    //endregion



    public Single<UserResponse> setUserUpdate(String customer_id, String name, String email, String mobile, String address, String city, String pincode, String department_id, String designation_id, String fb_id, String gmail_id, String image) {
    return networkLayer.setUserUpdate( customer_id, name,  email,  mobile,
            address,  city,  pincode, department_id,  designation_id, fb_id, gmail_id, image);
    }
    //endregion


    public Single<ResponseRegister> Register1(String doctor_id,String name, String email , String app_id, String mobile   ){
        return networkLayer.Register1( doctor_id,name,  email,    app_id,mobile);
    }
    //endregion

    public Single<ResponseRegister> DeleteLogo(String doctor_id){
        return networkLayer.DeleteLogo( doctor_id);
    }
    //endregion

    public Single<ResponseRegister> Register2(String doctor_id, String registration_no, String designation_name,
                                              String ug_id, String pg_id, String addtional_qualification ){
        return networkLayer.Register2(  doctor_id,  registration_no,  designation_name,  ug_id,pg_id,addtional_qualification);
    }
    //endregion

    public Single<ResponseRegister> registerQualifications(String addtional_qualification,String doctor_id){
        return networkLayer.registerQualifications(addtional_qualification,doctor_id);
    }

    public Single<ResponseRegister> Register3(String doctor_id, String mobile_letter_head,String email_letter_head,
                                              String working_days,String visiting_hr_from, String visiting_hr_to,
                                              String close_day){
        return networkLayer.Register3( doctor_id,  mobile_letter_head, email_letter_head,
                working_days, visiting_hr_from,  visiting_hr_to, close_day);
    }
    //endregion


    public Single<ResponseAddPatient> PatientRegister(String p_name, String age, String gender,
                                                      String created_by, String p_mobile, String p_email,String address,String bloud_group,String dob ){
        return networkLayer.PatientRegister( p_name,  age,  gender,    created_by,  p_mobile,  p_email,address,bloud_group,dob );
    }
    //endregion

    public Single<PatientsItem> PatientRegisterWithPatientItem(String p_name, String age, String gender,
                                                               String created_by, String p_mobile, String p_email, String address, String bloud_group, String dob ){
        return networkLayer.PatientRegisterWithPatientItem( p_name,  age,  gender,    created_by,  p_mobile,  p_email,address,bloud_group,dob );
    }

    public Single<ResponseSuccess> PatientUpdate(  String patient_id,String p_name, String age, String gender,
                                                     String p_mobile, String p_email ,String address,String bloud_group,String dob){
        return networkLayer.PatientUpdate( patient_id, p_name,  age,  gender,  p_mobile,  p_email,address,bloud_group,dob );
    }
    //endregion


    public Single<ResponseRegister> Registe5(String doctor_id, String registration_no, String specialities_id,
                                             String ug_id, String pg_id, String addtional_qualification ){
        return networkLayer.Register5(doctor_id,  registration_no,  specialities_id,  ug_id,pg_id,addtional_qualification);
    }
    //endregion


    public Single<ResponseRegister> DeleteSignature(String doctor_id){
        return networkLayer.DeleteSignature(doctor_id);
    }
    //endregion



    public Single<ResponseDesignation> getDesignations(){
        return networkLayer.getDesignations();
    }
    //endregion


    public Single<ResponsePG> getPGs(){
        return networkLayer.getPGs();
    }
    //endregion


    public Single<ResponseUG> getUGs(){
        return networkLayer.getUGs();
    }
    //endregion

    public Single<ResponseBanners> getBanners(){
        return networkLayer.getBanners();
    }
    //endregion


    public Single<ResponseSubscriptions> getSubscriptions(){
        return networkLayer.getSubscriptions();
    }

    //region CabTabDetails API Call
    public Single<Subscription> mysubscription(String created_by) {
        return networkLayer.mysubscription(created_by);
    }
    //endregion

    public Single<ResponseSpecialities> getSpecialities(){
        return networkLayer.getSpecialities();
    }
    //endregion


    //region CabTabDetails API Call
    public Single<ResponsePatients> getPatients(String created_by) {
        return networkLayer.getPatients(created_by);
    }
    //endregion

    //region CabTabDetails API Call
    public Single<ResponseLabTest> getLabTests(String created_by) {
        return networkLayer.getLabTests(created_by);
    }
    //endregion


    //region CabTabDetails API Call
    public Single<ResponseMedicines> getMedicines(String created_by) {
        return networkLayer.getMedicines(created_by);
    }
    //endregion


    //region CabTabDetails API Call
    public Single<ResponseNewMyTemplates> getTemplates(String created_by) {
        return networkLayer.getTemplates(created_by);
    }
    //endregion


    //region CabTabDetails API Call
    public Single<ResponseLabTemplates> getLabTemplates(String created_by) {
        return networkLayer.getLabTemplates(created_by);
    }
    //endregion


    //region CabTabDetails API Call
    public Single<ResponseSuccess> AddTemplate(String template_name, String created_by, String date, String medicine_name,
                                               String frequency, String no_of_days, String route, String instruction, String additiona_comment)  {
        return networkLayer.AddTemplate(template_name,     created_by,  date,  medicine_name,
                frequency,    no_of_days,  route,  instruction,  additiona_comment);
    }
    //endregion


    //region CabTabDetails API Call
    public Single<ResponseSuccess> Prescription(String json) {
        return networkLayer.Prescription(json);
    }


    //region CabTabDetails API Call
    public Single<ResponseSuccess> sendDataOnInvoices(String json) {
        return networkLayer.sendDataOnInvoices(json);
    }


    //region CabTabDetails API Call
    public Single<ResponseHistory> getAllPrescription(String created_by,String patient_id,String limit,String offset,String search,String type) {
        return networkLayer.getAllPrescription(created_by,patient_id,limit,offset,search,type);
    }
    //endregion

    //region CabTabDetails API Call
    public Single<ResponseSuccess> AddTemplate(String json) {
        return networkLayer.AddTemplate(json);
    }
    //region CabTabDetails API Call


    //region CabTabDetails API Call
    public Single<ResponseSuccess> AddTemplateLabTest(String json) {
        return networkLayer.AddTemplateLabTest(json);
    }
    //region CabTabDetails API Call

    public Single<ResponseProfileDetails> getProfileDetails(String created_by) {
        return networkLayer.getProfileDetails(created_by);
    }
    //endregion

    //region CabTabDetails API Call
    public Single<Response> deleteProfile(String created_by) {
        return networkLayer.deleteProfile(created_by);
    }
    //endregion



    //region CabTabDetails API Call
    public Single<ResponseSuccess> getDeleteTemplate(String created_by) {
        return networkLayer.getDeleteTemplate(created_by);
    }
    //endregion


    //region CabTabDetails API Call
    public Single<ResponseSuccess> getDeleteTemplateLabTest(String created_by) {
        return networkLayer.getDeleteTemplateLabTest(created_by);
    }
    //endregion

    //region CabTabDetails API Call
    public Single<ResponseSuccess> deletePatient(String patient_id) {
        return networkLayer.deletePatient(patient_id);
    }
    //endregion

    //region CabTabDetails API Call
    public Single<Response> Feedback(String customer_id, String type, String subject, String message) {
        return networkLayer.Feedback( customer_id,type, subject, message);
    }
    //endregion

    //region CabTabDetails API Call
    public Single<ResponseSubscription> AddSubscription(String doctor_id, String subscription_id, String payment_status,
                                                        String transaction_id, String transaction_amount) {
        return networkLayer.AddSubscription( doctor_id,  subscription_id, payment_status,
                transaction_id,  transaction_amount);
    }
    //endregion

    //region CabTabDetails API Call
    public Single<CmsResponse> Cms(String slug) {
        return networkLayer.Cms( slug);
    }
    //endregion

    //region CabTabDetails API Call
    public Single<ResponseAddUG> AddUG(String ug_name) {
        return networkLayer.AddUG( ug_name);
    }
    //endregion

    public Single<FaqsResponse> getFaqs() {
        return networkLayer.getFaqs();
    }

    public Single<ResponseVersion> getVersion() {
        return networkLayer.getVersion();
    }
}
