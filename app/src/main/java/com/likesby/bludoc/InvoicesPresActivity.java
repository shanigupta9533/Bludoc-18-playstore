package com.likesby.bludoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.likesby.bludoc.Adapter.BottomShareAdapter;
import com.likesby.bludoc.Adapter.BottomSheetAdapter;
import com.likesby.bludoc.Adapter.InvoicesPresAdapter;
import com.likesby.bludoc.ModelLayer.Entities.BottomSheetItem;
import com.likesby.bludoc.ModelLayer.Entities.PatientsItem;
import com.likesby.bludoc.ModelLayer.Entities.ResponseSuccess;
import com.likesby.bludoc.ModelLayer.InvoicePresModel;
import com.likesby.bludoc.ModelLayer.InvoicesModel.InvoicesDataModel;
import com.likesby.bludoc.ModelLayer.NewEntities.ResponseProfileDetails;
import com.likesby.bludoc.ModelLayer.NewEntities3.Doctor;
import com.likesby.bludoc.SessionManager.SessionManager;
import com.likesby.bludoc.databinding.ActivityInvoicesPresBinding;
import com.likesby.bludoc.viewModels.ApiViewHolder;
import com.squareup.picasso.Picasso;

import org.apache.commons.collections4.ListUtils;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class InvoicesPresActivity extends AppCompatActivity {

    private ActivityInvoicesPresBinding activity;
    private int counter;
    SessionManager manager;
    private InvoicePresModel invoicePewModel;
    private final ArrayList<InvoicesDataModel> invoicesDataModels = new ArrayList<>();
    private BottomShareAdapter mAdapter;
    private static ArrayList<Uri> filesGlobal = new ArrayList<>();
    private String patientName;
    private PatientsItem patientsItem;
    private ApiViewHolder apiViewHolder;
    private CompositeDisposable mBag = new CompositeDisposable();
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    private ProgressDialog progressDialog;
    private String date;
    private InvoicesPresAdapter invoicesPresAdapter;
    private String currency;
    private int sizeGlobal;

    private void initViewHolder() {
        apiViewHolder = ViewModelProviders.of(this).get(ApiViewHolder.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = DataBindingUtil.setContentView(this, R.layout.activity_invoices_pres);

        manager = new SessionManager();
        Doctor doctor = getDataFromSession();
        initViewHolder();
        Intent intent = getIntent();
        if (intent != null) {
            invoicePewModel = intent.getParcelableExtra("invoicePewModel");
            patientName = intent.getStringExtra("patientName");
            patientsItem = intent.getParcelableExtra("patientObject");
        }

        if (invoicePewModel != null) {

            String advancePercentage = invoicePewModel.getAdvance_amount_percentage();
            String discountPercentage = invoicePewModel.getDiscount_percentage();
            String taxPercentage = invoicePewModel.getTax_percentage();
            date = invoicePewModel.getDate();

            if (!TextUtils.isEmpty(advancePercentage))
                advancePercentage = "(" + advancePercentage + "%)";

            if (!TextUtils.isEmpty(discountPercentage))
                discountPercentage = "(" + discountPercentage + "%)";

            if (!TextUtils.isEmpty(taxPercentage))
                taxPercentage = "(" + taxPercentage + "%)";

            if (invoicePewModel.getCurrency().equalsIgnoreCase("INR")) {
                activity.totalAmount.setText("₹ " + formatter.format(Long.parseLong(checkNullValues(invoicePewModel.getFinal_amount(), activity.totalAmount))));
                currency = "₹ ";
            } else {
                activity.totalAmount.setText(invoicePewModel.getCurrency() + " " + formatter.format(Long.parseLong(checkNullValues(invoicePewModel.getFinal_amount(), activity.totalAmount))));
                currency = invoicePewModel.getCurrency() + " ";
            }

            activity.patientName.setText("Name - " + checkNullValues(invoicePewModel.getInvoice_name(), activity.patientName));
            activity.invoicesTitle.setText(checkNullValues(invoicePewModel.getInvoice_title(), activity.invoicesTitle));
            activity.discountAmount.setText(discountPercentage + " " + currency + formatter.format(Float.parseFloat(checkNullValues(invoicePewModel.getDiscount(), activity.parentOfDiscount))));
            activity.advancePartialAmount.setText(advancePercentage + " " + currency + formatter.format(Float.parseFloat(checkNullValues(invoicePewModel.getAdvance_amount(), activity.parentOfAmountPaid))));
            activity.taxAmount.setText(taxPercentage + " " + currency + formatter.format(Float.parseFloat(checkNullValues(invoicePewModel.getTax(), activity.parentOfTax))));
            activity.endNote.setText("Remarks - " + checkNullValues(invoicePewModel.getNote(), activity.endNote));

            if (TextUtils.isEmpty(invoicePewModel.getInvoice_no())) {

                long invoiceNumber = new Date().getTime() / 1000;
                activity.invoiceNumber.setText("Invoice Number - #" + invoiceNumber);
                invoicePewModel.setInvoice_no(String.valueOf(invoiceNumber));

            } else
                activity.invoiceNumber.setText("Invoice Number - #" + invoicePewModel.getInvoice_no());

            if (!TextUtils.isEmpty(invoicePewModel.getPay_status()) && invoicePewModel.getPay_status().equalsIgnoreCase("None"))
                activity.parentOfPaymentStatus.setVisibility(View.GONE);
            else
                activity.paymentStatus.setText("Status - " + checkNullValues(invoicePewModel.getPay_status(), activity.parentOfPaymentStatus));

            if (!TextUtils.isEmpty(invoicePewModel.getAdvance_amount_title())) {
                activity.amountPaidTitle.setText(invoicePewModel.getAdvance_amount_title());
            }

            if (!TextUtils.isEmpty(invoicePewModel.getDiscount_title())) {
                activity.discountTitle.setText(invoicePewModel.getDiscount_title());
            }

            if (!TextUtils.isEmpty(invoicePewModel.getTax_title())) {
                activity.gstTitle.setText(invoicePewModel.getTax_title());
            }

            if (invoicePewModel.getItems() != null) {

                invoicesDataModels.clear();
                invoicesDataModels.addAll(invoicePewModel.getItems());

                activity.invoicesInRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                invoicesPresAdapter = new InvoicesPresAdapter(this, invoicesDataModels);
                invoicesPresAdapter.setCountModel(0,false);
                activity.invoicesInRecyclerView.setAdapter(invoicesPresAdapter);
                invoicesPresAdapter.setCurrenyDecide(currency);
            }

        }

        activity.generatePDF.setOnClickListener(v -> {

            progressDialog = new ProgressDialog(InvoicesPresActivity.this);
            progressDialog.setMessage("Please Wait ....");
            progressDialog.setTitle("Generating Invoice");
            progressDialog.setCancelable(false);
            progressDialog.show();

            setViewInInches(6.5f, 6.5f, activity.parentOfInvoices);

            if (invoicePewModel != null) {

                sendModelOnServer(invoicePewModel, progressDialog);

            }


        });

        activity.btnBackEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        // todo doctor clinic name =================>
        if (!("").equalsIgnoreCase(doctor.getClinicName())) {
            activity.textViewClinicName.setText(doctor.getClinicName());
        } else {
            activity.textViewClinicName.setVisibility(View.GONE);
        }

        // todo doctor name =================>
        if (!("").equalsIgnoreCase(manager.getPreferences(getApplicationContext(), "name").trim())) {
            activity.textViewDocName.setText(doctor.getName().trim());
        }

        // todo doctor UG and PG Name =================>
        if (!("").equalsIgnoreCase(doctor.getPgName()) && doctor.getPgName() != null) {
            if (!("").equalsIgnoreCase(doctor.getAddtionalQualification()) && (doctor.getAddtionalQualification() != null)) {
                activity.textViewDegree.setText(doctor.getUgName() + " " + doctor.getPgName() + " " + doctor.getAddtionalQualification());
            } else {
                activity.textViewDegree.setText(doctor.getUgName() + " " + doctor.getPgName());
            }

            // todo doctor additional qualification =================>
        } else if (!("").equalsIgnoreCase(doctor.getAddtionalQualification()) && (doctor.getAddtionalQualification() != null)) {
            activity.textViewDegree.setText(doctor.getUgName() + " " + doctor.getAddtionalQualification());

        } else {
            activity.textViewDegree.setText(doctor.getUgName());
        }

        // todo doctor registration number =================>
        activity.textViewResNum.setText("Reg. No - " + doctor.getRegistrationNo());
        if (!("").equalsIgnoreCase(doctor.getDesignationName())) {
            activity.textViewDes.setText(doctor.getDesignationName());
        } else {
            activity.textViewDes.setVisibility(View.GONE);
        }

        // todo doctor working days =================>
        if (!("").equalsIgnoreCase(doctor.getWorkingDays())) {
            String work_days = doctor.getWorkingDays();
            if (work_days.contains("MONDAY"))
                work_days = work_days.replace("MONDAY", " MON");

            if (work_days.contains("TUESDAY"))
                work_days = work_days.replace("TUESDAY", " TUE");

            if (work_days.contains("WEDNESDAY"))
                work_days = work_days.replace("WEDNESDAY", " WED");

            if (work_days.contains("THURSDAY"))
                work_days = work_days.replace("THURSDAY", " THUR");

            if (work_days.contains("FRIDAY"))
                work_days = work_days.replace("FRIDAY", " FRI");

            if (work_days.contains("SATURDAY"))
                work_days = work_days.replace("SATURDAY", " SAT");

            if (work_days.contains("SUNDAY"))
                work_days = work_days.replace("SUNDAY", " SUN");

            activity.textViewDays.setText("Working : " + work_days);
        } else {
            activity.textViewDays.setVisibility(View.GONE);
            counter = counter + 1;
        }

        // todo doctor visiting hour =================>
        if (!("").equalsIgnoreCase(doctor.getVisitingHrFrom())) {
            String[] visiting_hr_from_details = manager.getPreferences(getApplicationContext(), "visiting_hr_from").split(Pattern.quote("|"));

            String[] visiting_hr_to_details = manager.getPreferences(getApplicationContext(), "visiting_hr_to").split(Pattern.quote("|"));

            if (visiting_hr_from_details.length > 1)
                activity.textViewTime.setText("Time : " + visiting_hr_from_details[0].toLowerCase().trim() + " - " + visiting_hr_to_details[0].toLowerCase().trim() + ", " + visiting_hr_from_details[1].toLowerCase().trim() + " - " + visiting_hr_to_details[1].toLowerCase().trim());
            else
                activity.textViewTime.setText("Time : " + visiting_hr_from_details[0].toLowerCase().trim() + " - " + visiting_hr_to_details[0].toLowerCase().trim());
            //  textView_time.setText("Time : " + doctor.getVisitingHrFrom() + " to " + doctor.getVisitingHrTo());

            if (visiting_hr_from_details.length == 1 && visiting_hr_from_details[0].equals(""))
                if (visiting_hr_to_details.length == 1 && visiting_hr_to_details[0].equals(""))
                    activity.textViewTime.setVisibility(View.GONE);
        } else {

            activity.textViewTime.setVisibility(View.GONE);
            counter = counter + 1;
        }

        //todo contact info =================>
        if (!("").equalsIgnoreCase(doctor.getMobileLetterHead())) {
            activity.textViewContact.setText("Contact : " + doctor.getMobileLetterHead());
        } else {
            activity.textViewContact.setVisibility(View.GONE);
            counter = counter + 1;
        }

        // todo mail id =================>
        if (!("").equalsIgnoreCase(doctor.getEmailLetterHead())) {
            activity.textViewEmail.setText("Mail Id : " + doctor.getEmailLetterHead());
        } else {
            activity.textViewEmail.setVisibility(View.GONE);
            counter = counter + 1;
        }

        // todo address of a doctor =================>
        if (!("").equalsIgnoreCase(doctor.getClinicAddress())) {
            activity.textViewAdd.setText("Address : " + doctor.getClinicAddress());
        } else {
            activity.textViewAdd.setVisibility(View.GONE);
            counter = counter + 1;
        }

        // todo image of a doctor =================>
        if (!("").equalsIgnoreCase(doctor.getImage())) {
            Picasso.with(getApplicationContext()).
                    load(doctor.getImage())
                    .into(activity.imageLayout, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            activity.imageLayout.setVisibility(View.INVISIBLE);
        }

        // todo signature of a doctor =================>
        if (!("").equalsIgnoreCase(doctor.getSignature())) {
            Picasso.with(getApplicationContext()).
                    load(doctor.getSignature())
                    .into(activity.ivSignature, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            activity.ivSignature.setVisibility(View.GONE);
            activity.sigText.setVisibility(View.GONE);
        }

        //todo set date of doctor =========>

        if (TextUtils.isEmpty(date)) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Calendar c2 = Calendar.getInstance();
            String formattedDate = df.format(c2.getTime());
            activity.textViewDate.setText("Date : " + formattedDate);
        } else {

            activity.textViewDate.setText("Date : " + date);
        }

    }


    private SingleObserver<ResponseSuccess> responseSendDataOnInvoices = new SingleObserver<ResponseSuccess>() {
        @Override
        public void onSubscribe(Disposable d) {
            mBag.add(d);
        }

        @Override
        public void onSuccess(ResponseSuccess response) {
            if (response != null) {
                ResponseSuccess jsonResponse = response;
                assert jsonResponse != null;
                progressDialog.dismiss();
                if (jsonResponse.getSuccess().equalsIgnoreCase("success")) {

                    if (invoicePewModel!=null && !invoicePewModel.getItems().isEmpty())
                        generateBitmapScreenShots(invoicePewModel);
                    else {
                        filesGlobal.clear();
                        Bitmap screen1 = getBitmapFromView(activity.nestedParentLinear);  // here give id of our root layout (here its my RelativeLayout's id)
                        filesGlobal.add(getImageUri(InvoicesPresActivity.this, screen1, "" + patientName));
                        popupBottomMenu();
                    }

                } else {

                    Toast.makeText(InvoicesPresActivity.this, "" + jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }

        @Override
        public void onError(@NotNull Throwable e) {

            progressDialog.dismiss();
            Toast.makeText(InvoicesPresActivity.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();

        }
    };

    private void generateBitmapScreenShots(InvoicePresModel invoicePewModel) {

        List<List<InvoicesDataModel>> partition = ListUtils.partition(invoicePewModel.getItems(), 5);
        filesGlobal.clear();
        activity.parentOfFinalAmount.setVisibility(View.GONE);

        final boolean[] isLoop = {true};
        final boolean[] isExecuteValue = {true};
        final int[] i = {0};

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isLoop[0]) {

                    if(isExecuteValue[0]) {

                        invoicesDataModels.clear();
                        invoicesDataModels.addAll(partition.get(i[0]));
                        isExecuteValue[0] = false;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                invoicesPresAdapter.setCountModel(i[0],true);
                                invoicesPresAdapter.notifyDataSetChanged();

                                if (partition.size() - 1 == i[0]) {
                                    activity.parentOfFinalAmount.setVisibility(View.VISIBLE);
                                }

                                new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Bitmap screen1 = getBitmapFromView(activity.nestedParentLinear);  // here give id of our root layout (here its my RelativeLayout's id)
                                        filesGlobal.add(getImageUri(InvoicesPresActivity.this, screen1, "" + patientName));

                                        if(partition.size()-1 == i[0]){
                                            popupBottomMenu();
                                            isLoop[0] = false;
                                        }

                                        isExecuteValue[0] = true;
                                        i[0]++;

                                    }
                                },500);

                            }
                        });

                    }

                }


            }
        }).start();

    }

    private void sendModelOnServer(InvoicePresModel invoicePewModel, ProgressDialog progressDialog) {

        ResponseProfileDetails responseProfileDetails = manager.getObjectProfileDetails(getApplicationContext(), "profile");

        invoicePewModel.setPatient_id(patientsItem.getPatientId());
        invoicePewModel.setHospital_code(responseProfileDetails.getHospitalCode());
        invoicePewModel.setDoctor_id(manager.getPreferences(InvoicesPresActivity.this, "doctor_id"));
        Gson gson = new Gson();
        String invoicesValue = gson.toJson(invoicePewModel);

        apiViewHolder.sendDataOnInvoices(invoicesValue)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseSendDataOnInvoices);


