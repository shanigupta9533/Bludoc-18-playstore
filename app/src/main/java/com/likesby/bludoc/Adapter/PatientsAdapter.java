package com.likesby.bludoc.Adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.likesby.bludoc.AppointmentActivity;
import com.likesby.bludoc.CreateAppointmentActivity;
import com.likesby.bludoc.DigitalClinicActivity;
import com.likesby.bludoc.DigitalClinicConfirmationActivity;
import com.likesby.bludoc.Fragment.CreatePrescription;
import com.likesby.bludoc.Fragment.PopUpSubscriptionDialogFragment;
import com.likesby.bludoc.HomeActivity;
import com.likesby.bludoc.InvoiceActivity;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.Entities.ResponsePatients;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.NetworkLayer.EndpointInterfaces.WebServices;
import com.likesby.bludoc.ModelLayer.NetworkLayer.Helpers.RetrofitClient;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities.SubcriptionsItem;
import com.likesby.bludoc.R;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.SplashActivity;
import com.likesby.bludoc.constants.ApplicationConstant;
import com.likesby.bludoc.utils.DateUtils;
import com.likesby.bludoc.utils.Utils;
import com.likesby.bludoc.viewModels.ApiViewHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;
import static com.likesby.bludoc.Fragment.CreatePrescription.certificate_selection;
import static com.likesby.bludoc.Fragment.CreatePrescription.patient_id;
import static com.likesby.bludoc.Fragment.CreatePrescription.pos;

