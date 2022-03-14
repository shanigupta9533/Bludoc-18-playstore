package com.likesby.bludoc.ModelLayer.NetworkLayer;

import android.content.Context;

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
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseNewMyTemplates;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseSubscriptions;
import com.likesby.bludoc.ModelLayer.NewEntities2.ResponseLabTemplates;
import com.likesby.bludoc.ModelLayer.NewEntities3.ResponseHistory;

import io.reactivex.Single;
import retrofit2.http.Field;

public class NetworkLayer {

    WebServices services;

    //region Retrofit Instance

    //region Init NetworkLayer Instance
    public static volatile NetworkLayer instance;
    private static final Object LOCK = new Object();
    private static Context context;
    //endregion

    //region Handle NetworkLayer Instance
    public static NetworkLayer getInstance(Context mContext) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    context = mContext;
                    instance = new NetworkLayer();
                }
            }
        }
        return instance;
    }

    public NetworkLayer() {
        services = ApiClient.createService(WebServices.class);
    }

   /* // region Varify User
    public Single<Register> getLogin(String mobile) {
        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
        return services.getLogin(mobile);
    }
    // endregion*/

    // region Register User
    public Single<ResponseRegister> userLogin(String email,String hospitalCode) {
        services = ApiClient.createService(WebServices.class);
        return services.userLogin(email, hospitalCode);
    }
    // endregion

    public Single<ResponseProfileDetails> updateHospitalCode(String doctor_id,String hospital_code ){
        services = ApiClient.createService(WebServices.class);
        return services.updateHospitalCode(doctor_id, hospital_code);
    }



    // region Register User
    public Single<OtpResponse> getOtpFromBackEnd(String email){
        services = ApiClient.createService(WebServices.class);
        return services.getOtpFromBackEnd(email );
    }
    // endregion


