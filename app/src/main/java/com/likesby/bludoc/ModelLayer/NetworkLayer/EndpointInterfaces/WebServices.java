package com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces;

import com.likesby.bludoc.ModelLayer.Entities.CmsResponse;
import com.likesby.bludoc.ModelLayer.Entities.FaqsResponse;
import com.likesby.bludoc.ModelLayer.Entities.OtpResponse;
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
import com.likesby.bludoc.ModelLayer.Entities.ResponseUG;
import com.likesby.bludoc.ModelLayer.Entities.ResponseVersion;
import com.likesby.bludoc.ModelLayer.Entities.Subscription;
import com.likesby.bludoc.ModelLayer.Entities.UserResponse;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseNewMyTemplates;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseSubscriptions;
import com.likesby.bludoc.ModelLayer.NewEntities2.ResponseLabTemplates;
import com.likesby.bludoc.ModelLayer.NewEntities3.ResponseHistory;
import com.likesby.bludoc.viewModels.AllPharmacistModels;
import com.likesby.bludoc.viewModels.ResultOfApi;

import java.util.ArrayList;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WebServices {
    @Multipart
    @POST("UpdateProfile")
    Call<ResponseRegister> UpdateProfileImage(@Part("doctor_id") RequestBody doctor_id,
                                              @Part("clinic_name") RequestBody clinic_name,
                                              @Part("clinic_address") RequestBody clinic_address,
                                              @Part("definer") RequestBody signature,
                                              @Part MultipartBody.Part file);


    @Multipart
    @POST("UpdateProfile")
    Call<ResponseRegister> UpdateProfileImage22(@Part("doctor_id") RequestBody doctor_id,
                                                @Part("clinic_name") RequestBody clinic_name,
                                                @Part("clinic_address") RequestBody clinic_address,
                                                @Part("definer") RequestBody signature);

    @Multipart
    @POST("UpdateProfile")
    Call<ResponseRegister> UpdateProfileSign(@Part("doctor_id") RequestBody doctor_id,
                                             @Part("definer") RequestBody image,
                                             @Part MultipartBody.Part file);

    @Multipart
    @POST("Mail")
    Call<ResponseRegister> sendEmailPrescription(@Part("email") RequestBody email,
                                                 @Part("subject") RequestBody subject,
                                                 @Part("message") RequestBody message,

                                                 @Part ArrayList<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST("UpdateProfileImage")
    Single<UserResponse> UpdateProfileImage2(@Field("customer_id") String customer_id,
                                             @Field("customer_name") String customer_name,
                                             @Field("customer_email") String customer_email,
                                             @Field("customer_mobile") String customer_mobile,
                                             @Field("department_id") String department_id,
                                             @Field("designation_id") String designation_id,
                                             @Field("fb_id") String fb_id,
                                             @Field("gmail_id") String gmail_id,
                                             @Part MultipartBody.Part file,
                                             @Field("country_id") String country_id,
                                             @Field("address") String address,
                                             @Field("city") String city,
                                             @Field("pincode") String pincode
    );

    @FormUrlEncoded
    @POST("AddPharmacist")
    Call<ResultOfApi> addPharmacist(@Field("doctor_id") String doctor_id,
                                    @Field("pharmacist_name") String pharmacist_name,
                                    @Field("pharmacist_mobile") String pharmacist_mobile,
                                    @Field("pharmacist_email") String pharmacist_email);

    @FormUrlEncoded
    @POST("AllPharmacist")
    Call<AllPharmacistModels> allPharmacist(@Field("doctor_id") String doctor_id);

    @FormUrlEncoded
    @POST("EditPharmacist")
    Call<ResultOfApi> editPharmacist(@Field("pharmacist_id") String pharmacist_id,
                                     @Field("pharmacist_name") String pharmacist_name,
                                     @Field("pharmacist_mobile") String pharmacist_mobile,
                                     @Field("pharmacist_email") String pharmacist_email);

    @FormUrlEncoded
    @POST("DeletePharmacist")
    Call<ResultOfApi> deletePharmacist(@Field("pharmacist_id") String pharmacist_id);

    @FormUrlEncoded
    @POST("UpdateProfile")
    Single<UserResponse> updateUser(@Field("customer_id") String customer_id,
                                    @Field("customer_name") String customer_name,
                                    @Field("customer_email") String customer_email,
                                    @Field("customer_mobile") String customer_mobile,
                                    @Field("department_id") String department_id,
                                    @Field("designation_id") String designation_id,
                                    @Field("fb_id") String fb_id,
                                    @Field("gmail_id") String gmail_id,
                                    @Field("customer_image") String customer_image);

    @FormUrlEncoded
    @POST("UpdateProfile")
    Single<ResponseRegister> Register1(@Field("doctor_id") String doctor_id,
                                       @Field("name") String name,
                                       @Field("email") String email,
                                       @Field("app_id") String app_id,
                                       @Field("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("DeleteLogo")
    Single<ResponseRegister> DeleteLogo(@Field("doctor_id") String doctor_id

    );


    @FormUrlEncoded
    @POST("PatientRegister")
    Single<ResponseAddPatient> PatientRegister(@Field("p_name") String p_name,
                                               @Field("age") String age,
                                               @Field("gender") String gender,
                                               @Field("created_by") String created_by,
                                               @Field("p_mobile") String p_mobile,
                                               @Field("p_email") String p_email,
                                               @Field("p_address") String p_address,
                                               @Field("p_blood_grp") String p_blood_grp,
                                               @Field("p_dob") String p_dob

    );


    @FormUrlEncoded
    @POST("PatientEdit")
    Single<ResponseSuccess> PatientUpdate(@Field("patient_id") String patient_id,
                                          @Field("p_name") String p_name,
                                          @Field("age") String age,
                                          @Field("gender") String gender,
                                          @Field("p_mobile") String p_mobile,
                                          @Field("p_email") String p_email,
                                          @Field("p_address") String p_address,
                                          @Field("p_blood_grp") String p_blood_grp,
                                          @Field("p_dob") String p_dob

    );


    @FormUrlEncoded
    @POST("UpdateProfile")
    Single<ResponseRegister> Register2(@Field("doctor_id") String doctor_id,
                                       @Field("registration_no") String registration_no,
                                       @Field("designation_name") String designation_name,
                                       @Field("ug_id") String ug_id,
                                       @Field("pg_id") String pg_id,
                                       @Field("addtional_qualification") String addtional_qualification);


    @FormUrlEncoded
    @POST("UpdateProfile")
    Single<ResponseRegister> Register3(@Field("doctor_id") String doctor_id,
                                       @Field("mobile_letter_head") String mobile_letter_head,
                                       @Field("email_letter_head") String email_letter_head,
                                       @Field("working_days") String working_days,
                                       @Field("visiting_hr_from") String visiting_hr_from,
                                       @Field("visiting_hr_to") String visiting_hr_to,
                                       @Field("close_day") String close_day);


    @FormUrlEncoded
    @POST("UpdateProfile")
    Single<ResponseRegister> Register4(@Field("doctor_id") String doctor_id,
                                       @Field("registration_no") String registration_no,
                                       @Field("speciality_id") String specialities_id,
                                       @Field("ug_id") String ug_id,
                                       @Field("pg_id") String pg_id,
                                       @Field("addtional_qualification") String addtional_qualification);


    @FormUrlEncoded
    @POST("UpdateProfile")
    Single<ResponseRegister> Register5(@Field("doctor_id") String doctor_id,
                                       @Field("registration_no") String registration_no,
                                       @Field("speciality_id") String specialities_id,
                                       @Field("ug_id") String ug_id,
                                       @Field("pg_id") String pg_id,
                                       @Field("addtional_qualification") String addtional_qualification);


    @FormUrlEncoded
    @POST("Categories")
    Single<UserResponse> updateUser(@Field("customer_id") String customer_id,
                                    @Field("customer_name") String customer_name,
                                    @Field("customer_email") String customer_email);


    @FormUrlEncoded
    @POST("DeleteSigniture")
    Single<ResponseRegister> DeleteSignature(@Field("doctor_id") String doctor_id);

    @GET("GetDesignation")
    Single<ResponseDesignation> getDesignations();


    @GET("GetPG")
    Single<ResponsePG> getPGs();


    @GET("GetUG")
    Single<ResponseUG> getUGs();

    @GET("Banners")
    Single<ResponseBanners> getBanners();

    @GET("Subsciptions")
    Single<ResponseSubscriptions> getSubscriptions();


    @GET("GetSpecialities")
    Single<ResponseSpecialities> getSpecialities();

    @FormUrlEncoded
    @POST("MyPatients")
    Single<ResponsePatients> getPatients(@Field("created_by") String created_by);

    @FormUrlEncoded
    @POST("MySubsciptions")
    Single<Subscription> mysubscription(@Field("doctor_id") String created_by);


    @FormUrlEncoded
    @POST("LabTest")
    Single<ResponseLabTest> getLabTests(@Field("created_by") String created_by);


    @FormUrlEncoded
    @POST("AllMedicines")
    Single<ResponseMedicines> getMedicines(@Field("created_by") String created_by);

    @FormUrlEncoded
    @POST("MyTemplates")
    Single<ResponseNewMyTemplates> getTemplates(@Field("created_by") String created_by);


    @FormUrlEncoded
    @POST("MyLabTemplates")
    Single<ResponseLabTemplates> getLabTemplates(@Field("created_by") String created_by);


    @FormUrlEncoded
    @POST("AddTemplate")
    Single<ResponseSuccess> AddTemplate(@Field("template_name") String template_name,
                                        @Field("created_by") String created_by,
                                        @Field("date") String date,
                                        @Field("medicine_name") String medicine_name,
                                        @Field("frequency") String frequency,
                                        @Field("no_of_days") String no_of_days,
                                        @Field("route") String route,
                                        @Field("instruction") String instruction,
                                        @Field("additiona_comment") String additiona_comment);


    @Headers("Content-Type: application/json")
    @POST("AddTemplate")
    Single<ResponseSuccess> AddTemplate(@Body String bean);


    @Headers("Content-Type: application/json")
    @POST("AddLabTest")
    Single<ResponseSuccess> AddTemplateLabTest(@Body String bean);


    @Headers("Content-Type: application/json")
    @POST("Prescription")
    Single<ResponseSuccess> Prescription(@Body String bean);


    @FormUrlEncoded
    @POST("Feedbacks")
    Single<Response> Feedback(@Field("doctor_id") String customer_id,
                              @Field("type") String type,
                              @Field("subject") String subject,
                              @Field("message") String message);


    @FormUrlEncoded
    @POST("AddSubsciption")
    Single<ResponseSubscription> AddSubscription(@Field("doctor_id") String doctor_id,
                                                 @Field("subscription_id") String subscription_id,
                                                 @Field("payment_status") String payment_status,
                                                 @Field("transaction_id") String transaction_id,
                                                 @Field("transaction_amount") String transaction_amount
    );


    @FormUrlEncoded
    @POST("Cms")
    Single<CmsResponse> Cms(@Field("slug") String slug);

    @FormUrlEncoded
    @POST("AddUG")
    Single<ResponseAddUG> AddUG(@Field("ug_name") String ug_name);


    @GET("faqs")
    Single<FaqsResponse> getFaqs();

    @GET("Version")
    Single<ResponseVersion> getVersion();

    @FormUrlEncoded
    @POST("ProfileDetails")
    Single<ResponseProfileDetails> getProfileDetails(@Field("doctor_id") String doctor_id);

    @FormUrlEncoded
    @POST("DeleteProfile")
    Single<Response> deleteProfile(@Field("doctor_id") String doctor_id);


    @FormUrlEncoded
    @POST("DeleteTemplate")
    Single<ResponseSuccess> getDeleteTemplate(@Field("template_id") String doctor_id);


    @FormUrlEncoded
    @POST("DeleteLabTestTemplate")
    Single<ResponseSuccess> getDeleteTemplateLabTest(@Field("lab_template_id") String lab_template_id);

    @FormUrlEncoded
    @POST("DeletePatient")
    Single<ResponseSuccess> deletePatient(@Field("patient_id") String patient_id);


    @FormUrlEncoded
    @POST("AllPrescription")
    Single<ResponseHistory> getAllPrescription(@Field("doctor_id") String doctor_id);


//    @FormUrlEncoded
//    @POST("CustomerSubcription")
//    Single<SubscriptionHistoryResponse> customerSubscription(@Field("customer_id") String customer_id,
//                                                             @Field("subscription_id") String subscription_id,
//                                                             @Field("type") String type,
//                                                             @Field("promo_code") String promo_code,
//                                                             @Field("offer_id") String offer_id,
//                                                             @Field("discount") String discount,
//                                                             @Field("final_amount") String final_amount);
//
//    @FormUrlEncoded
//    @POST("PromoCheck")
//    Single<PromoCheckResponse> promoCheck(@Field("customer_id") String customer_id,
//                                          @Field("total") String total,
//                                          @Field("promo_code") String promo_code);
//
//
//    @FormUrlEncoded
//    @POST("CustomerSubcriptionUpdate")
//    Single<Response> customerSubscriptionUpdate(@Field("customers_subscription_id") String customers_subscription_id,
//                                                @Field("payment_id") String payment_id);

 /*   @FormUrlEncoded
    @POST("login")
    Single<UserResponse> userLogin(@Field("customer_mobile") String mobile);*/
    //@Headers("x-api-key: cw00ggcsw4co0g804gcggwo088g4kokgk88sso4s")

    @FormUrlEncoded
    @POST("Login")
    Single<ResponseRegister> userLogin(
            @Field("email") String customer_email
    );

    @FormUrlEncoded
    @POST("Otp")
    Single<OtpResponse> getOtpFromBackEnd(@Field("email") String email);
//
//    @FormUrlEncoded
//    @POST("Myinvites")
//    Single<MyInvitesResponse> MyInvites(@Field("customer_id") String customer_id);
//
//    @FormUrlEncoded
//    @POST("MySubscription")
//    Single<SubscriptionDetailsResponse> MySubscriptions(@Field("customer_id") String customer_id);
//
//    @FormUrlEncoded
//    @POST("GetEmergencyContacts")
//    Single<EmergencyResponse> getEmergencyContact(@Field("customer_id") String customer_id);
//
//    @FormUrlEncoded
//    @POST("SendEmergency")
//    Single<Response> sendEmergencyAlert(@Field("parent_id") String parent_id);
//
//    @FormUrlEncoded
//    @POST("CustomerLatLong")
//    Single<Response> setLatLong(@Field("tracking_id") String tracking_id,
//                                @Field("latitude") String latitude,
//                                @Field("longitude") String longitude);
//
//    @FormUrlEncoded
//    @POST("Pay")
//    Single<PaymentOrderResponse> getPaymentOrderId(@Field("amount") String amount,
//                                                   @Field("currency") String currency,
//                                                   @Field("receipt") String receipt,
//                                                   @Field("payment_capture") String payment_capture);
//
//
//    @FormUrlEncoded
//    @POST("Feedbacks")
//    Single<Response> Feedback(@Field("customer_id") String customer_id,
//                              @Field("type") String type,
//                              @Field("subject") String subject,
//                              @Field("message") String message);
//
//
//    @FormUrlEncoded
//    @POST("Payment")
//    Single<PaymentResponse> setPayment(@Field("customer_id") String customer_id,
//                                       @Field("customers_subscription_id") String customer_subscription_id,
//                                       @Field("transaction_id") String transaction_id,
//                                       @Field("status") String status,
//                                       @Field("amount") String amount);
//
//    @Headers("Content-Type: application/json")
//    @POST("CustomerLatLong")
//    Single<Response> setLocation(@Body String bean);
//
//
//    @POST("CustomerLatLong")
//    Call<Response> setLocationCLL(@Body Locc locc);
//
//    @FormUrlEncoded
//    @POST("StopTracking")
//    Single<Response> stopTracking(@Field("customer_id") String customer_id,
//                                  @Field("tracking_id") String tracking_id);
//
//    @FormUrlEncoded
//    @POST("Cms")
//    Single<CmsResponse> Cms(@Field("slug") String slug);
//
//    @FormUrlEncoded
//    @POST("ProfileDetails")
//    Single<UserResponse> ProfileDetails(@Field("customer_id") String customer_id);
//
//    @FormUrlEncoded
//    @POST("CustomerInviteList")
//    Single<PendingsResponse> CustomerInviteList(@Field("customer_id") String customer_id);
//
//    @FormUrlEncoded
//    @POST("CustomerSubscriptionDetails")
//    Single<SubscriptionDetailsResponse> subscriptionDetails(@Field("customer_id") String customer_id,
//                                                            @Field("category_id") String category_id);
//
//    @FormUrlEncoded
//    @POST("AcceptInvitation")
//    Single<Response> invitePushResponse(@Field("tracking_id") String tracking_id,
//                                        @Field("customer_id") String customer_id,
//                                        @Field("parent_id") String parent_id,
//                                        @Field("category_id") String category_id,
//                                        @Field("type") String type);
//
//    @FormUrlEncoded
//    @POST("AcceptOtherPlatform")
//    Single<Response> AcceptOtherPlatform(@Field("invite_code") String invite_code,
//                                         @Field("customer_id") String customer_id,
//                                         @Field("category_id") String category_id,
//                                         @Field("start_date") String start_date,
//                                         @Field("end_date") String end_date,
//                                         @Field("start_time") String start_time,
//                                         @Field("end_time") String end_time);
//
//    @FormUrlEncoded
//    @POST("InviteList")
//    Single<TrackingsResponse> inviteList(@Field("parent_id") String parent_id,
//                                         @Field("category_id") String category_id);
//
//    @FormUrlEncoded
//    @POST("reports")
//    Single<ReportsResponse> reports(@Field("parent_id") String parent_id,
//                                    @Field("category_id") String category_id);
//
//    @Headers("Content-Type: application/json")
//    @POST("CustomerInvite")
//    Single<TrackingsResponse> customerInvite(@Body String bean);
//
//    @Headers("Content-Type: application/json")
//    @POST("EmergencyContacts")
//    Single<Response> addEmergencyContact(@Body String bean);
//
//    @Headers("Content-Type: application/json")
//    @POST("AddPinLocation")
//    Single<PinLocationResponse> addPinLocation(@Body String bean);
//
//    @Headers("Content-Type: application/json")
//    @POST("MultipleStopTracking")
//    Single<Response> MultipleStopTracking(@Body String bean);
//
//    @Headers("Content-Type: application/json")
//    @POST("MultipleInvites")
//    Single<Response> resendInvite(@Body String bean);
//
//    @FormUrlEncoded
//    @POST("RemovePin")
//    Single<Response> removePin(@Field("pin_id") String pin_id);
//
//
//    @FormUrlEncoded
//    @POST("DeleteTracking")
//    Single<Response> removeInvite(@Field("tracking_id") String tracking_id);
//
//
//    @FormUrlEncoded
//    @POST("ActiveTracking")
//    Single<ActiveTrackingsResponse> activeTrackings(@Field("customer_id") String customer_id,
//                                                    @Field("category_id") String category_id);
//
//    @FormUrlEncoded
//    @POST("RemoveEmergencyContacts")
//    Single<Response> RemoveEmergencyContact(@Field("contact_id") String contact_id,
//                                            @Field("emergency_mobile") String emergency_mobile);
//
//    @FormUrlEncoded
//    @POST("LastLocation")
//    Single<LastLocationResponse> LastLocation(@Field("parent_id") String parent_id,
//                                              @Field("category_id") String category_id);
//
//    @FormUrlEncoded
//    @POST("PinLocations")
//    Single<PinLocationsResponse> PinLocations(@Field("tracking_id") String tracking_id);
//
//
//    /*@FormUrlEncoded
//    @POST("login")
//    Single<ResponseSms> sendSMS(@Field("mobile") String mobile,
//                                @Field("hash_key")   String hash_key);*/
//
//    @Headers("Content-Type: application/json")
//    @POST("MemberLastLocation")
//    Single<MemberLastLocationResponse> MemberLastLocation(@Body String bean);
//
//    @GET("department")
//    Single<DepartmentResponse> getDepartments();
//

//
//    @GET("Categories")
//    Single<CategoriesResponse> getCategories();
//
//    @GET("Countries")
//    Single<CountryResponse> getCountries();
//
//    @GET("Banners")
//    Single<BannerResponse> getBanners();
}