import org.joda.time.DateTime;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolder> implements Filterable {
    private final FragmentActivity fragmentActivity;
    private final DateTime dateTime;
    private ArrayList<PatientsItem> mArrayList = new ArrayList<>();
    private ArrayList<PatientsItem> mFilteredList = new ArrayList<>();
    private Context mContext;
    TextView patientdetails;
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-6756023122563497/5728747901";
    private UnifiedNativeAd nativeAd;
    LinearLayout ll_patients_view, ll_prescription_view;
    RelativeLayout top_view;
    Button btn_create_patient;
    ApiViewHolder apiViewHolder;
    CompositeDisposable mBag;
    String gender_ = "";
    SessionManager manager = new SessionManager();
    FrameLayout fl_progress_bar;
    LinearLayout ll_medicinal_lab, ll_medicine_product, ll_end_note, ll_certificate;
    LinearLayout ll_main_medicine_details, ll_main_lab_test_details, ll_main_end_note_details, ll_main_certificate_details;
    android.app.AlertDialog alertDialog;
    CreatePrescription createPrescription;
    Boolean showNativeAdFlag;
    private boolean isConstant;
    TextView header;
    private boolean isOnPrescribe;
    private Dialog dialog_data;
    private RelativeLayout progress_bar;
    private boolean isOnCertificate;
    private onClickListener onClickLsitener;

    public PatientsAdapter(ArrayList<PatientsItem> arrayList, LinearLayout ll_patients_view, LinearLayout ll_prescription_view, RelativeLayout top_view,
                           Button btn_create_patient, TextView patientdetails, ApiViewHolder apiViewHolder, CompositeDisposable mBag, FrameLayout fl_progress_bar, LinearLayout ll_medicinal_lab, LinearLayout ll_medicine_product, LinearLayout ll_end_note, LinearLayout ll_certificate, LinearLayout ll_main_medicine_details, LinearLayout ll_main_lab_test_details, LinearLayout ll_main_end_note_details, LinearLayout ll_main_certificate_details, CreatePrescription createPrescription, boolean showNativeAdFlag, TextView header, Context context) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
        this.header = header;
        this.ll_patients_view = ll_patients_view;
        this.ll_prescription_view = ll_prescription_view;
        this.top_view = top_view;
        this.showNativeAdFlag = showNativeAdFlag;
        this.createPrescription = createPrescription;

        dateTime = new DateTime();

        this.btn_create_patient = btn_create_patient;
        this.patientdetails = patientdetails;
        this.apiViewHolder = apiViewHolder;
        this.mBag = mBag;
        this.ll_medicine_product = ll_medicine_product;
        this.ll_medicinal_lab = ll_medicinal_lab;
        this.ll_end_note = ll_end_note;
        this.ll_certificate = ll_certificate;
        this.ll_main_medicine_details = ll_main_medicine_details;
        this.ll_main_lab_test_details = ll_main_lab_test_details;
        this.ll_main_end_note_details = ll_main_end_note_details;
        this.ll_main_certificate_details = ll_main_certificate_details;

        fragmentActivity = (FragmentActivity) context;
        this.fl_progress_bar = fl_progress_bar;
    }

    public interface onClickListener {

        void onClick();

        void onDestroy();

        void onLoadCertificated();

        void onLoadPrescribe();

    }

    public void setOnClickLsitener(onClickListener onClickLsitener) {

        this.onClickLsitener = onClickLsitener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.cv_patients,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,int i) {

        PatientsItem patientsItem = mFilteredList.get(i);

        viewHolder.PATIENT_NAME.setText(mFilteredList.get(i).getPName());
        viewHolder.PATIENT_ID.setText(mFilteredList.get(i).getPatientId());

        if (TextUtils.isEmpty(mFilteredList.get(i).getpDob())) {

            Log.i(TAG, "onBindViewHolder:" + mFilteredList.get(i).getAge());

            if (TextUtils.isEmpty(mFilteredList.get(i).getAge())
                    || mFilteredList.get(i).getAge().trim().equalsIgnoreCase("0 yr")
                    || mFilteredList.get(i).getAge().trim().equalsIgnoreCase("yr"))
                viewHolder.PATIENT_AGE.setText(mFilteredList.get(i).getGender());
            else
                viewHolder.PATIENT_AGE.setText(mFilteredList.get(i).getAge() + " / " + mFilteredList.get(i).getGender());

        } else {

            String[] split = mFilteredList.get(i).getpDob().split("-");
            String return_value = decideMonthOrYear(split);
            viewHolder.PATIENT_AGE.setText(return_value + " / " + mFilteredList.get(i).getGender());

        }

        viewHolder.book_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "registration_no"))) {
                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");
                    if (!("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                        if (responseProfileDetails.getAccess().equals("Approve")) {

                            String sub_valid = "", premium_valid = "";
                            boolean flag_reset_paid = false;
                            Date date1 = null, date2 = null;
                            int days_left_free = 0, days_left_paid = 0;
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(mContext, "profile");
                            if (profileDetails.getSubcriptions() != null) {
                                if (profileDetails.getSubcriptions().size() != 0) {
                                    for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                                        if (!si.getHospital_code().equals("")) {
                                            try {
                                                Calendar c2 = Calendar.getInstance();
                                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                                Date dateEnd = dateFormat.parse(si.getEnd());
                                                Date c = Calendar.getInstance().getTime();
                                                assert dateEnd != null;

                                                //premium_valid = si.getDays();
                                                Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                                // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                                // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                                try {
                                                    date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                                    date2 = dateFormat.parse(si.getEnd());
                                                    Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");

                                                    String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                                    days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                                    // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                                    // break;
                                                    sub_valid = si.getEnd();
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                                //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                                            } catch (ParseException pe) {
                                                // handle the failure
                                            }

                                        }
                                    }

                                } else {
                                    popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                                    return;
                                }
                            } else {
                                popupPaused("Your subscription has expired. Please contact to " + profileDetails.getHospital_name());
                                return;
                            }

                            if (days_left_free < 0) {
                                popupPaused("Your subscription has expired. Please contact to hospital  admin");
                                return;
                            } else {
                                Intent intent = new Intent(mContext,CreateAppointmentActivity.class);
                                intent.putExtra("patientItem",patientsItem);
                                mContext.startActivity(intent);
                            }
//Now run it
                        } else if (responseProfileDetails.getAccess().equals("Pending")) {
                            popupPaused("Your account is pending for approval. Please contact " + responseProfileDetails.getHospital_name());

                        } else if (responseProfileDetails.getAccess().equals("Remove")) {
                            popupPaused("You have been removed from " + responseProfileDetails.getHospital_name() + ". You now have accesses like BluDoc standard user");
                            //popupHospitalCode();
                        } else if (responseProfileDetails.getAccess().equals("Pause")) {

                            popupPaused("Your subscription is paused. Please contact to " + responseProfileDetails.getHospital_name());

                        } else {
                            popupAccess();
                        }

                    } else {

                        checkSubsciptionForAppointment(patientsItem);

                    }
                } else {
                    popup();
                }

            }


        });

        if (("").equalsIgnoreCase(mFilteredList.get(i).getpDob())) {
            viewHolder.date_of_birth.setText("____");
        } else {
            viewHolder.date_of_birth.setText(mFilteredList.get(i).getpDob());
        }

        if (("").equalsIgnoreCase(mFilteredList.get(i).getPMobile())) {
            viewHolder.PATIENT_MOB.setText("____");
        } else if (("0").equalsIgnoreCase(mFilteredList.get(i).getPMobile())) {
            viewHolder.PATIENT_MOB.setText("____");
        } else {
            viewHolder.PATIENT_MOB.setText(mFilteredList.get(i).getPMobile());
        }

        if (("").equalsIgnoreCase(mFilteredList.get(i).getPEmail())) {
            viewHolder.PATIENT_EMAIL.setText("____");
        } else {
            viewHolder.PATIENT_EMAIL.setText(mFilteredList.get(i).getPEmail());
        }

        if (("").equalsIgnoreCase(mFilteredList.get(i).getpBloodGrp())) {
            viewHolder.blood_group.setText("____");
        } else {
            viewHolder.blood_group.setText(mFilteredList.get(i).getpBloodGrp());
        }

        viewHolder.invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "registration_no"))) {
                    ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");
                    if (("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                        String sub_valid = "", premium_valid = "";
                        boolean flag_reset_paid = false;
                        Date date1 = null, date2 = null;
                        int days_left_free = 0, days_left_paid = 0;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(mContext, "profile");
                        if (profileDetails.getSubcriptions() != null) {
                            if (profileDetails.getSubcriptions().size() != 0) {
                                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                                    if (!si.getHospital_code().equals("")) {
                                        boolean flag_reset_free;
                                        try {
                                            Calendar c2 = Calendar.getInstance();
                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                            Date dateEnd = dateFormat.parse(si.getEnd());
                                            Date c = Calendar.getInstance().getTime();
                                            assert dateEnd != null;

                                            //premium_valid = si.getDays();
                                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                            try {
                                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                                date2 = dateFormat.parse(si.getEnd());
                                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                                flag_reset_free = true;

                                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                                // break;
                                                sub_valid = si.getEnd();
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                                        } catch (ParseException pe) {
                                            // handle the failure
                                            flag_reset_free = false;
                                        }

                                    }
                                }

                            } else {
                                popupPaused("Invoice");
                                return;

                            }
                        } else {
                            popupPaused("Invoice");
                            return;
                        }

                        if (days_left_free < 0) {
                            popupPaused("Invoice");
                        } else {

                            Intent intent = new Intent(mContext, InvoiceActivity.class);
                            intent.putExtra("patientName", patientsItem.getPName());
                            intent.putExtra("patientObject", patientsItem);
                            mContext.startActivity(intent);
                        }

                    }
                } else {
                    popup();
                }

            }
        });

        viewHolder.prescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popUpPrescription(viewHolder,patientsItem,i);

            }
        });

        viewHolder.certificate.setOnClickListener(v -> {

            if (!("").equalsIgnoreCase(manager.getPreferences(mContext, "registration_no"))) {
                ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(mContext, "profile");
                if (("").equalsIgnoreCase(responseProfileDetails.getHospitalCode())) {

                    String sub_valid = "", premium_valid = "";
                    boolean flag_reset_paid = false;
                    Date date1 = null, date2 = null;
                    int days_left_free = 0, days_left_paid = 0;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(mContext, "profile");
                    if (profileDetails.getSubcriptions() != null) {
                        if (profileDetails.getSubcriptions().size() != 0) {
                            for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                                if (!si.getHospital_code().equals("")) {
                                    boolean flag_reset_free;
                                    try {
                                        Calendar c2 = Calendar.getInstance();
                                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                        Date dateEnd = dateFormat.parse(si.getEnd());
                                        Date c = Calendar.getInstance().getTime();
                                        assert dateEnd != null;

                                        //premium_valid = si.getDays();
                                        Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                                        // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                                        // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                                        try {
                                            date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                            date2 = dateFormat.parse(si.getEnd());
                                            Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                            flag_reset_free = true;

                                            String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                            days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                            // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                            // break;
                                            sub_valid = si.getEnd();
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                                    } catch (ParseException pe) {
                                        // handle the failure
                                        flag_reset_free = false;
                                    }

                                }
                            }

                        } else {
                            popupPaused("Medical certificate");
                            return;

                        }
                    } else {
                        popupPaused("Medical certificate");
                        return;
                    }

                    if (days_left_free < 0) {
                        popupPaused("Medical certificate");
                    } else {
                        certificateOpenModel(viewHolder, patientsItem,i);

                    }

                }
            } else {
                popup();
            }

        });

    }

    private void checkSubsciptionForAppointment(PatientsItem patientsItem) {

        String sub_valid = "", premium_valid = "";
        boolean flag_reset_paid = false;
        Date date1 = null, date2 = null;
        int days_left_free = 0, days_left_paid = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        ResponseProfileDetails profileDetails = manager.getObjectProfileDetails(fragmentActivity, "profile");
        if (profileDetails.getSubcriptions() != null) {
            if (profileDetails.getSubcriptions().size() != 0) {
                for (SubcriptionsItem si : profileDetails.getSubcriptions()) {
                    if (!si.getHospital_code().equals("")) {
                        boolean flag_reset_free;
                        try {
                            Calendar c2 = Calendar.getInstance();
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                            Date dateEnd = dateFormat.parse(si.getEnd());
                            Date c = Calendar.getInstance().getTime();
                            assert dateEnd != null;

                            //premium_valid = si.getDays();
                            Log.e("DATE_____1 = ", DateUtils.printDifference(c, dateEnd));
                            // Log.e("DATE_____2 = ",DateUtils.outFormatsetMMM(DateUtils.printDifference(c,dateEnd)));
                            // Log.e("DATE_____3 = ",DateUtils.outFormatset(DateUtils.printDifference(c,dateEnd)));


                            try {
                                date1 = dateFormat.parse(dateFormat.format(c2.getTime()));
                                date2 = dateFormat.parse(si.getEnd());
                                Log.e("DATE_____1 = ", DateUtils.printDifference(date1, date2) + " left");
                                flag_reset_free = true;

                                String[] splited = DateUtils.printDifference(date1, date2).split("\\s+");

                                days_left_free = days_left_free + Integer.parseInt(splited[0]);
                                // popupFreeSubscription(DateUtils.printDifference(date1,date2),true);
                                // break;
                                sub_valid = si.getEnd();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //viewHolder.expiry.setText(DateUtils.printDifference(date1,date2)+" left");

                        } catch (ParseException pe) {
                            // handle the failure
                            flag_reset_free = false;
                        }

                    }
                }

            } else {
                popupPausedForChecking("Doctor Appointment");
                return;

            }
        } else {
            popupPausedForChecking("Doctor Appointment");
            return;
        }

        if (days_left_free < 0) {
            popupPausedForChecking("Doctor Appointment");
        } else {
            Intent intent = new Intent(mContext,CreateAppointmentActivity.class);
            intent.putExtra("patientItem",patientsItem);
            mContext.startActivity(intent);
        }

    }

    private void popupPausedForChecking(String keywords) {

        PopUpSubscriptionDialogFragment popUpSubscriptionDialogFragment= new PopUpSubscriptionDialogFragment();
        popUpSubscriptionDialogFragment.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putString("keywords", keywords);
        popUpSubscriptionDialogFragment.setArguments(bundle);
        popUpSubscriptionDialogFragment.show(fragmentActivity.getSupportFragmentManager(),"");

    }

    private void popupAccess() {

        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.popup_access);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        Button btn_add = dialog_data.findViewById(R.id.btn_register);
        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.dismiss();

            }
        });
        dialog_data.show();
    }

    private void popUpPrescription(ViewHolder viewHolder, PatientsItem patientsItem, int i) {

        manager.setPreferences(mContext, "isOnPrescribe", "true");

        patient_id = viewHolder.PATIENT_ID.getText().toString().trim();
        pos = findBypatientId(patientsItem.getPatientId());
        btn_create_patient.setVisibility(View.GONE);
        ll_patients_view.setVisibility(View.GONE);
        ll_prescription_view.setVisibility(View.VISIBLE);
        top_view.setVisibility(View.VISIBLE);
        ll_certificate.setVisibility(View.GONE);
        ll_main_certificate_details.setVisibility(View.GONE);
        ll_medicinal_lab.setVisibility(View.VISIBLE);
        ll_main_lab_test_details.setVisibility(View.GONE);
        ll_medicine_product.setVisibility(View.VISIBLE);
        ll_main_medicine_details.setVisibility(View.GONE);
        ll_end_note.setVisibility(View.VISIBLE);
        ll_main_end_note_details.setVisibility(View.VISIBLE);
        certificate_selection = false;

        String age = "";

        if (!TextUtils.isEmpty(patientsItem.getAge())) {
            age = " / " + patientsItem.getAge();
        }

        if (patientsItem.getAge().equalsIgnoreCase("yr")) {
            age = "";
        }

        patientdetails.setText(patientsItem.getPName() + " - " + patientsItem.getGender() + age);

        if (onClickLsitener != null)
            onClickLsitener.onLoadPrescribe();

    }

    private  void popup()
    {
        final Dialog dialog_data = new Dialog(mContext);
        dialog_data.setCancelable(true);

        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);

        dialog_data.setContentView(R.layout.feedback);

        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_data.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());

        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

        //window.setGravity(Gravity.CENTER);
        window.setAttributes(lp_number_picker);

        ImageView btn_close = dialog_data.findViewById(R.id.btn_close);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_data.dismiss();

            }
        });
        dialog_data.show();
    }

    private void popupPaused(String keywords) {

        PopUpSubscriptionDialogFragment popUpSubscriptionDialogFragment= new PopUpSubscriptionDialogFragment();
        popUpSubscriptionDialogFragment.setCancelable(false);
        Bundle bundle = new Bundle();
        bundle.putString("keywords",keywords);
        popUpSubscriptionDialogFragment.setArguments(bundle);
        popUpSubscriptionDialogFragment.show(fragmentActivity.getSupportFragmentManager(),"");

    }

    private void certificateOpenModel(ViewHolder viewHolder, PatientsItem patientsItem, int i) {
        isOnCertificate = true;
        manager.setPreferences(mContext, "isOnCertificate", "true");
        patient_id = viewHolder.PATIENT_ID.getText().toString().trim();
        pos = findBypatientId(patientsItem.getPatientId());
        btn_create_patient.setVisibility(View.GONE);
        ll_patients_view.setVisibility(View.GONE);
        ll_prescription_view.setVisibility(View.VISIBLE);
        top_view.setVisibility(View.VISIBLE);
        ll_certificate.setVisibility(View.VISIBLE);
        ll_main_certificate_details.setVisibility(View.VISIBLE);
        ll_medicinal_lab.setVisibility(View.GONE);
        ll_main_lab_test_details.setVisibility(View.GONE);
        ll_medicine_product.setVisibility(View.GONE);
        ll_main_medicine_details.setVisibility(View.GONE);
        ll_end_note.setVisibility(View.GONE);
        ll_main_end_note_details.setVisibility(View.GONE);
        certificate_selection = true;

        String age = "";

        if (!TextUtils.isEmpty(patientsItem.getAge())) {
            age = " / " + patientsItem.getAge();
        }

        if (patientsItem.getAge().equalsIgnoreCase("yr")) {
            age = "";
        }

        patientdetails.setText(patientsItem.getPName() + " - " + patientsItem.getGender() + age);

        if (onClickLsitener != null)
            onClickLsitener.onLoadCertificated();

        // viewHolder.PATIENT_CREATED.setText(formattedDate);
        viewHolder.PATIENT_CREATED.setText(DateUtils.outFormatsetMMM(mFilteredList.get(i).getCreated().trim()));
        viewHolder.PATIENT_MODIFIED.setText(mFilteredList.get(i).getModified());

    }

    private String decideMonthOrYear(String[] split) {

        DateTime dateTime = new DateTime();
        int year = Integer.parseInt(split[2]);
        int month = Integer.parseInt(split[1]);

        if (dateTime.getYear() - year > 0) {

            return dateTime.getYear() - year + " yr";

        } else {

            if (dateTime.getMonthOfYear() - (month) == 0)
                return "1 month";
            else
                return dateTime.getMonthOfYear() - (month) + " month";
        }

    }

    private int findBypatientId(String patientId) {

        for (int i = 0; i < mArrayList.size(); i++) {

            if (patientId.equalsIgnoreCase(mArrayList.get(i).getPatientId())) {

                return i;

            }

        }


        return 0;

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<PatientsItem> filteredList = new ArrayList<>();
                    for (PatientsItem row : mArrayList) {
                        if (row.getPName().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d(TAG, "performFiltering: " + charString + " == " + row);
                            filteredList.add(row);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                mFilteredList = (ArrayList<PatientsItem>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public void onBackPressedHappen() {

        if (manager.getPreferences(fragmentActivity, "isOnPrescribe").equalsIgnoreCase("true")) {

            btn_create_patient.setVisibility(View.VISIBLE);
            ll_patients_view.setVisibility(View.VISIBLE);
            ll_prescription_view.setVisibility(View.GONE);
            top_view.setVisibility(View.GONE);
            ll_certificate.setVisibility(View.VISIBLE);
            ll_main_certificate_details.setVisibility(View.VISIBLE);
            ll_medicinal_lab.setVisibility(View.GONE);
            ll_main_lab_test_details.setVisibility(View.VISIBLE);
            ll_medicine_product.setVisibility(View.GONE);
            ll_main_medicine_details.setVisibility(View.VISIBLE);
            ll_end_note.setVisibility(View.GONE);
            ll_main_end_note_details.setVisibility(View.VISIBLE);

            manager.setPreferences(fragmentActivity, "isOnPrescribe", "false");

            if (onClickLsitener != null) {
                onClickLsitener.onClick();
            }


        } else if (manager.getPreferences(fragmentActivity, "isOnCertificate").equalsIgnoreCase("true")) {

            btn_create_patient.setVisibility(View.VISIBLE);
            ll_patients_view.setVisibility(View.VISIBLE);
            ll_prescription_view.setVisibility(View.GONE);
            top_view.setVisibility(View.GONE);
            ll_certificate.setVisibility(View.VISIBLE);
            ll_main_certificate_details.setVisibility(View.VISIBLE);
            ll_medicinal_lab.setVisibility(View.GONE);
            ll_main_lab_test_details.setVisibility(View.VISIBLE);
            ll_medicine_product.setVisibility(View.GONE);
            ll_main_medicine_details.setVisibility(View.VISIBLE);
            ll_end_note.setVisibility(View.GONE);
            ll_main_end_note_details.setVisibility(View.VISIBLE);

            manager.setPreferences(fragmentActivity, "isOnCertificate", "false");

            if (onClickLsitener != null) {
                onClickLsitener.onClick();
            }

        } else {

            try {

                if (onClickLsitener != null)
                    onClickLsitener.onDestroy();

            } catch (IllegalStateException e) {
                Log.i("TAG", "onBackPressedHappen: " + e.getMessage());
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView PATIENT_NAME, PATIENT_ID, PATIENT_MOB, date_of_birth,
                PATIENT_EMAIL, PATIENT_CREATED, PATIENT_AGE, PATIENT_MODIFIED, blood_group, prescribe, certificate, invoice,book_appointment;
        // FrameLayout fl;
        ProgressBar pb;
        ImageView patient_prescribe;
        LinearLayout ll_main_patient_view;
        ImageView patient_edit, patient_delete;

        public ViewHolder(View view) {
            super(view);

            mContext = view.getContext();

            PATIENT_NAME = view.findViewById(R.id.tv_patient_name);
            PATIENT_ID = view.findViewById(R.id.tv_patient_id);
            PATIENT_AGE = view.findViewById(R.id.tv_age);
            PATIENT_MOB = view.findViewById(R.id.tv_mobile);
            date_of_birth = view.findViewById(R.id.date_of_birth);
            PATIENT_EMAIL = view.findViewById(R.id.tv_email);
            PATIENT_CREATED = view.findViewById(R.id.tv_patient_created);
            PATIENT_MODIFIED = view.findViewById(R.id.tv_patient_modified);
            ll_main_patient_view = view.findViewById(R.id.ll_main_patient_view);
            invoice = view.findViewById(R.id.invoice);
            patient_edit = view.findViewById(R.id.patient_edit);
            patient_delete = view.findViewById(R.id.patient_delete);
            blood_group = view.findViewById(R.id.blood_group);
            patient_prescribe = view.findViewById(R.id.patient_prescribe);
            prescribe = view.findViewById(R.id.prescribe);
            certificate = view.findViewById(R.id.certificate);
            book_appointment = view.findViewById(R.id.book_appointment);

            patient_prescribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ll_main_patient_view.performClick();
                }
            });

            PATIENT_NAME.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ll_main_patient_view.performClick();
                }
            });


            ll_main_patient_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //=============================================================================================

            patient_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog_data = new Dialog(mContext);
                    dialog_data.setCancelable(false);
                    dialog_data.setOnKeyListener(new DialogInterface.OnKeyListener() {

                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                            if (keyCode == KeyEvent.KEYCODE_BACK) {

                                dialog_data.dismiss();

                                hideKeyboard(fragmentActivity);

                                return true;
                            }
                            return false;

                        }
                    });

                    dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Objects.requireNonNull(dialog_data.getWindow()).setGravity(Gravity.CENTER);
                    }

                    dialog_data.setContentView(R.layout.popup_patient_update);

                    WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
                    Window window = dialog_data.getWindow();
                    lp_number_picker.copyFrom(window.getAttributes());

                    lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp_number_picker.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    //window.setGravity(Gravity.CENTER);
                    window.setAttributes(lp_number_picker);

                    final EditText et_name = dialog_data.findViewById(R.id.et_name);

                    progress_bar = dialog_data.findViewById(R.id.progress_bar);

                    final ImageView cancel_action = dialog_data.findViewById(R.id.cancel_action);
                    cancel_action.setVisibility(View.GONE);

                    final EditText et_age = dialog_data.findViewById(R.id.et_age);
                    final EditText et_whtsapp = dialog_data.findViewById(R.id.et_whtsapp);
                    final EditText et_mail = dialog_data.findViewById(R.id.et_mail);
                    Button btnUpload = dialog_data.findViewById(R.id.btn_upload_image);
                    Button btnCancel = dialog_data.findViewById(R.id.btn_cancel);
                    final EditText address = dialog_data.findViewById(R.id.address);
                    final EditText blood_group = dialog_data.findViewById(R.id.blood_group);

                    final RadioButton rb_year, rb_month, rb_male, rb_female, rb_other;
                    final String[] age_type = {"yr"};
                    rb_year = dialog_data.findViewById(R.id.rb_year);

                    rb_month = dialog_data.findViewById(R.id.rb_month);
                    rb_male = dialog_data.findViewById(R.id.rb_male);
                    rb_female = dialog_data.findViewById(R.id.rb_female);
                    rb_other = dialog_data.findViewById(R.id.rb_other);
                    final TextView date_f_birth = dialog_data.findViewById(R.id.date_of_birth);

                    final PatientsItem patientsItem = mFilteredList.get(getAdapterPosition());

                    date_f_birth.setText(patientsItem.getpDob());

                    cancel_action.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            date_f_birth.setText("");


                        }
                    });

                    date_f_birth.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                            if (s.length() > 0) {

                                cancel_action.setVisibility(View.VISIBLE);
                                rb_month.setEnabled(false);
                                rb_year.setEnabled(false);
                                et_age.setEnabled(false);

                            } else {

                                cancel_action.setVisibility(View.GONE);
                                rb_month.setEnabled(true);
                                et_age.setEnabled(true);
                                rb_year.setEnabled(true);
                                et_age.setEnabled(true);

                            }

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                    date_f_birth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final Calendar myCalendar = Calendar.getInstance();
                            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                    myCalendar.set(Calendar.YEAR, year);
                                    myCalendar.set(Calendar.MONTH, monthOfYear);
                                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                    DateTime dateTime = new DateTime();

                                    if (dateTime.getYear() - year > 1) {

                                        isConstant = true;
                                        String age = getAge(year, monthOfYear, dayOfMonth);

                                        if (Integer.parseInt(age) < 0) {
                                            age = "0";
                                        }

                                        et_age.setText(age);
                                        et_age.setSelection(et_age.getText().toString().length());
                                        rb_year.setChecked(true);

                                    }
                                    if (dateTime.getYear() - year == 1) {
                                        et_age.setText("1");
                                        et_age.setSelection(et_age.getText().toString().length());
                                        rb_year.setChecked(true);

                                    } else {

                                        isConstant = true;

                                        if (dateTime.getMonthOfYear() - (monthOfYear + 1) == 0)
                                            et_age.setText("1");
                                        else
                                            et_age.setText(String.valueOf(dateTime.getMonthOfYear() - (monthOfYear + 1)));

                                        et_age.setSelection(et_age.getText().toString().length());
                                        rb_month.setChecked(true);

                                    }

                                    String myFormat = "dd-MM-yyyy"; // your format
                                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

                                    date_f_birth.setText(sdf.format(myCalendar.getTime()));

                                }

                            };

                            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, R.style.DialogTheme, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

                            datePickerDialog.getDatePicker().setMaxDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000).getTime());

                            datePickerDialog.show();

                        }
                    });

                    ImageView info = dialog_data.findViewById(R.id.info);
                    ImageView info1 = dialog_data.findViewById(R.id.info1);
                    info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(mContext, R.style.AlertDialog);
                            alertDialogBuilder.setCancelable(false);

                            alertDialogBuilder.setMessage("On Approval, prescription will be directly sent to this number");

                            alertDialogBuilder.setPositiveButton("Okay",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            alertDialog.dismiss();

                                            hideKeyboard(fragmentActivity);

                                        }
                                    });
                            alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });
                    info1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(mContext, R.style.AlertDialog);
                            alertDialogBuilder.setCancelable(false);

                            alertDialogBuilder.setMessage("On Approval, prescription will be directly sent to this mail Id");

                            alertDialogBuilder.setPositiveButton("Okay",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            alertDialog.dismiss();

                                            hideKeyboard(fragmentActivity);

                                        }
                                    });
                            alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });


                    rb_year.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                rb_month.setChecked(false);
                                age_type[0] = "yr";