//    // region Register User
//    public Single<SubscriptionHistoryResponse> customerSubscription(String customer_id, String subscription_id, String type, String promo_code, String offer_id,String discount, String final_amount){
//        services = ApiClient.createService(WebServices.class);
//        return services.customerSubscription( customer_id,subscription_id,type,promo_code,  offer_id, discount,  final_amount);
//    }
//    // endregion
//
//    // region Register User
//    public Single<PromoCheckResponse> promoCheck(String customer_id, String total, String promocode) {
//        services = ApiClient.createService(WebServices.class);
//        return services.promoCheck( customer_id, total,  promocode);
//    }
//    // endregion
//
//    // region Register User
//    public Single<Response> customerSubscriptionUpdate(String customers_subscription_id, String payment_id){
//        services = ApiClient.createService(WebServices.class);
//        return services.customerSubscriptionUpdate(  customers_subscription_id,  payment_id);
//    }
//    // endregion
//
//    // region Register User
//    public Single<PaymentResponse> setPayment(String customer_id, String customer_subscription_id,
//                                              String transaction_id, String status, String amount){
//        services = ApiClient.createService(WebServices.class);
//        return services.setPayment( customer_id,  customer_subscription_id,
//                transaction_id, status, amount);
//    }
//    // endregion
//
//
//    // region Register User
//    public Single<Response> setLocation(String json){
//        services = ApiClient.createService(WebServices.class);
//        return services.setLocation(json);
//    }
//    // endregion
//
//
//    // region Register User
//    public Single<Response> setPushResponse(String tracking_id, String customer_id, String parent_id, String category_id, String type){
//        services = ApiClient.createService(WebServices.class);
//        return services.invitePushResponse( tracking_id,  customer_id,  parent_id,  category_id,type);
//    }
//    // endregion.
//
//    // region Register User
//    public Single<Response> AcceptOtherPlatform(String invite_code, String customer_id,  String category_id,
//                                                String start_date, String end_date, String start_time, String end_time){
//        services = ApiClient.createService(WebServices.class);
//        return services.AcceptOtherPlatform( invite_code,  customer_id,  category_id,
//                start_date, end_date, start_time, end_time);
//    }
//    // endregion
//
//
//    // region Register User
//    public Single<LastLocationResponse> liveLocation(String parent_id, String category_id){
//        services = ApiClient.createService(WebServices.class);
//        return services.LastLocation(   parent_id,  category_id);
//    }
//    // endregion
//
//    // region Register User
//    public Single<MemberLastLocationResponse> MemberLastLocation(String json){
//        services = ApiClient.createService(WebServices.class);
//        return services.MemberLastLocation(   json);
//    }
//    // endregion
//
//    // region Register User
//    public Single<Response> stopTracking(String customer_id,String tracking_id){
//        services = ApiClient.createService(WebServices.class);
//        return services.stopTracking(customer_id, tracking_id);
//    }
//    // endregion
//
//    // region Register User
//    public Single<CmsResponse> Cms(String slug){
//        services = ApiClient.createService(WebServices.class);
//        return services.Cms( slug);
//    }
//    // endregion
//
//    // region Register User
//    public Single<UserResponse> ProfileDetails(String tracking_id){
//        services = ApiClient.createService(WebServices.class);
//        return services.ProfileDetails( tracking_id);
//    }
//    // endregion
//
//    // region Register User
//    public Single<PendingsResponse> CustomerInviteList(String customer_id){
//        services = ApiClient.createService(WebServices.class);
//        return services.CustomerInviteList( customer_id);
//    }
//    // endregion
//
//
//// region Register User
//    public Single<SubscriptionDetailsResponse> subscriptionDetails(String customer_id, String category_id){
//        services = ApiClient.createService(WebServices.class);
//        return services.subscriptionDetails( customer_id, category_id);
//    }
//    // endregion
//
//
//
//    // region Register User
//    public Single<PinLocationsResponse> pinLocations(String tracking_id){
//        services = ApiClient.createService(WebServices.class);
//        return services.PinLocations( tracking_id);
//    }
//    // endregion
//
//    // region Register User
//    public Single<DepartmentResponse> getDepartments() {
//        services = ApiClient.createService(WebServices.class);
//        return services.getDepartments();
//    }
//
    public Single<UserResponse> setUserUpdate(String customer_id, String name, String email, String mobile, String address, String city, String pincode, String department_id, String designation_id, String fb_id, String gmail_id, String image) {
        services = ApiClient.createService(WebServices.class);
        return services.updateUser( customer_id, name,  email,  mobile,
                 department_id,  designation_id, fb_id, gmail_id, image);
    }


    public Single<ResponseRegister> Register1(String doctor_id,String name, String email , String app_id  ,String mobile ){
        services = ApiClient.createService(WebServices.class);
        return services.Register1(doctor_id, name,  email,  app_id,mobile);
    }

    public Single<ResponseRegister> DeleteLogo(String doctor_id){
        services = ApiClient.createService(WebServices.class);
        return services.DeleteLogo(doctor_id);
    }

    public Single<ResponseRegister> Register2(String doctor_id, String registration_no, String designation_name,
                                              String ug_id, String pg_id, String addtional_qualification ){
        services = ApiClient.createService(WebServices.class);
        return services.Register2( doctor_id,  registration_no,  designation_name,  ug_id,pg_id,addtional_qualification);
    }

    public Single<ResponseRegister> registerQualifications(String addtional_qualification, String doctor_id){
        services = ApiClient.createService(WebServices.class);
        return services.registerQualification(addtional_qualification,doctor_id);
    }

    public Single<ResponseRegister> Register3(String doctor_id, String mobile_letter_head,String email_letter_head,
                                             String working_days,String visiting_hr_from, String visiting_hr_to,
                                              String close_day){
        services = ApiClient.createService(WebServices.class);
        return services.Register3( doctor_id,  mobile_letter_head, email_letter_head,
                 working_days, visiting_hr_from,  visiting_hr_to, close_day);
    }



    public Single<ResponseAddPatient> PatientRegister(String p_name, String age, String gender,
                                                      String created_by, String p_mobile, String p_email,String address,String bloud_group,String dob ){
        services = ApiClient.createService(WebServices.class);
        return services.PatientRegister( p_name,  age,  gender,    created_by,  p_mobile,  p_email, address,bloud_group,dob ) ;
    }

    public Single<PatientsItem> PatientRegisterWithPatientItem(String p_name, String age, String gender,
                                                               String created_by, String p_mobile, String p_email, String address, String bloud_group, String dob ){
        services = ApiClient.createService(WebServices.class);
        return services.PatientRegisterWithPatientItem( p_name,  age,  gender,    created_by,  p_mobile,  p_email, address,bloud_group,dob ) ;
    }

    public Single<ResponseSuccess> PatientUpdate(String patient_id, String p_name, String age, String gender,
                                 String p_mobile, String p_email,String address,String bloud_group,String dob ){
        services = ApiClient.createService(WebServices.class);
        return services.PatientUpdate( patient_id, p_name,  age,  gender,     p_mobile,  p_email,address,bloud_group,dob );
    }

    public Single<ResponseRegister> Register5(String doctor_id, String registration_no, String specialities_id,
                                              String ug_id, String pg_id, String addtional_qualification ){
        services = ApiClient.createService(WebServices.class);
        return services.Register5( doctor_id,  registration_no,  specialities_id,  ug_id,pg_id,addtional_qualification);
    }

    public Single<ResponseRegister> DeleteSignature(String doctor_id){
        services = ApiClient.createService(WebServices.class);
        return services.DeleteSignature( doctor_id);
    }


    // region Register User
    public Single<ResponseDesignation> getDesignations() {
        services = ApiClient.createService(WebServices.class);
        return services.getDesignations();
    }


    // region Register User
    public Single<ResponsePG> getPGs() {
        services = ApiClient.createService(WebServices.class);
        return services.getPGs();
    }


    // region Register User
    public Single<ResponseUG> getUGs() {
        services = ApiClient.createService(WebServices.class);
        return services.getUGs();
    }


    // region Register User
    public Single<ResponseBanners> getBanners() {
        services = ApiClient.createService(WebServices.class);
        return services.getBanners();
    }



    // region Register User
    public Single<ResponseSubscriptions> getSubscriptions() {
        services = ApiClient.createService(WebServices.class);
        return services.getSubscriptions();
    }


    // region Register User
    public Single<Subscription> mysubscription(String created_by) {
        services = ApiClient.createService(WebServices.class);
        return services.mysubscription(created_by);
    }
    // endregion


    // region Register User
    public Single<ResponseSpecialities> getSpecialities() {
        services = ApiClient.createService(WebServices.class);
        return services.getSpecialities();
    }


    // region Register User
    public Single<ResponsePatients> getPatients(String created_by) {
        services = ApiClient.createService(WebServices.class);
        return services.getPatients(created_by);
    }
    // endregion

    // region Register User
    public Single<ResponseLabTest> getLabTests(String created_by) {
        services = ApiClient.createService(WebServices.class);
        return services.getLabTests(created_by);
    }
    // endregion

    // region Register User
    public Single<ResponseMedicines> getMedicines(String created_by) {
        services = ApiClient.createService(WebServices.class);
        return services.getMedicines(created_by);
    }
    // endregion

    // region Register User
    public Single<ResponseNewMyTemplates> getTemplates(String created_by) {
        services = ApiClient.createService(WebServices.class);
        return services.getTemplates(created_by);
    }
    // endregion
    //
    // region Register User
    public Single<ResponseLabTemplates> getLabTemplates(String created_by) {
        services = ApiClient.createService(WebServices.class);
        return services.getLabTemplates(created_by);
    }
    // endregion


    // region Register User
    public Single<ResponseSuccess> AddTemplate(String template_name, String created_by, String date, String medicine_name,
                                               String frequency, String no_of_days, String route, String instruction, String additiona_comment) {
        services = ApiClient.createService(WebServices.class);
        return services.AddTemplate(template_name,     created_by,  date,  medicine_name,
                 frequency,    no_of_days,  route,  instruction,  additiona_comment);
    }
    // endregion


    // region Register User
    public Single<ResponseSuccess> AddTemplate(String json) {
        services = ApiClient.createService(WebServices.class);
        return services.AddTemplate(json);
    }


    // region Register User
    public Single<ResponseSuccess> AddTemplateLabTest(String json) {
        services = ApiClient.createService(WebServices.class);
        return services.AddTemplateLabTest(json);
    }



    // region Register User
    public Single<ResponseSuccess> Prescription(String json) {
        services = ApiClient.createService(WebServices.class);
        return services.Prescription(json);
    }


    // region Register User
    public Single<ResponseSuccess> sendDataOnInvoices(String json) {
        services = ApiClient.createService(WebServices.class);
        return services.sendDataOnInvoices(json);
    }


    // region Register User
    public Single<ResponseHistory> getAllPrescription(String created_by,String patient_id,String limit,String offset,String search,String type) {
        services = ApiClient.createService(WebServices.class);
        return services.getAllPrescription(created_by,patient_id,limit,offset,search,type);
    }
    // endregion


    public Single<Response> Feedback(String customer_id, String type, String subject, String message) {
        services = ApiClient.createService(WebServices.class);
        return services.Feedback(  customer_id,type, subject, message);
    }
    // endregion


    public Single<ResponseSubscription> AddSubscription(String doctor_id, String subscription_id, String payment_status,
                                                        String transaction_id, String transaction_amount) {
        services = ApiClient.createService(WebServices.class);
        return services.AddSubscription( doctor_id,  subscription_id, payment_status,
                 transaction_id,  transaction_amount);
    }
    // endregion


    // region Register User
    public Single<CmsResponse> Cms(String slug){
        services = ApiClient.createService(WebServices.class);
        return services.Cms( slug);
    }
    // endregion

    // region Register User
    public Single<ResponseAddUG> AddUG(String ug_name){
        services = ApiClient.createService(WebServices.class);
        return services.AddUG( ug_name);
    }
    // endregion

    // region Register User
    public Single<FaqsResponse> getFaqs() {
        services = ApiClient.createService(WebServices.class);
        return services.getFaqs();
    }


    // region Register User
    public Single<ResponseVersion> getVersion() {
        services = ApiClient.createService(WebServices.class);
        return services.getVersion();
    }

    // region Register User
    public Single<ResponseProfileDetails> getProfileDetails(String created_by) {
        services = ApiClient.createService(WebServices.class);
        return services.getProfileDetails(created_by);
    }
    // endregion

    // region Register User
    public Single<Response> deleteProfile(String created_by) {
        services = ApiClient.createService(WebServices.class);
        return services.deleteProfile(created_by);
    }
    // endregion


    // region Register User
    public Single<ResponseSuccess> getDeleteTemplate(String created_by) {
        services = ApiClient.createService(WebServices.class);
        return services.getDeleteTemplate(created_by);
    }
    // endregion


    // region Register User
    public Single<ResponseSuccess> getDeleteTemplateLabTest(String created_by) {
        services = ApiClient.createService(WebServices.class);
        return services.getDeleteTemplateLabTest(created_by);
    }
    // endregion

    // region Register User
    public Single<ResponseSuccess> deletePatient(String patient_id) {
        services = ApiClient.createService(WebServices.class);
        return services.deletePatient(patient_id);
    }
    // endregion