//        if (Utils.isConnectingToInternet(this)) {
//
//            Retrofit retrofit = RetrofitClient.getInstance();
//
//            final WebServices request = retrofit.create(WebServices.class);
//
//            Call<ResponseSuccess> call = request.sendDataOnInvoices(invoicesValue);
//
//            call.enqueue(new Callback<ResponseSuccess>() {
//                @Override
//                public void onResponse(@NonNull Call<ResponseSuccess> call, @NonNull retrofit2.Response<ResponseSuccess> response) {
//                    ResponseSuccess jsonResponse = response.body();
//                    assert jsonResponse != null;
//                    progressDialog.dismiss();
//                    if (jsonResponse.getSuccess().equalsIgnoreCase("success")) {
//
//                            Bitmap screen1 = getBitmapFromView(activity.nestedParentLinear);  // here give id of our root layout (here its my RelativeLayout's id)
//                            filesGlobal.clear();
//                            filesGlobal.add(getImageUri(InvoicesPresActivity.this, screen1, "" + patientName));
//                            progressDialog.dismiss();
//                            popupBottomMenu();
//
//
//                    } else {
//
//                        Toast.makeText(InvoicesPresActivity.this, ""+jsonResponse.getMessage(), Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<ResponseSuccess> call, @NonNull Throwable t) {
//                    progressDialog.dismiss();
//                    Log.e("Error  ***", t.getMessage());
//                    Toast.makeText(InvoicesPresActivity.this, "Something Went Wrong....", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        } else {
//            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
//        }


    }

    private String checkNullValues(String invoicePatientName, View edittext) {

        if (TextUtils.isEmpty(invoicePatientName) || (!TextUtils.isEmpty(invoicePatientName) && invoicePatientName.equalsIgnoreCase("0"))) {
            edittext.setVisibility(View.GONE);
            return "0";
        }

        edittext.setVisibility(View.VISIBLE);
        return invoicePatientName;

    }

    public Uri getImageUri(Context inContext, Bitmap inImage, String pName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, pName + "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }


    public Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view

        view.setDrawingCacheEnabled(true);
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        return returnedBitmap;
    }

    public void setViewInInches(float width, float height, View v) {

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width1 = displayMetrics.widthPixels;
        int height1 = displayMetrics.heightPixels;

        int widthInches = Math.round((int) ((width1 * 180) / 100));
        int heightInches = Math.round((int) ((height1 * 180) / 100));

        v.setLayoutParams(new FrameLayout.LayoutParams(widthInches, heightInches));
        v.requestLayout();
    }

    private Doctor getDataFromSession() {

        Doctor doctor = new Doctor();
        doctor.setName(manager.getPreferences(getApplicationContext(), "name"));
        doctor.setEmail(manager.getPreferences(getApplicationContext(), "email"));
        doctor.setAddtionalQualification(manager.getPreferences(getApplicationContext(), "addtional_qualification"));
        doctor.setClinicAddress(manager.getPreferences(getApplicationContext(), "clinic_address"));
        doctor.setClinicName(manager.getPreferences(getApplicationContext(), "clinic_name"));
        doctor.setCloseDay(manager.getPreferences(getApplicationContext(), "close_day"));
        doctor.setDesignationName(manager.getPreferences(getApplicationContext(), "designation_name"));
        doctor.setEmailLetterHead(manager.getPreferences(getApplicationContext(), "email_letter_head"));
        doctor.setImage(manager.getPreferences(getApplicationContext(), "image"));
        doctor.setLogo(manager.getPreferences(getApplicationContext(), "logo"));
        doctor.setMobile(manager.getPreferences(getApplicationContext(), "mobile"));
        doctor.setMobileLetterHead(manager.getPreferences(getApplicationContext(), "mobile_letter_head"));
        doctor.setPgName(manager.getPreferences(getApplicationContext(), "pg_name"));
        doctor.setUgName(manager.getPreferences(getApplicationContext(), "ug_name"));
        doctor.setRegistrationNo(manager.getPreferences(getApplicationContext(), "registration_no"));
        doctor.setSignature(manager.getPreferences(getApplicationContext(), "signature"));
        doctor.setVisitingHrFrom(manager.getPreferences(getApplicationContext(), "visiting_hr_from"));
        doctor.setVisitingHrTo(manager.getPreferences(getApplicationContext(), "visiting_hr_to"));
        doctor.setWorkingDays(manager.getPreferences(getApplicationContext(), "working_days"));

        return doctor;

    }

    private void popupBottomMenu() {

        Dialog dialog_dataShareMenu = new Dialog(this);
        dialog_dataShareMenu.setCancelable(false);
        dialog_dataShareMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog_dataShareMenu.getWindow()).setGravity(Gravity.CENTER);
        dialog_dataShareMenu.setContentView(R.layout.popup_share_menu);
        WindowManager.LayoutParams lp_number_picker = new WindowManager.LayoutParams();
        Window window = dialog_dataShareMenu.getWindow();
        lp_number_picker.copyFrom(window.getAttributes());
        lp_number_picker.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp_number_picker.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp_number_picker);

        Button btn_mobile = dialog_dataShareMenu.findViewById(R.id.btn_mobile);
        TextView __bottom_sheet_name = dialog_dataShareMenu.findViewById(R.id.__bottom_sheet_name);
        TextView open_via_page_pdf = dialog_dataShareMenu.findViewById(R.id.open_via_page_pdf);
        open_via_page_pdf.setVisibility(View.GONE);
        btn_mobile.setBackground(getResources().getDrawable(R.drawable.green_faint_round_btn_gradient2));
        __bottom_sheet_name.setTextColor(getResources().getColor(R.color.guidee));
        __bottom_sheet_name.setBackground(getResources().getDrawable(R.drawable.faint_white_round_border_green));
        btn_mobile.setTextColor(getResources().getColor(R.color.colorDarkBlue));
        btn_mobile.setText("A4 Size View");
        TextView send_to = dialog_dataShareMenu.findViewById(R.id.send_to);
        send_to.setVisibility(View.GONE);

        open_via_page_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAdapter != null) {

                    Intent intentShareFile = new Intent(Intent.ACTION_VIEW);
                    File fileWithinMyDir = new File(mAdapter.getPdfViaApps());
                    Uri bmpUri = FileProvider.getUriForFile(InvoicesPresActivity.this, BuildConfig.APPLICATION_ID + ".provider", fileWithinMyDir);
                    if (fileWithinMyDir.exists()) {
                        intentShareFile.setDataAndType(bmpUri, "application/pdf");
                        intentShareFile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(Intent.createChooser(intentShareFile, "Open File Using..."));

                    }
                }

            }
        });

        RecyclerView recyclerView = dialog_dataShareMenu.findViewById(R.id.recyclerView_bottom_sheet);
        //Create new GridLayoutManager
        @SuppressLint("WrongConstant") GridLayoutManager gridLayoutManagerr = new GridLayoutManager(this,
                3,//span count no of items in single row
                GridLayoutManager.VERTICAL,//Orientation
                false);//reverse scrolling of recyclerview
        //set layout manager as gridLayoutManager

        recyclerView.setLayoutManager(gridLayoutManagerr);
        ArrayList<BottomSheetItem> bottomSheetItemArrayList = new ArrayList<>();
        BottomSheetItem bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("1");
        bottomSheetItem.setMenuName("View Pdf");
        bottomSheetItem.setMenuImage("mail");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("2");
        bottomSheetItem.setMenuName("Share");
        bottomSheetItem.setMenuImage("ic_download_");

        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("3");
        bottomSheetItem.setMenuName("Download");
        bottomSheetItem.setMenuImage("ic_share__");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("4");
        bottomSheetItem.setMenuName("Email");
        bottomSheetItem.setMenuImage("ic_share__");
        bottomSheetItemArrayList.add(bottomSheetItem);

        bottomSheetItem = new BottomSheetItem();
        bottomSheetItem.setMenuId("5");
        bottomSheetItem.setMenuName("WhatsApp");
        bottomSheetItem.setMenuImage("whatsapp");
        bottomSheetItemArrayList.add(bottomSheetItem);

        mAdapter = new BottomShareAdapter(InvoicesPresActivity.this, bottomSheetItemArrayList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setPatientName(patientName);
        mAdapter.setPatientObject(patientsItem);

        mAdapter.setOnClickListener(new BottomSheetAdapter.onClickListener() {
            @Override
            public void onClick(int i) {

                if (i == 0) {

                    if (mAdapter != null) {

                        Intent intentShareFile = new Intent(Intent.ACTION_VIEW);
                        File fileWithinMyDir = new File(mAdapter.getPdfViaApps());
                        Uri bmpUri = FileProvider.getUriForFile(InvoicesPresActivity.this, BuildConfig.APPLICATION_ID + ".provider", fileWithinMyDir);
                        if (fileWithinMyDir.exists()) {
                            intentShareFile.setDataAndType(bmpUri, "application/pdf");
                            intentShareFile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intentShareFile.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(Intent.createChooser(intentShareFile, "Open File Using..."));

                        }
                    }

                }

            }
        });

        RecyclerView recyclerView_bottom_sheet_send_to = dialog_dataShareMenu.findViewById(R.id.recyclerView_bottom_sheet_send_to);
        recyclerView_bottom_sheet_send_to.setVisibility(View.GONE);
        //Create new GridLayoutManager

        ImageView iv_close = dialog_dataShareMenu.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog_dataShareMenu.dismiss();

                Intent intent = new Intent(InvoicesPresActivity.this, InvoicesPresActivity.class);
                intent.putExtra("patientName", patientName);
                intent.putExtra("patientObject", patientsItem);
                intent.putExtra("invoicePewModel", invoicePewModel);
                startActivity(intent);

                finish();

                /*assert getFragmentManager() != null;
                getFragmentManager().popBackStackImmediate();*/
            }
        });

        dialog_dataShareMenu.show();
    }

    public static ArrayList<Uri> getFiles() {
        return filesGlobal;
    }

}