//
//                                if (TextUtils.isEmpty(et_age.getText().toString())) {
//
//
//
//                                } else if (!isConstant) {
//
//                                    String age = et_age.getText().toString();
//                                    et_age.setText(String.valueOf(Integer.parseInt(age) / 12));
//                                    et_age.setSelection(et_age.getText().toString().length());
//                                    isConstant = false;
//                                }

                            }
                        }
                    });

                    rb_month.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                rb_year.setChecked(false);
                                age_type[0] = "months";

//                                if (TextUtils.isEmpty(et_age.getText().toString())) {
//
//
//
//                                } else {
//
//                                    String age = et_age.getText().toString();
//                                    et_age.setText(String.valueOf(Integer.parseInt(age) * 12));
//                                    et_age.setSelection(et_age.getText().toString().length());
//                                }
//
//                                isConstant = false;

                            }
                        }
                    });

                    rb_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                rb_female.setChecked(false);
                                rb_other.setChecked(false);
                                gender_ = "Male";

                            }
                        }
                    });

                    rb_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                rb_male.setChecked(false);
                                rb_other.setChecked(false);
                                gender_ = "Female";
                            }
                        }
                    });

                    rb_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                rb_female.setChecked(false);
                                rb_male.setChecked(false);
                                gender_ = "Other";
                            }
                        }
                    });

                    if (patientsItem.getAge().contains("yr") || patientsItem.getAge().contains("Yr"))
                        rb_year.setChecked(true);
                    else if (patientsItem.getAge().contains("month") || patientsItem.getAge().contains("Month"))
                        rb_month.setChecked(true);
                    else
                        rb_year.setChecked(true);


               /*     Objects.requireNonNull((Activity)mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            et_age.addTextChangedListener(new TextWatcher(){
                                public void afterTextChanged(Editable s) {}

                                public void beforeTextChanged(CharSequence s, int start, int count, int after){}

                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    String strEnteredVal = et_age.getText().toString().trim();

                                    if (!strEnteredVal.equals("")) {
                                        int num = Integer.parseInt(strEnteredVal);
                                        if (num < 151) {
                                            et_age.setText("" + num);
                                        } else {
                                            et_age.setText("");
                                        }
                                    }

                                }});
                        }
                    });*/

                    Log.e("Patient______", "" + patientsItem.toString());
                    et_name.setText(patientsItem.getPName().trim());
                    String age__ = patientsItem.getAge().trim();
                    age__ = age__.replace("months", "");
                    age__ = age__.replace("month", "");
                    age__ = age__.replace("yr", "");
                    age__ = age__.replace("year", "");
                    et_age.setText(age__.trim());
                    if (patientsItem.getPMobile().equalsIgnoreCase("0"))
                        et_whtsapp.setText("");
                    else
                        et_whtsapp.setText(patientsItem.getPMobile());

                    if (patientsItem.getPEmail().equalsIgnoreCase("-"))
                        et_mail.setText("");
                    else
                        et_mail.setText(patientsItem.getPEmail());

                    date_f_birth.setText(patientsItem.getpDob());
                    address.setText(patientsItem.getAddress());
                    blood_group.setText(patientsItem.getpBloodGrp());


                    gender_ = patientsItem.getGender();
                    if (gender_.equalsIgnoreCase("Male"))
                        rb_male.setChecked(true);
                    else if (gender_.equalsIgnoreCase("Female"))
                        rb_female.setChecked(true);
                    else
                        rb_other.setChecked(true);

                    btnUpload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (et_name.getText().toString().trim().equalsIgnoreCase("")) {
                                et_name.setFocusable(true);
                                et_name.setError("Patient's Full Name");
                            } else {
                                if (et_age.getText().toString().trim().equalsIgnoreCase("0")) {
                                    et_age.setFocusable(true);
                                    et_age.setError("Zero Not Allowed");
                                    Toast.makeText(mContext, "Zero not allowed", Toast.LENGTH_SHORT).show();

                                } else {
                                    String strEnteredVal = et_age.getText().toString().trim();
                                    if (!strEnteredVal.equals("")) {
                                        int num = Integer.parseInt(strEnteredVal);
                                        if (age_type[0].equals("yr") && num < 151) {
                                            et_age.setText("" + num);

                                            fl_progress_bar.setVisibility(View.VISIBLE);
                                            et_name.getText().toString().trim();

                                            editPatients(patientsItem.getPatientId(), et_name.getText().toString().trim(),
                                                    et_age.getText().toString().trim() + " " + age_type[0], gender_,
                                                    et_whtsapp.getText().toString().trim(), et_mail.getText().toString().trim(), address.getText().toString().trim(), blood_group.getText().toString().trim(), date_f_birth.getText().toString());

                                            progress_bar.setVisibility(View.VISIBLE);

                                        } else if (age_type[0].equals("month") || age_type[0].equals("months")) {
                                            et_age.setText("" + num);

                                            fl_progress_bar.setVisibility(View.VISIBLE);
                                            et_name.getText().toString().trim();
                                            if (age_type[0].equals("months"))
                                                if (et_age.getText().toString().trim().equals("1"))
                                                    age_type[0] = "month";


                                            editPatients(patientsItem.getPatientId(), et_name.getText().toString().trim(),
                                                    et_age.getText().toString().trim() + " " + age_type[0], gender_,
                                                    et_whtsapp.getText().toString().trim(), et_mail.getText().toString().trim(), address.getText().toString().trim(), blood_group.getText().toString().trim(), date_f_birth.getText().toString());

                                            progress_bar.setVisibility(View.VISIBLE);

                                        } else {
                                            et_age.setFocusable(true);
                                            et_age.setError("Age not allowed");
                                            Toast.makeText(mContext, "Age not allowed", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {

                                        fl_progress_bar.setVisibility(View.VISIBLE);
                                        editPatients(patientsItem.getPatientId(), et_name.getText().toString().trim(),
                                                et_age.getText().toString().trim(), gender_,
                                                et_whtsapp.getText().toString().trim(), et_mail.getText().toString().trim(), address.getText().toString().trim(), blood_group.getText().toString().trim(), date_f_birth.getText().toString());

                                        progress_bar.setVisibility(View.VISIBLE);

                                    }
                                }
                            }
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog_data.dismiss();

                            hideKeyboard(fragmentActivity);

                        }
                    });

                    dialog_data.show();

                }
            });


            patient_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AlertDialog));
                    builder.setMessage("Do you wish to delete the Patient?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    fl_progress_bar.setVisibility(View.VISIBLE);
                                    //manager.getPreferences(Registration_.this, "service_provider");

                                    deletePatients(PATIENT_ID.getText().toString().trim());

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
    }

    public void deletePatients(String patientsId) {

        if (Utils.isConnectingToInternet(mContext)) {

            fl_progress_bar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResponseSuccess> call = request.deletePatientFromApi(patientsId);

            call.enqueue(new Callback<ResponseSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResponseSuccess> call, @NonNull retrofit2.Response<ResponseSuccess> response) {
                    ResponseSuccess jsonResponse = response.body();
                    fl_progress_bar.setVisibility(View.GONE);
                    if (jsonResponse != null && jsonResponse.getSuccess().equalsIgnoreCase("Success")) {

                        Toast.makeText(mContext, "Delete SuccessFully", Toast.LENGTH_SHORT).show();

                        AllGetPatients();

                    } else if (jsonResponse != null) {

                        Toast.makeText(mContext, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseSuccess> call, @NonNull Throwable t) {
                    fl_progress_bar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(mContext, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertFormat(String date) {

        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateInput = null;
        try {
            dateInput = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dateInput == null)
            return "";

        return outputFormat.format(dateInput);
    }

    public void editPatients(String patient_id, String p_name, String age, String gender,
                             String p_mobile, String p_email, String address, String bloud_group, String dob) {

        if (Utils.isConnectingToInternet(mContext)) {

            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResponseSuccess> call = request.PatientUpdateFromApi(patient_id, p_name, age, gender, p_mobile, p_email, address, bloud_group, convertFormat(dob));

            call.enqueue(new Callback<ResponseSuccess>() {
                @Override
                public void onResponse(@NonNull Call<ResponseSuccess> call, @NonNull retrofit2.Response<ResponseSuccess> response) {

                    if (progress_bar != null && dialog_data != null) {
                        progress_bar.setVisibility(View.GONE);
                        dialog_data.dismiss();

                        hideKeyboard(fragmentActivity);

                    }

                    ResponseSuccess jsonResponse = response.body();
                    if (jsonResponse != null && jsonResponse.getSuccess().equalsIgnoreCase("Success")) {

                        Toast.makeText(mContext, "Updated Successfully", Toast.LENGTH_SHORT).show();

                        AllGetPatients();

                    } else if (jsonResponse != null) {

                        Toast.makeText(mContext, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseSuccess> call, @NonNull Throwable t) {
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(mContext, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void AllGetPatients() {

        if (Utils.isConnectingToInternet(mContext)) {

            fl_progress_bar.setVisibility(View.VISIBLE);
            Retrofit retrofit = RetrofitClient.getInstance();

            final WebServices request = retrofit.create(WebServices.class);

            Call<ResponsePatients> call = request.getPatients2(manager.getPreferences(mContext, "doctor_id"));

            call.enqueue(new Callback<ResponsePatients>() {
                @Override
                public void onResponse(@NonNull Call<ResponsePatients> call, @NonNull retrofit2.Response<ResponsePatients> response) {

                    ResponsePatients responsePatients = response.body();

                    if (responsePatients != null) {

                        if (responsePatients.getMessage() != null) {
                            if (responsePatients.getMessage().equals("patients")) {

                                try {

                                    SplashActivity splashActivity = new SplashActivity();
                                    splashActivity.setPatients(responsePatients.getPatients());

                                    FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                                    manager.popBackStack();

                                    CreatePrescription myFragment = new CreatePrescription();
                                    manager.beginTransaction().replace(R.id.homePageContainer, myFragment, "prescription").addToBackStack(null).commit();

                                } catch (IllegalStateException e) {

                                    Log.i("TAG", "onSuccess: " + e.getMessage());

                                }

                            } else if (responsePatients.getMessage().equals("Data not available")) {

                                try {

                                    SplashActivity splashActivity = new SplashActivity();
                                    splashActivity.setPatients(null);

                                    FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                                    manager.popBackStack();

                                    CreatePrescription myFragment = new CreatePrescription();
                                    manager.beginTransaction().replace(R.id.homePageContainer, myFragment, "prescription").addToBackStack(null).commit();

                                } catch (IllegalStateException e) {

                                    Log.i("TAG", "onSuccess: " + e.getMessage());

                                }
                            }
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResponsePatients> call, @NonNull Throwable t) {
                    fl_progress_bar.setVisibility(View.GONE);
                    Log.e("Error  ***", t.getMessage());
                    Toast.makeText(mContext, "Profile Update Error", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(mContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public static void hideKeyboard(Context mContext) {
        InputMethodManager inputManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) mContext).getCurrentFocus();
        if (v == null)
            return;
        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private boolean isBloodGroupValid(String bloodGroup, Context context) {
        if (bloodGroup != null) {
            String[] bloodGroups = context.getResources().getStringArray(R.array.blood_groups);
            int len = bloodGroups.length;
            for (int i = 0; i < len; i++) {
                if (bloodGroup.equalsIgnoreCase(bloodGroups[i]))
                    return true;
            }
        }
        return false;
    }

    SingleObserver<ResponseSuccess> responseProfile = new SingleObserver<ResponseSuccess>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseSuccess response) {

            fl_progress_bar.setVisibility(View.GONE);

            if (response != null) {
                if (response.getMessage() == null) {
                } else if (response.getMessage().equals("Patient Deleted")) {

                    Toast.makeText(fragmentActivity, "delete successfully", Toast.LENGTH_SHORT).show();

                    fl_progress_bar.setVisibility(View.GONE);
                    apiViewHolder.getPatients(manager.getPreferences(mContext, "doctor_id"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responsePatients);
                }
            } else {
                fl_progress_bar.setVisibility(View.GONE);
                Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            //intentCall();
            fl_progress_bar.setVisibility(View.GONE);
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


    SingleObserver<ResponseSuccess> responsePatientUpdate = new SingleObserver<ResponseSuccess>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseSuccess response) {

            if (progress_bar != null) {
                progress_bar.setVisibility(View.GONE);
            }

            if (response != null) {

                if (response.getMessage() == null) {
                } else if (response.getMessage().equals("Patient Updated")) {

                    Toast.makeText(fragmentActivity, "Updated successfully", Toast.LENGTH_SHORT).show();

                    if (dialog_data != null) {
                        dialog_data.dismiss();

                        hideKeyboard(fragmentActivity);

                    }

                    apiViewHolder.getPatients(manager.getPreferences(mContext, "doctor_id"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responsePatients);


                }
            } else {
                Toast.makeText(mContext, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    private SingleObserver<ResponsePatients> responsePatients = new SingleObserver<ResponsePatients>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponsePatients response) {
            if (response != null) {

                if (response.getMessage() != null) {
                    if (response.getMessage().equals("patients")) {

                        try {

                            SplashActivity splashActivity = new SplashActivity();
                            splashActivity.setPatients(response.getPatients());

                            FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                            CreatePrescription myFragment = new CreatePrescription();
                            manager.beginTransaction().replace(R.id.homePageContainer, myFragment, "prescription").addToBackStack(null).commit();

                            manager.popBackStack();

                        } catch (IllegalStateException e) {

                            Log.i("TAG", "onSuccess: " + e.getMessage());

                        }

                    } else if (response.getMessage().equals("Data not available")) {

                        try {

                            SplashActivity splashActivity = new SplashActivity();
                            splashActivity.setPatients(null);

                            FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                            manager.popBackStack();

                            CreatePrescription myFragment = new CreatePrescription();
                            manager.beginTransaction().replace(R.id.homePageContainer, myFragment, "prescription").addToBackStack(null).commit();

                        } catch (IllegalStateException e) {

                            Log.i("TAG", "onSuccess: " + e.getMessage());

                        }
                    }
                }
            }
        }

        @Override
        public void onError(Throwable e) {
            //intentCall();
            Toast.makeText(mContext, ApplicationConstant.ANYTHING_WRONG, Toast.LENGTH_SHORT).show();
        }
    };


   /* private void NativeAd(Dialog v, ProgressBar pb, Button btnProceed, Button btnCancel) {
        refreshAd(v, pb, btnProceed, btnCancel);
        //  "ca-app-pub-3940256099942544/1044960115"
    }

    *//**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView   the view to be populated
     *//*
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {

            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {


            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.

                    super.onVideoEnd();
                }
            });
        } else {

        }
    }

    *//**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     * @param
     *//*
    private void refreshAd(final Dialog dialog_view, final ProgressBar pb, final Button btnProceed, final Button btnCancel) {

        AdLoader.Builder builder = new AdLoader.Builder(mContext, ADMOB_AD_UNIT_ID);

        builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    // OnUnifiedNativeAdLoadedListener implementation.
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        boolean isDestroyed = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = createPrescription.isRemoving();
                        }
                        if (isDestroyed) {
                            unifiedNativeAd.destroy();
                            return;
                        }
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        nativeAd = unifiedNativeAd;
                        FrameLayout frameLayout = dialog_view.findViewById(R.id.fl_adplaceholder);
                        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        UnifiedNativeAdView adView = (UnifiedNativeAdView) inflater.inflate(R.layout.my_ad_layout, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);

                        pb.setVisibility(View.GONE);
                        btnProceed.setVisibility(View.VISIBLE);
                        btnCancel.setVisibility(View.VISIBLE);
                    }
                });

           *//* VideoOptions videoOptions =
                    new VideoOptions.Builder().build();*//*

        NativeAdOptions adOptions =
                new NativeAdOptions.Builder().build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader =
                builder
                        .withAdListener(
                                new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        String error =
                                                String.format(
                                                        "domain: %s, code: %d, message: %s",
                                                        loadAdError.getDomain(),
                                                        loadAdError.getCode(),
                                                        loadAdError.getMessage());
//                                        Toast.makeText(
//                                                mContext,
//                                                "Failed to load native ad with error " + error,
//                                                Toast.LENGTH_SHORT)
//                                                .show();
                                    }
                                })
                        .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }*/

}