//    // endregion
//
//    /*public Single<UserResponse> UpdateProfileImage(RequestBody customer_id, RequestBody name, RequestBody email, RequestBody mobile,
//                                                   RequestBody department_id, RequestBody designation_id, RequestBody fb_id, RequestBody gmail_id,
//                                                   RequestBody image,
//                                                   RequestBody country_id, RequestBody address, RequestBody city, RequestBody pincode) {
//        services = ApiClient2.createService(WebServices.class);
//        return services.UpdateProfileImage(  customer_id,  name,  email,  mobile,
//                 department_id,  designation_id,  fb_id,  gmail_id,  image,
//                 country_id, address,  city,  pincode);
//    }
//    // endregion*/
//
//

//
//
//
//    public Single<TrackingsResponse> getInviteList(String parent_id,String category_id) {
//        services = ApiClient.createService(WebServices.class);
//        return services.inviteList( parent_id, category_id);
//    }
//    // endregion
//
//    public Single<ReportsResponse> reports(String parent_id, String category_id) {
//        services = ApiClient.createService(WebServices.class);
//        return services.reports( parent_id, category_id);
//    }
//    // endregion
//
//
//    public Single<MyInvitesResponse> MyInvites(String customer_id) {
//        services = ApiClient.createService(WebServices.class);
//        return services.MyInvites( customer_id);
//    }
//    // endregion
//
//
//    public Single<Response> Feedback(String customer_id,String type,String subject,String message) {
//        services = ApiClient.createService(WebServices.class);
//        return services.Feedback(  customer_id,type, subject, message);
//    }
//    // endregion
//
//
//    public Single<SubscriptionDetailsResponse> MySubscriptions(String customer_id) {
//        services = ApiClient.createService(WebServices.class);
//        return services.MySubscriptions( customer_id);
//    }
//    // endregion
//
//    public Single<EmergencyResponse> getEmergencyContact(String customer_id) {
//        services = ApiClient.createService(WebServices.class);
//        return services.getEmergencyContact( customer_id);
//    }
//    // endregion
//
//    public Single<Response> sendEmergencyAlert(String parent_id) {
//        services = ApiClient.createService(WebServices.class);
//        return services.sendEmergencyAlert( parent_id);
//    }
//    // endregion
//
//    public Single<Response> addEmergencyContact(String json) {
//        services = ApiClient.createService(WebServices.class);
//        return services.addEmergencyContact( json);
//    }
//    // endregion
//
//
//    // region Register User
//    public Single<CountryResponse> getCountries() {
//        services = ApiClient.createService(WebServices.class);
//        return services.getCountries();
//    }
//
//    // region Register User
//    public Single<BannerResponse> getBanners() {
//        services = ApiClient.createService(WebServices.class);
//        return services.getBanners();
//    }
//
//    // region Register User
//    public Single<PaymentOrderResponse> getPaymentOrderId(String amount,String currency,String receipt,String payment_capture) {
//        services = ApiClient.createService(WebServices.class);
//        return services.getPaymentOrderId( amount, currency, receipt, payment_capture);
//    }
//
//    // region Register User
//    public Single<TrackingsResponse> setCustomerInvite(String json) {
//        services = ApiClient.createService(WebServices.class);
//        return services.customerInvite(json);
//    }
//
//    // region Register User
//    public Single<PinLocationResponse> addPinLocation(String json) {
//        services = ApiClient.createService(WebServices.class);
//        return services.addPinLocation(json);
//    }
//
//    // region Register User
//    public Single<Response> MultipleStopTracking(String json) {
//        services = ApiClient.createService(WebServices.class);
//        return services.MultipleStopTracking(json);
//    }
//
//    // region Register User
//    public Single<Response> resendInvite(String json) {
//        services = ApiClient.createService(WebServices.class);
//        return services.resendInvite(json);
//    }
//
//
//    // region Register User
//    public Single<Response> removePin(String pin_id) {
//        services = ApiClient.createService(WebServices.class);
//        return services.removePin(pin_id);
//    }
//
//
//
//    // region Register User
//    public Single<Response>removeInvite(String tracking_id){
//        services = ApiClient.createService(WebServices.class);
//        return services.removeInvite(tracking_id);
//    }
//
//    // region Register User
//    public Single<ActiveTrackingsResponse> activeTrackings(String customer_id, String category_id) {
//        services = ApiClient.createService(WebServices.class);
//        return services.activeTrackings(customer_id,category_id);
//    }
//
//    // region Register User
//    public Single<Response> RemoveEmergencyContact( String contact_id,String emergency_mobile) {
//        services = ApiClient.createService(WebServices.class);
//        return services.RemoveEmergencyContact(contact_id,emergency_mobile);
//    }
//
//    // region Register User
//    public Single<FaqsResponse> getFaqs() {
//        services = ApiClient.createService(WebServices.class);
//        return services.getFaqs();
//    }
//
//
//    /*// region Varify User
//    public Single<Register> varifyUser(String mobile) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.varifyUser(mobile);
//    }
//    // endregion
//
//
//    public Single<Cab> addCabTabDetailsViewHolder(String cab_tab_driver_mobile) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getCabTabDetails(cab_tab_driver_mobile);
//    }
//
//    public Single<Cab> getCabTabByDriverImei(String cab_tab_imei) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getCabTabByDriverImei(cab_tab_imei);
//    }
//
//
//
//    public Single<Video> getVideoListByPincode(String pincode) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getVideoListByPincode(pincode);
//    }
//
//    public Single<Campaign> getCampaignByPincode(String pincode,int age,String gender) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getCampaignListByPincode(pincode, age, gender);
//    }
//
//    public Single<CampaignOffer> getCampaignOfferByPincode(String pincode, int age, String gender) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getCampaignListOfferByPincode(pincode, age, gender);
//    }
//
//
//    public Single<Campaign> getCampaignFillerByPincode(String pincode) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getCampaignListFillerByPincode(pincode);
//    }
//
//    public Single<CountryModel> getCountryModel() {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getCountry();
//    }
//
//    public Single<CityModel> getCityModel() {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getCity();
//    }
//
//    public Single<StateModel> getStateModel() {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getState();
//    }
//
//    public Single<PincodeModel> getPincodeModel() {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.GetPincode();
//    }
//
//    public Single<Map> getTime() {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.GetTime();
//    }
//
//
//    public Single<VideoLogResponse> setVideoLogJson(String videoLog) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.addVideoLogJson(videoLog);
//    }
//
//
// public Single<OfferLogResponse> setOfferLogJson(String offerLog) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.addOfferLogJson(offerLog);
//    }
//
//*/
//    /*public Single<ProductResponse> getProduct() {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getProduct();
//    }
//
//    public Single<Address> updateAddress(String address_id, String user_id, String address_name, String phone, String address_type, String address_line1, String address_line2, String landmark, String city, String state, String country, String pincode) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.updateAddress(address_id , user_id , address_name ,  phone ,  address_type ,  address_line1 ,  address_line2 ,  landmark ,  city , state , country,  pincode);
//    }
//
//    public Single<Coupon> applyCoupon(String coupon_code) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.applyCoupon(coupon_code);
//    }
//
//    public Single<SubscriptionModifiedData> getSubscriptionProduct(String userId) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getSubscriptionOrder(userId);
//    }
//
//    public Single<OrderSubscription> getSubscription(String userId) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getSubscription(userId);
//    }*/
//
//    /*public Single<ResponseModel> proceedToCheckout(String userId, int couponId, int parseInt, String repeat_status_id, int addressId, String repeat, String order_status, String order_payment_mode, String order_payment_status, String orderDate, String total, String subtotal, String coupon_savings_amount, String startDate, String quantity, String doorBell) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.proceedToCheckout(userId,  couponId,  parseInt,  repeat_status_id,  addressId,  repeat,  order_status,  order_payment_mode,  order_payment_status,  orderDate,  total ,  subtotal,  coupon_savings_amount ,  startDate,  quantity,  doorBell);
//    }
//
//    public Single<Coupon> couponUsed(int coupon_id, String userId, int orderId, String orderDate, String couponValue) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.couponUsed(coupon_id,  userId,  orderId,  orderDate,  couponValue);
//    }
//
//    public Single<Address> getAddress(String userId) {
//        services = ApiClient.createService(WebServices.class, Utils.getToken(context),Utils.getUserId(context));
//        return services.getAddress(userId);
//    }*/
}
